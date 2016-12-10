package bit.clarksj4.labyrinth.Engine;

import java.util.Random;

/**
 * Static utility class containing math functions
 */
public class MathExtension
{
    private static Random random = new Random();

    public static Vector onUnitCircle()
    {
        return insideUnitCircle().normalized();
    }

    public static Vector insideUnitCircle()
    {
        double rand = random.nextDouble();

        double a = 2 * Math.PI * rand;
        double r = Math.sqrt(rand);

        double x = r * Math.cos(a);
        double y = r * Math.sin(a);

        return new Vector((float)x, (float)y);
    }

    public static boolean random()
    {
        return random.nextBoolean();
    }

    public static int random(int max)
    {
        return random.nextInt(max);
    }

    /**
     * Clamps the given integer value to the given range
     * @param val The value to clamp
     * @param min The minimum value
     * @param max The maximum value
     * @return The value clamped to the given range
     */
    public static int clamp(int val, int min, int max)
    {
        return Math.max(min, Math.min(max, val));
    }

    /**
     * Clamps the given float value to the given range
     * @param val The value to clamp
     * @param min The minimum value
     * @param max The maximum value
     * @return The value clamped to the given range
     */
    public static float clamp(float val, float min, float max) { return Math.max(min, Math.min(max, val)); }

    /**
     * Linearly interpolates between the given values by the given amount
     * @param a The starting value
     * @param b The end value
     * @param t The amount to interpolate by
     * @return Interpolation between the two values by the given amount
     */
    public static float lerp(float a, float b, float t)
    {
        return (a * (1-t)) + (b * t);
    }

    public static double lerp(double a, double b, float t) { return (a * (1 - t)) + (b * t); }

    public static int lerp(int a, int b, float t){ return (int)((a * (1 - t)) + (b * t)); }

    // Calculates the amount that x has lerped between lowerBounds and upperBounds
    public static float percentLerped(float lowerBounds, float upperBounds, float x)
    {
        float distanceBetweenBounds = upperBounds - lowerBounds;
        float xDelta = x - lowerBounds;
        float t = xDelta / distanceBetweenBounds;
        return t;
    }
}
