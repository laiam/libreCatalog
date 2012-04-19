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
     * @param args the command line arguments allow for --no-gui arg.
     */
    public static void main(String[] args)
    {
        intro();
        /* ---Archaic invocations-------------------------------------------- */
        
        //<editor-fold defaultstate="collapsed" desc="UIManager Look and Feel" >
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
        //</editor-fold>
        
        //load configuration file and settle it in.
        Configure.main(args);
        
        Patrons.main(args);
        Items.main(args);
        //ItemAvailability.main(args);
        //Fines.main(args);
        
        UserInterface.main(args);
        
        gracefulExit();
        conclude();
    }
    static void gracefulExit() {
        Configure.unload();
        Patrons.unload();
        Items.unload();
        //ItemAvailability.unload()
        //Fines.unload()
    }

    private static void intro()
    {
        String intro = ""
        + "*******************************************************************\n"
        + "***                                                             ***\n"
        + "***     Welcome to the Liberated Library Cataloguing System     ***\n"
        + "***  ---------------------------------------------------------  ***\n"
        + "***  Lead Programmer: Stephen VanDusseldorp                     ***\n"
        + "***  Assistant:       David Cross                               ***\n"
        + "***  Programmers:     Alex Petsche                              ***\n"
        + "***                   Charlie Kaden                             ***\n"
        + "***                                                             ***\n"
        + "***  Commencing System Startup...                               ***\n"
        + "***                                                             ***\n"
        + "*******************************************************************\n";
        System.out.println(intro);
    }
    private static void conclude() {
        String conclusion = ""
        + "----Thank you for choosing the Liberated Library Catalog System\n"
        + "If you have any issues or feedback on this program please contact\n"
        + "us on the github wiki for this program.\n"
        + "github.com/laiam/libreCatalog";
        System.out.println(conclusion);
    }
}
