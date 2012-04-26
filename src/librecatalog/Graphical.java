/*
 * Name:       Stephen VanDusseldorp
 * Course:     CS225
 * Program:    
 * Problem:    
 * Class:      Graphical
 * Input:      
 * Output:     
 */
package librecatalog;

import java.util.Scanner;
import javax.swing.*;

/**
 *
 * @author Van
 */
public class Graphical extends JFrame
{

    private static int userLevel = 0;
    private static JFrame appFrame = new JFrame();
    private static JSplitPane appSplitPane = new JSplitPane();
    
    public static void main ( String[] args )
    {
        
        if ( args.length > 0 )
        {
            for ( int idx = 0; idx < args.length; idx++ )
            {
                if ( args[idx].equals( "--first-run" ) )
                {
                    Configure.addSetting( "first-run", "true" );
                    break;
                }
                if (args[idx].equals("--old-gui"))
                {
                    Configure.addSetting( "old-gui", "true" );
                    break;
                }
            }
        }
        if ( Configure.getSetting( "first-run" ).equalsIgnoreCase( "true" ) )
        {
            firstRun.main();
        }
        login.main();
        if ( Configure.getSetting( "no-gui" ).equalsIgnoreCase( "true" ) ||
             Configure.getSetting( "old-gui" ).equalsIgnoreCase( "true" ) ) {
            UserInterface.main(userLevel);
        } else {
            initAppFrame();
            appFrame.setVisible(true);
        }
        Configure.removeSetting( "old-gui" );
        
    }

    //<editor-fold defaultstate="collapsed" desc="Graphical dialogs and Alternatives">
    /**
     * Get information from the user.
     *
     * @param title   title of the graphical message if gui enabled
     * @param message the message to be displayed when prompting for information
     *
     * @return the string input received from the user
     */
    public static String askUser ( String title, String message )
    {
        if ( Configure.getSetting( "no-gui" ).contains( "true" ) )
        {
            Scanner in = new Scanner( System.in );
            System.out.println( title + "\n" + message );
            return in.nextLine();
        }
        return JOptionPane.showInputDialog( null, message, title,
                                            JOptionPane.QUESTION_MESSAGE );
    }

    /**
     * Get an integer number from the user.
     * @param title title of the graphical message if gui is enabled
     * @param message message to be displayed.
     * @return user input integer.
     */
    public static int askUserForInt ( String title, String message )
    {
        try
        {
            if ( Configure.getSetting( "no-gui" ).contains( "true" ) )
            {
                Scanner in = new Scanner( System.in );
                System.out.println( title + "\n" + message );
                int next = in.nextInt();
                in.nextLine();
                return next;
            }
            return Integer.parseInt(
                    JOptionPane.showInputDialog( null, message, title,
                                                 JOptionPane.QUESTION_MESSAGE ) );
        } catch ( NumberFormatException nfe )
        {
            return askUserForInt( title, message );
        }
    }

    /**
     * Get confirmation from the user.
     *
     * @see askUser
     * @param title   title of the graphical message.
     * @param message the message to be displayed.
     *
     * @return boolean true if ok false if cancel.
     */
    public static boolean confirm ( String title, String message )
    {
        if ( Configure.getSetting( "no-gui" ).contains( "true" ) )
        {
            Scanner in = new Scanner( System.in );
            System.out.println( title + "\n" + message + "(y or n)" );
            String next = in.nextLine().toUpperCase().charAt( 0 ) + "";
            while ( !next.equals( "Y" ) && !next.equals( "N" ) )
            {
                System.out.println( title + "\n" + message + "(y or n)" );
                next = in.nextLine().toUpperCase().charAt( 0 ) + "";
            }
            if ( next.equals( "Y" ) )
            {
                return true;
            }
            return false;
        }
        int value = JOptionPane.showConfirmDialog( null, message, title,
                                                   JOptionPane.OK_CANCEL_OPTION,
                                                   JOptionPane.QUESTION_MESSAGE );
        if ( value == JOptionPane.OK_OPTION )
        {
            return true;
        }
        return false;
    }

