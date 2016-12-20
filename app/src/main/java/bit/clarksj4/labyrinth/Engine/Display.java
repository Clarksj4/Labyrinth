package bit.clarksj4.labyrinth.Engine;

import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.SurfaceView;

/**
 * Encapsulates a drawing surface
 */
public class Display
{
    // TODO: clear reference to display when game pauses, get new reference to surface view when resumed
    // TODO: Use display density to adjust sizes of things at startup

    private SurfaceView surfaceView;
    private DisplayMetrics displayMetrics;

    /**
     * A new display that draws to the given surface view and has the given metrics.
     * @param surfaceView The surface view to draw to
     * @param displayMetrics The density of screen pixels and other display metrics
     */
    public Display(SurfaceHolder surfaceView, DisplayMetrics displayMetrics)
    {
        this.surfaceView = surfaceView;
        this.displayMetrics = displayMetrics;
    }

    /**
     * Locks and returns the display's canvas object to allow for drawing.
     * @return The displays canvas object
     */
    public Canvas lockCanvas() { return surfaceView.getHolder().lockCanvas(); }

    /**
     * Draws the given canvas to the display on the UI Thread.
     * @param canvas The canvas to draw. This should be the canvas obtained from the lock canvas
     *               method
     */
    public void drawCanvas(Canvas canvas) { surfaceView.getHolder().unlockCanvasAndPost(canvas); }

    /**
     * Gets the width of this display
     * @return The width of this display in pixels
     */
    public float getWidth() { return surfaceView.getWidth(); }

    /**
     * Gets the height of this display
     * @return The height of this display in pixels
     */
    public float getHeight() { return surfaceView.getHeight(); }

    /**
     * Gets the size of this display
     * @return The size of this display in pixles
     */
    public Vector getSize() { return new Vector(getWidth(), getHeight()); }

    /**
     * Gets the normalized size of this display. Use this to get the aspect ratio of the display.
     * @return A normalized vector of the size of this display
     */
    public Vector getSizeNormalized() { return getSize().normalized(); }

    /**
     * Gets the pixel density of this display
     * @return The pixel density of this display.
     */
    public float getDensity() { return displayMetrics.density; }
}
