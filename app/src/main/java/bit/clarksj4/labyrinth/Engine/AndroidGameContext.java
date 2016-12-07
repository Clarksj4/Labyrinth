package bit.clarksj4.labyrinth.Engine;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by StickasaurusRex on 07-Dec-16.
 */

public class AndroidGameContext extends GameContext
{
    private Context context;

    public AndroidGameContext(Context context)
    {
        this.context = context;
    }

    @Override
    public void saveJSONToFile(String filename, String json) throws IOException
    {
        FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        outputStream.write(json.getBytes());
        outputStream.close();
    }

    @Override
    public String getJSONFromFile(String filename) throws IOException
    {
        // Input streams
        FileInputStream fis = context.openFileInput(filename);          // Read file as bytes
        InputStreamReader isr = new InputStreamReader(fis);             // Read as chars, instead of bytes
        BufferedReader bufferedReader = new BufferedReader(isr);        // Performance increased

        // Parts to construct entire string contents of file
        StringBuilder sb = new StringBuilder();
        String line;

        // Read each line
        while ((line = bufferedReader.readLine()) != null)
            sb.append(line);

        // Entire string contents of file
        return sb.toString();
    }
}
