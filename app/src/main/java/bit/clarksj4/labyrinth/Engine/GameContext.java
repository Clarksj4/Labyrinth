package bit.clarksj4.labyrinth.Engine;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Created by StickasaurusRex on 07-Dec-16.
 */

// Encapsulates the things that vary across different operating systems:
    // e.g. IO - saving and loading from file
    //      Screens / displays - how to get metrics etc
public abstract class GameContext
{
    public abstract void savePreferences(Map<String, Object> preferences);
    public abstract Map<String, Object> loadPreferences();
    public abstract boolean saveJSONToFile(String filename, String json, boolean overwrite);
    public abstract String getJSONFromFile(String filename);
}
