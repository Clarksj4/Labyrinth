package bit.clarksj4.labyrinth.Engine;

import android.graphics.Bitmap;

import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Stephen on 2/12/2016.
 */

// TODO: Assets takes care of gson
public class Assets
{
    private static GameContext _context;
    private static Gson gson;

    public static boolean save(GameObject object)
    {
        return save(object, GameObject.class, object.getName());
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

    static void init(GameContext context)
    {
        _context = context;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(AnimationFrame.class, new AnimationFrameSerializer());
        gsonBuilder.registerTypeHierarchyAdapter(Bitmap.class, new BitmapSerializer());
        gson = gsonBuilder.create();
    }

    static boolean save(Object object, Type type, String name)
    {
        String json = toJson(object, type);

        try
        {
            saveJSONToFile(name, json);
            return true;
        }

        catch(IOException e) { e.printStackTrace(); }

        return false;
    }

    static <T> T load(String name, Class<T> classOfT)
    {
        try
        {
            String json = getJSONFromFile(name);
            return fromJson(json, classOfT);
        }

        catch(IOException e) { e.printStackTrace(); }

        return null;
    }

    private static void saveJSONToFile(String filename, String json) throws IOException
    {
        _context.saveJSONToFile(filename, json);
    }

    private static String getJSONFromFile(String filename) throws IOException
    {
        return _context.getJSONFromFile(filename);
    }
}
