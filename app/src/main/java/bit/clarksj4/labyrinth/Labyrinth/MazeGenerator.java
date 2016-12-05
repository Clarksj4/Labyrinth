package bit.clarksj4.labyrinth.Labyrinth;

import java.util.ArrayList;

import bit.clarksj4.labyrinth.Engine.CollectionExtension;
import bit.clarksj4.labyrinth.Engine.Coordinate;
import bit.clarksj4.labyrinth.Engine.UniformArrays;

/**
 * Created by Stephen on 28/11/2016.
 *
 * Creating a standard perfect Maze usually involves "growing" the Maze while ensuring the
 * 'no loops' and 'no isolations' restriction is kept. Start with the outer wall, and add a wall
 * segment touching it at random. Keep on adding wall segments to the Maze at random, but ensure
 * that each new segment touches an existing wall at one end, and has its other end in an unmade
 * portion of the Maze. If you ever added a wall segment where both ends were separate from the rest
 * of the Maze, that would create a detached wall with a loop around it, and if you ever added a
 * segment such that both ends touch the Maze, that would create an inaccessible area. This is the
 * wall adding method; a nearly identical way to do it is passage carved, where new passage sections
 * are carved such that exactly one end touches an existing passage.
 */

public class MazeGenerator
{
    public static final int OPEN = 1;
    public static final int WALL = 3;

    public static int[][] generate(int width, int height)
    {
        int[][] tiles = new int[height][width];

        createOutsideWalls(tiles);
        ArrayList<Coordinate> validTiles = getValidTiles(tiles);
        while(validTiles.size() > 0)
        {
            Coordinate position = CollectionExtension.popRandom(validTiles);
            UniformArrays.set(tiles, position.x, position.y, WALL);

            updateSurroundingValidTiles(tiles, position, validTiles);
        }

        return tiles;
    }

    static void createOutsideWalls(int[][] tiles)
    {
        int width = UniformArrays.getColumns(tiles);
        int height = UniformArrays.getRows(tiles);

        for (int column = 0; column < width; column++)
        {
            for (int row = 0; row < height; row++)
            {
                // Open tile
                int tile = OPEN;

                // If tile is a border tile...
                if (column == 0 || column == width - 1 ||
                    row == 0 || row == height - 1)
                    tile = WALL;                       // Wall tile

                // Create new tile with given tile type (open or wall)
                UniformArrays.set(tiles, column, row, tile);
            }
        }
    }

    static void updateSurroundingValidTiles(int[][] tiles, Coordinate position, ArrayList<Coordinate> validTiles)
    {
        for (Coordinate surrounding : position.surrounding())
        {
            if (isValid(tiles, surrounding))
            {
                if (!validTiles.contains(surrounding))
                    validTiles.add(surrounding);
            }

            else validTiles.remove(surrounding);
        }
    }

    static ArrayList<Coordinate> getValidTiles(int[][] tiles)
    {
        int width = UniformArrays.getColumns(tiles);
        int height = UniformArrays.getRows(tiles);
        ArrayList<Coordinate> validTiles = new ArrayList<>();

        for (int column = 1; column < width - 1; column++)
        {
            for (int row = 1; row < height - 1; row++)
            {
                Coordinate position = new Coordinate(column, row);
                if (isValid(tiles, position))
                    validTiles.add(position);
            }
        }

        return validTiles;
    }

    static boolean isValid(int[][] tiles, Coordinate position)
    {
        // If tile is a passageway
        if (UniformArrays.get(tiles, position.x, position.y) == OPEN &&
            !touchingMultipleAdjacentWalls(tiles, position) &&
            !isolated(tiles, position) &&
            checkDiagonals(tiles, position))
            return true;

        else return false;
    }

    static boolean touchingMultipleAdjacentWalls(int[][] tiles, Coordinate position)
    {
        int adjacentWalls = 0;
        for (Coordinate adjacent : position.adjacent())
        {
            if (UniformArrays.get(tiles, adjacent.x, adjacent.y) == WALL)
                adjacentWalls++;
        }

        return adjacentWalls > 1;
    }

    static boolean isolated(int[][] tiles, Coordinate position)
    {
        int adjacentWalls = 0;
        for (Coordinate adjacent : position.adjacent())
        {
            if (UniformArrays.get(tiles, adjacent.x, adjacent.y) == WALL)
                adjacentWalls++;
        }

        return adjacentWalls == 0;
    }

    static boolean checkDiagonals(int[][] tiles, Coordinate position)
    {
        // Walls adjacent to 'position'
        ArrayList<Coordinate> adjacentWalls = adjacentWalls(tiles, position);
        ArrayList<Coordinate> diagonalWalls = diagonalWalls(tiles, position);

        if (diagonalWalls.size() == 0) return true;

        // Check each diagonal wall
        for (Coordinate diagonalWall : diagonalWalls)
        {
            // Walls adjacent to diagonal wall
            ArrayList<Coordinate> adjacentsToDiagonal = adjacentWalls(tiles, diagonalWall);

            // Diagonal does NOT share an adjacent wall with position
            boolean contains = CollectionExtension.containsAny(adjacentsToDiagonal, adjacentWalls);
            if (!contains)
                return false;
        }

        return true;
    }

    static ArrayList<Coordinate> adjacentWalls(int[][] tiles, Coordinate position)
    {
        ArrayList<Coordinate> adjacentWalls = new ArrayList<>();

        // Check every adjacent tile
        for (Coordinate adjacent : position.adjacent())
        {
            // Is adjacent tile a wall?
            if (contains(tiles, adjacent) &&
                UniformArrays.get(tiles, adjacent.x, adjacent.y) == WALL)
                adjacentWalls.add(adjacent);
        }
        return adjacentWalls;
    }

    static ArrayList<Coordinate> diagonalWalls(int[][] tiles, Coordinate position)
    {
        ArrayList<Coordinate> diagonalWalls = new ArrayList<>();

        // Check every diagonal tile
        for (Coordinate diagonal : position.diagonal())
        {
            // Is diagonal tile a wall?
            if (contains(tiles, diagonal) &&
                UniformArrays.get(tiles, diagonal.x, diagonal.y) == WALL)
                diagonalWalls.add(diagonal);
        }
        return diagonalWalls;
    }

    static boolean contains(int[][] tiles, Coordinate position)
    {
        int width = UniformArrays.getColumns(tiles);
        int height = UniformArrays.getRows(tiles);

        boolean contains = position.x >= 0 && position.x <= width - 1 &&
                position.y >= 0 && position.y <= height - 1;
        return contains;
    }
}
