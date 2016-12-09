package bit.clarksj4.labyrinth.Engine;

import java.util.ArrayList;

/**
 * An entity in the game. All units of behaviour must be attached to a game object.
 */
public class GameObject extends UIDObject
{
    /** The default name of a game object */
    private static final String DEFAULT_NAME = "Game Object";

    private String name;
    private ArrayList<Component> components;
    private Transform transform;

    /**
     * A new game object with the default name
     */
    public GameObject() { this(DEFAULT_NAME); }

    /**
     * A new game object with the given name
     * @param name The name of this game object
     */
    public GameObject(String name)
    {
        this.name = name;

        // Empty list of components
        components = new ArrayList<>();
        transform = addComponent(Transform.class);      // EVERY game object gets a transform

        // Add this game object to world
        World.current().add(this);
    }

    /**
     * Calls the start method on each of this game objects components.
     */
    public void start()
    {
        for (Component component : components)
            component.start();
    }

    /**
     * Calls the update method on each of this game objects components
     */
    public void update()
    {
        for(Component component : components)
            component.update();
    }

    /**
     * Adds this game object to the collection of objects that will be destroyed at the end of this
     * frame. A destroyed game object has its components and all of its children destroyed as well
     */
    public void destroy()
    {
        // Notify components that this object is being destroyed
        for (Component component : components)
            component.destroy();

        // Remove this object from world
        World.current().destroy(this);
    }

    /**
     * Adds the given component to this game objects collection of components
     * @param component The component to add to this game object
     */
    void addComponent(Component component)
    {
        // Check if there is already a component of the same type attached to this game object
        for (Component child : components)
        {
            if (child.getClass().equals(component.getClass()))
                throw new IllegalArgumentException(toString() + " already contains a " + component.getClass() + " component.");
        }
        components.add(component);
    }

    public <T extends Component> T addComponent(Class<T> cls)
    {
        // Object defaults to null
        T component = null;

        // Instantiate an object of the given type, add it, checking if a component of the same type has already been added
        try { component = cls.getConstructor(GameObject.class).newInstance(this); }
        catch (Exception e) { e.printStackTrace(); }

        return  component;
    }

    /**
     * Searches this game objects parent hierarchy for a component of the given type
     * @param cls The component type to search for
     * @param <T> The component type
     * @return The first instance of the given component type in this game objects parent hierarchy,
     * or null if no component of the given type exists
     */
    public <T extends Component> T getComponentInParent(Class<T> cls)
    {
        // Walk up the hierarchy checking for the given component type
        T dst = null;
        Transform walker = transform;
        while(walker != null && dst == null)
        {
            dst = walker.getGameObject().getComponent(cls);
            walker = walker.getParent();
        }

        return dst;
    }

    /**
     * Searches this game objects collection of components for a component of the given type
     * @param cls The component type to search for.
     * @param <T> The component type
     * @return A component of the given type attached to this game object or null if there is no
     * component of the given type
     */
    public <T extends Component> T getComponent(Class<T> cls)
    {
        // Check each component to see if it is the correct type
        for (Component component : components)
        {
            if (component.getClass().isAssignableFrom(cls) ||
                    cls.isAssignableFrom(component.getClass()))
                return (T)component;
        }

        return null;
    }

    /**
     * Searches this game objects child hierarchy for a component of the given type
     * @param cls The component type to search for
     * @param <T> The component type
     * @return The first instance of the given component type in this game object's child hierarchy
     * or null if there is no component of the given type.
     */
    public <T extends Component> T getComponentInChildren(Class<T> cls)
    {
        // Check each child for a component of the given type
        T component = null;
        ArrayList<Transform> children = transform.getChildren();
        for (Transform child : children)
        {
            // Check child for component of given type
            component = child.getGameObject().getComponent(cls);
            if (component != null)
                return component;

            // Check child's children for component of given type
            component = child.getGameObject().getComponentInChildren(cls);
            if (component != null)
                return component;
        }

        return component;
    }

    /**
     * Gets the name of this game object
     * @return The name of this game object
     */
    public String getName() { return name; }

    /**
     * Gets this game objects transform component
     * @return The game objects transform components
     */
    public Transform getTransform() { return transform; }

    /**
     * Sets this game objects name
     * @param name The name to assign to this game object
     */
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() { return name; }
}
