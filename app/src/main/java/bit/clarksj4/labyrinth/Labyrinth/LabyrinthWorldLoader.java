package bit.clarksj4.labyrinth.Labyrinth;

import android.graphics.Bitmap;

import bit.clarksj4.labyrinth.Engine.Assets;
import bit.clarksj4.labyrinth.Engine.Collider;
import bit.clarksj4.labyrinth.Engine.Animation;
import bit.clarksj4.labyrinth.Engine.AnimationController;
import bit.clarksj4.labyrinth.Engine.Game;
import bit.clarksj4.labyrinth.Engine.GameObject;
import bit.clarksj4.labyrinth.Engine.Rigidbody;
import bit.clarksj4.labyrinth.Engine.SpriteRenderer;
import bit.clarksj4.labyrinth.Engine.TextRenderer;
import bit.clarksj4.labyrinth.Engine.TileMap;
import bit.clarksj4.labyrinth.Engine.TileMapRenderer;
import bit.clarksj4.labyrinth.Engine.Vector;
import bit.clarksj4.labyrinth.Engine.Viewport;
import bit.clarksj4.labyrinth.Engine.WorldLoader;

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





    public LabyrinthWorldLoader(Game game) { super(game); }

    @Override
    public void load()
    {
        //
        // Viewport
        //
        GameObject viewportObject = new GameObject("Viewport");
        viewportObject.addComponent(Viewport.class);

        //
        // Tile map
        //
        GameObject tileMapObject = new GameObject("TileMap");
        tileMapObject.addComponent(TileMap.class);
        tileMapObject.addComponent(Labyrinth.class);

        // Tile map renderer
        TileMapRenderer tileMapRenderer = tileMapObject.addComponent(TileMapRenderer.class);
        tileMapRenderer.setZIndex(-1);

        // Load and assign tiles
        Bitmap[] tileSet = Assets.load("Tiles", Bitmap[].class);
        tileMapRenderer.setTileSet(tileSet);

        //
        // Dwarf
        //
        GameObject dwarfObject = new GameObject("Dwarf");
        viewportObject.getTransform().setParent(dwarfObject.getTransform());    // Follow camera
        viewportObject.getTransform().setLocalPosition(Vector.zero());          // Centered on dwarf
        dwarfObject.addComponent(Dwarf.class);                                  // Dwarf script
        dwarfObject.addComponent(AccelerometerGravity.class);                   // Gravity script

        // AnimationController
        AnimationController dwarfAnimationController = dwarfObject.addComponent(AnimationController.class);
        dwarfAnimationController.addAnimation(Assets.load("Dwarf idle", Animation.class));
        dwarfAnimationController.addAnimation(Assets.load("Dwarf running", Animation.class));
        dwarfAnimationController.addAnimation(Assets.load("Dwarf hurt", Animation.class));

        // Collider
        Collider dwarfCollider = dwarfObject.addComponent(Collider.class);
        dwarfCollider.setSize(new Vector(22, 22));

        dwarfObject.addComponent(Rigidbody.class);                          // Rigidbody

        // Renderer
        SpriteRenderer dwarfSpriteRenderer = dwarfObject.addComponent(SpriteRenderer.class);
        dwarfSpriteRenderer.setZIndex(1);

        //
        // Current time text
        //
        GameObject currentTimeText = new GameObject("Current time");
        currentTimeText.addComponent(LabyrinthUI.class);
        currentTimeText.getTransform().setParent(viewportObject.getTransform());    // Child of viewport

        // Text Renderer
        TextRenderer currentTimeRenderer = currentTimeText.addComponent(TextRenderer.class);
        currentTimeRenderer.setTextColour(Assets.load("Current time colour", int.class));
        currentTimeRenderer.setTextSize(60);
        currentTimeRenderer.setTextStyle(TextRenderer.TextStyle.BOLD);
        currentTimeRenderer.setZIndex(2);
        //
        // Fastest time text
        //
        GameObject fastestTimeText = new GameObject("Fastest Time");
        fastestTimeText.getTransform().setParent(currentTimeText.getTransform());
        fastestTimeText.getTransform().setLocalPosition(new Vector(0, 50));

        // Text Renderer
        TextRenderer fastestTimeRenderer = fastestTimeText.addComponent(TextRenderer.class);
        fastestTimeRenderer.setTextColour(Assets.load("Fastest time colour", int.class));
        fastestTimeRenderer.setTextSize(35);
        fastestTimeRenderer.setTextStyle(TextRenderer.TextStyle.BOLD);
        fastestTimeRenderer.setZIndex(2);
    }
}
