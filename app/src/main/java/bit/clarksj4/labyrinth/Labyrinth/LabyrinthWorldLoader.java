package bit.clarksj4.labyrinth.Labyrinth;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import bit.clarksj4.labyrinth.Engine.AndroidGame;
import bit.clarksj4.labyrinth.Engine.AnimationFrame;
import bit.clarksj4.labyrinth.Engine.AnimationFrameSerializer;
import bit.clarksj4.labyrinth.Engine.BitmapSerializer;
import bit.clarksj4.labyrinth.Engine.Collider;
import bit.clarksj4.labyrinth.Engine.Animation;
import bit.clarksj4.labyrinth.Engine.AnimationController;
import bit.clarksj4.labyrinth.Engine.GameObject;
import bit.clarksj4.labyrinth.Engine.Rigidbody;
import bit.clarksj4.labyrinth.Engine.SpriteRenderer;
import bit.clarksj4.labyrinth.Engine.TextRenderer;
import bit.clarksj4.labyrinth.Engine.TileMap;
import bit.clarksj4.labyrinth.Engine.TileMapRenderer;
import bit.clarksj4.labyrinth.Engine.Vector;
import bit.clarksj4.labyrinth.Engine.Viewport;
import bit.clarksj4.labyrinth.Engine.World;
import bit.clarksj4.labyrinth.Engine.WorldLoader;
import bit.clarksj4.labyrinth.R;

/**
 * Created by Stephen on 29/05/2016.
 */
public class LabyrinthWorldLoader extends WorldLoader
{
    // TODO: Changing gravity should exit in a script, not the game object
    // TODO: Get json world loader working
        // TODO: Save colours and handle value of type: colour
    // TODO: Create splash screen
    // TODO: Move collision logic out of dwarf running state
    // TODO: Zoom on multi-finger touch
    // TODO: Prevent UI Text from enlarging on zoom
    // TODO: Allow resetting fastest time
    // TODO: Add fall, door open, and stepping sounds
    // TODO: Add background music
    // TODO: Save animations to file

    // TODO: Fix hurt animation
    // TODO: Calculate door placement in procedural maze (do in new Labyrinth script attached to tileMap object)
    // TODO: Calculate start position (middle of map)
    // TODO: Calculate placement of holes
    // TODO: Menu screen
    // TODO: Allow for world (scene) switching
    // TODO: Rigidbody collisions can be turned off (isKinematic or just disabled etc)





    public LabyrinthWorldLoader(AndroidGame game) { super(game); }

