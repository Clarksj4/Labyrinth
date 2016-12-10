package bit.clarksj4.labyrinth.Engine;

import android.graphics.Canvas;

/**
 * Base class for all components capable of renderering themselves to a canvas object
 */
public abstract class Renderer extends Component implements Comparable<Renderer>
{
    private static final float DEFAULT_Z_INDEX = 0;
    private float zIndex = DEFAULT_Z_INDEX;

    /**
     * A new renderer component attached to the given parent game object
     */
    public Renderer(GameObject parent)
    {
        super(parent);
        Graphics.addRenderer(this);
    }

    /**
     * Gets the bounds of this renderer in world space
     * @return The bounds of this renderer in world space
     */
    public abstract Rectangle getBounds();

    /**
     * Draws a section of this renderer defined by 'source' to the given canvas, at the given
     * position
     * @param canvas The canvas to draw to
     * @param source The portion of this renderer to draw
     * @param positionOnCanvas The position on the canvas to draw at
     */
    public abstract void draw(Canvas canvas, Rectangle source, Vector positionOnCanvas);

    /**
     * Sets the z-index of this renderer. The z-index defines the order in which renderers will draw
     * to the canvas
     */
    public void setZIndex(float zIndex)
    {
        this.zIndex = zIndex;

        // Re-add this renderer to the graphics object so that its list of renderers is sorted correctly
        Graphics.removeRenderer(this);
        Graphics.addRenderer(this);
    }

    /**
     * Gets the z-index of this renderer
     * @return The z-index of this renderer
     */
    public float getZIndex() { return zIndex; }

    @Override
    public int compareTo(Renderer other)
    {
        // Sorted by zIndex
        if (zIndex < other.getZIndex()) return -1;
        else if (zIndex > other.getZIndex()) return 1;
        else return 0;
    }
}
