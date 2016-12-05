package bit.clarksj4.labyrinth.Engine;

import java.util.ArrayList;

/**
 * Created by Stephen on 30/11/2016.
 */

public class CollectionExtension
{
    public static <T> T random(ArrayList<T> list)
    {
        int index = MathExtension.random(list.size());
        return list.get(index);
    }

    public static <T> T popRandom(ArrayList<T> list)
    {
        int index = MathExtension.random(list.size());
        T item = list.get(index);
        list.remove(index);
        return  item;
    }

    public static <T> boolean containsAny(ArrayList<T> list, ArrayList<T> other)
    {
        for (T item : other)
        {
            if (list.contains(item))
                return true;
        }

        return false;
    }
}
