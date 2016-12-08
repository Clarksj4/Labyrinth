package bit.clarksj4.labyrinth.Labyrinth;

import bit.clarksj4.labyrinth.Engine.Component;
import bit.clarksj4.labyrinth.Engine.Coordinate;
import bit.clarksj4.labyrinth.Engine.GameObject;
import bit.clarksj4.labyrinth.Engine.MathExtension;
import bit.clarksj4.labyrinth.Engine.Tile;
import bit.clarksj4.labyrinth.Engine.TileMap;

/**
 * Created by Stephen on 2/12/2016.
 */

public class Labyrinth extends Component
{
    private TileMap tileMap;

    public Labyrinth(GameObject gameObject)
    {
        super(gameObject);
    }

    @Override
    public void start()
    {
        tileMap = getComponent(TileMap.class);

        int[][] tiles = MazeGenerator.generate(50, 50);
        tileMap.setTiles(tiles);

        placeHoles();
        placeDoor();
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
                    {
                        tile.setValue(2);
                    }
                }
            }
        }
    }

    private void placeDoor()
    {
        Coordinate outerWall = randomAccessibleOuterWall();

        // TODO: remove wall
        // TODO: Create door object here!
        // TODO: prefabs! need door animations and images etc
    }

    private Coordinate randomAccessibleOuterWall()
    {
        // pick random outer wall section
        // Check if it is adjacent to a open tile
        // if it is, replace wall with empty, place door

        boolean adjacentToOpen;
        Coordinate outerWall = randomOuterWall();

        do
        {
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

            if (!adjacentToOpen) outerWall = randomOuterWall();
        } while(adjacentToOpen == false);

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
