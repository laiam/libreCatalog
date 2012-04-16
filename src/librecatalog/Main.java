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
        
        //assume gui enabled by default
        UserInterface.main(args);
    }

    /**
     * System startup
     */
    private static void systemStartup()
    {
        Configure conf = new Configure("config.properties");
        if (!conf.firstRun)
        {
            Patron.load(Configure.getProp("PatronDB"));
            //Item.load(Configure.getProp("ItemDB"));
            //Fines.load(Configure.getProp("FineDB"));
            //ItemAvailability.load(Configure.getProp("AvailabilityDB"));
        }
        else
        {
            //UserInterface.firstRun();
        }
    }
}