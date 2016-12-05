package bit.clarksj4.labyrinth.Labyrinth;

import android.content.res.Resources;

import bit.clarksj4.labyrinth.Engine.AndroidGame;
import bit.clarksj4.labyrinth.Engine.GameObject;
import bit.clarksj4.labyrinth.Engine.TextRenderer;
import bit.clarksj4.labyrinth.Engine.Vector;
import bit.clarksj4.labyrinth.Engine.Viewport;
import bit.clarksj4.labyrinth.Engine.World;
import bit.clarksj4.labyrinth.Engine.WorldLoader;

/**
 * Created by Stephen on 25/11/2016.
 */

public class MenuWorldLoader extends WorldLoader
{
    public MenuWorldLoader(AndroidGame game) { super(game); }

    @Override
    public void load(World world)
    {
        Resources resources = game.getContext().getResources();

        //
        // Viewport
        //
        GameObject viewportObject = new GameObject(world);
        viewportObject.getTransform().setPosition(Vector.zero());
        viewportObject.addComponent(Viewport.class);

        //
        // Title
        //
        GameObject titleTextObject = new GameObject(world);
        titleTextObject.addComponent(TextRenderer.class);

//        // Door bitmaps
//        Bitmap closed = BitmapFactory.decodeResource(resources, R.drawable.door_0);
//        Bitmap opening1 = BitmapFactory.decodeResource(resources, R.drawable.door_1);
//        Bitmap opening2 = BitmapFactory.decodeResource(resources, R.drawable.door_2);
//        Bitmap opening3 = BitmapFactory.decodeResource(resources, R.drawable.door_3);
//        Bitmap open = BitmapFactory.decodeResource(resources, R.drawable.door_4);
//
//        // Door animations
//        Animation closedAnimation = new Animation("Closed", 0.2f, closed);
//        Animation openingAnimation = new Animation("Opening", 0.2f, closed, opening1, opening2, opening3, open);
//        Animation closingAnimation = new Animation("Closing", 0.2f, open, opening3, opening2, opening1, closed);
//        Animation openAnimation = new Animation("Open", 0.2f, open);
//
//        // Dwarf bitmaps
//        Bitmap idle = BitmapFactory.decodeResource(resources, R.drawable.dwarf_0);
//        Bitmap running1 = BitmapFactory.decodeResource(resources, R.drawable.dwarf_1);
//        Bitmap running2 = BitmapFactory.decodeResource(resources, R.drawable.dwarf_2);
//        Bitmap hurt = BitmapFactory.decodeResource(resources, R.drawable.dwarf_3);
//
//        // Dwarf animations
//        Animation idleAnimation = new Animation("Idle", 0.2f, idle);
//        Animation runningAnimation = new Animation("Running", 0.2f, running1, running2);
//        runningAnimation.setLoop(true);
//        Animation hurtAnimation = new Animation("Hurt", 0.2f, hurt);
    }
}
