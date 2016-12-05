package bit.clarksj4.labyrinth;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import bit.clarksj4.labyrinth.Engine.AndroidGame;
import bit.clarksj4.labyrinth.Engine.ClassFinder;
import bit.clarksj4.labyrinth.Engine.Component;
import bit.clarksj4.labyrinth.Engine.GameObject;
import bit.clarksj4.labyrinth.Engine.Vector;
import bit.clarksj4.labyrinth.Engine.World;
import bit.clarksj4.labyrinth.Engine.WorldLoader;

/**
 * Created by Stephen on 4/06/2016.
 */
public class JSONWorldLoaderTest extends WorldLoader
{
    // Loaded items saved to hashmaps by name
    private HashMap<String, Integer> resourceIDs = new HashMap<>();
    private HashMap<String, Bitmap> bitmaps = new HashMap<>();
//    private HashMap<String, Animation> animations = new HashMap<>();
    private World world;
    private Resources resources;

    public JSONWorldLoaderTest(AndroidGame game) { super(game); }

    @Override
    public void load(World world)
    {
        this.world = world;
        resources = game.getContext().getResources();

        String jsonString = loadJsonFromAsset("Labyrinth.json");
        try
        {
            // Get root JSON Object from string
            JSONObject rootJSON = new JSONObject(jsonString);
            //
            // Resource IDs
            //
            JSONArray resourceIDsJSON = rootJSON.getJSONArray("ResourceIDs");
            for (int i = 0; i < resourceIDsJSON.length(); i++)
            {
                // Each array slot contains object with parameters for resources.getIdentifier() method
                JSONObject resourceIDJSON = resourceIDsJSON.getJSONObject(i);
                String name = resourceIDJSON.getString("name");
                String res = resourceIDJSON.getString("res");
                String pkg = resourceIDJSON.getString("package");

                // Get resource ID int from getIdentifier() method
                int resID = resources.getIdentifier(name, res, pkg);

                // Add to hash map to be used by other loaded assets
                resourceIDs.put(name, resID);
            }
            //
            // Bitmaps
            //
            JSONArray bitmapsJSON = rootJSON.getJSONArray("Bitmaps");
            for (int i = 0; i < bitmapsJSON.length(); i++)
            {
                // Each array slot contains name of resID to be decoded as a bitmap
                JSONObject bitmapJSON = bitmapsJSON.getJSONObject(i);
                String name = bitmapJSON.getString("name");
                String resIDKey = bitmapJSON.getString("resID");

                // Use resource ID key to get resourceID
                int resId = resourceIDs.get(resIDKey);

                // Use resource ID to load bitmap, add to bitmap has map
                Bitmap bitmap = BitmapFactory.decodeResource(resources, resId);
                bitmaps.put(name, bitmap);
            }
            //
            // Animations
            //
            JSONArray animationsJSON = rootJSON.getJSONArray("Animations");
            for (int i = 0; i < animationsJSON.length(); i++)
            {
                // Each array slot contains the parameters for animation
                JSONObject animationJSON = animationsJSON.getJSONObject(i);
                String name = animationJSON.getString("name");
                double delay = animationJSON.getDouble("delay");

                // Frames array contains keys for bitmaps hash map
                JSONArray framesJSON = animationJSON.getJSONArray("frames");
                Bitmap[] frames = new Bitmap[framesJSON.length()];

                // Iterate through frames array to get key for each bitmap frame in this animation
                for (int j = 0; j < frames.length; j++)
                {
                    // Get key then get bitmap from hash map by key
                    String frameKey = framesJSON.getString(j);
                    Bitmap frame = bitmaps.get(frameKey);

                    // Save ref to bitmap in frames array
                    frames[j] = frame;
                }

                // Create new animation with retrieved params, save to animations hash map
//                Animation animation = new Animation(name, (float)delay, frames);
//                animations.put(name, animation);
            }
            //
            // Game Objects
            //
            JSONArray gameObjectsJSON = rootJSON.getJSONArray("GameObjects");
            for (int i = 0; i < gameObjectsJSON.length(); i++)
            {
                JSONObject gameObjectJSON = gameObjectsJSON.getJSONObject(i);
                loadGameObject(gameObjectJSON, null);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private GameObject loadGameObject(JSONObject gameObjectJSON, GameObject parent)
    {
        GameObject gameObject = null;

        try
        {
            // Create game object
            String name = gameObjectJSON.getString("name");
            gameObject = new GameObject(world, name);

            //Components
            JSONArray componentsJSON = gameObjectJSON.getJSONArray("components");
            for (int j = 0; j < componentsJSON.length(); j++)
            {
                JSONObject componentJSON = componentsJSON.getJSONObject(j);
                String componentType = componentJSON.getString("type");

                Class cls = ClassFinder.findClassByName(componentType);
                Constructor constructor = cls.getConstructor(GameObject.class);
                Object instance = constructor.newInstance(gameObject);

                JSONArray propertiesJSON = componentJSON.getJSONArray("properties");
                for (int k = 0; k < propertiesJSON.length(); k++)
                {
                    JSONObject propertyJSON = propertiesJSON.getJSONObject(k);
                    String propertyName = propertyJSON.getString("name");
                    String propertyType = propertyJSON.getString("type");

                    JSONArray valuesJSON = propertyJSON.getJSONArray("values");
                    Object[] values = new Object[valuesJSON.length()];
                    for (int l = 0; l < valuesJSON.length(); l++)
                        values[l] = valuesJSON.get(l);

                    Object value;
                    switch (propertyType)
                    {
                        case "Vector":
                            value = new Vector(((Double)values[0]).floatValue(), ((Double)values[1]).floatValue());
                            break;
                        case "Animations":
//                            Animation[] frames = new Animation[values.length];
//                            for (int m = 0; m < values.length; m++)
//                                frames[m] = animations.get((String)values[m]);
//                            value = frames;
                            break;
                        default:
                            value = values[0];
                            break;
                    }

//                    Method setter;
//                    Method[] componentMethods = cls.getMethods();
//                    for (Method method : componentMethods)
//                    {
//                        if (method.getName().equals("set" + propertyName))
//                        {
//                            setter = method;
//                            setter.invoke(instance, value);
//                            break;
//                        }
//                    }
                }
            }
        }

        catch(JSONException |
                NoSuchMethodException |
                InstantiationException |
                IllegalAccessException |
                InvocationTargetException e)
        {
            e.printStackTrace();
        }

        return gameObject;
    }

    private String loadJsonFromAsset(String filename)
    {
        String json = null;

        try
        {
            // Open file from assets folder
            InputStream stream = game.getContext().getAssets().open(filename);

            // Create byte array big enough to hold file contents
            int size = stream.available();
            byte[] buffer = new byte[size];

            // Read file into byte array, then close stream
            stream.read(buffer);
            stream.close();

            // Convert byte array to string
            json = new String(buffer, "UTF-8");
        }

        catch(IOException e) { e.printStackTrace(); }

        return json;
    }
}
