package bit.clarksj4.labyrinth.Engine;

import java.io.IOException;

/**
 * Created by StickasaurusRex on 07-Dec-16.
 */

// Encapsulates the things that vary across different operating systems:
    // e.g. IO - saving and loading from file
    //      Screens / displays - how to get metrics etc
public abstract class GameContext
{
    public abstract void saveJSONToFile(String filename, String json) throws IOException;
    public abstract String getJSONFromFile(String filename) throws IOException;
}
