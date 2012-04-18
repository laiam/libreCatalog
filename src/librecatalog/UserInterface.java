/*
 * Name:       Team Innovation
 * Course:     CS225
 * Program:    Project Library
 * Problem:    Create a system for storing library books and patrons, provide methods
 *             for checking out books, and other library related tasks.
 * Class:      UserInterface
 */
package librecatalog;

import javax.swing.JOptionPane;

/**
 *
 * @author van
 */
public class UserInterface
{

    /**
     * main menu interface. displays a graphical interface with the available
     * options. Load up the real interface its time to play with the big guns.
     */
    public static void main(String[] args)
    {
        int userLevel = 0;
        if (args.length > 0)
            for (int idx = 0; idx < args.length; idx++)
            {
                if (args[idx].equals("--first-run"))
                {
                    Configure.addSetting("first-run", "true");
                    break;
                }
            }
        if (Configure.getSetting("first-run").equalsIgnoreCase("true")) {
            firstRun();
        }
        String passphrase = JOptionPane.showInputDialog("For Patron Access leave blank,"
                + "Enter System Password:");
        //I strongly advise encrypting system passphrases with a sha1 of the
        //password and a random salt right here would be one of the places to
        //encrypt the input passphrase and then compare with the stored hash.
        if (passphrase == null || passphrase.equals(""))
            userLevel = 3;
        else if (passphrase.equals(Configure.getSetting("levelonepass")))
            userLevel = 1;
        else if (passphrase.equals(Configure.getSetting("leveltwopass")))
            userLevel = 2;
        menu(userLevel);

    }

    private static void firstRun()
    {
        if (productSetupKey()) {
            Configure.addSetting("first-run", "false");
            String title = "Initial Setup";
            String admin = "Enter the Admin level passphrase now:";
            String librarian = "Enter the librarian level passphrase now:";
            Configure.addSetting("levelonepass", getInput(title, admin));
            Configure.addSetting("leveltwopass", getInput(title, librarian));
        } else
            System.exit(0);
        System.out.println("Eventually you will configure the system here.");
    }
    
