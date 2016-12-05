package bit.clarksj4.labyrinth.Labyrinth;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Stephen on 7/06/2016.
 */
public class TileMapLoader
{
    public static int[][] load(Context context, String filePath)
    {
        int[][] tileMap = null;
        ArrayList<Integer> tiles = new ArrayList<>();

        try
        {
            AssetManager assetManager = context.getAssets();

            InputStream csvStream = assetManager.open(filePath);
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            BufferedReader csvBufferedReader = new BufferedReader(csvStreamReader);
            String line;

            int columns = 0;
            int rows = 0;
             while ((line = csvBufferedReader.readLine()) != null)
             {
                 String[] tileStrings = line.split(",");
                 for (String tileString : tileStrings)
                     tiles.add(Integer.parseInt(tileString));

                 columns = tileStrings.length;
                 rows++;
            }

            tileMap = new int[rows][columns];
            for (int i = 0; i < tiles.size(); i++)
            {
                int column = i % columns;
                int row = i / columns;

                tileMap[row][column] = tiles.get(i);
            }
        }

        catch(IOException e)
        {
            e.printStackTrace();
        }

        return tileMap;
    }
}
