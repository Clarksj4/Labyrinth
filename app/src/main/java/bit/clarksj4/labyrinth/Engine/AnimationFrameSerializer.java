package bit.clarksj4.labyrinth.Engine;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Stephen on 5/12/2016.
 */

public class AnimationFrameSerializer implements JsonSerializer<AnimationFrame>,
        JsonDeserializer<AnimationFrame>
{
    @Override
    public JsonElement serialize(AnimationFrame src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject json = new JsonObject();
        json.addProperty("Component Type", src.component.getName());             // Component type
        json.addProperty("Time", src.time);                                      // Time
        json.addProperty("Property", src.property);                              // Property
        json.addProperty("Value Type", src.value.getClass().getName());          // Value type

        JsonElement value = context.serialize(src.value, src.value.getClass());  // Value
        json.add("Value", value);

        return json;
    }

    @Override
    public AnimationFrame deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException
    {
        try
        {
            JsonObject jObject = (JsonObject)json;

            // Component type
            String componentName = jObject.get("Component Type").getAsString();
            Class<?> componentType = Class.forName(componentName);

            // Time and property to manipulate
            float time = jObject.get("Time").getAsFloat();
            String property = jObject.get("Property").getAsString();

            // Property value
            String valueTypeName = jObject.get("Value Type").getAsString();     // Get name of property type
            JsonElement valueElement = jObject.get("Value");                    // Get property value as json

            Class<?> valueType = Class.forName(valueTypeName);                  // Get Type of property
            Object value = context.deserialize(valueElement, valueType);        // Deserialize value from json into given type

            return new AnimationFrame(time, componentType, property, value);
        }

        catch (ClassNotFoundException e) { e.printStackTrace(); }

        return null;
    }
}
