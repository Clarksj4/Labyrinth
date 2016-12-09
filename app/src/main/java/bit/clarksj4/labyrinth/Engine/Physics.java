package bit.clarksj4.labyrinth.Engine;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Physics singleton Responsible for checking for collisions between rigidbodies and colliders
 */
public class Physics
{
    // TODO: Add methods for calculating collisions with a given point and a given rectangle

    /** The gravity of earth = 9.807m/s */
    private static final float EARTH_GRAVITY = 9.807f;

    public static Vector gravity = new Vector(0, EARTH_GRAVITY);

    // Maintain separate list of moving and static colliders so as to reduce collision checks
    private static LinkedList<Collider> colliders = new LinkedList<>();
    private static LinkedList<Rigidbody> rigidbodies = new LinkedList<>();
    private static TileMap _tileMap;

    /**
     * Moves each rigidbody registered to this physics object then checks for collisions
     */
    static void update()
    {
        // Move each rigidbody
        for (Rigidbody rigidbody : rigidbodies)
        {
            if (rigidbody.isEnabled())
                rigidbody.resolveForces();
        }

        // TODO: Need better collision resolution. What if an extraction causes a collision?
        checkCollisions();
    }

    /**
     * Registers a rigidbody with the physics object, the rigidbody will be moved by the physics
     * engine
     * @param rigidbody The new rigidbody
     */
    static void addRigidbody(Rigidbody rigidbody)
    {
        colliders.remove(rigidbody.getCollider());
        colliders.addFirst(rigidbody.getCollider());
        rigidbodies.addFirst(rigidbody);
    }

    /**
     * Unregisters a rigidbody from this physics object
     * @param rigidbody The rigidbody to remove
     */
    static void removeRigidbody(Rigidbody rigidbody) { rigidbodies.remove(rigidbody); }

    /**
     * Registers a collider with the physics object
     * @param collider The new collider
     */
    static void addCollider(Collider collider) { colliders.addLast(collider); }

    /**
     * Unregisters a collider with the physics object
     * @param collider The collider to remove
     */
    static void removeCollider(Collider collider) { colliders.remove(collider); }

    /**
     * Registers a tilemap with the physics object
     * @param tileMap The new tilemap
     */
    static void addTileMap(TileMap tileMap) { _tileMap = tileMap; }

    /**
     * Checks for collisions between rigidbodies and colliders, and rigidbodies and the tilemap
     */
    private static void checkCollisions()
    {
        // Check collisions for each collider
        for (Rigidbody rigidbody : rigidbodies)
        {
            // Only check collisions for rigidbodies that are enabled
            if (rigidbody.isEnabled())
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
                if (_tileMap != null)
                {
                    // Get intersection between this collider and tileMap
                    Rectangle intersection = collider.intersection(_tileMap.getBounds());
                    if (intersection!= null)
                    {
                        // Get array of tiles that the collider is touching and pass to collider
                        Tile[][] contactedTiles = _tileMap.getTiles(intersection);
                        collider.onTileContact(contactedTiles);
                    }
                }
            }
        }
    }
}
