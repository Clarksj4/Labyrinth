package bit.clarksj4.labyrinth.Engine;

/**
 * Input singleton. Contains all inputs types and their associated values as at the current frame.
 */
public class Input
{
    private static GameContext _context;

    // TODO: other inputs and stuff
    public static void init(GameContext context) { _context = context; }

    /**
     * Gets the accelerometer input as at the current frame.
     * @return The accelerometer input as at the current frame. Returns 0 for all values in array if
     * no accelerometer input is being received.
     */
    public static float[] getAccelerometerInput() { return _context.getAccelerometerInput(); }
}
