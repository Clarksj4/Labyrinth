package bit.clarksj4.labyrinth.Engine;

// TODO: on game start need to: make timer, init context, display

/**
 * Game base class. Descend from this class to make a game object for each platform.
 */
public abstract class Game  implements Time.OnTickListener
{
    /** The default unit size in the game */
    public static final int UNIT = 32;

    /** How frequently the game updates in milliseconds */
    private static final long UPDATE_FREQUENCY = 30;

    private GameOverListener gameOverListener;
    protected World world;
    private boolean isPaused;

    /**
     * A new game
     */
    public Game()
    {
        Time.getInstance().registerOnTickListener(this);
        world = new World();
    }

    /**
     * Populates the world in the manner described by the given WorldLoader object
     * @param loader An object that describes what to load into the world
     */
    public void loadWorld(WorldLoader loader) { loader.load(world); }

    /**
     * Starts the game
     */
    public void start()
    {
        world.start();
        Time.getInstance().start(UPDATE_FREQUENCY);
    }

    /**
     * Stops the game
     */
    public void stop() { Time.getInstance().stop(); }

    /**
     * Gets whether the game is currently paused
     * @return Whether the game is currently paused
     */
    public boolean getIsPaused() { return isPaused; }

    /**
     * Gets whether the game has started
     * @return Whether the game has started
     */
    public boolean getIsStarted() { return Time.getInstance().isRunning(); }

    /**
     * Sets whether the game is currently paused or not
     * @param isPaused Whether the game is currently paused
     */
    public void setIsPaused(boolean isPaused)
    {
        // If paused state is changing
        if (this.isPaused != isPaused)
        {
            // Pause or resume timer
            if (isPaused) Time.getInstance().pause();
            else Time.getInstance().resume();

            // Remember current state
            this.isPaused = isPaused;
        }
    }

    @Override
    public void tick()
    {
        world.update();

        Physics.getInstance().update();
        Graphics.getInstance().draw();

        world.recycle();    // Recycle all destroyed objects now that update is complete
    }

    /**
     * Interface for listening for when the game has ended
     */
    private interface GameOverListener
    {
        /**
         * Called when the game has ended.
         */
        void gameOver();
    }
}
