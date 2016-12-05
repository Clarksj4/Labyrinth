package bit.clarksj4.labyrinth.Engine;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by Stephen on 1/12/2016.
 */

public class UniformArrays
{
    public static int[][] createInt(int columns, int rows) { return new int[rows][columns]; }
    public static <T> T[][] create(Class<T> type, int columns, int rows)
    {
        return (T[][])Array.newInstance(type, rows, columns);
    }

    public static <T> int getColumns(T[][] array) { return array[0].length; }
    public static int getColumns(int[][] array) { return array[0].length; }

    public static <T> int getRows(T[][] array) { return array.length; }
    public static int getRows(int[][] array) { return array.length; }

    public static <T> void set(T[][] array, int column, int row, T item) { array[row][column] = item; }
    public static void set(int[][] array, int column, int row, int item) { array[row][column] = item; }

    public static <T> T get(T[][] array, int column, int row) { return array[row][column]; }
    public static int get(int[][] array, int column, int row) { return array[row][column]; }

    public static <T> boolean contains(T[][] array, int column, int row)
    {
        return column >= 0 && column < getColumns(array) &&
                row >= 0 && row < getRows(array);
    }

    public static boolean contains(int[][] array, int column, int row)
    {
        return column >= 0 && column < getColumns(array) &&
                row >= 0 && row < getRows(array);
    }
}
