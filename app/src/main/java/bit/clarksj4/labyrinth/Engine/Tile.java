package bit.clarksj4.labyrinth.Engine;

/**
 * A single tile in a tile map
 */
public class Tile
{
    private TileMap parent;
    private Coordinate position;
    private int value;

    public Tile(TileMap parent, Coordinate position, int value)
    {
        this.parent = parent;
        this.position = position;
        this.value = value;
    }

    /**
     * Gets the world position of this tile
     * @return The world position of this tile
     */
    public Vector getPosition()
    {
        Rectangle parentBounds = parent.getBounds();
        Vector size = getSize();

        // Parent top left
        float left = parentBounds.getLeft();
        float top = parentBounds.getTop();

        // Position of top left corner
        float tileLeft = left + (position.x * size.x);
        float tileTop = top + (position.y * size.y);

        // Calculate centre point
        float tileX = tileLeft + (size.x / 2);
        float tileY = tileTop + (size.y / 2);

        return new Vector(tileX, tileY);
    }

    /**
     * Gets the global size of this tile
     * @return The global size of this tile
     */
    public Vector getSize()
    {
        Vector scale = parent.getTransform().getScale();
        float tileSize = parent.getTileSize();

        float width = tileSize * scale.x;
        float height = tileSize * scale.y;

        return new Vector(width, height);
    }

    /**
     * Gets the bounds of this tile in world space
     * @return The bounds of this tile in world space
     */
    public Rectangle getBounds() { return new Rectangle(getPosition(), getSize()); }

    /**
     * Gets the value stored in this tile
     * @return THe value stored in this tile
     */
    public int getValue() { return value; }

    /**
     * Sets the value stored in this tile
     * @param value The value stored in this tile
     */
    public void setValue(int value) { this.value = value; }
}
