package bit.clarksj4.labyrinth.Labyrinth;

import java.text.DecimalFormat;

import bit.clarksj4.labyrinth.Engine.Assets;
import bit.clarksj4.labyrinth.Engine.Component;
import bit.clarksj4.labyrinth.Engine.GameObject;
import bit.clarksj4.labyrinth.Engine.Time;
import bit.clarksj4.labyrinth.Engine.TextRenderer;
import bit.clarksj4.labyrinth.Engine.Vector;
import bit.clarksj4.labyrinth.Engine.Viewport;
import bit.clarksj4.labyrinth.Engine.World;

/**
 * Created by Stephen on 29/05/2016.
 */
public class LabyrinthUI extends Component implements Dwarf.DwarfExitListener, Dwarf.DwarfResetListener
{
    private static DecimalFormat formatter = new DecimalFormat("00.0");
    private float topPadding = 60;
    private TextRenderer currentTimeRenderer;
    private TextRenderer fastestTimeRenderer;
    private float fastestTime;
    private float timeRunning;

    private boolean dwarfResetting;

    public LabyrinthUI(GameObject parent) { super(parent); }

    @Override
    public void start()
    {
        currentTimeRenderer = getComponent(TextRenderer.class);
        fastestTimeRenderer = getComponentInChildren(TextRenderer.class);

        // Get viewport in order to size text correctly
        Viewport viewport =  getComponentInParent(Viewport.class);
        float viewportYExtents = viewport.getSourceBounds().size.y / 2;
        float textYExtents = currentTimeRenderer.getBounds().size.y / 2;

        // Set position so that top of text is at 'topPadding'
        getTransform().setLocalPosition(new Vector(0, -viewportYExtents + textYExtents + topPadding));

        // Get high score from save file
        Object fastestTimeObject = Assets.getPreference("FastestTime");
        if (fastestTimeObject != null && fastestTimeObject instanceof Float)
            fastestTime = (float)fastestTimeObject;

        // Set text to fastest time
        fastestTimeRenderer.setText(formatTime(fastestTime));

        // Get dwarf and listen
        Dwarf dwarf = World.current().findGameObjectByName("Dwarf").getComponent(Dwarf.class);
        dwarf.addExitListener(this);
        dwarf.addResetListener(this);

        dwarfResetting = false;
    }

    @Override
    public void update()
    {
        if (!dwarfResetting)
        {
            // Keep track of time and draw to screen
            timeRunning += Time.getDeltaTime();
            currentTimeRenderer.setText(formatTime(timeRunning));
        }
    }

    private String formatTime(float timeInSeconds)
    {
        int minutes = (int)(timeInSeconds / 60);
        float seconds = timeInSeconds - (minutes * 60);

        return String.format("%02d", minutes) + ":" + formatter.format(seconds);
    }

    @Override
    public void onExiting(Dwarf dwarf)
    {
        // Check for new high score!
        if (timeRunning < fastestTime || fastestTime == 0)
        {
            fastestTime = timeRunning;                                          // Set high score
            fastestTimeRenderer.setText(formatTime(timeRunning));               // Update display
            Assets.setPreference("FastestTime", timeRunning);    // Save score to file
        }

        // Dwarf is resetting, pause the timer
        dwarfResetting = true;
    }

    @Override
    public void onExited(Dwarf dwarf) { /* Nothing */ }

    @Override
    public void onReset(Dwarf dwarf)
    {
        timeRunning = 0;
        dwarfResetting = false;
    }
}
