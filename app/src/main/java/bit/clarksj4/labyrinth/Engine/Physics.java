package bit.clarksj4.labyrinth.Engine;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Physics singleton Responsible for checking for collisions between rigidbodies and colliders
 */
public class Physics
{
    /**
     * The gravity of earth = 9.807m/s
     */
    private static final float EARTH_GRAVITY = 9.807f;

    // Maintain separate list of moving and static colliders so as to reduce collision checks
    private LinkedList<Collider> colliders;
    private LinkedList<Rigidbody> rigidbodies;
    private TileMap tileMap;
    private Vector gravity;
    private static Physics instance = new Physics();

    private Physics()
    {
        colliders = new LinkedList<>();
        rigidbodies = new LinkedList<>();

        gravity = new Vector(0, EARTH_GRAVITY);
    }

    // TODO: Add methods for calculating collisions with a given point and a given rectangle

    /**
     * Moves each rigidbody registered to this physics object then checks for collisions
     */
    public void update()
    {
        // Move each rigidbody
        for (Rigidbody rigidbody : rigidbodies)
        {
            if (rigidbody.getEnabled())
                rigidbody.resolveForces();
        }

        // TODO: Need better collision resolution. What if an extraction causes a collision?
        checkCollisions();
    }

    /**
     * Checks for collisions between rigidbodies and colliders, and rigidbodies and the tilemap
     */
    public void checkCollisions()
    {
        // Check collisions for each collider
        for (Rigidbody rigidbody : rigidbodies)
        {
            // Only check collisions for rigidbodies that are enabled
            if (rigidbody.getEnabled())
            {
                // Get associated collider
                Collider collider = rigidbody.getCollider();

                // Walk list backwards while there are items
                Iterator<Collider> descendingIterator = colliders.descendingIterator();
                while (descendingIterator.hasNext())
                {
                    // Get next collider, stop walking list if the encountered object is the one being
                    // examined in the outer loop
                    Collider other = descendingIterator.next();
                    if (other == collider) break;

                    // Check for a collision, notify objects if there is one.
                    if (collider.intersects(other))
                    {
                        // Both objects get notified of collision with their opposite
                        collider.registerCollision(other);
                        other.registerCollision(collider);
                    }
                }

                // Collider has handled all collider collisions it is possible of having
                collider.resolveCollisions();

                // Check for tileMap collisions
                if (tileMap != null)
                {
                    // Get intersection between this collider and tileMap
                    Rectangle intersection = collider.intersection(tileMap.getBounds());
                    if (intersection!= null)
                    {
                        // Get array of tiles that the collider is touching and pass to collider
                        Tile[][] contactedTiles = tileMap.getTiles(intersection);
                        collider.onTileContact(contactedTiles);
                    }
                }
            }
        }
    }

    /**
     * Registers a rigidbody with the physics object, the rigidbody will be moved by the physics
     * engine
     * @param rigidbody The nwe rigidbody
     */
    public void registerRigidbody(Rigidbody rigidbody)
    {
        colliders.remove(rigidbody.getCollider());
        colliders.addFirst(rigidbody.getCollider());
        rigidbodies.addFirst(rigidbody);
    }

    /**
     * Unregisters a rigidbody from this physics object
     * @param rigidbody The rigidbody to remove
     */
    public void unregisterRigidbody(Rigidbody rigidbody) { rigidbodies.remove(rigidbody); }

    /**
     * Registers a collider with the physics object
     * @param collider The new collider
     */
    public void registerCollider(Collider collider) { colliders.addLast(collider); }

    /**
     * Unregisters a collider with the physics object
     * @param collider The collider to remove
     */
    public void unregisterCollider(Collider collider) { colliders.remove(collider); }

    /**
     * Registers a tilemap with the physics object
     * @param tileMap The new tilemap
     */
    public void registerTileMap(TileMap tileMap) { this.tileMap = tileMap; }

    /**
     * Gets the singleton instance
     * @return The singleton instance
     */
    public static Physics getInstance() { return instance; }

    /**
     * Gets the force of gravity being use by the physics object
     * @return The force of gravity
     */
    public Vector getGravity() { return new Vector(gravity.x, gravity.y); }

    /**
     * Sets the force of gravity being used by the physics object
     * @param gravity The force of gravity
     */
    public void setGravity(Vector gravity) { this.gravity = gravity; }
}
