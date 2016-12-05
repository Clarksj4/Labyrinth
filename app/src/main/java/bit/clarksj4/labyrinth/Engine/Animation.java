package bit.clarksj4.labyrinth.Engine;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Stephen on 25/11/2016.
 */

public class Animation
{
    private static final boolean DEFAULT_LOOP = false;

    private String name;
    private HashMap<String, ArrayList<AnimationFrame>> keyFrames;
    private float length;
    private boolean loop;

    public Animation(String name)
    {
        this.name = name;
        keyFrames = new HashMap<>();
        loop = DEFAULT_LOOP;
    }

    public <T extends Component> void addKeyFrame(Class<T> component, float time, String property, Object value)
    {
        // If no frames exist for the given component, create a new list to store the given frame
        if (!keyFrames.containsKey(component.getName()))
            keyFrames.put(component.getName(), new ArrayList<AnimationFrame>());

        // Add frame to list of frames
        ArrayList<AnimationFrame> componentFrames = keyFrames.get(component.getName());
        componentFrames.add(new AnimationFrame(time, component, property, value));
        Collections.sort(componentFrames);

        calculateLength();
    }

    public <T extends Component> AnimationFrame getCurrentFrame(Class<T> component, float time)
    {
        if (keyFrames.get(component.getName()).size() > 1)
        {
            // Get previous and next frames for given component
            AnimationFrame previous = getPreviousKeyFrame(component, time);
            AnimationFrame next = getNextKeyFrame(component, time);

            // Object to hold value, defaults to previous value, so previous value is returned if type does not lerp
            Object value = previous.value;

            // Only lerp if there is a next value to lerp between
            if (next != null)
            {
                // Calculate lerp percentage
                float t = MathExtension.percentLerped(previous.time, next.time, time);
                //
                // Lerp if able
                //
                if (previous.value instanceof Double)
                    value = MathExtension.lerp((double)previous.value, (double)next.value, t);

                else if (previous.value instanceof Integer)
                    value = MathExtension.lerp((int)previous.value, (int)next.value, t);

                else if (previous.value instanceof Float)
                    value = MathExtension.lerp((float)previous.value, (float)next.value, t);

                else if (previous.value instanceof Vector)
                    value = Vector.lerp((Vector)previous.value, (Vector) next.value, t);
            }

            // Return new frame (because this value is not necessarily a key frame)
            return new AnimationFrame(time, component, previous.property, value);
        }

        else if (keyFrames.get(component.getName()).size() == 1)
        {
            AnimationFrame frame = keyFrames.get(component.getName()).get(0);
            return new AnimationFrame(time, component, frame.property, frame.value);
        }

        else
            throw new NullPointerException("Animation contains no frames");
    }

    public <T extends Component> AnimationFrame getNextKeyFrame(Class<T> component, float time)
    {
        // Get frames for the given component
        ArrayList<AnimationFrame> componentFrames = keyFrames.get(component.getName());

        // binary search returns (-index - 1) when the item does not exist in collection
        int index = Collections.binarySearch(componentFrames, new AnimationFrame(time), new AnimationFrameComparator());

        // If time corresponds to a key frame, return that key frame
        if (index > -1) { return componentFrames.get(index); }

        // If time is between frames, find next frame
        else
        {
            // Undo the transform to get the next item in collection
            int nextFrameIndex = (index + 1) * -1;

            // If index exceeds collection upper bound...
            if (nextFrameIndex > componentFrames.size() - 1)
                throw new IllegalArgumentException("Given time (" + time + ") exceeds the length of the animation (" + length + ")");

            // Otherwise, return next frame
            else { return componentFrames.get(nextFrameIndex); }
        }
    }

    public <T extends Component> AnimationFrame getPreviousKeyFrame(Class<T> component, float time)
    {
        // Get frames for the given component
        ArrayList<AnimationFrame> componentFrames = keyFrames.get(component.getName());

        // binary search returns (-index - 1) when the item does not exist in collection
        int index = Collections.binarySearch(componentFrames, new AnimationFrame(time), new AnimationFrameComparator());

        // If time corresponds to a key frame, return that key frame
        if (index > -1) { return componentFrames.get(index); }

        // If time is between frames, find previous frame
        else
        {
            // Undo the transform to get the NEXT item in collection
            int nextFrameIndex = (index + 1) * -1;

            // If index exceeds collection upper bound...
            if (nextFrameIndex > componentFrames.size() - 1)
                throw new IllegalArgumentException("Given time (" + time + ") exceeds the length of the animation (" + length + ")");

            else
            {
                // Previous frame is NEXT frame - 1
                int previousFrameIndex = nextFrameIndex - 1;

                // Raise exception if time is less than 0
                if (previousFrameIndex == -1)
                    throw new IllegalArgumentException("Given time (" + time + ") is less than zero");

                // Return frame
                return componentFrames.get(previousFrameIndex);
            }
        }
    }

    public Set<Class> getAffectedComponents()
    {
        Set<Class> affectedComponents = new HashSet<>();

        for (ArrayList<AnimationFrame> componentFrames : keyFrames.values())
            affectedComponents.add(componentFrames.get(0).component);

        return affectedComponents;
    }

    public String getName()
    {
        return name;
    }

    public float getLength()
    {
        return length;
    }

    public void setLoop(boolean loop)
    {
        this.loop = loop;
    }

    public boolean getLoop(){ return loop; }

    private void calculateLength()
    {
        for (ArrayList<AnimationFrame> componentFrames : keyFrames.values())
        {
            int lastFrameIndex = componentFrames.size() - 1;
            if (componentFrames.get(lastFrameIndex).time > length)
                length = componentFrames.get(lastFrameIndex).time;
        }
    }

    private class AnimationFrameComparator implements Comparator<AnimationFrame>
    {
        @Override
        public int compare(AnimationFrame lhs, AnimationFrame rhs) {
            return lhs.compareTo(rhs);
        }
    }
}
