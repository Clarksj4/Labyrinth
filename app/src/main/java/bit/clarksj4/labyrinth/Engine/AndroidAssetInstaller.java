package bit.clarksj4.labyrinth.Engine;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import bit.clarksj4.labyrinth.Engine.Animation;
import bit.clarksj4.labyrinth.Engine.Assets;
import bit.clarksj4.labyrinth.Engine.SpriteRenderer;
import bit.clarksj4.labyrinth.R;

/**
 * Created by Stephen on 8/12/2016.
 */

public class AndroidAssetInstaller
{
    private static Resources resources;

    public static void install(Context context)
    {
        resources = context.getResources();
        loadDoor();
        loadDwarf();
        loadTiles();
        loadColours();
    }

    private static void loadDoor()
    {
        // Door bitmaps
        Bitmap closed = BitmapFactory.decodeResource(resources, R.drawable.door_0);
        Bitmap opening1 = BitmapFactory.decodeResource(resources, R.drawable.door_1);
        Bitmap opening2 = BitmapFactory.decodeResource(resources, R.drawable.door_2);
        Bitmap opening3 = BitmapFactory.decodeResource(resources, R.drawable.door_3);
        Bitmap open = BitmapFactory.decodeResource(resources, R.drawable.door_4);

        // Door closed animation
        Animation doorClosedAnimation = new Animation("Closed");
        doorClosedAnimation.addKeyFrame(SpriteRenderer.class, 0, "Sprite", closed);
        Assets.save(doorClosedAnimation, Animation.class, "Door closed", false);

        // Door opening animation
        Animation doorOpeningAnimation = new Animation("Opening");
        doorOpeningAnimation.addKeyFrame(SpriteRenderer.class, 0, "Sprite", closed);
        doorOpeningAnimation.addKeyFrame(SpriteRenderer.class, 0.2f, "Sprite", opening1);
        doorOpeningAnimation.addKeyFrame(SpriteRenderer.class, 0.4f, "Sprite", opening2);
        doorOpeningAnimation.addKeyFrame(SpriteRenderer.class, 0.6f, "Sprite", opening3);
        doorOpeningAnimation.addKeyFrame(SpriteRenderer.class, 0.8f, "Sprite", open);
        doorOpeningAnimation.addKeyFrame(SpriteRenderer.class, 1.0f, "Sprite", open);
        Assets.save(doorOpeningAnimation, Animation.class, "Door opening", false);

        // Door closing animation
        Animation doorClosingAnimation = new Animation("Closing");
        doorClosingAnimation.addKeyFrame(SpriteRenderer.class, 0, "Sprite", open);
        doorClosingAnimation.addKeyFrame(SpriteRenderer.class, 0.2f, "Sprite", opening3);
        doorClosingAnimation.addKeyFrame(SpriteRenderer.class, 0.4f, "Sprite", opening2);
        doorClosingAnimation.addKeyFrame(SpriteRenderer.class, 0.6f, "Sprite", opening1);
        doorClosingAnimation.addKeyFrame(SpriteRenderer.class, 0.8f, "Sprite", closed);
        doorClosingAnimation.addKeyFrame(SpriteRenderer.class, 1.0f, "Sprite", closed);
        Assets.save(doorClosingAnimation, Animation.class, "Door closing", false);

        // Door open animation
        Animation doorOpenAnimation = new Animation("Open");
        doorOpenAnimation.addKeyFrame(SpriteRenderer.class, 0, "Sprite", open);
        Assets.save(doorOpenAnimation, Animation.class, "Door open", false);
    }

    private static void loadDwarf()
    {
        // Dwarf bitmaps
        Bitmap idle = BitmapFactory.decodeResource(resources, R.drawable.dwarf_0);
        Bitmap running1 = BitmapFactory.decodeResource(resources, R.drawable.dwarf_1);
        Bitmap running2 = BitmapFactory.decodeResource(resources, R.drawable.dwarf_2);
        Bitmap hurt = BitmapFactory.decodeResource(resources, R.drawable.dwarf_3);

        // Dwarf idle animation
        Animation dwarfIdleAnimation = new Animation("Idle");
        dwarfIdleAnimation.addKeyFrame(SpriteRenderer.class, 0, "Sprite", idle);
        Assets.save(dwarfIdleAnimation, Animation.class, "Dwarf idle", false);

        // Dwarf running animation
        Animation dwarfRunningAnimation = new Animation("Running");
        dwarfRunningAnimation.addKeyFrame(SpriteRenderer.class, 0, "Sprite", running1);
        dwarfRunningAnimation.addKeyFrame(SpriteRenderer.class, 0.2f, "Sprite", running2);
        dwarfRunningAnimation.addKeyFrame(SpriteRenderer.class, 0.4f, "Sprite", running2);
        dwarfRunningAnimation.setLoop(true);
        Assets.save(dwarfRunningAnimation, Animation.class, "Dwarf running", false);

        // Dwarf hurt animation
        Animation dwarfHurtAnimation = new Animation("Hurt");
        dwarfHurtAnimation.addKeyFrame(SpriteRenderer.class, 0, "Sprite", hurt);
        Assets.save(dwarfHurtAnimation, Animation.class, "Dwarf hurt", false);
    }

    private static void loadTiles()
    {
        // Tile map tiles
        TypedArray tileIds = resources.obtainTypedArray(R.array.tile_set);
        Bitmap[] tileSet = new Bitmap[tileIds.length()];
        for (int i = 0; i < tileIds.length(); i++)
        {
            // Get resource Id for drawable
            int drawableId = tileIds.getResourceId(i, -1);

            // Convert to bitmap via bitmap factory
            if (drawableId != -1)
                tileSet[i] = BitmapFactory.decodeResource(resources, drawableId);
        }
        tileIds.recycle();
        Assets.save(tileSet, Bitmap[].class, "Tiles", false);
    }

    private static void loadColours()
    {
        int fastestTimeColour = resources.getColor(R.color.fastest_timer_text_color);
        Assets.save(fastestTimeColour, int.class, "Fastest time colour", false);

        int currentTimeColour = resources.getColor(R.color.current_timer_text_color);
        Assets.save(currentTimeColour, int.class, "Current time colour", false);
    }
}
