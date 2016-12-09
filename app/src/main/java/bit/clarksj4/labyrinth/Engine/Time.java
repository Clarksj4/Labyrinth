package bit.clarksj4.labyrinth.Engine;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Singleton Time object. Responsible for running the game loop at scheduled intervals and tracking
 * the interval duration
 */
public class Time
{
    private static ScheduledExecutorService executorService;
    private static ScheduledFuture loop;

    private static ArrayList<OnTickListener> tickListeners = new ArrayList<>();
    private static long _updateFrequency;
    private static long startTime;
    private static long timeAtLastFrame;
    private static float elapsedTime;
    private static float deltaTime;
    private static int iterations;


    /**
     * Starts this timer with the given delay between updates
     * @param updateFrequency The delay between updates in milliseconds
     */
    static void start(long updateFrequency)
    {
        if (executorService == null &&
                loop == null)
        {
            _updateFrequency = updateFrequency;

            // Create new scheduling service
            executorService = Executors.newSingleThreadScheduledExecutor();

            // Record start time. timeAtLastFrame inits to start time so first frame deltaTime is accurate
            startTime = SystemClock.elapsedRealtime();
            timeAtLastFrame = startTime;
            iterations = 0;

            resume();
        }
    }

    /**
     * Resumes this timer after a pause
     */
    static void resume()
    {
        if (executorService != null)
        {
            // Only start if the timer is not currently running
            if (loop == null)
            {
                timeAtLastFrame = SystemClock.elapsedRealtime();

                // Begin the update cycle immediately ( 0 = no initial delay)
                loop = executorService.scheduleAtFixedRate(new Tick(),
                        0,
                        _updateFrequency,
                        TimeUnit.MILLISECONDS);
            }
        }
    }

    /**
     * Pauses the game timer
     */
    static void pause()
    {
        if (executorService != null)
        {
            // Only stop if timer is currently running
            if (loop != null)
            {
                // Stop the update cycle
                loop.cancel(false);
                loop = null;
            }
        }
    }

    /**
     * Stops the game timer
     */
    static void stop()
    {
        if (loop != null)
        {
            loop.cancel(false);
            loop = null;
        }

        if (executorService != null)
        {
            executorService.shutdown();
            executorService = null;
        }
    }

    /**
     * Registers a listener to be notified each time the timer ticks over
     * @param listener The listener to be notified each time the timer ticks
     */
    static void addTickListener(OnTickListener listener) { tickListeners.add(listener); }

    /**
     * Removes the given listener from the collection of listeners
     * @param listener The listener to remove from the collection of listeners
     */
    static void removeTickListener(OnTickListener listener) { tickListeners.remove(listener); }

    /**
     * The time it took in seconds for the last game frame.
     */
    public static float getDeltaTime() { return deltaTime; }

    /**
     * The time in seconds the game has been running, as at the beginning of the current tick event
     */
    public static float getElapsedTime() { return elapsedTime; }

    /**
     * The number of times this timer has ticked
     */
    public static int getIterations() { return iterations; }

    /**
     * Sets the delay that occurs between each update in the game cycle. Setting the delay after
     * the game has begun will only take effect once the game loop is resumed or restarted
     * @param updateFrequency The delay between each update in the game cycle in milliseconds
     */
    public static void setUpdateFrequency(long updateFrequency) { _updateFrequency = updateFrequency; }

    /**
     * Checks if the timer is currently running
     * @return True if the timer is currently running
     */
    public static boolean isRunning() { return executorService != null; }

    /**
     * Tick listener interface
     */
    interface OnTickListener
    {
        /**
         * Called each time the timer updates
         */
        void tick();
    }

    private static class Tick implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                // Update delta time and time at last frame (convert from milliseconds to seconds)
                deltaTime = (SystemClock.elapsedRealtime() - timeAtLastFrame) / 1000.0f;
                timeAtLastFrame = SystemClock.elapsedRealtime();
                elapsedTime = (timeAtLastFrame - startTime) / 1000.0f;
                iterations++;                           // Number of times this timer has ticked

                for (OnTickListener listener : tickListeners)
                    listener.tick();
            }

            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
