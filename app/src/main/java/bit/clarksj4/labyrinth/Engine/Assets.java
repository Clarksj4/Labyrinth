package bit.clarksj4.labyrinth.Engine;

import android.graphics.Bitmap;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Stephen on 2/12/2016.
 */

public class Assets
{
    private static Map<String, Object> preferences;
    private static GameContext _context;
    private static Gson gson;

    public static Object getPreference(String name)
    {
        return preferences.get(name);
    }

    public static void setPrefernce(String name, Object value)
    {
        preferences.put(name, value);
    }

    public static boolean save(GameObject object)
    {
        return save(object, GameObject.class, object.getName(), true);
    }

    public static GameObject load(String name)
    {
        return load(name, GameObject.class);
    }

    public static String toJson(Object object, Type type)
    {
        return gson.toJson(object, type);
    }

    public static <T> T fromJson(String json, Class<T> classOfT)
    {
        return gson.fromJson(json, classOfT);
    }

    //
    // Package private methods
    //

    static void init(GameContext context)
    {
        _context = context;

        preferences = _context.loadPreferences();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(AnimationFrame.class, new AnimationFrameSerializer());
        gsonBuilder.registerTypeHierarchyAdapter(Bitmap.class, new BitmapSerializer());
        gson = gsonBuilder.create();
    }

    static boolean save(Object object, Type type, String name, boolean overwrite)
    {
        String json = toJson(object, type);
        return saveJSONToFile(name, json, overwrite);
    }

    static <T> T load(String name, Class<T> classOfT)
    {
        String json = getJSONFromFile(name);
        return fromJson(json, classOfT);
    }

    static void commit()
    {
        _context.savePreferences(preferences);
    }

    //
    // Private methods
    //

    private static boolean saveJSONToFile(String filename, String json, boolean overwrite)
    {
        return _context.saveJSONToFile(filename, json, overwrite);
    }

    private static String getJSONFromFile(String filename)
    {
        return _context.getJSONFromFile(filename);
    }
}
