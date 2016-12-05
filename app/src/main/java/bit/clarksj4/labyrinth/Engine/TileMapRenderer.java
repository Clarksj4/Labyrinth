package bit.clarksj4.labyrinth.Engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Renderer component for drawing a tile map
 */
public class TileMapRenderer extends Renderer
{
    private TileMap map;
    private Bitmap[] tileSet;

    /**
     * A new tile map renderer attached to the given game object
     * @param parent The game object this renderer is attached to
     */
    public TileMapRenderer(GameObject parent) { super(parent); }

    @Override
    public void start()
    {
        // Get ref to tile map that will be drawn
        map = gameObject.getComponent(TileMap.class);

        // Renderer requires tile map component. Don't continue without it.
        if (map == null)
            throw new NullPointerException(this.getClass().getName() + " requires " + TileMap.class.getName() + " component.");
    }

    /**
     * Draws the current view of the tileMap in memory
     */
    @Override
    public void draw(Canvas canvas, Rectangle source, Vector positionOnCanvas)
    {
        // Get tiles in (including partial) the given area
        Tile[][] srcTiles = map.getTiles(source);
        if (srcTiles != null)
        {
            // Adjust source bounds so it covers the area of the canvas to be drawn to
            source.translate(getBounds().center.reverse());

            Vector tileSize = srcTiles[0][0].getSize();

            for (int column = 0; column < srcTiles[0].length; column++)
            {
                for (int row = 0; row < srcTiles.length; row++)
                {
                    // Get tile for tile at current position, check if it is valid
                    Tile tile = srcTiles[row][column];
                    int tileValue = tile.getValue();

                    if (tileValue > 0 &&
                        tileValue < tileSet.length)
                    {
                         // Convert to image via tileSet array
                        Bitmap image = tileSet[tileValue];

                        // Calculate edges of destination rectangle
                        float left = tile.getPosition().add(positionOnCanvas).x - (tileSize.x / 2);
                        float top = tile.getPosition().add(positionOnCanvas).y - (tileSize.y / 2);
                        float right = left + tileSize.x;
                        float bottom = top + tileSize.y;

                        // Destination rectangle defines area on canvas to draw to
                        RectF dst = new RectF(left, top, right, bottom);

                        // Draw to canvas
                        canvas.drawBitmap(image, null, dst, null);
                    }
                }
            }
        }
    }

    public Rectangle getBounds() { return map.getBounds(); }

    /**
     * Sets the images this renderer will use to draw the tile map
     * @param tileSet The images this renderer will use to draw the tile map
     */
    public void setTileSet(Bitmap[] tileSet) { this.tileSet = tileSet; }
}
