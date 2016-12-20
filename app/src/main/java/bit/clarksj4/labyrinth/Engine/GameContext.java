package bit.clarksj4.labyrinth.Engine;

import java.util.Map;

/**
 * Created by StickasaurusRex on 07-Dec-16.
 */

// Encapsulates the things that vary across different operating systems:
    // e.g. IO - saving and loading from file
    //      Screens / displays - how to get metrics etc
abstract class GameContext
{
    void gameResumed() { /* Nothing */ }
    void gamePaused() { /* Nothing */ }
    void release() { /* Nothing */ }

    abstract void playAudio(String audio, float leftVolume, float rightVolume);
    void vibrate(long milliseconds) { /* Nothing */ }
    float[] getAccelerometerInput() { return null; }

    abstract void savePreferences(Map<String, Object> preferences);
    abstract Map<String, Object> loadPreferences();
    abstract boolean saveJSONToFile(String filename, String json, boolean overwrite);
    abstract String getJSONFromFile(String filename);
}
