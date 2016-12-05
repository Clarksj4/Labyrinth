package bit.clarksj4.labyrinth.Engine;

/**
 * Created by Stephen on 30/11/2016.
 */

public class Coordinate
{
    public int x;
    public int y;

    public Coordinate(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Coordinate()
    {
        x = 0;
        y = 0;
    }

    public Coordinate add(Coordinate other) { return add(other.x, other.y); }
    public Coordinate add(int x, int y) { return new Coordinate(this.x + x, this.y + y); }
    public Coordinate subtract(Coordinate other) { return subtract(other.x, other.y); }
    public Coordinate subtract(int x, int y) { return new Coordinate(this.x - x, this.y - y); }
    public Coordinate scale(int factor) { return new Coordinate(x * factor, y * factor); }
    public Coordinate scale(Coordinate factor) { return new Coordinate(x * factor.x, y * factor.y); }
    public Coordinate reverse() { return new Coordinate(-x, -x); }

    public Coordinate[] adjacent()
    {
        Coordinate[] adjacents = new Coordinate[4];
        adjacents[0] = this.add(Coordinate.up());
        adjacents[1] = this.add(Coordinate.right());
        adjacents[2] = this.add(Coordinate.down());
        adjacents[3] = this.add(Coordinate.left());
        return adjacents;
    }

    public Coordinate[] diagonal()
    {
        Coordinate[] diagonals = new Coordinate[4];
        diagonals[0] = this.add(Coordinate.up()).add(Coordinate.right());
        diagonals[1] = this.add(Coordinate.right().add(Coordinate.down()));
        diagonals[2] = this.add(Coordinate.down()).add(Coordinate.left());
        diagonals[3] = this.add(Coordinate.left()).add(Coordinate.up());
        return diagonals;
    }

    public Coordinate[] surrounding()
    {
        Coordinate[] surroundings = new Coordinate[8];
        surroundings[0] = this.add(Coordinate.up());
        surroundings[1] = this.add(Coordinate.up()).add(Coordinate.right());
        surroundings[2] = this.add(Coordinate.right());
        surroundings[3] = this.add(Coordinate.right().add(Coordinate.down()));
        surroundings[4] = this.add(Coordinate.down());
        surroundings[5] = this.add(Coordinate.down()).add(Coordinate.left());
        surroundings[6] = this.add(Coordinate.left());
        surroundings[7] = this.add(Coordinate.left()).add(Coordinate.up());
        return surroundings;
    }

    @Override
    public String toString() { return "(" + x + ", " + y + ")";}

    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof Coordinate))
            return false;

        Coordinate otherCoord = (Coordinate)other;
        return x == otherCoord.x && y == otherCoord.y;
    }

    public static Coordinate up() { return new Coordinate(0, 1); }
    public static Coordinate down() { return new Coordinate(0, -1); }
    public static Coordinate left() { return new Coordinate(-1, 0); }
    public static Coordinate right() { return new Coordinate(1, 0); }
    public static Coordinate one() { return new Coordinate(1, 1); }
    public static Coordinate zero() { return new Coordinate(0, 0); }
}
