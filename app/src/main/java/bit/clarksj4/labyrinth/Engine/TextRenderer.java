package bit.clarksj4.labyrinth.Engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

/**
 * Created by Stephen on 30/05/2016.
 */
public class TextRenderer extends Renderer
{
    // TODO: add z-order to game objects, so they draw in front of each other correctly
    // TODO: world stores game objects by z-order

    private static final String DEFAULT_TEXT = "New Text";
    private static final int DEFAULT_SIZE = 12;
    private static final int DEFAULT_COLOUR = Color.WHITE;
    private static final Paint.Align DEFAULT_ALIGN = Paint.Align.CENTER;

    private String text;
    private Paint paint;

    public TextRenderer(GameObject parent)
    {
        super(parent);

        // Set renderer defaults
        text = DEFAULT_TEXT;

        // Set paint defaults
        paint = new Paint();
        paint.setTextSize(DEFAULT_SIZE);
        paint.setColor(DEFAULT_COLOUR);
        paint.setTextAlign(DEFAULT_ALIGN);
    }

    @Override
    public Rectangle getBounds()
    {
        // Output rect
        Rect bounds = new Rect();

        // Paint measures text based on paint parameters
        paint.getTextBounds(text, 0, text.length(), bounds);

        // Convert to rectangle
        return new Rectangle(getTransform().getPosition(), new Vector(bounds.width(), bounds.height()));
    }

    @Override
    public void draw(Canvas canvas, Rectangle source, Vector positionOnCanvas)
    {
        canvas.drawText(text, positionOnCanvas.x, positionOnCanvas.y, paint);
    }

    public void setPaint(Paint paint) { this.paint = paint; }
    public void setText(String text) { this.text = text; }
    public void setTextSize(float size) { this.paint.setTextSize(size); }
    public void setTextColour(int colour) { this.paint.setColor(colour); }
    public void setTextStyle(TextStyle style)
    {
        switch (style)
        {
            case BOLD:
                paint.setTypeface(Typeface.create(paint.getTypeface(), Typeface.BOLD));
                break;
            case ITALIC:
                paint.setTypeface(Typeface.create(paint.getTypeface(), Typeface.ITALIC));
                break;
            case BOLD_ITALIC:
                paint.setTypeface(Typeface.create(paint.getTypeface(), Typeface.BOLD_ITALIC));
                break;
            case NORMAL:
            default:
                paint.setTypeface(Typeface.create(paint.getTypeface(), Typeface.NORMAL));
                break;
        }
    }

    public enum TextStyle { BOLD, ITALIC, BOLD_ITALIC, NORMAL }
}
