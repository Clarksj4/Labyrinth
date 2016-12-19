package bit.clarksj4.labyrinth.Engine;

/**
 * Created by StickasaurusRex on 16-Dec-16.
 */

public class AudioListener extends Component
{
    public static AudioListener current;

    public AudioListener(GameObject gameObject)
    {
        super(gameObject);

        if (current != null)
            throw new IllegalArgumentException("World already contains an AudioListener");

        current = this;
    }

    public void play(AudioSource source, String audio)
    {
        Audio.play(audio, 1f, 1f);
    }

    @Override
    public void destroy()
    {
        current = null;
    }
}
