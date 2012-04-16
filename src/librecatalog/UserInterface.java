/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author van
 */
public class UserInterface
{
    
    /**
     * main menu interface.
     * displays a graphical interface with the available options.
     * Load up the real interface its time to play with the big guns.
     */
    public static void main (String[] args) 
    {
        if (args.length<0)
            for (int idx = 0; idx < args.length;idx++)
                if (args[idx].equals("--first-run")) {
                    firstRun();
                    break;
                }
        String passphrase = JOptionPane.showInputDialog("For Patron Access leave blank,"
                + "Enter System Password:");
        //I strongly advise encrypting system passphrases with a sha1 of the
        //password and a random salt right here would be one of the places to
        //encrypt the input passphrase and then compare with the stored hash.
        if (passphrase.equals(Configure.getProp("adminHash")))
            Configure.setProp("access-level","1");
        else if (passphrase.equals(Configure.getProp("librarianHash")))
            Configure.setProp("access-level","2");
        else if (passphrase.equals(Configure.getProp("userHash")))
            Configure.setProp("access-level","3");
        menu(Configure.getProp("access-level"));
        
    }

    private static void menu(String prop)
    {
        String menu = "";
        switch (Integer.parseInt(prop)) {
            case 1:
                menu  = "7 = Configure System\n"
                      + "6 = Add Book\n"
                      + "9 = Remove Book\n"
                      + "8 = Modify Book\n";
            case 2:
                menu = "4 = Fines\n"
                     + "5 = Checkout Books\n"
                     + "6 = Return Books\n" + menu;
            case 3:
                menu  = "1 = Search Books\n"
                      + "2 = Place Hold\n"
                      + "3 = Patron Account\n" + menu;
            default:
                menu += "0 = Exit";
                
        }
        int menuchoice = Integer.parseInt(JOptionPane.showInputDialog(null, menu));
        while (menuchoice!=0) {
            switch (menuchoice) {
                case 0: break;
                case 1: JOptionPane.showMessageDialog(null, "Your searching books now!"); break;
                case 2: JOptionPane.showMessageDialog(null, "Your placing a hold!"); break;
            }
            menuchoice = Integer.parseInt(JOptionPane.showInputDialog(null, menu));
        }
    }
    
    
    /**
     * Provides for graphical error reporting.
     * @param err error code generated by program section.
     *            100 range - configuration class.
     *            200 range - GUI errors.
     */
    public static void Error(int err) {
        int type;
        String title;
        String message;
        switch(err) {
            case 101:
                message = "The configuration file was not found.";
                title = "Configuration Info";
                type = JOptionPane.WARNING_MESSAGE;
                break;
            case 102:
                message = "Unexpected file input error.";
                title = "Configuration Error";
                type = JOptionPane.ERROR_MESSAGE;
                break;
            case 201:
                message = "Error: Invalid Permission Level.";
                title = "Access Denied";
                type = JOptionPane.WARNING_MESSAGE;
                break;
            default:
                message = "Error "+err+": undefined error code generated.";
                title = "Warning";
                type = JOptionPane.WARNING_MESSAGE;
        }
        JOptionPane.showMessageDialog(null,message,title,type);
                    
    }

    /**
     * Allows for setup and reconfiguration of admin and librarian level passwords
     * in the event of a config file misplacement.
     * @return true if authentication succeeded false if it failed.
     */
    static boolean productSetupKey()
    {
        String setupPass = JOptionPane.showInputDialog("Setup mode detected please enter the setup\n"
                + " password you recieved with this software.");
        while (!setupPass.equals("Nova-Gamma-7even-d3lt4")) {
            setupPass = JOptionPane.showInputDialog(
                    "Unrecognized Password: Please re-enter\nthe setup password"
                    + " you recieved with this software."
                );
            if ("".equals(setupPass) )
                return false;
        }
        System.out.println("Setup mode activated.");
        return true;
    }

    private static void firstRun()
    {
        
    }
}
