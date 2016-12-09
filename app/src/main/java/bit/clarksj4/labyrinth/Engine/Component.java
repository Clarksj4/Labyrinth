package bit.clarksj4.labyrinth.Engine;

/**
 * Component base class. A component is a unit of functionality. Many components attached to a game
 * object describe the sum of the given game objects available behaviours / functionality.
 */
public abstract class Component extends UIDObject
{
    /** By default each component is enabled */
    private static final boolean DEFAULT_ENABLED = true;

    /** The game object this component is attached to. Every component must be attached to a game object */
    protected GameObject gameObject;
    private boolean enabled;

    public Component(GameObject gameObject)
    {
        this.gameObject = gameObject;
        enabled = DEFAULT_ENABLED;

        // Every component MUST be attached to a game object
        if (gameObject == null)
            throw new NullPointerException("GameObject reference cannot be null. Component must be attached to a gameObject");

        // Attach to given game object
        this.gameObject.addComponent(this);
    }

    /**
     * Called once at the beginning of the game after the world has been created and populated with
     * game objects. Use this method to initialize components and establish references between
     * components
     */
    public void start() { /* Empty virtual method */ }

    /**
     * Called each frame while the game is in execution.
     */
    public void update() { /* Empty virtual method*/ }

    /**
     * Called prior to the destruction of this component.
     */
    public void destroy() { /* Empty virtual method */ }

    /**
     * Gets whether this component is currently enabled. A disabled component has its functionality
     * temporarily switched off.
     * @return Whether this component is currently enabled
     */
    public boolean isEnabled() { return enabled; }

    /**
     * Gets the game object that this component is attached to.
     * @return The game object that this component is attached to.
     */
    public GameObject getGameObject() { return gameObject; }

    /**
     * Gets the transform component attached to this components game object.
     * @return The transform component attached to this components game object.
     */
    public Transform getTransform() { return gameObject.getTransform(); }

    /**
     * Searches this components game object's parent hierarchy for the first component of this given
     * type
     * @param cls The component type to search for.
     * @param <T> The component type.
     * @return The first instance of the given component in this component's game object's hierarchy,
     * or null if none exists.
     */
    public <T extends Component> T getComponentInParent(Class<T> cls) { return gameObject.getComponentInParent(cls); }

    /**
     * Searches this component's game object for the given component type.
     * @param cls The component type to search for.
     * @param <T> The component type.
     * @return The component of the given type attached to this game object, or null if there is none.
     */
    public <T extends Component> T getComponent(Class<T> cls) { return gameObject.getComponent(cls); }

    /**
     * Searches this component's game object's child hierarchy for the first component of the given
     * type.
     * @param cls The component type to search for.
     * @param <T> The component type.
     * @return The first instance of the given component type in this component's game object's
     * child hierarchy, or null if there is none of the given type.
     */
    public <T extends Component> T getComponentInChildren(Class<T> cls) { return gameObject.getComponentInChildren(cls); }

    /**
     * Sets whether this component is currently enabled
     * @param enabled Whether this component is currently enabled
     */
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
