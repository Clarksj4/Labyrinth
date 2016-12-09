package bit.clarksj4.labyrinth.Labyrinth;

import bit.clarksj4.labyrinth.Engine.Component;
import bit.clarksj4.labyrinth.Engine.Game;
import bit.clarksj4.labyrinth.Engine.GameObject;
import bit.clarksj4.labyrinth.Engine.Input;
import bit.clarksj4.labyrinth.Engine.Physics;
import bit.clarksj4.labyrinth.Engine.Vector;

/**
 * Script sets the gravity of the world based on the accelerometer input
 */
public class AccelerometerGravity extends Component
{
    private static final float GRAVITY_ALPHA = 0.8f;

    public AccelerometerGravity(GameObject gameObject)
    {
        super(gameObject);
    }

    @Override
    public void update()
    {
        // Calculate gravity (from android developer docs)
        float[] accelerometer = Input.getAccelerometerInput();
        Vector gravity = Physics.gravity.divide(Game.UNIT / 2);

        gravity.x = -(GRAVITY_ALPHA * -gravity.x + (1 - GRAVITY_ALPHA) * accelerometer[0]);
        gravity.y = GRAVITY_ALPHA * gravity.y + (1 - GRAVITY_ALPHA) * accelerometer[1];

        // Set gravity based on accelerometer input
        Physics.gravity = (gravity.scale(Game.UNIT / 2));
    }
}
