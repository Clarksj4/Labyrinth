package bit.clarksj4.labyrinth.Labyrinth;

import java.util.ArrayList;

import bit.clarksj4.labyrinth.Engine.Component;
import bit.clarksj4.labyrinth.Engine.AnimationController;
import bit.clarksj4.labyrinth.Engine.GameObject;

/**
 * Script controls door object. Door object opens when dwarf approaches.
 */
public class Door extends Component implements AnimationController.AnimationListener
{
    private ArrayList<DoorListener> doorListeners;
    private AnimationController animationController;

    public Door(GameObject gameObject)
    {
        super(gameObject);
        doorListeners = new ArrayList<>();
    }

    @Override
    public void start()
    {
        animationController = gameObject.getComponent(AnimationController.class);
        animationController.playAnimation("Closed");
        animationController.addListener(this);
    }

    public void open()
    {
        if (!animationController.getCurrentAnimation().getName().equals("Open"))
            animationController.playAnimation("Opening");
    }

    public void close()
    {
        if (!animationController.getCurrentAnimation().getName().equals("Closed"))
            animationController.playAnimation("Closing");
    }

    @Override
    public void onAnimationComplete(String animation)
    {
        switch (animation)
        {
            case "Opening":                                  // Door has completed opening animation
                animationController.playAnimation("Open");   // Set door to open
                notifyOpen();                                // notify listeners that the door is open
                break;

            case "Closing":                                  // Door has completed closing animation
                animationController.playAnimation("Closed"); // Set door to closed
                notifyClosed();                              // notify listeners that the door is closed
                break;
        }
    }

    public void addListener(DoorListener listener)
    {
        doorListeners.add(listener);
    }

    public void removeListener(DoorListener listener)
    {
        doorListeners.remove(listener);
    }

    private void notifyClosed()
    {
        // Notify listeners that the door has closed
        if (doorListeners.size() > 0)
        {
            for (DoorListener listener : doorListeners)
                listener.onDoorClosed(this);
        }
    }

    private void notifyOpen()
    {
        // Notify listeners that the door has opened
        if (doorListeners.size() > 0)
        {
            for (DoorListener listener : doorListeners)
                listener.onDoorOpen(this);
        }
    }

    public interface DoorListener
    {
        void onDoorClosed(Door door);
        void onDoorOpen(Door door);
    }
}
