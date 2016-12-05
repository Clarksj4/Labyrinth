package bit.clarksj4.labyrinth.Engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * World object equivalent to a single 'scene' in the game. World object holds reference to all
 * currently instantiated {@link GameObject}s.
 */
public class World
{
    private Map<String, Object> preferences;
    private ArrayList<GameObject> recycleBin;

    private LinkedList<GameObject> objects;

    public World()
    {
        objects = new LinkedList<>();
        recycleBin = new ArrayList<>();
        preferences = new HashMap<>();
    }

    public void start()
    {
        for (GameObject object : objects)
            object.start();
    }

    public void update()
    {
        for(GameObject object : objects)
            object.update();
    }

    public void next() { /* Progress to next world */ }
    public void end() { /* End the game */ }

    public void add(GameObject object) { objects.add(object); }
    public void destroy(GameObject toBeDestroyed) { recycleBin.add(toBeDestroyed); }

    public void recycle()
    {
        objects.removeAll(recycleBin);
        recycleBin.clear();
    }

    public GameObject findGameObjectByName(String name)
    {
        for (GameObject object : objects)
        {
            if (object.getName() != null &&
                    object.getName().equals(name))
                return object;
        }

        return null;
    }

    public LinkedList<GameObject> getObjects() { return objects; }
    public Object getPreference(String key) { return preferences.get(key); }
    public Map<String, Object> getPreferences() { return preferences; }

    public void setPreference(String key, Object value) { preferences.put(key, value); }
}
