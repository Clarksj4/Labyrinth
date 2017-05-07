package bit.clarksj4.labyrinth.Engine;

// TODO: on game start need to: make timer, init context, display

import android.content.Context;
import android.view.SurfaceView;

/**
 * Game base class. Descend from this class to make a game object for each platform.
 */
public class Game
{
    /** The default unit size in the game */
    public static final int UNIT = 32;

    /** How frequently the game updates in milliseconds */
    private static final long UPDATE_FREQUENCY = 20;

    private GameOverListener gameOverListener;
    private boolean isPaused;
    private GameContext context;

    public static Game Android(Context context, SurfaceView view)
    {
        Game game = new Game(new AndroidGameContext(context));

        Graphics.addDisplay(new Display(view, context.getResources().getDisplayMetrics()));
        AndroidAssetInstaller.install(context);

        return game;
    }

    /**
     * A new game
     */
    private Game(GameContext context)
    {
        this.context = context;
        Assets.init(context);
        Input.init(context);
        Hardware.init(context);

        Time.addTickListener(new GameCycle());
    }

    /**
     * Populates the world in the manner described by the given WorldLoader object
     * @param loader An object that describes what to load into the world
     */
    public void loadWorld(WorldLoader loader) { loader.load(); }

    /** Starts the game  */
    public void start()
    {
        context.gameResumed();
        World.current().start();
        Time.start(UPDATE_FREQUENCY);
    }

    /** Stops the game */
    public void stop()
    {
        if (isStarted())
        {
            Time.stop();
            Assets.commit();    // Save preferences to file
        }
    }

    public void pause()
    {
        // If paused state is changing
        if (!isPaused)
        {
            Time.pause();
            context.gamePaused();
            isPaused = true;
        }
    }

    public void resume()
    {
        if (isPaused)
        {
            Time.resume();
            context.gameResumed();
            isPaused = true;
        }
    }

    /**
     * Gets whether the game is currently paused
     * @return Whether the game is currently paused
     */
    public boolean isPaused() { return isPaused; }

    /**
     * Gets whether the game has started
     * @return Whether the game has started
     */
    public boolean isStarted() { return Time.isRunning(); }

    /**
     * Interface for listening for when the game has ended
     */
    interface GameOverListener
    {
        /** Called when the game has ended. */
        void gameOver();
    }

    private class GameCycle implements Time.OnTickListener
    {
        @Override
        public void tick()
        {
            World.current().update();

            Physics.update();
            Graphics.draw();

            World.current().recycle();    // Recycle all destroyed objects now that update is complete
        }
    }
}
