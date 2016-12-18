package bit.clarksj4.labyrinth.Labyrinth;

import android.content.res.Resources;
import android.graphics.Bitmap;

import bit.clarksj4.labyrinth.Engine.Animation;
import bit.clarksj4.labyrinth.Engine.AnimationController;
import bit.clarksj4.labyrinth.Engine.Assets;
import bit.clarksj4.labyrinth.Engine.Collider;
import bit.clarksj4.labyrinth.Engine.Game;
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

/**
 * Created by Stephen on 25/11/2016.
 */

public class MenuWorldLoader extends WorldLoader
{
    /**
     * A new loader object that will load objects to the given game
     *
     * @param game The game that this loader will load objects to
     */
    public MenuWorldLoader(Game game)
    {
        super(game);
    }

    @Override
    public void load()
    {
        // Viewport
        // Tilemap -> Lower third of screen
        // Door
        // Dwarf -> viewport is not a child of
        // Title text

        // Hidden
        // Start game button
        // Seed input field
        // High scores



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
    }
}
