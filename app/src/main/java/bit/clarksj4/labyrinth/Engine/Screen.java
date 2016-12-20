package bit.clarksj4.labyrinth.Engine;

/**
 * Created by StickasaurusRex on 21-Dec-16.
 */

public class Screen
{
    private static float width;
    private static float height;
    private static GameContext _context;

    public static void init(GameContext context)
    {
        _context = context;
    }

    public static void release()
    {
        _context = null;
    }

    public static float getWidth() { return width; }
    public static float getHeight() { return height; }
    public static Vector getSize() { return new Vector(width, height); }
}
