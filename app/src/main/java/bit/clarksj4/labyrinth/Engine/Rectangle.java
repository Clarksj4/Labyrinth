package bit.clarksj4.labyrinth.Engine;

/**
 * A rectangle shape
 */
public class Rectangle
{
    public Vector center;
    public Vector size;

    /**
     * A new rectangle with the given central coordinate and size
     * @param center The centre coordinate
     * @param size The width and height
     */
    public Rectangle(Vector center, Vector size)
    {
        this.center = center;
        this.size = size;
    }

    /**
     * A new rectangle with the given central coordinate and size
     * @param cX The central x-coordinate
     * @param cY The central y-coordinate
     * @param width The width
     * @param height The height
     */
    public Rectangle(float cX, float cY, float width, float height)
    {
        center = new Vector(cX, cY);
        size = new Vector(width, height);
    }

    /**
     * Creates a new rectangle offset from this rectangle's position by the given amount.
     * @param delta The offset from this rectangles centre
     * @return A new rectangle with this rectangles size, offset from its centre by the given delta
     */
    public Rectangle translate(Vector delta) { return new Rectangle(center.x + delta.x, center.y + delta.y, size.x, size.y); }

    /**
     * Creates a new rectangle bigger in size that this rectangle by the given factor
     * @param factor The increase in size
     * @return A new rectangle with this rectangles position, but increased in size by the given factor
     */
    public Rectangle increase(Vector factor) { return new Rectangle(center.x, center.y, size.x * factor.x, size.y * factor.y); }

    /**
     * Creates a new rectangle smaller than this rectangle by the given factor
     * @param factor The decrease in size
     * @return A new rectangle with this rectangle's position, but decreased in size by the given factor
     */
    public Rectangle decrease(Vector factor) { return new Rectangle(center.x, center.y, size.x / factor.x, size.y / factor.y); }

    /**
     * Check whether the given rectangle has the same position and size as this rectangle
     * @param other The rectangle to check for equality against
     * @return True if both rectangles have the same position and size
     */
    public boolean equals(Rectangle other) { return center.equals(other.center) && size.equals(other.size); }

    /**
     * Checks if this rectangle intersects with the given rectangle
     * @param other The rectangle to check for intersection with
     * @return True if the rectangles intersect
     */
    public boolean intersects(Rectangle other)
    {
        // If any of others corners lie inside this rectangle, then there is an intersection
        return !(other.getLeft() > getRight() ||
                 other.getRight() < getLeft() ||
                 other.getTop() > getBottom()||
                 other.getBottom() < getTop());
    }

    /**
     * Calculates the area of intersection between this rectangle and the given rectangle
     * @param other The rectangle for calculate and intersection with
     * @return A rectangle that is the intersection of this rectangle and the given rectangle, or
     * null if there is no intersection
     */
    public Rectangle intersection(Rectangle other)
    {
        // Left and right side of intersection area
        float left = Math.max(getLeft(), other.getLeft());
        float right = Math.min(getRight(), other.getRight());

        // If right side if further left than left side, there is no intersection
        if (left >= right)
            return null;

        // Top and bottom of intersection area
        float top = Math.max(getTop(), other.getTop());
        float bottom = Math.min(getBottom(), other.getBottom());

        // If bottom is higher than top, there is no intersection
        if (top >= bottom)
            return null;

        // Calculate rectangle parameters
        float width = right - left;
        float height = bottom - top;
        float cX = left + width / 2;
        float cY = top + height / 2;

        // New rectangle
        return new Rectangle(cX, cY, width, height);
    }

    /**
     * Calculates the shortest distance by which to move this rectangle to extract it from the given
     * rectangle
     * @param other The rectangle to calculate an extraction from
     * @return The shortest distance by which to move this rectangle to extract it from the given rectangle,
     * or null if there is no intersection
     */
    public Vector extract(Rectangle other)
    {
        // Gets the intersection of two rectangles
        Rectangle intersection = intersection(other);
        if (intersection == null)
            return null;

        // Get direction between centres
        Vector direction = center.subtract(other.center);

        // Move by the shortest route to get out of overlapping rectangle
        if (intersection.size.x < intersection.size.y)
            return new Vector(intersection.size.x * Math.signum(direction.x), 0);

        else
            return new Vector(0, intersection.size.y * Math.signum(direction.y));
    }

    /**
     * Checks if this rectangle contains the given point within its bounds
     * @param position The point to check for if it is contained in this rectangle
     * @return True if the given point is within this rectangles bounds
     */
    public boolean contains(Vector position)
    {
        return (position.x >= getLeft() &&
                position.x <= getRight() &&
                position.y >= getTop() &&
                position.y <= getBottom());
    }

    @Override
    public String toString()
    {
        return "x: " + center.x +
                ", y: " + center.y +
                ", width: " + size.x +
                ", height: " + size.y;
    }



    /**
     * Gets the magnitude of this rectangle's size vector
     * @return The magnitude of the rectangle's size vector
     */
    public float getMagnitude() { return (float)Math.sqrt((size.x * size.x) + (size.y * size.y)); }

    /**
     * Gets the x-coordinate of this rectangles left edge
     * @return The x-coordinate of this rectangles left edge
     */
    public float getLeft() { return center.x - (size.x / 2); }

    /**
     * Gets the y-coordinate of this rectangles top edge
     * @return The y-coordinate of this rectangles top edge
     */
    public float getTop() { return center.y - (size.y / 2); }

    /**
     * Gets the x-coordinate of this rectangles right edge
     * @return The x-coordinate of this rectangles right edge
     */
    public float getRight() { return center.x + (size.x / 2); }

    /**
     * Gets the y-coordinate of this rectangles bottom edge
     * @return The y-coordinate of this rectangles bottom edge
     */
    public float getBottom() { return center.y + (size.y / 2); }

    /**
     * Sets the position of the centre of this rectangle
     * @param position The position of the centre of this rectangle
     */
    public void setPosition(Vector position) { center = position; }

    /**
     * Sets the size of this rectangle
     * @param size The size of this rectangle
     */
    public void setSize(Vector size) { this.size = size; }
}
