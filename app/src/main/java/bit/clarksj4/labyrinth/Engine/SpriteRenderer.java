package bit.clarksj4.labyrinth.Engine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Renderer component for drawing a bitmap sprite.
 */
public class SpriteRenderer extends Renderer
{
    private Bitmap sprite;
    private boolean flipX;
    private boolean flipY;
    private Vector spriteCenter;

    /**
     * A new sprite renderer attached to the given game object
     * @param parent The game object this renderer is attached to
     */
    public SpriteRenderer(GameObject parent)
    {
        super(parent);
    }

//    @Override
//    public void start()
//    {
//        // Remember the center of this sprite
//        spriteCenter = new Vector(sprite.getWidth() / 2, sprite.getHeight() / 2);
//    }

    @Override
    public void draw(Canvas canvas, Rectangle source, Vector positionOnCanvas)
    {
        //
        // Convert game world source rectangle to a the portion of the sprite bitmap that will be drawn
        //
        Rectangle bounds = getBounds();

        /* Get rectangle relative renderer's world bounds, adjust position to center of sprite
        e.g. a source rectangle centered over renderer's bounds would be adjusted to 0, 0 relative
        to renderer bounds. However, a bitmap's origin is its top-left corner; therefore, the rectangle
        must be translated by half to size of the sprite, so it is relative to its center. */
        Rectangle overBitmap = source.translate(bounds.center.reverse())
                                     .translate(spriteCenter);

        // Undo gameObject scaling so source rectangle is sized based on sprite bitmap proportions
        Vector scale = getTransform().getScale();
        Rectangle withoutScale = overBitmap.decrease(scale);

        // Convert to int (for use with Rect) rounded appropriately so no pixels are lost
        int srcLeft = (int)Math.floor(withoutScale.getLeft());
        int srcTop = (int)Math.floor(withoutScale.getTop());
        int srcRight = (int)Math.ceil(withoutScale.getRight());
        int srcBottom = (int)Math.ceil(withoutScale.getBottom());

        // Source rectangle on bitmap from which to get pixels to be drawn
        Rect src = new Rect(srcLeft,
                            srcTop,
                            srcRight,
                            srcBottom);
        //
        // Calculate the canvas rectangle to which the sprite's pixels will be drawn
        //

        // Half size of sprite
        float xExtents = getWidth() / 2;
        float yExtents = getHeight() / 2;

        // Get coordinates for android RectF class. Based on the canvas position given as an arguement
        float left = positionOnCanvas.x - xExtents;
        float top = positionOnCanvas.y - yExtents;
        float right =  positionOnCanvas.x + xExtents;
        float bottom = positionOnCanvas.y + yExtents;

        // Android rectF, required to draw with canvas
        RectF dst = new RectF(left, top, right, bottom);

        // Calculate canvas central coordinate to use as a pivot when scaling
        float pivotX = positionOnCanvas.x;
        float pivotY = positionOnCanvas.y;

        // Scale canvas inversely so image appears flipped
        if (flipX) canvas.scale(-1, 1, pivotX, pivotY);
        if (flipY) canvas.scale(1, -1, pivotX, pivotY);

        // Draw image
        canvas.drawBitmap(sprite, src, dst, null);

        // Undo scaling so other items that draw to this canvas are not affected
        if (flipX) canvas.scale(-1, 1, pivotX, pivotY);
        if (flipY) canvas.scale(1, -1, pivotX, pivotY);
    }

    /**
     * Gets the width of this sprite
     * @return The width of this sprite in pixels
     */
    public float getWidth() { return sprite.getWidth() * getTransform().getScale().x; }

    /**
     * Gets the height of this sprite
     * @return The height of this sprite in pixels
     */
    public float getHeight() { return sprite.getHeight() * getTransform().getScale().y; }

    @Override
    public Rectangle getBounds()
    {
        Vector position = getTransform().getPosition();
        Vector scale = new Vector(getWidth(), getHeight());

        return new Rectangle(position, scale);
    }

    /**
     * Sets the bitmap this renderer will draw
     * @param sprite The bitmap sprite this renderer will draw
     */
    public void setSprite(Bitmap sprite)
    {
        this.sprite = sprite;
        spriteCenter = new Vector(sprite.getWidth() / 2, sprite.getHeight() / 2);
    }

    /**
     * Sets whether this renderer will draw reversed on the x-axis
     * @param flipX Whether this renderer will draw reversed on the x-axis
     */
    public void setFlipX(boolean flipX) { this.flipX = flipX; }

    /**
     * Sets whether this renderer will draw reversed on the y-axis
     * @param flipY Whether this renderer will draw reversed on the y-axis
     */
    public void setFlipY(boolean flipY) { this.flipY = flipY; }
}
