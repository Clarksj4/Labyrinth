package bit.clarksj4.labyrinth.Engine;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Physics component for detecting collisions.
 */
public class Collider extends Component
{
    /** Default position has no offset from parent gameObject */
    private static final Vector DEFAULT_POSITION = Vector.zero();

    /** Default size is one game unit */
    private static final Vector DEFAULT_SIZE = Vector.one().scale(Game.UNIT);

    /** By default collider is not a trigger collider */
    private static final boolean DEFAULT_IS_TRIGGER = false;

    // Listeners
    private ArrayList<CollisionBeginListener> collisionBeginListeners;
    private ArrayList<CollisionContinueListener> collisionContinueListeners;
    private ArrayList<CollisionEndListener> collisionEndListeners;
    private ArrayList<TileContactListener> tileContactListeners;

    private HashSet<Collider> collisions;               // Colliders this collider is currently intersecting
    private HashSet<Collider> currentFrameCollisions;   // Colliders this collider has intersected with during the current frame

    private boolean isTrigger;
    private Vector position;                            // Offset from game objects position
    private Vector size;

    public Collider(GameObject parent) {
        super(parent);

        position = DEFAULT_POSITION;
        size = DEFAULT_SIZE;
        isTrigger = DEFAULT_IS_TRIGGER;

        collisions = new HashSet<>();
        currentFrameCollisions = new HashSet<>();
        collisionBeginListeners = new ArrayList<>();
        collisionContinueListeners = new ArrayList<>();
        collisionEndListeners = new ArrayList<>();
        tileContactListeners = new ArrayList<>();

        // Collider gets registered with Physics object
        Physics.getInstance().registerCollider(this);
    }

    @Override
    public void destroy()
    {
        // When destroyed, remove reference to this collider from physics object
        Physics.getInstance().unregisterCollider(this);
    }

    /**
     * Checks whether this collider intersects with the given collider
     * @param other Collider to check for an intersection with
     * @return True if this collider intersects with the given one
     */
    public boolean intersects(Collider other) { return getBounds().intersects(other.getBounds()); }

    /**
     * Gets the intersection area where this collider overlaps with the given rectangle
     * @param other The rectangle to calculate the intersection area with
     * @return A rectangle representing the intersection between this collider and the given
     * rectangle OR null if there is no intersection
     */
    public Rectangle intersection(Rectangle other) { return getBounds().intersection(other); }

    /**
     * Checks whether the given point is within the bounds of this collider
     * @param point The point to check if it is contained in this colliders area
     * @return True if the given point is contained within this collider's area
     */
    public boolean contains(Vector point) { return getBounds().contains(point); }

    /**
     * Calculates the shortest movement by which an intersection between this collider and the given
     * rectangle can be resolved
     * @param other The rectangle to calculate an extraction from
     * @return The shortest distance by which an intersection between this collider and the given
     * rectangle can be resolved
     */
    public Vector extract(Rectangle other) { return getBounds().extract(other); }

    /**
     * Notifies this collider of a collision between itself and the given collider
     * @param other The collider that this collider has collided with
     */
    public void registerCollision(Collider other)
    {
        // Add to collection of collisions from this frame
        currentFrameCollisions.add(other);
        if (!collisions.contains(other)) // If not already registered as a continuing collision
        {
            // Notify listeners of a new collision
            for (CollisionBeginListener listener : collisionBeginListeners)
                listener.onCollisionBegin(other);
            collisions.add(other);      // Add to collection of continuing collisions
        }

        else                            // Collision is already known about, this is a continuing collision
        {
            // Notify listeners of a continuing collision
            for (CollisionContinueListener listener : collisionContinueListeners)
                listener.onCollisionContinue(other);
        }
    }

    /**
     * Resolves collisions for the current frame by checking for those collisions that are no longer
     * occurring
     */
    public void resolveCollisions()
    {
        // Iterate through collisions
        for (Collider collider : collisions)
        {
            // If one of these collisions did not occur on the current frame, then the collision has ended
            if (!currentFrameCollisions.contains(collider))
            {
                // Remove from continuing collisions, and notify listeners of a collision ending
                collisions.remove(collider);
                for (CollisionEndListener listener : collisionEndListeners)
                    listener.onCollisionEnd(collider);
            }
        }

        // Reset list of current collisions
        currentFrameCollisions = new HashSet<>();
    }

