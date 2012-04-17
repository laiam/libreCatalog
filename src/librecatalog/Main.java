/*
 * Name:       Team Innovation
 * Course:     CS225
 * Program:    Project Library
 * Problem:    Create a system for storing library books and patrons, provide methods
 *             for checking out books, and other library related tasks.
 * Class:      Main
 */
package librecatalog;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

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
        
        try {
            //tell java to use the native look.
            String os = System.getProperty("os.name");
            if (os.equalsIgnoreCase("Linux"))
            {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } else
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            //because I really and truly hate the default swing cross platform
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
        
        //load configuration file and settle it in.
        args = Configure.main(args);
        
        //Patron.main(args);
        //Item.main(args);
        //ItemAvailability.main(args);
        //Fines.main(args);
        
        //for now assume gui enabled by default load the user interface
        UserInterface.main(args);
    }
}