    @Override
    public void load(World world)
    {
        int[][] tiles = MazeGenerator.generate(50, 50);
//        int[][] tiles = TileMapLoader.load(game.getContext(), "LabyrinthTileMap.csv");
        Resources resources = game.getContext().getResources();

        // Door bitmaps
        Bitmap closed = BitmapFactory.decodeResource(resources, R.drawable.door_0);
        Bitmap opening1 = BitmapFactory.decodeResource(resources, R.drawable.door_1);
        Bitmap opening2 = BitmapFactory.decodeResource(resources, R.drawable.door_2);
        Bitmap opening3 = BitmapFactory.decodeResource(resources, R.drawable.door_3);
        Bitmap open = BitmapFactory.decodeResource(resources, R.drawable.door_4);

        // Door animations
        Animation doorClosedAnimation = new Animation("Closed");
        doorClosedAnimation.addKeyFrame(SpriteRenderer.class, 0, "Sprite", closed);

       //String animationString = new Gson().toJson(doorClosedAnimation);

        Animation doorOpeningAnimation = new Animation("Opening");
        doorOpeningAnimation.addKeyFrame(SpriteRenderer.class, 0, "Sprite", closed);
        doorOpeningAnimation.addKeyFrame(SpriteRenderer.class, 0.2f, "Sprite", opening1);
        doorOpeningAnimation.addKeyFrame(SpriteRenderer.class, 0.4f, "Sprite", opening2);
        doorOpeningAnimation.addKeyFrame(SpriteRenderer.class, 0.6f, "Sprite", opening3);
        doorOpeningAnimation.addKeyFrame(SpriteRenderer.class, 0.8f, "Sprite", open);
        doorOpeningAnimation.addKeyFrame(SpriteRenderer.class, 1.0f, "Sprite", open);

        Animation doorClosingAnimation = new Animation("Closing");
        doorClosingAnimation.addKeyFrame(SpriteRenderer.class, 0, "Sprite", open);
        doorClosingAnimation.addKeyFrame(SpriteRenderer.class, 0.2f, "Sprite", opening3);
        doorClosingAnimation.addKeyFrame(SpriteRenderer.class, 0.4f, "Sprite", opening2);
        doorClosingAnimation.addKeyFrame(SpriteRenderer.class, 0.6f, "Sprite", opening1);
        doorClosingAnimation.addKeyFrame(SpriteRenderer.class, 0.8f, "Sprite", closed);
        doorClosingAnimation.addKeyFrame(SpriteRenderer.class, 1.0f, "Sprite", closed);

        Animation doorOpenAnimation = new Animation("Open");
        doorOpenAnimation.addKeyFrame(SpriteRenderer.class, 0, "Sprite", open);

        // Dwarf bitmaps
        Bitmap idle = BitmapFactory.decodeResource(resources, R.drawable.dwarf_0);
        Bitmap running1 = BitmapFactory.decodeResource(resources, R.drawable.dwarf_1);
        Bitmap running2 = BitmapFactory.decodeResource(resources, R.drawable.dwarf_2);
        Bitmap hurt = BitmapFactory.decodeResource(resources, R.drawable.dwarf_3);

        // Dwarf
        Animation dwarfIdleAnimation = new Animation("Idle");
        dwarfIdleAnimation.addKeyFrame(SpriteRenderer.class, 0, "Sprite", idle);

        Animation dwarfRunningAnimation = new Animation("Running");
        dwarfRunningAnimation.addKeyFrame(SpriteRenderer.class, 0, "Sprite", running1);
        dwarfRunningAnimation.addKeyFrame(SpriteRenderer.class, 0.2f, "Sprite", running2);
        dwarfRunningAnimation.addKeyFrame(SpriteRenderer.class, 0.4f, "Sprite", running2);
        dwarfRunningAnimation.setLoop(true);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(AnimationFrame.class, new AnimationFrameSerializer());
        gsonBuilder.registerTypeHierarchyAdapter(Bitmap.class, new BitmapSerializer());
        Gson gson = gsonBuilder.create();

        String filename = "myfile";

//        try
//        {
//            saveJSONToFile(filename, gson.toJson(dwarfRunningAnimation, Animation.class));
//        }
//
//        catch (IOException e)
//        {
//            e.printStackTrace();
//        }

        dwarfRunningAnimation = null;
        String json = null;


        try
        {
            json = getJSONFromFile(filename);
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        dwarfRunningAnimation = gson.fromJson(json, Animation.class);

        Animation dwarfHurtAnimation = new Animation("Hurt");
        dwarfHurtAnimation.addKeyFrame(SpriteRenderer.class, 0, "Sprite", hurt);

        // Tile map tiles
        TypedArray tileIds = game.getContext().getResources().obtainTypedArray(R.array.tile_set);
        Bitmap[] tileSet = new Bitmap[tileIds.length()];
        for (int i = 0; i < tileIds.length(); i++)
        {
            // Get resource Id for drawable
            int drawableId = tileIds.getResourceId(i, -1);

            // Convert to bitmap via bitmap factory
            if (drawableId != -1)
                tileSet[i] = BitmapFactory.decodeResource(game.getContext().getResources(), drawableId);
        }
        tileIds.recycle();
        //
        // Tile map
        //
        GameObject tileMapObject = new GameObject(world, "TileMap");
        tileMapObject.addComponent(Labyrinth.class);

        // Tile map script
        TileMap tileMap = tileMapObject.addComponent(TileMap.class);
        tileMap.setTiles(tiles);

        // Tile map renderer
        TileMapRenderer tileMapRenderer = tileMapObject.addComponent(TileMapRenderer.class);
        tileMapRenderer.setTileSet(tileSet);
        tileMapRenderer.setZIndex(-1);

        //
        // Door
        //
        GameObject doorObject = new GameObject(world, "Door");
        doorObject.getTransform().setPosition(new Vector(-592, 176));
        doorObject.addComponent(Door.class);                            // Door script

        // AnimationController
        AnimationController doorAnimationController = doorObject.addComponent(AnimationController.class);
        doorAnimationController.addAnimation(doorOpenAnimation);
        doorAnimationController.addAnimation(doorOpeningAnimation);
        doorAnimationController.addAnimation(doorClosingAnimation);
        doorAnimationController.addAnimation(doorClosedAnimation);

        // Collider
        Collider doorCollider = doorObject.addComponent(Collider.class);
        doorCollider.setSize(new Vector(32, 32));
        doorCollider.setIsTrigger(true);

        // Renderer
        SpriteRenderer doorRenderer = doorObject.addComponent(SpriteRenderer.class);
        doorRenderer.setZIndex(0);
        //
        // Dwarf
        //
        GameObject dwarfObject = new GameObject(world, "Dwarf");
//        dwarfObject.getTransform().setPosition(new Vector(-208, -356));
//        dwarfObject.getTransform().setPosition(new Vector(-520, 180));
        dwarfObject.addComponent(Dwarf.class);                              // Dwarf script
        dwarfObject.addComponent(AccelerometerGravity.class);               // Gravity script

        // AnimationController
        AnimationController dwarfAnimationController = dwarfObject.addComponent(AnimationController.class);
        dwarfAnimationController.addAnimation(dwarfIdleAnimation);
        dwarfAnimationController.addAnimation(dwarfRunningAnimation);
        dwarfAnimationController.addAnimation(dwarfHurtAnimation);

        // Collider
        Collider dwarfCollider = dwarfObject.addComponent(Collider.class);
        dwarfCollider.setSize(new Vector(22, 22));

        dwarfObject.addComponent(Rigidbody.class);                          // Rigidbody

        // Renderer
        SpriteRenderer dwarfSpriteRenderer = dwarfObject.addComponent(SpriteRenderer.class);
        dwarfSpriteRenderer.setZIndex(1);
        //
        // Viewport
        //
        GameObject viewportObject = new GameObject(world);
        viewportObject.getTransform().setParent(dwarfObject.getTransform());    // Child of dwarf
        viewportObject.getTransform().setLocalPosition(Vector.zero());          // Centered on dwarf
        viewportObject.addComponent(Viewport.class);
        //
        // Current time text
        //
        GameObject currentTimeText = new GameObject(world);
        currentTimeText.addComponent(LabyrinthUI.class);
        currentTimeText.getTransform().setParent(viewportObject.getTransform());    // Child of viewport

        // Text Renderer
        TextRenderer currentTimeRenderer = currentTimeText.addComponent(TextRenderer.class);
        currentTimeRenderer.setTextColour(game.getContext().getResources().getColor(R.color.current_timer_text_color));
        currentTimeRenderer.setTextSize(60);
        currentTimeRenderer.setTextStyle(TextRenderer.TextStyle.BOLD);
        //
        // Fastest time text
        //
        GameObject fastestTimeText = new GameObject(world);
        fastestTimeText.getTransform().setParent(currentTimeText.getTransform());
        fastestTimeText.getTransform().setLocalPosition(new Vector(0, 50));

        // Text Renderer
        TextRenderer fastestTimeRenderer = fastestTimeText.addComponent(TextRenderer.class);
        fastestTimeRenderer.setTextColour(game.getContext().getResources().getColor(R.color.fastest_timer_text_color));
        fastestTimeRenderer.setTextSize(35);
        fastestTimeRenderer.setTextStyle(TextRenderer.TextStyle.BOLD);
    }

    private void saveJSONToFile(String filename, String json) throws IOException
    {
        FileOutputStream outputStream = game.getContext().openFileOutput(filename, Context.MODE_PRIVATE);
        outputStream.write(json.getBytes());
        outputStream.close();
    }

    private String getJSONFromFile(String filename) throws IOException
    {
        // Input streams
        FileInputStream fis = game.getContext().openFileInput(filename);    // Read file as bytes
        InputStreamReader isr = new InputStreamReader(fis);                 // Read as chars, instead of bytes
        BufferedReader bufferedReader = new BufferedReader(isr);            // Performance increased

        // Parts to construct entire string contents of file
        StringBuilder sb = new StringBuilder();
        String line;

        // Read each line
        while ((line = bufferedReader.readLine()) != null)
            sb.append(line);

        // Entire string contents of file
        return sb.toString();
    }
}
