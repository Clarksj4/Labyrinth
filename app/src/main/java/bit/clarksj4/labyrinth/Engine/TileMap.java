package bit.clarksj4.labyrinth.Engine;

import java.util.Arrays;

/**
 * A tilemap component
 */
public class TileMap extends Component
{
    /** Tile size defaults to game unit */
    private static final float DEFAULT_TILE_SIZE = Game.UNIT;

    private Tile[][] tiles;
    private float tileSize;

    /**
     * A new tile map attached to the given parent game object
     * @param gameObject The game object this tile map is attached to
     */
    public TileMap(GameObject gameObject)
    {
        super(gameObject);
        tileSize = DEFAULT_TILE_SIZE;

        // Register the tile map with the physics object
        Physics.getInstance().registerTileMap(this);
    }

    /**
     * Gets the number of columns this tile map has
     * @return The number of columns this tile map has
     */
    public int getColumns() { return tiles[0].length; }

    /**
     * Gets the number of rows this tile map has
     * @return The number of rows this tile map has
     */
    public int getRows() { return tiles.length; }

    /**
     * Gets the size of the tiles in this tile map
     * @return The size of the tiles in this tile map
     */
    public float getTileSize() { return tileSize; }

    /**
     * Gets the bounds of this tile map in world space
     * @return The bounds of this tile map in world space
     */
    public Rectangle getBounds()
    {
        Vector position = getTransform().getPosition();
        Vector scale = getTransform().getScale();

        float width = tileSize * getColumns() * scale.x;
        float height = tileSize * getRows() * scale.y;

        return new Rectangle(position, new Vector(width, height));
    }

    public Tile getTile(Coordinate position){ return UniformArrays.get(tiles, position.x, position.y); }
    public Tile getTile(int column, int row){ return UniformArrays.get(tiles, column, row); }

    public boolean contains(Coordinate position) { return UniformArrays.contains(tiles, position.x, position.y); }

    /**
     * Gets the tiles intersected by the given area
     * @param area The area to get tiles from
     * @return The tiles intersected by the given area, or null if there is no intersection
     */
    public Tile[][] getTiles(Rectangle area)
    {
        Rectangle bounds = getBounds();

        if (area.equals(getBounds()))
            return tiles;

        // Get intersection of this grids bounds and given area
        Rectangle intersection = area.intersection(bounds);
        if (intersection == null)
            return null;

        // Calculate dimensions on this tileMap
        int tileMapLeftIndex = worldXToIndex(intersection.getLeft());
        int tileMapTopIndex = worldYToIndex(intersection.getTop());
        int tileMapRightIndex = worldXToIndex(intersection.getRight());
        int tileMapBottomIndex = worldYToIndex(intersection.getBottom());

        // Always at least one tile if there is an intersection
        int nColumns = (tileMapRightIndex - tileMapLeftIndex) + 1;
        int nRows = (tileMapBottomIndex - tileMapTopIndex) + 1;

        // Create a new grid to hold subset of tiles
        Tile[][] subTileMap = UniformArrays.create(Tile.class, nColumns, nRows);
        for (int column = tileMapLeftIndex; column < tileMapRightIndex + 1; column++)
        {
            for (int row = tileMapTopIndex; row < tileMapBottomIndex + 1; row++)
            {
                // Get tile from this grid
                Tile tile = getTile(new Coordinate(column, row));

                // Calculate indices in new grid
                int subTileMapColumn = column -  tileMapLeftIndex;
                int subTileMapRow = row - tileMapTopIndex;

                // Put tile into new grid
                UniformArrays.set(subTileMap, subTileMapColumn, subTileMapRow, tile);
            }
        }

        // Return subset of this grid
        return subTileMap;
    }

    /**
     * Sets the values of the tiles in this tile map
     * @param values The values of the tiles in this tile map
     */
    public void setTiles(int[][] values)
    {
        int columns = UniformArrays.getColumns(values);
        int rows = UniformArrays.getRows(values);

        tiles = UniformArrays.create(Tile.class, columns, rows);
        for (int column = 0; column < columns; column++)
        {
            for (int row = 0; row < rows; row++)
            {
                // Position and value of tile
                Coordinate position = new Coordinate(column, row);
                int value = UniformArrays.get(values, column, row);

                // Create tile and add it to array of tiles
                Tile newTile = new Tile(this, position, value);
                UniformArrays.set(tiles, column, row, newTile);
            }
        }
    }

    /**
     * Sets the size of the tiles in this tile map
     * @param tileSize The size of the tiles in this tile map
     */
    public void setTileSize(float tileSize) { this.tileSize = tileSize; }

    /**
     * Converts a world position to a column index in this tile map
     * @param worldX The world position
     * @return A column index in this tile map
     */
    private int worldXToIndex(float worldX)
    {
        float left = getBounds().getLeft();
        float dX = worldX - left;
        float index = dX / (tileSize * getTransform().getScale().x);
        int rounded = (int) Math.floor(index);
        int clamped = MathExtension.clamp(rounded, 0, getColumns() - 1);
        return clamped;
    }

    /**
     * Converts a world position to a row index in this tile map
     * @param worldY The world position
     * @return A row index in this tile map
     */
    private int worldYToIndex(float worldY)
    {
        float top = getBounds().getTop();
        float dY = worldY - top;
        float index = dY / (tileSize * getTransform().getScale().x);
        int rounded = (int) Math.floor(index);
        int clamped = MathExtension.clamp(rounded, 0, getRows() - 1);
        return clamped;
    }

    private float columnToWorldX(int column)
    {

    }
}
