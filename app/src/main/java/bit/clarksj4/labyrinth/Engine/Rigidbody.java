package bit.clarksj4.labyrinth.Engine;

/**
 * A component for exacting physical force on a game object
 */
public class Rigidbody extends Component implements Collider.TileContactListener, Collider.CollisionBeginListener
{
    /** By default the rigidbody has no velocity */
    private static final Vector DEFAULT_VELOCITY = Vector.zero();

    private Collider collider;
    private Vector velocity;

    /**
     * A new rigidbody component attached to the given game object
     * @param gameObject The game object this rigidbody is attached to
     */
    public Rigidbody(GameObject gameObject)
    {
        super(gameObject);

        velocity = DEFAULT_VELOCITY;

        // Rigidbody MUST have a collider attached to the same gameObject
        collider = getComponent(Collider.class);
        if (collider == null)
            throw new IllegalArgumentException("Rigidbody component requires collider component");
        collider.addCollisionBeginListener(this);
        collider.addTileContactListener(this);

        // Register this rigidbody with the physics object
        Physics.getInstance().registerRigidbody(this);
    }

    @Override
    public void destroy()
    {
        // Remove reference from physics object
        Physics.getInstance().unregisterRigidbody(this);
    }

    /**
     * Applies the given force to this rigidbody
     * @param force The force to apply to this rigidbody
     */
    public void applyForce(Vector force)
    {
        // TODO: Placeholder method. Need to learn some physics or something.
    }

    /**
     * Causes the rigidbody to be moved by all the forces currently acting upon it
     */
    public void resolveForces()
    {
        // TODO: Placeholder logic. This is not how gravity works.
        float deltaTime = Time.getInstance().getDeltaTime();
        Vector gravity = Physics.getInstance().getGravity();

        velocity = gravity.scale(deltaTime);
        getTransform().translate(velocity);
    }

    @Override
    public void onCollisionBegin(Collider other)
    {
        // Only extract self if the other collider is not a trigger collider
        if (!other.getIsTrigger())
        {
            Rectangle otherBounds = other.getBounds();
            Rectangle intersection = collider.intersection(otherBounds);

            // Possible for the intersection to be null is a sprite has already extracted itself
            if (intersection != null)
            {
                Vector extract = collider.extract(otherBounds);
                getTransform().translate(extract);
            }
        }
    }

    @Override
    public void onTileContact(Tile[][] contactedTiles)
    {
        // TODO: should be able to set which tiles are impassable
        // TODO: should be able to set movement cost of tiles

        for (int column = 0; column < contactedTiles[0].length; column++)
        {
            for (int row = 0; row < contactedTiles.length; row++)
            {
                Rectangle tileRectangle = contactedTiles[row][column].getBounds();
                if (contactedTiles[row][column].getValue() == 3)
                {
                    Rectangle intersection = collider.intersection(tileRectangle);

                    // Possible for the intersection to be null is a sprite has already extracted itself
                    if (intersection != null)
                    {
                        Vector extract = collider.extract(tileRectangle);
                        getTransform().translate(extract);
                    }
                }
            }
        }
    }

    /**
     * Gets the velocity that this rigidbody is moving at
     * @return The velocity that this rigidbody is moving at
     */
    public Vector getVelocity() { return velocity; }

    /**
     * Gets the collider associated with this rigidbody
     * @return The collider associated with this rigidbody
     */
    public Collider getCollider() { return collider; }
}
