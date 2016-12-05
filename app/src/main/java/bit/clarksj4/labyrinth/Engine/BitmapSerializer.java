package bit.clarksj4.labyrinth.Engine;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;

/**
 * Created by Stephen on 4/12/2016.
 */

public class BitmapSerializer implements JsonSerializer<Bitmap>, JsonDeserializer<Bitmap>
{
    private static final int COMPRESSION_QUALITY = 100;

    @Override
    public JsonElement serialize(Bitmap src, Type typeOfSrc, JsonSerializationContext context)
    {
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY,
                byteArrayBitmapStream);
        byte[] b = byteArrayBitmapStream.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        return new JsonPrimitive(encodedImage);
    }

    @Override
    public Bitmap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        byte[] decodedString = Base64.decode(json.getAsJsonPrimitive().getAsString(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }
}
