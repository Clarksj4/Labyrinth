package bit.clarksj4.labyrinth.Engine;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by StickasaurusRex on 07-Dec-16.
 */

public class AndroidGameContext extends GameContext
{
    /** Preferences are saved under this key */
    public static final String PREFERENCES_KEY = "Labyrinth";

    private Context context;

    public AndroidGameContext(Context context)
    {
        this.context = context;
    }

    @Override
    public void savePreferences(Map<String, Object> preferences)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCES_KEY, 0).edit();

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
        editor.commit();    // Apply immediately: game is closing
    }

    @Override
    public Map<String, Object> loadPreferences()
    {
        Map<String, ?> loadedPreferences = context.getSharedPreferences(PREFERENCES_KEY, 0).getAll();
        Map<String, Object> preferences = new HashMap<>();

        for (Map.Entry<String, ?> entry : preferences.entrySet())
        {
            String key = entry.getKey();
            Object value = entry.getValue();

            preferences.put(key, value);
        }

        return preferences;
    }

    @Override
    public boolean saveJSONToFile(String filename, String json, boolean overwrite)
    {
        // If not overwritting, check if the file already exists
        if (!overwrite)
        {
            // Check if file already exists
            File file = context.getFileStreamPath(filename);
            if (file.exists())
                return false;       // File already exists, json will not be saved
        }

        try
        {
            // Open stream, and write json to file as bytes
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
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
        File file = context.getFileStreamPath(filename);
        if (!file.exists())
            return null;       // File doesn't exist, json can't be loaded

        try
        {
            // Input streams
            FileInputStream fis = context.openFileInput(filename);          // Read file as bytes
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
}
