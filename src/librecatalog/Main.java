/*
 * Name:       Team Innovation (gag)
 * Course:     CS225
 * Program:    Project Library
 * Problem:    Create a system for storing library books and patrons, provide methods
 *             for checking out books, and other library related tasks.
 * Class:      Main
 */
package librecatalog;

/**
 *
 * @author van
 */
public class Main
{

    /**
     * Initialize the program, setup the configuration file and load file databases
     * into active memory.
     * 
     * @param args the command line arguments potentially allow for -no-gui arg.
     */
    public static void main(String[] args)
    {
        /* ---Archaic invocations-------------------------------------------- */
        systemStartup();
    }

    /**
     * System startup
     */
    private static void systemStartup()
    {
        Configuration conf = new Configuration("config.properties");
        dbFlat.load(Configuration.getProp("PatronDB"));
        dbFlat.load(Configuration.getProp("ItemDB"));
        dbFlat.load(Configuration.getProp("FineDB"));
        dbFlat.load(Configuration.getProp("AvailabilityDB"));
        
    }
}
