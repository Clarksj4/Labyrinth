package bit.clarksj4.labyrinth.Engine;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import bit.clarksj4.labyrinth.GameActivity;

/**
 * Created by StickasaurusRex on 07-Dec-16.
 */

public class AndroidGameContext extends GameContext
{
    /** Preferences are saved under this key */
    public static final String PREFERENCES_KEY = "Labyrinth";

    private Activity activity;
    private MediaPlayer mediaPlayer;
    private Resources resources;
    private Vibrator vibrator;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private AccelerometerListener accelerometerListener;

    private float[] accelerometerInput;

    public AndroidGameContext(Activity activity)
    {
        this.activity = activity;
        resources = activity.getResources();

        vibrator = (Vibrator)activity.getSystemService(Context.VIBRATOR_SERVICE);

        // Get sensor manager and accelerometer
        sensorManager = (SensorManager)activity.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
    }

    public void release()
    {
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void playAudio(String audio, float leftVolume, float rightVolume)
    {
        String test = activity.getPackageName();
        int resID = resources.getIdentifier(audio , "raw", activity.getPackageName());

        mediaPlayer = MediaPlayer.create(activity, resID);
        //player.setVolume(leftVolume, rightVolume);
        mediaPlayer.start();
    }

    public Bitmap getBitmap(String name)
    {
        // Get identifier by name
        int identifier = resources.getIdentifier(name, "drawable", activity.getPackageName());

        // Load and return the corresponding image
        return BitmapFactory.decodeResource(resources, identifier);

    }

    @Override
    void gameResumed()
    {
        // Create and register a new accelerometer listener
        accelerometerListener = new AccelerometerListener();
        sensorManager.registerListener(accelerometerListener, accelerometer, SensorManager.SENSOR_DELAY_GAME);

        if (mediaPlayer != null)
            mediaPlayer.start();
    }

    @Override
    void gamePaused()
    {
        // Unregister accelerometer listener
        sensorManager.unregisterListener(accelerometerListener);

        if (mediaPlayer != null)
            mediaPlayer.pause();
    }

    @Override
    public void vibrate(long milliseconds)
    {
        vibrator.vibrate(milliseconds);
    }

    @Override
    public float[] getAccelerometerInput() { return accelerometerInput; }

    @Override
    public void savePreferences(Map<String, Object> preferences)
    {
        SharedPreferences.Editor editor = activity.getSharedPreferences(PREFERENCES_KEY, 0).edit();

        for (Map.Entry<String, Object> entry : preferences.entrySet())
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
        editor.apply();
    }

    @Override
    public Map<String, Object> loadPreferences()
    {
        // Preferences loaded from device
        Map<String, ?> loadedPreferences = activity.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE).getAll();

        // Save each item in loaded preferences to new map that uses 'Object' instead of '?'
        Map<String, Object> preferences = new HashMap<>();
        for (Map.Entry<String, ?> entry : loadedPreferences.entrySet())
            preferences.put(entry.getKey(), entry.getValue());          // Convert '?' to object so its actually useful

        return preferences;
    }

    @Override
    public boolean saveJSONToFile(String filename, String json, boolean overwrite)
    {
        // If not overwritting, check if the file already exists
        if (!overwrite)
        {
            // Check if file already exists
            File file = activity.getFileStreamPath(filename);
            if (file.exists())
                return false;       // File already exists, json will not be saved
        }

        try
        {
            // Open stream, and write json to file as bytes
            FileOutputStream outputStream = activity.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(json.getBytes());
            outputStream.close();

            return true;        // Json saved successfully
        }

        // FileNotFound when opening stream, or IOException when closing stream
        catch (IOException e)
        {
            e.printStackTrace();
            return false;       // Json not saved
        }
    }

    @Override
    public String getJSONFromFile(String filename)
    {
        // Check if file exists
        File file = activity.getFileStreamPath(filename);
        if (!file.exists())
            return null;       // File doesn't exist, json can't be loaded

        try
        {
            // Input streams
            FileInputStream fis = activity.openFileInput(filename);          // Read file as bytes
            InputStreamReader isr = new InputStreamReader(fis);             // Read as chars, instead of bytes
            BufferedReader bufferedReader = new BufferedReader(isr);        // Performance increased

            // Parts to construct entire string contents of file
            StringBuilder sb = new StringBuilder();
            String line;

            // Read each line
            while ((line = bufferedReader.readLine()) != null)
                sb.append(line);

            bufferedReader.close();

            // Entire string contents of file
            return sb.toString();
        }

        // IOException when stream is closed prior to read finishing
        catch (IOException e) { e.printStackTrace(); }

        // Some error has occurred, no json to return
        return null;
    }

    private class AccelerometerListener implements SensorEventListener
    {
        @Override
        public void onSensorChanged(SensorEvent event) { accelerometerInput = event.values; }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { /* Nothing! */ }
    }

    private class SurfaceHandler implements SurfaceHolder.Callback
    {
        Display display;

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
        {
            display = new Display(holder, activity.getResources().getDisplayMetrics());
            Graphics.addDisplay(display);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder)
        {
            Graphics.removeDisplay(display);
        }
    }
}
