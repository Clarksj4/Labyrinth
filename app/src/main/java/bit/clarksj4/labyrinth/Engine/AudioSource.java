package bit.clarksj4.labyrinth.Engine;

/**
 * Component for playing location relative audio
 */
public class AudioSource extends Component
{
    /** By default an audio source will not loop once playback has finished */
    private static final boolean DEFAULT_LOOP = false;

    private int soundID;
    private float volume;
    private boolean loop;

    /**
     * An audio source component attached to the given game object
     * @param gameObject
     */
    public AudioSource(GameObject gameObject)
    {
        super(gameObject);

        loop = DEFAULT_LOOP;
    }

    public int getSoundID() { return soundID; }
    public float getVolume() { return volume; }
    public boolean getLoop() { return loop; }

    public void setSoundID(int soundID) { this.soundID = soundID; }
    public void setVolume(float volume) { this.volume = volume; }
    public void setLoop(boolean loop) { this.loop = loop; }
}
