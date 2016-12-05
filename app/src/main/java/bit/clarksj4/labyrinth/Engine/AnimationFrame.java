package bit.clarksj4.labyrinth.Engine;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by Stephen on 27/11/2016.
 */

public class AnimationFrame implements Comparable<AnimationFrame>
{
    public float time;
    public Class<?> component;
    public String property;
    public Object value;

    public AnimationFrame(float time)
    {
        this.time = time;

        // Check if time is valid
        if (time < 0)
            throw new IllegalArgumentException("Time: " + time + " is invalid. Time must be positive");
    }

    public AnimationFrame(float time, Class<?> component, String property, Object value)
    {
        this(time);
        this.component = component;
        this.property = property;
        this.value = value;
    }

    @Override
    public int compareTo(AnimationFrame another)
    {
        if (time < another.time) return -1;
        if (time > another.time) return 1;
        else return 0;
    }
}