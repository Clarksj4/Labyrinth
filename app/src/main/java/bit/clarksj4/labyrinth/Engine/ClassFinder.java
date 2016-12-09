//package bit.clarksj4.labyrinth.Engine;
//
///**
// * Static utility class for finding a class by name in package hierarchy
// */
//public class ClassFinder
//{
//    public static final String[] searchPackages = {
//            "bit.clarksj4.labyrinth",
//            "bit.clarksj4.labyrinth.Engine" };
//
//    /**
//     * Searches for the given class by name in the package hierarchy
//     * @param name The name of the class to search for
//     * @return The class object or null if it was not found
//     */
//    public static Class<?> findClassByName(String name)
//    {
//        for (int i = 0; i < searchPackages.length; i++)
//        {
//            try { return Class.forName(searchPackages[i] + "." + name); }
//            catch (ClassNotFoundException e) { /* Not in this package, try another */ }
//        }
//
//        return null;    // Class not found
//    }
//}
