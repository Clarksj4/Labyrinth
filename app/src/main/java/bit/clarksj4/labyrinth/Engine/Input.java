package bit.clarksj4.labyrinth.Engine;

/**
 * Input singleton. Contains all inputs types and their associated values as at the current frame.
 */
public class Input
{
    private float[] accelerometerInput;
    private static Input instance = new Input();

    /**
     * private singleton constructor
     */
    private Input()
    {
        accelerometerInput = new float[] { 0, 0, 0 };
    }

    // TODO: other inputs and stuff

    /**
     * Gets the singleton instance
     * @return The singleton instance
     */
    public static Input getInstance() { return instance; }

    /**
     * Gets the accelerometer input as at the current frame.
     * @return The accelerometer input as at the current frame. Returns 0 for all values in array if
     * no accelerometer input is being received.
     */
    public float[] getAccelerometerInput() { return accelerometerInput; }

    /**
     * Sets the accelerometer input for the current frame
     * @param accelerometerInput The accelerometer input for the current frame
     */
    public void setAccelerometerInput(float[] accelerometerInput) { this.accelerometerInput = accelerometerInput; }
}
