package bit.clarksj4.labyrinth.Engine;

/**
 * Component for playing location relative audio
 */
public class AudioSource extends Component
{
    // Audio source has ref to audio listener
    // Audio source has method play(string) which passes string ref to audio listener
    // Audio listener plays sound at a vol dependant on distance from source
    // Audio listener passes play command to context
    // Context loads the resource via resource name



    /** By default an audio source will not loop once playback has finished */
    private static final boolean DEFAULT_LOOP = false;

    private float volume;
    private boolean loop;

    /**
     * An audio source component attached to the given game object
     */
    public AudioSource(GameObject gameObject)
    {
        super(gameObject);

        loop = DEFAULT_LOOP;
    }

    public void play(String resource)
    {
        AudioListener.current.play(this, resource);
    }

    public void setLoop(boolean loop) { this.loop = loop; }
//    public int getSoundID() { return soundID; }
//    public float getVolume() { return volume; }
//    public boolean getLoop() { return loop; }
//
//    public void setSoundID(int soundID) { this.soundID = soundID; }
//    public void setVolume(float volume) { this.volume = volume; }
//    public void setLoop(boolean loop) { this.loop = loop; }
}