    /**
     * Notifies this collider of contact with the tile map. This will occur each frame the collider
     * is located on the tile map
     * @param contactedTiles The grid of tiles that this collider is contacting
     */
    public void onTileContact(Tile[][] contactedTiles)
    {
        // Pass notification to listeners if there are any
        for (TileContactListener listener : tileContactListeners)
            listener.onTileContact(contactedTiles);
    }

    /**
     * Gets the bounds of this collider in world space.
     * @return The bounds of this collider in world space
     */
    public Rectangle getBounds()
    {
        // Calculate rect coordinates in world space
        Vector globalPosition = position.add(getTransform().getPosition());
        Vector globalSize = size.scale(getTransform().getScale());

        return new Rectangle(globalPosition, globalSize);
    }

    /**
     * Gets whether this collider is a trigger collider. A trigger collider is one that will register
     * collisions (with itself and the thing it collides with) but will not cause a rigidbody to
     * respond to these collisions.
     * @return Whether this collider is a trigger collider
     */
    public boolean getIsTrigger() { return isTrigger; }

    /**
     * Sets the offset of this collider from its parent gameObject
     * @param position The offset of this collider from its parent gameObject
     */
    public void setPosition(Vector position) { this.position = position; }

    /**
     * Sets the size of this coollider
     * @param size The size of this collider
     */
    public void setSize(Vector size) { this.size = size; }

    /**
     * Adds a listener to be notified when this collider begins a collision with another collider
     * @param listener Listener that will be notified when this collider begins a collision with
     *                 another collider
     */
    public void addCollisionBeginListener(CollisionBeginListener listener) { collisionBeginListeners.add(listener); }

    /**
     * Adds a listener to be notified when this collider continues to collide with another collider
     * @param listener Listener that will be notified when this collider continues to collide with
     *                 another collider
     */
    public void addCollisionContinueListener(CollisionContinueListener listener) { collisionContinueListeners.add(listener); }

    /**
     * Adds a listener to be notified when this collider ends a collision with another collider
     * @param listener Listener that will be notified when this collider ends a collision with
     *                 another collider
     */
    public void addCollisionEndListener(CollisionEndListener listener) { collisionEndListeners.add(listener); }

    /**
     * Adds a listener to be notified when this collie contacts the tile map
     * @param listener Listener that will be notified of the tiles contacted when this collider
     *                 contacts the tile map
     */
    public void addTileContactListener(TileContactListener listener) { tileContactListeners.add(listener); }

    /**
     * Sets whether this collider is a trigger collider. A trigger collider is one that will register
     * collisions (with itself and the thing it collides with) but will not cause a rigidbody to
     * respond to these collisions.
     */
    public void setIsTrigger(boolean isTrigger) { this.isTrigger = isTrigger; }

    /**
     * Listener interface for when a collider has collided with another collider
     */
    public interface CollisionBeginListener
    {
        /**
         * Called on the first frame a collider collides with the collider being listened to
         * @param other The collider that was collided with
         */
        void onCollisionBegin(Collider other);
    }

    /**
     * Listener interface for when a collider is continuing to collide with another collider
     */
    public interface CollisionContinueListener
    {
        /**
         * Called each frame that a collider continues to collide with the collider being listened to
         * @param other The collider that was collided with
         */
        void onCollisionContinue(Collider other);
    }

    /**
     * Listener interface for when a collider stops colliding with another collider
     */
    public interface CollisionEndListener
    {
        /**
         * Called on the frame that a collider stops colliding with the collider being listened to
         * @param other The collider that the collision ended with
         */
        void onCollisionEnd(Collider other);
    }

    /**
     * Listener interface for when a collider contacts the tile map
     */
    public interface TileContactListener
    {
        /**
         * Called each frame the listened to collider contacts the tile map
         * @param contactedTiles The tiles the listened to collider has contacted
         */
        void onTileContact(Tile[][] contactedTiles);
    }
}
