package bit.clarksj4.labyrinth.DwarfState;

import bit.clarksj4.labyrinth.Engine.Rigidbody;
import bit.clarksj4.labyrinth.Labyrinth.Dwarf;
import bit.clarksj4.labyrinth.Engine.Time;
import bit.clarksj4.labyrinth.Engine.Vector;

/**
 * Created by Stephen on 1/06/2016.
 */
public class DwarfResettingState extends DwarfState
{
    private static final float DURATION = 0.5f;

    private Vector scaleToResetTo;
    private Vector positionToResetTo;
    private Vector positionResettingFrom;
    private float time;

    public DwarfResettingState(Dwarf dwarf, Vector positionToResetTo, Vector scaleToResetTo)
    {
        super(dwarf);

        this.scaleToResetTo = scaleToResetTo;
        this.positionToResetTo = positionToResetTo;
        positionResettingFrom = dwarf.getTransform().getPosition();

        // Turn off collisions
        dwarf.getComponent(Rigidbody.class).setEnabled(false);
    }

    @Override
    public void update()
    {
        time += Time.getDeltaTime();

        // Percentage of time that has passed
        float t = time / DURATION;

        // Lerp between position at beginning of reset and the position to reset to
        Vector position = Vector.lerp(positionResettingFrom, positionToResetTo, t);
        dwarf.getTransform().setPosition(position);

        // If duration has expired
        if (time > DURATION)
        {
            dwarf.getTransform().setLocalScale(scaleToResetTo); // Set scale to the reset scale
            dwarf.notifyReset();                                // Notify listeners that the reset has occurred
            setState(new DwarfIdleState(dwarf));                // Return to idle state
        }

    }

    @Override
    public void stateClosing()
    {
        dwarf.getComponent(Rigidbody.class).setEnabled(true);
    }
}
