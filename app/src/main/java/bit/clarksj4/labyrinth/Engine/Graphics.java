package bit.clarksj4.labyrinth.Engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Graphics object singleton. Responsible for drawing viewports and their perceived renderers to
 * the devices screen.
 */
public class Graphics
{
    /**
     * Gets the singleton instance
     * @return The singleton instance
     */
    public static Graphics getInstance() { return instance; }
    private static Graphics instance = new Graphics();

    private ArrayList<Display> displays;
    private ArrayList<Viewport> viewports;
    private PriorityQueue<Renderer> renderers;

    /**
     * Private singleton constructor
     */
    private Graphics()
    {
        displays = new ArrayList<>();
        viewports = new ArrayList<>();
        renderers = new PriorityQueue<>();
    }

    /**
     * Tells all viewports to draw
     */
    public void draw()
    {
        for (Viewport viewport : viewports)
            viewport.draw(renderers);
    }

    /**
     * Registers a new display with this graphics object. A display presents a new surface to draw
     * upon
     * @param display The new display
     */
    public void addDisplay(Display display) { displays.add(display); }

    /**
     * Registers a viewport with this graphics object.
     * @param viewport The new viewport
     */
    public void registerViewport(Viewport viewport) { viewports.add(viewport); }

    /**
     * Registers a renderer with this graphics object
     * @param renderer The new renderer
     */
    public void registerRenderer(Renderer renderer) { renderers.add(renderer); }

    /**
     * Unregisters the given viewport from this graphics object
     * @param viewport The viewport to unregister
     */
    public void removeViewport(Viewport viewport) { viewports.remove(viewport); }

    /**
     * Unregisters the given renderer from this graphics object
     * @param renderer The renderer to unregister
     */
    public void removeRenderer(Renderer renderer) { renderers.remove(renderer); }

    /**
     * Gets the list of known displays
     * @return The list of known displays
     */
    public ArrayList<Display> getDisplays() { return displays; }
}
