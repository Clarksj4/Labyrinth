package bit.clarksj4.labyrinth.Labyrinth;

import bit.clarksj4.labyrinth.Engine.Animation;
import bit.clarksj4.labyrinth.Engine.AnimationController;
import bit.clarksj4.labyrinth.Engine.Assets;
import bit.clarksj4.labyrinth.Engine.Collider;
import bit.clarksj4.labyrinth.Engine.Component;
import bit.clarksj4.labyrinth.Engine.Coordinate;
import bit.clarksj4.labyrinth.Engine.Game;
import bit.clarksj4.labyrinth.Engine.GameObject;
import bit.clarksj4.labyrinth.Engine.MathExtension;
import bit.clarksj4.labyrinth.Engine.Rectangle;
import bit.clarksj4.labyrinth.Engine.SpriteRenderer;
import bit.clarksj4.labyrinth.Engine.Tile;
import bit.clarksj4.labyrinth.Engine.TileMap;
import bit.clarksj4.labyrinth.Engine.UniformArrays;
import bit.clarksj4.labyrinth.Engine.Vector;

/**
 * Created by Stephen on 2/12/2016.
 */

public class Labyrinth extends Component
{
    private String doorPrefab = "Door";
    private TileMap tileMap;

    public Labyrinth(GameObject gameObject)
    {
        super(gameObject);
    }

    @Override
    public void start()
    {
        tileMap = getComponent(TileMap.class);

        int[][] tiles = MazeGenerator.generate(20, 20);
        tileMap.setTiles(tiles);

        placeHoles();
        spawnDoor();
        clearCenterArea();
    }

    private void clearCenterArea()
    {
        Rectangle dwarfStartBounds = new Rectangle(Vector.zero(), Vector.one().scale(Game.UNIT));
        Tile[][] centreArea = tileMap.getTiles(dwarfStartBounds);

        for (int column = 0; column < UniformArrays.getColumns(centreArea); column++)
        {
            for (int row = 0; row < UniformArrays.getRows(centreArea); row++)
            {
                UniformArrays.get(centreArea, column, row).setValue(1);
            }
        }
    }

    private void placeHoles()
    {
        for (int column = 1; column < tileMap.getColumns() - 1; column++)
        {
            for (int row = 1; row < tileMap.getRows() - 1; row++)
            {
                Tile tile = tileMap.getTile(column, row);
                if (tile.getValue() == 3)
                {
                    // 50% chance to turn wall into hole
                    if (Math.random() > 0.75f)
                        tile.setValue(2);
                }
            }
        }
    }

    private void spawnDoor()
    {
        // Remove wall at position
        Coordinate outerWall = randomAccessibleOuterWall();
        tileMap.getTile(outerWall).setValue(0);

        // Get world position
        Vector worldPosition = tileMap.getTile(outerWall).getBounds().center;

        // Create a new door at the world position
        loadDoor(worldPosition);
    }

    private void loadDoor(Vector position)
    {
        GameObject doorObject = new GameObject("Door");
        doorObject.getTransform().setPosition(position);
        doorObject.addComponent(Door.class);                            // Door script

        // AnimationController
        AnimationController doorAnimationController = doorObject.addComponent(AnimationController.class);
        doorAnimationController.addAnimation(Assets.load("Door open", Animation.class));
        doorAnimationController.addAnimation(Assets.load("Door opening", Animation.class));
        doorAnimationController.addAnimation(Assets.load("Door closing", Animation.class));
        doorAnimationController.addAnimation(Assets.load("Door closed", Animation.class));

        // Collider
        Collider doorCollider = doorObject.addComponent(Collider.class);
        doorCollider.setSize(new Vector(32, 32));
        doorCollider.setIsTrigger(true);

        // Renderer
        SpriteRenderer doorRenderer = doorObject.addComponent(SpriteRenderer.class);
        doorRenderer.setZIndex(0);
    }

    private Coordinate randomAccessibleOuterWall()
    {
        // pick random outer wall section
        // Check if it is adjacent to a open tile
        // if it is, replace wall with empty, place door

        boolean adjacentToOpen;
        Coordinate outerWall;

        do
        {
            outerWall = randomOuterWall();
            adjacentToOpen = false;

            // Check each adjacent tile
            for (Coordinate adjacent : outerWall.adjacent())
            {
                // Only worry about it if the coordinate is contained within tileMap
                if (tileMap.contains(adjacent))
                {
                    // Get tile, check if its open
                    Tile adjacentTile = tileMap.getTile(adjacent);
                    if (adjacentTile.getValue() == 1)
                    {
                        adjacentToOpen = true;
                        break;
                    }
                }
            }

        } while(!adjacentToOpen);

        return outerWall;
    }

    public Coordinate randomOuterWall()
    {
        int columns = tileMap.getColumns();
        int rows = tileMap.getRows();

        // Wall will be on the left or right side
        if (MathExtension.random())
        {
            int column = MathExtension.random() ? 0 : columns - 1;  // Left or right column
            int row = MathExtension.random(rows);                   // Random row

            return new Coordinate(column, row);
        }

        // Wall will be on the top or bottom side
        else
        {
            int column = MathExtension.random(columns);             // Random column
            int row = MathExtension.random() ? 0 : columns - 1;     // Top or bottom row

            return new Coordinate(column, row);
        }
    }
}
