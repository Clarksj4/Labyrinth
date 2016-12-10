package bit.clarksj4.labyrinth.Labyrinth.DwarfState;

import bit.clarksj4.labyrinth.Engine.AnimationController;
import bit.clarksj4.labyrinth.Engine.GameObject;
import bit.clarksj4.labyrinth.Engine.MathExtension;
import bit.clarksj4.labyrinth.Engine.Renderer;
import bit.clarksj4.labyrinth.Engine.Rigidbody;
import bit.clarksj4.labyrinth.Engine.Time;
import bit.clarksj4.labyrinth.Engine.Transform;
import bit.clarksj4.labyrinth.Engine.Vector;
import bit.clarksj4.labyrinth.Engine.Viewport;
import bit.clarksj4.labyrinth.Engine.World;
import bit.clarksj4.labyrinth.Labyrinth.Dwarf;

/**
 * Created by StickasaurusRex on 10-Dec-16.
 */

public class DwarfEnteringLevelState extends DwarfState
{
    private static final float SCREEN_SHAKE_DURATION = 1f;
    private static final float DWARF_FALL_DURATION = 0.75f;

    private Viewport followCamera;
    private Renderer renderer;
    private Vector offScreenPosition;
    private Vector landingPosition;
    private float time;
    private boolean even;

    public DwarfEnteringLevelState(Dwarf dwarf, Vector landingPosition)
    {
        super(dwarf);

        this.landingPosition = landingPosition;

        // Set to idle animation
        dwarf.getComponent(AnimationController.class).playAnimation("Idle");

        // Turn off collisions
        dwarf.getComponent(Rigidbody.class).setEnabled(false);

        // detatch camera
        // set position to just above the camera's top bounds
        followCamera = dwarf.getComponentInChildren(Viewport.class);
        renderer = dwarf.getComponent(Renderer.class);

        followCamera.getTransform().setParent(null);
        positionDwarfOffscreen();

        GameObject timer = World.current().findGameObjectByName("Current time");
        timer.setEnabled(false);
    }

    @Override
    public void update()
    {
        super.update();

        time += Time.getDeltaTime();
        if (time > DWARF_FALL_DURATION + SCREEN_SHAKE_DURATION)
        {
            // TODO: notify entered level, NOT reset
            dwarf.notifyReset();                                // Notify listeners that the reset has occurred
            setState(new DwarfIdleState(dwarf));                // Return to idle state
        }

        else if (time > DWARF_FALL_DURATION)
        {
            dwarf.getTransform().setPosition(landingPosition);
            Transform cameraTransform = followCamera.getTransform();

            if (even)
                cameraTransform.setPosition(cameraTransform.getPosition().add(MathExtension.onUnitCircle().scale(3)));

            else
                cameraTransform.setPosition(landingPosition);

            even = !even;
        }

        else
        {
            // Percentage of time that has passed
            float t = time / DWARF_FALL_DURATION;

            // Lerp between position at beginning of reset and the position to reset to
            Vector position = Vector.lerp(offScreenPosition, landingPosition, t);
            dwarf.getTransform().setPosition(position);
        }
    }

    @Override
    public void stateClosing()
    {
        // Turn on collisions
        dwarf.getComponent(Rigidbody.class).setEnabled(true);
        followCamera.getTransform().setParent(dwarf.getTransform());

        GameObject timer = World.current().findGameObjectByName("Current time");
        timer.setEnabled(true);
    }

    private void positionDwarfOffscreen()
    {
        // Point at the top-centre of the viewport's bounds
        Vector topCentre = new Vector(followCamera.getSourceBounds().center.x,
                                      followCamera.getSourceBounds().getTop());

        // Point above the top of the viewport at which the dwarf can be positioned and not be visible to the viewport
        offScreenPosition = new Vector(topCentre.x,
                                       topCentre.y + (renderer.getBounds().size.y / 2));

        dwarf.getTransform().setPosition(offScreenPosition);
    }
}
