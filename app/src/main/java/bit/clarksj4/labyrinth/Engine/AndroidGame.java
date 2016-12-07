package bit.clarksj4.labyrinth.Engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.SurfaceView;

import java.util.Map;

/**
 * Game object for running on an android platform
 */
public class AndroidGame extends Game
{
    /** Preferences are saved under this key */
    public static final String PREFERENCES_KEY = "Labyrinth";

    private Context context;

    /**
     * A new game for an android platform, using the given context, and drawing to the given view
     * @param context The context from whence this game was created
     * @param gameView The view the game will draw to
     */
    public AndroidGame(Context context, SurfaceView gameView)
    {
        super();

        this.context = context;

        // Create a new display using the given view
        Graphics.getInstance().addDisplay(new Display(gameView, context.getResources().getDisplayMetrics()));

        // Load preferences from shared prefs
        loadPreferences();
    }

    @Override
    public void stop()
    {
        super.stop();

        // Save preferences when game stops
        savePreferences();
    }

    /**
     * Registers accelerometer input from the android device this game is running on
     * @param values The event values from the accelerometer listener
     */
    public void accelerometerInput(float[] values) { Input.getInstance().setAccelerometerInput(values); }

    /**
     * Registers multiple finger input from the android device this game is running on
     */
    public void multiFingerInput() { /* TODO: Adjust scale of game */ }

    /**
     * Gets the context this game is running in
     * @return The context this game is running in.
     */
    public Context getContext() { return context; }

    /**
     * Loads shared preferences
     */
    private void loadPreferences()
    {
        Map<String, ?> preferences = context.getSharedPreferences(PREFERENCES_KEY, 0).getAll();
        for (Map.Entry<String, ?> entry : preferences.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();

            world.setPreference(key, value);
        }
    }

    /**
     * Saves shared preferences
     */
    private void savePreferences()
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_KEY, 0).edit();

        for (Map.Entry<String, Object> entry : world.getPreferences().entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Convert object to correct type
            if (value instanceof Float) editor.putFloat(key, (float)value);
            else if(value instanceof String) editor.putString(key, (String)value);
            else if(value instanceof Integer) editor.putInt(key, (int)value);
            else if(value instanceof Boolean) editor.putBoolean(key, (boolean)value);
            else if(value instanceof Long) editor.putLong(key, (long)value);
        }
        editor.commit();    // Apply immediately: game is closing
    }
}
