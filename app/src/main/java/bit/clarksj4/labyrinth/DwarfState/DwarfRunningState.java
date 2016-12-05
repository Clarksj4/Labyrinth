package bit.clarksj4.labyrinth.DwarfState;

import bit.clarksj4.labyrinth.Engine.AnimationController;
import bit.clarksj4.labyrinth.Labyrinth.Door;
import bit.clarksj4.labyrinth.Labyrinth.Dwarf;
import bit.clarksj4.labyrinth.Engine.Collider;
import bit.clarksj4.labyrinth.Engine.Rectangle;
import bit.clarksj4.labyrinth.Engine.Rigidbody;
import bit.clarksj4.labyrinth.Engine.Tile;
import bit.clarksj4.labyrinth.Engine.Vector;

/**
 * Created by Stephen on 19/05/2016.
 */
public class DwarfRunningState extends DwarfState
{
    private static final float RUNNING_SPEED_THRESHOLD = 0.2f;

    public DwarfRunningState(Dwarf dwarf)
    {
        super(dwarf);

        AnimationController animController = dwarf.getGameObject().getComponent(AnimationController.class);
        animController.playAnimation("Running");
    }

    @Override
    public void update()
    {
        super.update();

        Rigidbody rigidbody = dwarf.getComponent(Rigidbody.class);

        // If velocity is within threshold, switch to idle state
        if (rigidbody.getVelocity().getMagnitude() < RUNNING_SPEED_THRESHOLD)
            setState(new DwarfIdleState(dwarf));
    }

    @Override
    public void onCollisionBegin(Collider other)
    {
        super.onCollisionBegin(other);

        // Check if dwarf overlaps with door centre
        if (other.getGameObject().getName().equals("Door"))
        {
            Collider collider = dwarf.getComponent(Collider.class);

            // If the dwarf collider is atleast partially over the center of the door collider
            Vector doorCentre = other.getBounds().center;
            if (collider.contains(doorCentre))
                setState(new DwarfExitingState(dwarf, other.getComponent(Door.class))); // Exit!
        }
    }

    public void onCollisionContinue(Collider other)
    {
        super.onCollisionContinue(other);

        // Check if dwarf overlaps with door centre
        if (other.getGameObject().getName().equals("Door"))
        {
            Collider collider = dwarf.getComponent(Collider.class);

            // If the dwarf collider is atleast partially over the center of the door collider
            Vector otherPosition = other.getBounds().center;
            if (collider.contains(otherPosition))
                setState(new DwarfExitingState(dwarf, other.getComponent(Door.class))); // Exit!
        }
    }

    @Override
    public void onTileContact(Tile[][] contactedTiles)
    {
        super.onTileContact(contactedTiles);

        for (int column = 0; column < contactedTiles[0].length; column++)
        {
            for (int row = 0; row < contactedTiles.length; row++)
            {
                Collider collider = dwarf.getComponent(Collider.class);
                Rectangle tileRectangle = contactedTiles[row][column].getBounds();

                // Hole
                if (contactedTiles[row][column].getValue() == 2)
                {
                    if (collider.contains(tileRectangle.center))
                        setState(new DwarfFallingState(dwarf, tileRectangle.center));
                }
            }
        }
    }

    @Override
    public void stateClosing() { /* Nothing - running state does not listen to anything */ }
}
