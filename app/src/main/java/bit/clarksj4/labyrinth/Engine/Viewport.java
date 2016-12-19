package bit.clarksj4.labyrinth.Engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;

import java.util.Collection;

/**
 * A viewport component for renderer a rectangular view of the game world
 */
public class Viewport extends Component
{
    public static Viewport main() { return _main; }
    private static Viewport _main;

    private static final float DEFAULT_MAGNITUDE = 750;

    private Display display;
    private Rectangle destinationBounds;
    private Vector sourceSize;

    /**
     * A new viewport component attached to the given game object
     * @param parent The game object this viewport is attached to
     */
    public Viewport(GameObject parent)
    {
        super(parent);

        // Display to draw to defaults to the first display registered in the graphics object
        display = Graphics.getDisplays().get(0);

        // Register self with graphics object
        Graphics.addViewport(this);


        if (_main == null) _main = this;
    }

    @Override
    public void start()
    {
        if (_main == null)
            _main = this;

        // TODO: Display doesn't know its width or height until now, find how to set these values earlier (maybe use normalized values)
        setSourceMagnitude(DEFAULT_MAGNITUDE);
        destinationBounds = new Rectangle(Vector.zero(), display.getSize());
    }

    @Override
    public void destroy()
    {
        // Remove reference from Graphics object
        Graphics.removeViewport(this);
        _main = null;
    }

    /**
     * Draws the renderers that this viewport can see
     * @param renderers All renderers in the game world
     */
    public void draw(Collection<Renderer> renderers)
    {
        // TODO: adjust the scale at which things are drawn by the display density

        Canvas canvas = display.lockCanvas();
        Rectangle bounds = getSourceBounds();

        // Clear the canvas by replacing each pixel with a transparent pixel
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        // Calculate the difference in size between the source and destination bounds
        Vector sizeDelta = destinationBounds.size.divide(sourceSize);

        // Scale the canvas by the difference
        canvas.scale(sizeDelta.x, sizeDelta.y);

        for (Renderer renderer : renderers)
        {
            if (renderer.isEnabled())
            {
                // Calculate renderer position on canvas
                Vector rendererPosition = renderer.getBounds().center;
                Vector viewportPosition = bounds.center;
                Vector positionDelta = rendererPosition.subtract(viewportPosition);

                Vector canvasCenter = bounds.size.divide(2);
                Vector positionOnCanvas = positionDelta.add(canvasCenter);

                // Calculate the area of renderer to draw
                Rectangle intersection = bounds.intersection(renderer.getBounds());
                if (intersection != null)
                    renderer.draw(canvas, intersection, positionOnCanvas);
            }
        }
        display.drawCanvas(canvas);
    }

    /**
     * Gets the bounds in the game world that this viewport will draw
     * @return The world boudns that this viewport will draw
     */
    public Rectangle getSourceBounds() { return new Rectangle(getTransform().getPosition(), sourceSize); }

    /**
     * Gets the magnitude of the world bounds that this viewport will draw
     * @param magnitude The magnitude of the world bounds that this viewport will draw
     */
    public void setSourceMagnitude(float magnitude)
    {
        sourceSize = display.getSizeNormalized().scale(magnitude);
    }

    /**
     * Sets the size of the game world bounds that this viewport will draw
     * @param sourceSize The size of the game world bounds that this viewport will draw
     */
    public void setSourceSize(Vector sourceSize) { this.sourceSize = sourceSize; }

    /**
     * Sets the bounds on the destination surface that this viewport will draw to
     * @param destinationBounds The bounds on the destination surface that this viewport will draw to
     */
    public void setDestinationBounds(Rectangle destinationBounds) { this.destinationBounds = destinationBounds; }

    /**
     * Sets the display this viewport will draw to
     * @param display The display this viewport will draw to
     */
    public void setDisplay(Display display) { this.display = display; }

    public static void setMainViewport(Viewport main) { _main = main; }
}
