package bit.clarksj4.labyrinth.Engine;

/**
 * A vector for storing direction and magnitude in 2d space
 */
public class Vector
{
    public float x;
    public float y;

    /**
     * A new vector with x and y set to 0
     */
    public Vector()
    {
        x = 0;
        y = 0;
    }

    /**
     * A new vector with the given x and y values
     */
    public Vector(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    // Vector Maths
    public Vector add(Vector other) { return new Vector(x + other.x, y + other.y); }
    public Vector add(float x, float y) { return new Vector(this.x + x, this.y + y); }
    public Vector subtract(Vector other) { return new Vector(x - other.x, y - other.y); }
    public Vector divide(float dividend) { return new Vector(x / dividend, y / dividend); }
    public Vector divide(Vector dividend) { return new Vector(x / dividend.x, y / dividend.y); }
    public Vector scale(float factor) { return new Vector(x * factor, y * factor); }
    public Vector scale(Vector factor) { return new Vector(x * factor.x, y * factor.y); }

    /**
     * Gets the magnitude of this vector as calculated by pythagoras' theorom
     * @return The magnitude of this vector
     */
    public float getMagnitude() { return (float)Math.sqrt((x * x) + (y * y)); }

    /**
     * Calculates a vector with the same direction as this vector but a magnitude of 1.
     * @return A new vector with the same direction as this vector but a magnitude of 1.
     */
    public Vector normalized() { return divide(getMagnitude()); }

    /**
     * Calculates a new vector with the same magnitude as this vector but its direction reversed.
     * @return A new vector with the same magnitude as this vector but its direction reversed.
     */
    public Vector reverse() { return new Vector(-x, -y); }

    /**
     * Checks if the given vector and this vector have the same x and y values
     * @param other The vector to check for equality with
     * @return True if both vectors have the same x and y values
     */
    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof Vector))
            return false;

        Vector otherVector = (Vector)other;
        return x == otherVector.x && y == otherVector.y;
    }

    @Override
    public String toString() { return "(" + x + ", " + y + ")";}

    /**
     * Linearly interpolates between two vectors
     * @param a The vector to interpolate from
     * @param b The vector to interpolate towards
     * @param t The amount to interpolate by
     * @return A Vector linearly interpolated relative to the given vectors by the given amount
     */
    public static Vector lerp(Vector a, Vector b, float t)
    {
        t = MathExtension.clamp(t, 0, 1);

        Vector aConcentration = a.scale(1 - t);
        Vector bConcentration = b.scale(t);

        return aConcentration.add(bConcentration);
    }

    /**
     * A vector with both x and y set to 1
     */
    public static Vector one() { return new Vector(1, 1); }

    /**
     * A vector with both x and y set to 0
     */
    public static Vector zero() { return new Vector(0, 0); }
}
