package bit.clarksj4.labyrinth.Labyrinth.DwarfState;

import bit.clarksj4.labyrinth.Engine.AnimationController;
import bit.clarksj4.labyrinth.Labyrinth.Dwarf;
import bit.clarksj4.labyrinth.Engine.Physics;

/**
 * Created by Stephen on 19/05/2016.
 */
public class DwarfIdleState extends DwarfState
{
    public static final float GRAVITY_THRESHOLD = 1;

    public DwarfIdleState(Dwarf dwarf)
    {
        super(dwarf);

        AnimationController animController = dwarf.getGameObject().getComponent(AnimationController.class);
        animController.playAnimation("Idle");
    }

    @Override
    public void update()
    {
        super.update();

        // If either x or y velocity exceed the threshold, then switch to running state
        if (Physics.gravity.getMagnitude() > GRAVITY_THRESHOLD)
            dwarf.setState(new DwarfRunningState(dwarf));
    }

    @Override
    public void stateClosing() { /* Nothing -> idle state doesn't listen to anythin */ }

}
