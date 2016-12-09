package bit.clarksj4.labyrinth.DwarfState;

import bit.clarksj4.labyrinth.Engine.Rigidbody;
import bit.clarksj4.labyrinth.Labyrinth.Door;
import bit.clarksj4.labyrinth.Labyrinth.Dwarf;
import bit.clarksj4.labyrinth.Engine.Time;
import bit.clarksj4.labyrinth.Engine.Vector;

/**
 * Created by Stephen on 1/06/2016.
 */
public class DwarfExitingState extends DwarfState implements Door.DoorListener {
    private static final float DURATION = 0.25f;

    private Door door;
    private Vector startingScale;
    private Vector startingPosition;
    private Vector destination;
    private float time;
    private boolean exited;

    public DwarfExitingState(Dwarf dwarf, Door door) {
        super(dwarf);

        this.door = door;
        destination = door.getTransform().getPosition();
        startingScale = dwarf.getTransform().getScale();
        startingPosition = dwarf.getTransform().getPosition();
        exited = false;

        dwarf.getComponent(Rigidbody.class).setEnabled(false);
    }

    @Override
    public void init()
    {
        super.init();
        dwarf.notifyExiting();
    }

    @Override
    public void update()
    {
        // Get smaller and move towards door centre to give appearance of moving through door
        if (!exited)
        {
            time += Time.getDeltaTime();

            // Percentage of duration that has expired
            float t = time / DURATION;

            // Lerp from starting scale to zero
            Vector scale = Vector.lerp(startingScale, Vector.zero(), t);
            dwarf.getTransform().setLocalScale(scale);

            // Lerp from starting position to door position
            Vector position = Vector.lerp(startingPosition, destination, t);
            dwarf.getTransform().setPosition(position);

            // If duration expired
            if (time > DURATION)
            {
                exited = true;
                door.close();               // Close door
                door.addListener(this);      // Listen for closure
            }
        }
    }

    @Override
    public void stateClosing()
    {
        door.removeListener(this);
        dwarf.getComponent(Rigidbody.class).setEnabled(true);
    }

    @Override
    public void onDoorClosed(Door door)
    {
        dwarf.notifyExited();       // Tell listeners the dwarf has exited
        dwarf.reset();              // Reset dwarf position and scale to spawn location
    }

    @Override
    public void onDoorOpen(Door door) { /* Nothing */ }
}
