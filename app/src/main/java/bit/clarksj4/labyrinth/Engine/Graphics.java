package bit.clarksj4.labyrinth.Engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

/**
 * Responsible for drawing viewports and their perceived renderers to the devices screen.
 */
public class Graphics
{
    private static ArrayList<Display> displays = new ArrayList<>();
    private static ArrayList<Viewport> viewports = new ArrayList<>();
    private static ArrayList<Renderer> renderers = new ArrayList<>();

    static void release()
    {
        displays = null;
        viewports = null;
        renderers = null;
    }

    /** Tells all viewports to draw */
    static void draw()
    {
        for (Viewport viewport : viewports)
            viewport.draw(renderers);
    }

    /**
     * Registers a new display with this graphics object. A display presents a new surface to draw
     * upon
     * @param display The new display
     */
    static void addDisplay(Display display) { displays.add(display); }

    static void removeDisplay(Display display) { displays.remove(display); }

    /**
     * Registers a viewport with this graphics object.
     * @param viewport The new viewport
     */
    static void addViewport(Viewport viewport) { viewports.add(viewport); }

    /**
     * Registers a renderer with this graphics object
     * @param renderer The new renderer
     */
    static void addRenderer(Renderer renderer)
    {
        renderers.add(renderer);
        Collections.sort(renderers);
    }

    /**
     * Unregisters the given viewport from this graphics object
     * @param viewport The viewport to unregister
     */
    static void removeViewport(Viewport viewport) { viewports.remove(viewport); }

    /**
     * Unregisters the given renderer from this graphics object
     * @param renderer The renderer to unregister
     */
    static void removeRenderer(Renderer renderer) { renderers.remove(renderer); }

    /**
     * Gets the list of known displays
     * @return The list of known displays
     */
    public static ArrayList<Display> getDisplays() { return displays; }
}
