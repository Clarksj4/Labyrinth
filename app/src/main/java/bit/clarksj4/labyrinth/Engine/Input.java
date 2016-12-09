package bit.clarksj4.labyrinth.Engine;

/**
 * Input singleton. Contains all inputs types and their associated values as at the current frame.
 */
public class Input
{
    private static float[] _accelerometerInput = new float[] { 0, 0, 0 };

    // TODO: other inputs and stuff

    /**
     * Gets the accelerometer input as at the current frame.
     * @return The accelerometer input as at the current frame. Returns 0 for all values in array if
     * no accelerometer input is being received.
     */
    public static float[] getAccelerometerInput() { return _accelerometerInput; }

    /**
     * Sets the accelerometer input for the current frame
     * @param accelerometerInput The accelerometer input for the current frame
     */
    public static void setAccelerometerInput(float[] accelerometerInput) { _accelerometerInput = accelerometerInput; }
}
