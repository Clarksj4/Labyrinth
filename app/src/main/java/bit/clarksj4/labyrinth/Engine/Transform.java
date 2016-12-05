package bit.clarksj4.labyrinth.Engine;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Component for describing the position and scale of a game object. A transform also stores the
 * hierarchy of a game object
 */
public class Transform extends Component
{
    private Transform parent;
    private ArrayList<Transform> children;

    private Vector localPosition;
    private Vector localScale;

    /**
     * A new transform attached to the given game object
     * @param gameObject The game object with transform is attached to
     */
    public Transform(GameObject gameObject)
    {
        super(gameObject);

        // Default values
        localPosition = Vector.zero();
        localScale = Vector.one();

        children = new ArrayList<>();
    }

    /**
     * Moves this transforms position by the given amount
     * @param delta The amount to move this transform by
     */
    public void translate(Vector delta) { localPosition = localPosition.add(delta); }

    /**
     * Adds a child to this transforms hierarchy
     * @param child The child to add
     */
    public void addChild(Transform child) { children.add(child); }

    /**
     * Removes a child from this transforms hierarchy
     * @param child THe child to remove
     */
    public void removeChild(Transform child) { children.remove(child); }

    /**
     * Gets the parent of this transform
     * @return The parent of this transform or null if it has none
     */
    public Transform getParent() { return parent; }

    /**
     * Gets the children transforms
     * @return The children transforms attached to this transform
     */
    public ArrayList<Transform> getChildren() { return children; }

    /**
     * Gets the number of children this transform has
     * @return The number of children this transform has
     */
    public int getChildCount() { return children.size(); }

    /**
     * Gets the local position of this transform
     * @return The local position of this transform
     */
    public Vector getLocalPosition() { return new Vector(localPosition.x, localPosition.y); }

    /**
     * Gets the global position of this transform
     * @return The global position of this transform
     */
    public Vector getPosition() { return getParentPosition().add(getLocalPosition()); }

    /**
     * Gets the local scale of this transform
     * @return The local scale of this transform
     */
    public Vector getLocalScale() { return new Vector(localScale.x, localScale.y); }

    /**
     * Gets the global scale of this transform
     * @return The global scale of this transform
     */
    public Vector getScale() { return getParentScale().scale(getLocalScale()); }

    /**
     * Sets the local position of this transform
     * @param position The local position of this transform
     */
    public void setLocalPosition(Vector position) { localPosition = position; }

    /**
     * Sets the global position of this transform
     * @param position The global position of this transform
     */
    public void setPosition(Vector position) { this.localPosition = position.subtract(getParentPosition()); }

    /**
     * Sets the local scale of this transform
     * @param scale The local scale of this transform
     */
    public void setLocalScale(Vector scale) { localScale = scale; }

    /**
     * Sets the parent of this transform. Global position and scale will be maintained.
     * @param parent The parent of this transform
     */
    public void setParent(Transform parent)
    {
        // If there is already a parent and its not the new parent
        if (this.parent != null && this.parent != parent)
            this.parent.removeChild(this);      // Remove ref to this object from old parent

        // Get global variables so they can be maintained when switching parents
        Vector globalPosition = getPosition();
        Vector globalScale = getScale();

        // If the old parent is not null
        if (parent != null)
        {
            // Get new parent global variables
            Vector parentPosition = parent.getPosition();
            Vector parentScale = parent.getScale();

            // New local variables are based off of new parent position
            Vector localPosition = globalPosition.subtract(parentPosition);
            Vector localScale = globalScale.divide(parentScale);

            setLocalPosition(localPosition);
            setLocalScale(localScale);

            // Register self as child of new parent
            parent.addChild(this);
        }

        else
        {
            setLocalPosition(globalPosition);
            setLocalScale(globalScale);
        }

        this.parent = parent;
    }

    /**
     * Gets the parents global position
     * @return The parent's global position or 0, 0 if there is no parent
     */
    private Vector getParentPosition()
    {
        Vector globalPosition = Vector.zero();
        Transform walker = parent;
        while(walker != null)
        {
            globalPosition = globalPosition.add(walker.getLocalPosition());
            walker = walker.getParent();
        }

        return globalPosition;
    }

    /**
     * Gets the parents global scale
     * @return The parent's global scale or 0, 0 if there is no parent
     */
    private Vector getParentScale()
    {
        Vector globalScale = Vector.one();
        Transform walker = parent;
        while(walker != null)
        {
            globalScale = globalScale.scale(walker.getLocalScale());
            walker = walker.getParent();
        }

        return globalScale;
    }
}
