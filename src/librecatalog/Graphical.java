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
    
    public Graphical() {
        initComponents();
    }
    
    public static Graphical main () {
        
        return new Graphical();
    }

    private void initComponents()
    {
        //<editor-fold defaultstate="collapsed" desc="Menus">
        //Menus
        JMenuBar mainMenuBar = new JMenuBar();
        JMenu fileMenu = new JMenu();
        JMenu helpMenu = new JMenu();
        JMenuItem save = new JMenuItem();
        JMenuItem exit = new JMenuItem();
        JMenuItem reconfigure = new JMenuItem();
        JMenuItem about = new JMenuItem();
        
        save.setMnemonic('S');
        save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        save.setText("Save");
        save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveChanges();
            }
        });
        
        exit.setMnemonic('x');
        exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
        exit.setText("Exit");
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setVisible(false);
                dispose();
                Main.gracefulExit();
                Main.conclude();
            }
        });
        
        fileMenu.setText("File");
        fileMenu.setMnemonic('f');
        fileMenu.add(save);
        fileMenu.add(exit);
        
        
        reconfigure.setMnemonic(7);
        reconfigure.setText("Setup Product");
        reconfigure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                firstRun.main();
            }
        });
        
        about.setMnemonic('A');
        about.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        about.setText("About");
        about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tellUser("About","Libre Catalog is a simple library database application.");
            }
        });
        
        helpMenu.setText("Help");
        helpMenu.setMnemonic('h');
        helpMenu.add(reconfigure);
        helpMenu.add(about);
        //add File menu
        mainMenuBar.add(fileMenu);
        mainMenuBar.add(helpMenu);
        
        setJMenuBar(mainMenuBar);
        //</editor-fold>
        
        JTabbedPane tabs = new JTabbedPane();
        
        JTabbedPane patronsTab   = new Patrons.patronTab(userLevel);
        JTabbedPane itemsTab     = new Items.itemTab(userLevel);
        JTabbedPane holdsTab     = new Holds.holdsTab( userLevel );
        JTabbedPane checkOutsTab = new Checkouts.checkoutsTab( userLevel );
        JTabbedPane finesTab     = new JTabbedPane();
        JPanel configTab         = new Configure.configPanel();
        
        patronsTab.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        itemsTab.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        checkOutsTab.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        finesTab.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        
        if (userLevel==1)
            tabs.add("Config",configTab);
        tabs.add("Patrons",patronsTab);
        tabs.add("Items",itemsTab);
        tabs.add("Holds and Checkouts",checkOutsTab);
        if (userLevel<3)
            tabs.add("Fines",finesTab);
        
        
        
        add(tabs);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Libre Catalog");
        pack();
        
    }
    
    
    public static void saveChanges ()
    {
        Configure.unload();
        Patrons.unload();
        Items.unload();
    }
    
    private static int userLevel;
    
    public static int getUserLevel () {
        return userLevel;
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
    
    //<editor-fold defaultstate="collapsed" desc="extras">
    static class firstRun {
        
        public static void main () {
            if ( productSetupKey() )
            {
                Configure.addSetting( "first-run", "false" );
                String title = "Initial Setup";
                String admin = "Enter the Admin level passphrase now:";
                String librarian = "Enter the librarian level passphrase now:";
                //I strongly advise encryption of passwords...
                //right here would be as good a place as any o do so.
                //TODO password encryption.
                Configure.addSetting( "levelonepass", askUser( title, admin ) );
                Configure.addSetting( "leveltwopass", askUser( title, librarian ) );
                System.out.println( "Log: System configuration complete." );
            } else
            {
                System.exit( 0 );
            }
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
            } else if ( passphrase.equals( Configure.getSetting( "leveltwopass" ) ) )
            {
                userLevel = 2;
            } else if ( passphrase.equals( Configure.getSetting( "levelonepass" ) ) )
            {
                userLevel = 1;
            }
        }
    }
    //</editor-fold>
}
