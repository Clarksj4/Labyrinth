package bit.clarksj4.labyrinth.DwarfState;

import bit.clarksj4.labyrinth.Labyrinth.Door;
import bit.clarksj4.labyrinth.Labyrinth.Dwarf;
import bit.clarksj4.labyrinth.Engine.Collider;
import bit.clarksj4.labyrinth.Engine.Tile;

/**
 * Created by Stephen on 19/05/2016.
 */
public abstract class DwarfState
{
    protected Dwarf dwarf;

    public DwarfState(Dwarf dwarf) { this.dwarf = dwarf; }

    public void setState(DwarfState newState)
    {
        stateClosing();
        dwarf.setState(newState);
    }

    public void init() { /* Empty virtual method*/ }
    public void update() { /* Empty virtual method */ }
    public void onCollisionBegin(Collider other)
    {
        if (other.getGameObject().getName().equals("Door"))
            other.getComponent(Door.class).open();
    }
    public void onCollisionContinue(Collider other) { /* Empty virtual method */ }
    public void onTileContact(Tile[][] contactedTiles) { /* Empty virtual method */ }

    // All states MUST implement closing state method -> gives states a chance to unregister as listeners
    public abstract void stateClosing();

    public String getName() { return this.getClass().getSimpleName(); }
}
