package bit.clarksj4.labyrinth.Engine;

import android.content.Context;

/**
 * Created by StickasaurusRex on 20-Dec-16.
 */

public class Audio
{
    private static GameContext context;

    static void init(GameContext _context)
    {
        context = _context;
    }

    static void release()
    {
        context = null;
    }

    static void play(String audio, float leftVolume, float rightVolume)
    {
        context.playAudio(audio, leftVolume, rightVolume);
    }
}
