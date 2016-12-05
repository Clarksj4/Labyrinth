package bit.clarksj4.labyrinth.Engine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Stephen on 26/11/2016.
 */

public class AnimationController extends Component
{
    private ArrayList<AnimationListener> animationListeners;
    private HashMap<String, Animation> animations;
    private String current;
    private float startTime;

    public AnimationController(GameObject gameObject)
    {
        super(gameObject);
        animations = new HashMap<>();
        animationListeners = new ArrayList<>();
    }

    @Override
    public void start()
    {
        play();
    }

    @Override
    public void update()
    {
        // If time has exceeded the end of the animation
        if (getElapsedTime() > animations.get(current).getLength())
        {
            // Inform listeners that animation has complete an iteration
            if (animationListeners.size() > 0)
            {
                for (AnimationListener listener : animationListeners)
                    listener.onAnimationComplete(current);
            }

            // If animation loops...
            if (getCurrentAnimation().getLoop())
            {
                startTime = Time.getInstance().getElapsedTime();  // reset the start time
                play();                                           // Play the animation from the beginning
            }
        }

        // Otherwise, play the next frame in the animation
        else play();
    }

    public void addAnimation(Animation animation)
    {
        animations.put(animation.getName(), animation);
    }

    public void playAnimation(String animation)
    {
        current = animation;
        startTime = Time.getInstance().getElapsedTime();
    }

    public Animation getCurrentAnimation()
    {
        return animations.get(current);
    }

    public void addListener(AnimationListener listener)
    {
        animationListeners.add(listener);
    }

    public void removeListener(AnimationListener listener)
    {
        animationListeners.remove(listener);
    }

    private void play()
    {
        // Get the animation that is currently being played
        Animation currentAnimation = getCurrentAnimation();

        // Calculate how far through the animation is, looping if necessary
        float animationLocalTime = getElapsedTime();

        // Manipulate each component that the animation affects
        for (Class component : currentAnimation.getAffectedComponents())
        {
            // Literal component attached to this gameObject
            Component attachedComponent = getComponent(component);

            // Current frame of animation for given component
            AnimationFrame frame = currentAnimation.getCurrentFrame(component, animationLocalTime);

            try
            {
                // Property to animate
                Method method = attachedComponent.getClass().getDeclaredMethod("set" + frame.property, frame.value.getClass());
                method.invoke(attachedComponent, frame.value);
            }

            // If property is not successfully animated, print error message and continue without animating
            catch (NoSuchMethodException e) { e.printStackTrace(); }
            catch (InvocationTargetException e) { e.printStackTrace(); }
            catch (IllegalAccessException e) { e.printStackTrace(); }
        }
    }

    private float getElapsedTime() { return Time.getInstance().getElapsedTime() - startTime; }

    /**
     * Listener interface for when this animation controller's current animation has complete an
     * iteration of all of its frames
     */
    public interface AnimationListener
    {
        /**
         * Called when the associated animation controller has completed an iteration of all of its
         * frames
         * @param animation The animation that has just completed
         */
        void onAnimationComplete(String animation);
    }
}
