package bit.clarksj4.labyrinth.Labyrinth;

import java.util.ArrayList;

import bit.clarksj4.labyrinth.Engine.AudioSource;
import bit.clarksj4.labyrinth.Labyrinth.DwarfState.DwarfEnteringLevelState;
import bit.clarksj4.labyrinth.Labyrinth.DwarfState.DwarfIdleState;
import bit.clarksj4.labyrinth.Labyrinth.DwarfState.DwarfRespawningState;
import bit.clarksj4.labyrinth.Labyrinth.DwarfState.DwarfState;
import bit.clarksj4.labyrinth.Engine.Collider;
import bit.clarksj4.labyrinth.Engine.Component;
import bit.clarksj4.labyrinth.Engine.GameObject;
import bit.clarksj4.labyrinth.Engine.Rigidbody;
import bit.clarksj4.labyrinth.Engine.SpriteRenderer;
import bit.clarksj4.labyrinth.Engine.Tile;
import bit.clarksj4.labyrinth.Engine.Vector;

/**
 * Created by Stephen on 28/05/2016.
 */
public class Dwarf extends Component implements Collider.TileContactListener, Collider.CollisionBeginListener, Collider.CollisionContinueListener
{
    private ArrayList<DwarfDyingListener> dyingListeners;
    private ArrayList<DwarfExitListener> exitListeners;
    private ArrayList<DwarfResetListener> resetListeners;

    private Vector startingPosition;
    private Vector startingLocalScale;

    private Rigidbody rigidbody;
    private SpriteRenderer renderer;

    private DwarfState state;

    /**
     * Creates a new sprite with its upper left corner located at the world coordinate (0, 0)
     */
    public Dwarf(GameObject parent)
    {
        super(parent);

        // Initialize listener collections
        dyingListeners = new ArrayList<>();
        exitListeners = new ArrayList<>();
        resetListeners = new ArrayList<>();
    }

    @Override
    public void start()
    {
        startingPosition = getTransform().getPosition();
        startingLocalScale = getTransform().getLocalScale();

        Collider collider = getComponent(Collider.class);
        collider.addTileContactListener(this);
        collider.addCollisionBeginListener(this);
        collider.addCollisionContinueListener(this);
        setState(new DwarfEnteringLevelState(this, getTransform().getPosition()));

        rigidbody = getComponent(Rigidbody.class);
        renderer = getComponent(SpriteRenderer.class);

        getComponent(AudioSource.class).play("the_looming_battle.ogg");
    }

    @Override
    public void update()
    {
        state.update();

        // Flip image depending upon direction faced
        if (rigidbody.getVelocity().x < 0) renderer.setFlipX(true);
        else renderer.setFlipX(false);

        if (rigidbody.getVelocity().y < 0) renderer.setFlipY(true);
        else renderer.setFlipY(false);
    }

    @Override
    public void onTileContact(Tile[][] contactedTiles) { state.onTileContact(contactedTiles); }

    @Override
    public void onCollisionBegin(Collider other)
    {
        state.onCollisionBegin(other);
    }

    @Override
    public void onCollisionContinue(Collider other)
    {
        state.onCollisionContinue(other);
    }

    public void reset()
    {
        setState(new DwarfRespawningState(this, startingPosition, startingLocalScale));
    }

    public void setState(DwarfState state)
    {
        this.state = state;
        state.init();    // Initialize the state object (may cause the sprite to change state again)
    }

    //
    // Listener methods
    //

    public void addDyingListener(DwarfDyingListener listener) { dyingListeners.add(listener); }
    public void addExitListener(DwarfExitListener listener) { exitListeners.add(listener); }
    public void addResetListener(DwarfResetListener listener) { resetListeners.add(listener); }

    public void removeDyingListener(DwarfDyingListener listener) { dyingListeners.remove(listener); }
    public void removeExitListener(DwarfExitListener listener) { exitListeners.remove(listener); }
    public void removeResetListener(DwarfResetListener listener) { resetListeners.remove(listener); }

    public void notifyDying()
    {
        if (dyingListeners.size() > 0)
        {
            for (DwarfDyingListener listener : dyingListeners)
                listener.onDying(this);
        }
    }

    public void notifyExiting()
    {
        if (exitListeners.size() > 0)
        {
            for (DwarfExitListener listener : exitListeners)
                listener.onExiting(this);
        }
    }

    public void notifyExited()
    {
        if (exitListeners.size() > 0)
        {
            for (DwarfExitListener listener : exitListeners)
                listener.onExited(this);
        }
    }

    public void notifyReset()
    {
        if (resetListeners.size() > 0)
        {
            for (DwarfResetListener listener : resetListeners)
                listener.onReset(this);
        }
    }

    //
    // Listener Interfaces
    //

    public interface DwarfDyingListener
    {
        void onDying(Dwarf dwarf);
    }

    public interface DwarfExitListener
    {
        void onExiting(Dwarf dwarf);
        void onExited(Dwarf dwarf);
    }

    public interface DwarfResetListener
    {
        void onReset(Dwarf dwarf);
    }
}
