/*
 * Name:       Team Innovation
 * Course:     CS225
 * Program:    Project Library
 * Problem:    Create a system for storing library books and patrons, provide methods
 *             for checking out books, and other library related tasks.
 * Class:      Main
 */
package librecatalog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author van
 */
public class Main
{

    /**
     * Initialize the program, setup the configuration file and load file
     * databases into active memory.
     *
     * @param args the command line arguments allow for --no-gui arg.
     */
    public static void main(String[] args)
    {
        intro();
        /*
         * ---Archaic invocations--------------------------------------------
         */

        //<editor-fold defaultstate="collapsed" desc="UIManager Look and Feel" >
        try
        {
            //tell java to use the native look.
            String os = System.getProperty("os.name");
            if (os.equalsIgnoreCase("Linux"))
                UIManager.setLookAndFeel(
                        "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            else
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            //because I really and truly hate the default swing cross platform
        } catch (UnsupportedLookAndFeelException e)
        {
            // handle exception
        } catch (ClassNotFoundException e)
        {
            // handle exception
        } catch (InstantiationException e)
        {
            // handle exception
        } catch (IllegalAccessException e)
        {
            // handle exception
        }
        //</editor-fold>

        //load configuration file and settle it in.
        Configure.main(args);

        Patrons.main(args);
        Items.main(args);
        Holds.main(args);
        Checkouts.main(args);
        //Fines.main(args);

        if (args.length > 0)
            for (int idx = 0; idx < args.length; idx++)
            {
                if (args[idx].equals("--first-run"))
                {
                    Configure.addSetting("first-run", "true");
                    break;
                }
                if (args[idx].equals("--old-gui"))
                {
                    Configure.addSetting("old-gui", "true");
                    break;
                }
            }
        if (Configure.getSetting("first-run").equalsIgnoreCase("true"))
            Graphical.firstRun.main();
        Graphical.login.main();
        if (Configure.getSetting("no-gui").equalsIgnoreCase("true")
                || Configure.getSetting("old-gui").equalsIgnoreCase("true"))
        {
            UserInterface.main(Graphical.getUserLevel());
            gracefulExit();
            conclude();
            Configure.removeSetting("old-gui");
        } else
        {
            JFrame AppWindow = Graphical.main();
            AppWindow.setVisible(true);
            AppWindow.addWindowListener(new WindowAdapter()
            {

                public void windowClosing(WindowEvent we)
                {
                    gracefulExit();
                    conclude();
                }
            });
        }
    }

    static void gracefulExit()
    {
        Configure.unload();
        Patrons.unload();
        Items.unload();
        Holds.unload();
        Checkouts.unload();
        //Fines.unload();
    }

    static void intro()
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

    static void conclude()
    {
        String conclusion = ""
                + "*******************************************************************\n"
                + "***  Thank you for choosing the Liberated Library Catalog.      ***\n"
                + "***  If you have any issues or feedback on this program please  ***\n"
                + "***  contact us on the github wiki for this program.            ***\n"
                + "***  http://github.com/laiam/libreCatalog                       ***\n"
                + "*******************************************************************";
        System.out.println(conclusion);
    }
}
