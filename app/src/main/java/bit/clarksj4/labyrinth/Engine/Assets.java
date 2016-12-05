package bit.clarksj4.labyrinth.Engine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Stephen on 2/12/2016.
 */

public class Assets
{
    public static <T extends Serializable> void serialize(T object)
    {
    }

    /*
    * This functions converts Bitmap picture to a string which can be
    * JSONified.
    * */
    public String getStringFromBitmap(Bitmap bitmapPicture)
    {
        // 0 = compress for small size, 100 = compress for maximum quality
        final int COMPRESSION_QUALITY = 100;

        // Output stream
        ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();

        // Compress bitmap
        bitmapPicture.compress(Bitmap.CompressFormat.PNG, COMPRESSION_QUALITY, byteArrayBitmapStream);

        // Convert to byte array
        byte[] b = byteArrayBitmapStream.toByteArray();

        // Convert to string
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /*
    * This Function converts the String back to Bitmap
    * */
    public Bitmap getBitmapFromString(String stringPicture)
    {
        byte[] decodedString = Base64.decode(stringPicture, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