    /**
     * Inform the user of something.
     *
     * @param title   title of the message to be displayed if using gui
     * @param message the message to be displayed.
     */
    public static void tellUser ( String title, String message )
    {
        System.out.println( message );
        if ( Configure.getSetting( "no-gui" ).equals( "false" ) )
        {
            JOptionPane.showMessageDialog( null, message, title,
                                           JOptionPane.INFORMATION_MESSAGE );
        }
    }//end telluser
    //</editor-fold>

    private static void initAppFrame()
    {
        //File Menu
        JMenuBar mainMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu();
        fileMenu.setText("File");
        
        //File > Save 
        JMenuItem save = new JMenuItem();
        
        save.setMnemonic('S');
        save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveChanges();
            }
        });
        fileMenu.setMnemonic('f');
        fileMenu.add(save);
        
        //add File menu
        mainMenuBar.add(fileMenu);
        
//        JMenu HelpMenu = new JMenu();
//        JMenuItem info = new JMenuItem();
//        HelpMenu.setText("Help");
//        HelpMenu.add(info);
        
//        JMenu EditMenu = new JMenu();
//        EditMenu.setText("Edit");
//        mainMenuBar.add(EditMenu);
        
        //Admin level = 3
        //Librarian level = 2
        //Patron level = 1
        
        appFrame.setJMenuBar(mainMenuBar);
        
        //splitpane
        appSplitPane.setOrientation(javax.swing.JSplitPane.HORIZONTAL_SPLIT);
        
        
        appFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        appFrame.setTitle("Libre Catalog");
        appFrame.setSize(500, 300);
    }
    
    public static void saveChanges ()
    {
        Configure.unload();
        Patrons.unload();
        Items.unload();
    }
    
    static class firstRun {
        
        public static void main () {
            if ( productSetupKey() )
            {
                Configure.addSetting( "first-run", "false" );
                String title = "Initial Setup";
                String admin = "Enter the Admin level passphrase now:";
                String librarian = "Enter the librarian level passphrase now:";
                Configure.addSetting( "levelonepass", askUser( title, admin ) );
                Configure.addSetting( "leveltwopass", askUser( title, librarian ) );
            } else
            {
                System.exit( 0 );
            }
            System.out.println( "Log: System configuration complete." );
        }

        /**
        * Allows for setup and reconfiguration of admin and librarian level
        * passwords in the event of a config file misplacement.
        *
        * @return true if authentication succeeded false if it failed.
        */
        static boolean productSetupKey ()
        {
            System.out.println( "Log: Setup mode activated." );
            String setupPass = askUser( "Setup Product",
                                        "Setup mode detected please enter the product\n"
                    + "product key you received with this software." );
            if ( setupPass == null )
            {
                return false;
            }
            while ( !setupPass.equals( "Nova-Gamma-7even-d3lt4" ) )
            {
                setupPass = askUser( "Setup Product",
                                    "Unrecognized Password: Please re-enter\n"
                        + "the product key you received with this software." );
                if ( setupPass == null )
                {
                    return false;
                }
            }
            return true;
        }
    }
    
    public static class login {
        public static void main () {
            String passphrase = askUser( "System Authorization",
                                     "For Patron Access leave blank,\n"
                    + "Enter System Password:" );
            //I strongly advise encrypting system passphrases with a sha1 of the
            //password and a random salt right here would be one of the places to
            //encrypt the input passphrase and then compare with the stored hash.
            if ( passphrase == null || passphrase.equals( "" ) )
            {
                userLevel = 3;
            } else if ( passphrase.equals( Configure.getSetting( "levelonepass" ) ) )
            {
                userLevel = 1;
            } else if ( passphrase.equals( Configure.getSetting( "leveltwopass" ) ) )
            {
                userLevel = 2;
            }
        }
    }
    
}
