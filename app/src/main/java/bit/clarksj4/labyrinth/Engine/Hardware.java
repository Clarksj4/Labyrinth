package bit.clarksj4.labyrinth.Engine;

/**
 * Created by StickasaurusRex on 11-Dec-16.
 */

public class Hardware
{
    private static GameContext _context;

    static void init(GameContext context)
    {
        _context = context;
    }

    public static void vibrate(long milliseconds)
    {
        _context.vibrate(milliseconds);
    }
}