    public static String getInput(String title, String message) {
        return JOptionPane.showInputDialog(null, message, title, JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * The menu itself. Generates a menu for the user based on which pass-phrase
     * they entered.
     *
     * @param prop which user level is available for menus
     */
    private static void menu(int userLevel)
    {
        String menu = "";
        switch (userLevel)
        {
            case 1:
                menu = "7 = Add patron\n"
                        + "8 = Modify Patron\n"
                        + "9 = Remove Patron\n"
                        + "10 = Add Book\n"
                        + "11 = Modify Book\n"
                        + "12 = Remove Book\n"
                        + "13 = Configure System\n";
            case 2:
                menu = "4 = View and Modify Fines\n"
                        + "5 = Checkout Books\n"
                        + "6 = Return Books\n" + menu;
            case 3:
                menu = "1 = Search Books\n"
                        + "2 = Place Hold\n"
                        + "3 = Patron Obligations\n" + menu;
            default:
                menu += "0 = Exit";

        }
        String output = "";
        String userchoice = JOptionPane.showInputDialog(null, menu);
        if (userchoice == null || userchoice.equals(""))
            userchoice = "0";
        int menuchoice = Integer.parseInt(userchoice);

        //each of these JOptionPanes will become their own method calling
        //information from the classes.
        while (menuchoice != 0)
        {
            switch (menuchoice)
            {
                case 0:
                    output = "Thank you come again.";
                    break;
                case 1:
                    output = "You're searching books now!";
                    break;
                case 2:
                    output = "You're placing a hold!";
                    break;
                case 3:
                    output = "You are currently viewing a patron account";
                    break;
                case 4:
                    output = "You are currently paying or removing a fine.";
                    break;
                case 5:
                    output = "You are currently checking out a book.";
                    break;
                case 6:
                    output = "You are currently returning a book.";
                    break;
                case 7:
                    addPatron(userLevel);
                    break;
                case 8:
                    modPatron(userLevel);
                    break;
                case 9:
                    remPatron(userLevel);
                    break;
                case 10:
                    output = "You are currently adding a book.";
                    break;
                case 11:
                    output = "You are currently modifying a book.";
                    break;
                case 12:
                    output = "You are currently removing a book.";
                    break;
                case 13:
                    output = "You are currently configuring the system.";
                    break;
                default:
                    output = "You really messed up this time.";
            }
            System.out.println(output);
            JOptionPane.showMessageDialog(null, output);
            userchoice = JOptionPane.showInputDialog(null, menu);
            if (userchoice == null || userchoice.equals(""))
                userchoice = "0";
            menuchoice = Integer.parseInt(userchoice);
        }
    }

    static void addPatron(int userLevel)
    {
        String barcode,
                phone,
                firstName,
                lastName,
                address,
                email;
        int birthDate;

        if (userLevel == 1)
        {
            firstName =
            JOptionPane.showInputDialog(null,
                                        "Enter the Patrons First Name.",
                                        "Add Patron",
                                        JOptionPane.QUESTION_MESSAGE);
            lastName =
            JOptionPane.showInputDialog(null,
                                        "Enter the Patrons Larst Name.",
                                        "Add Patron",
                                        JOptionPane.QUESTION_MESSAGE);
            address = JOptionPane.showInputDialog(null,
                                                  "Enter the Patrons Address.",
                                                  "Add Patron",
                                                  JOptionPane.QUESTION_MESSAGE);
            email =
            JOptionPane.showInputDialog(null,
                                        "Enter the Patrons Email.",
                                        "Add Patron",
                                        JOptionPane.QUESTION_MESSAGE);
            phone = JOptionPane.showInputDialog(null,
                                                "Enter the Patrons Phone Number.",
                                                "Add Patron",
                                                JOptionPane.QUESTION_MESSAGE);
            String BirthYear =
                   JOptionPane.showInputDialog(null,
                                               "Enter the Year the patron was Born.",
                                               "Add Patron",
                                               JOptionPane.QUESTION_MESSAGE);
            String BirthMonth =
                   JOptionPane.showInputDialog(null,
                                               "Enter the Month the patron was Born.",
                                               "Add Patron",
                                               JOptionPane.QUESTION_MESSAGE);
            String BirthDay =
                   JOptionPane.showInputDialog(null,
                                               "Enter the Day the patron was Born.",
                                               "Add Patron",
                                               JOptionPane.QUESTION_MESSAGE);
            birthDate = Integer.parseInt(BirthYear + BirthMonth + BirthDay);
            barcode = "1" + Configure.getSetting("library") + Patrons.nextAvailableNumber();
            Patrons.addPatron(new Patron(barcode, firstName, lastName, address,
                                         email, phone, birthDate));
        } else
            Error(201);

    }//end addPatron

    static void modPatron(int userLevel)
    {
        if (userLevel == 1)
        {
            String title = "Modify a Patron";
            String menu = "Search for a patron to modify:\n"
                    + "1 - Search by patron barcode\n"
                    + "2 - Search by patron First Name\n"
                    + "3 - Search by patron Last Name";
            String userchoice = JOptionPane.showInputDialog(null, menu, title,
                                                            JOptionPane.QUESTION_MESSAGE);
            if (userchoice == null || userchoice.equals(""))
                userchoice = "0";
            int menuchoice = Integer.parseInt(userchoice);
            switch (menuchoice)
            {
                case 1:
                    String barcode =
                           JOptionPane.showInputDialog(null,
                                                       "Enter the barcode to search for.",
                                                       title,
                                                       JOptionPane.QUESTION_MESSAGE);
                    Patron[] found = Patrons.searchPatron(menuchoice, barcode);
                    if (found.length == 0)
                        JOptionPane.showMessageDialog(null,
                                                      "Unable to find user with that barcode",
                                                      title,
                                                      JOptionPane.INFORMATION_MESSAGE);
                    else
                    {
                        Patron tomodify = found[0];
                        String recordFound = "Record Found\n"
                                + "1 - First Name: " + tomodify.getFirstName() + "\n"
                                + "2 - Last Name" + tomodify.getLastName() + "\n"
                                + "3 - Address: " + tomodify.getAddress() + "\n"
                                + "4 - Phone: " + tomodify.getPhoneNumber() + "\n"
                                + "5 - Email: " + tomodify.getEmail() + "\n"
                                + "Enter the number of the record value you would like to modify";
                        String modify =
                               JOptionPane.showInputDialog(null,
                                                           recordFound,
                                                           title,
                                                           JOptionPane.QUESTION_MESSAGE);

                        switch (Integer.parseInt(modify))
                        {

                        }
                    }
                    break;
                case 2:

                case 3:

                default:

            }
        } else
            Error(201);
    }
    
    public static void remPatron(int userLevel) {
        
    }

    /**
     * Provides for graphical error reporting.
     *
     * @param err error code generated by program section. 100 range -
     * configuration class. 200 range - GUI errors. 300 range - file system
     * errors
     */
    public static void Error(int err)
    {
        int type;
        String title;
        String message;
        switch (err)
        {
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
            case 301:
                message = "There was an error with the patron database,\n"
                        + "The file must have been removed or is inaccessible.";
                title = "File Error";
                type = JOptionPane.ERROR_MESSAGE;
                break;
            case 302:
                message = "There was an error with the Items database,\n"
                        + "The file must have been removed or is inaccessible.";
                title = "File Error";
                type = JOptionPane.ERROR_MESSAGE;
                break;
            case 303:
                message = "There was an error with the Items Availability database,\n"
                        + "The file must have been removed or is inaccessible.";
                title = "File Error";
                type = JOptionPane.ERROR_MESSAGE;
                break;
            case 304:
                message = "There was an error with the Fines database,\n"
                        + "The file must have been removed or is inaccessible.";
                title = "File Error";
                type = JOptionPane.ERROR_MESSAGE;
                break;
            default:
                message = "Error " + err + ": undefined error code generated.";
                title = "Warning";
                type = JOptionPane.WARNING_MESSAGE;
        }
        JOptionPane.showMessageDialog(null, message, title, type);

    }

    /**
     * Allows for setup and reconfiguration of admin and librarian level
     * passwords in the event of a config file misplacement.
     *
     * @return true if authentication succeeded false if it failed.
     */
    static boolean productSetupKey()
    {
        String setupPass = JOptionPane.showInputDialog("Setup mode detected please enter the product\n"
                + " product key you received with this software.");
            if (setupPass==null)
                return false;
        while (!setupPass.equals("Nova-Gamma-7even-d3lt4"))
        {
            setupPass = JOptionPane.showInputDialog(
                    "Unrecognized Password: Please re-enter\nthe product key"
                    + " you received with this software.");
            if (setupPass==null)
                return false;
        }
        System.out.println("Setup mode activated.");
        return true;
    }
}
