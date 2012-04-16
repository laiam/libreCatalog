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
     * @param args the command line arguments potentially allow for --no-gui arg.
     */
    public static void main(String[] args)
    {
        /* ---Archaic invocations-------------------------------------------- */
        args = systemStartup(args);
        
        //assume gui enabled by default
        UserInterface.main(args);
    }

    /**
     * System startup
     */
    private static String[] systemStartup(String[] args)
    {
        Configure conf = new Configure("config.properties");
        if (args.length == 0) {
            args    = new String[1];
            args[0] = "--FirstRun";
        } else {
            boolean flagSet = false;
            for (int idx=0;idx < args.length-1; idx++)
                if (args[idx].equals("--FirstRun")||args[idx].equals("-F")) {
                    flagSet=true;
                    break;
                }
            if (!flagSet) {
                String[] temp = new String[args.length+1];
                System.arraycopy(args, 0, temp, 0, args.length);
                temp[args.length]="--FirstRun";
                args = temp;
            }
        }
        return args;
        
    }
}
