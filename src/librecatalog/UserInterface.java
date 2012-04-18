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
        if (Configure.getSetting("first-run").equalsIgnoreCase("true"))
            firstRun();
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
        if (productSetupKey())
        {
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

    /**
     * Get information from the user.
     * Eventually we may add support for checking to see if gui is enabled, for
     * now we shall assume it is.
     * @param title title of the graphical message if gui enabled
     * @param message the message to be displayed when prompting for information
     * @return the string input received from the user
     */
    public static String getInput(String title, String message)
    {
        return JOptionPane.showInputDialog(null, message, title,
                                           JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Get confirmation from the user.
     * @see getInput
     * @param title title of the graphical message.
     * @param message the message to be displayed.
     * @return boolean true if ok false if cancel.
     */
    public static boolean confirm(String title, String message)
    {
        int value = JOptionPane.showConfirmDialog(null, message, title,
                                                  JOptionPane.OK_CANCEL_OPTION,
                                                  JOptionPane.QUESTION_MESSAGE);
        if (value == JOptionPane.OK_OPTION)
            return true;
        return false;
    }

    /**
     * Inform the user of something.
     * @param title title of the message to be displayed if using gui
     * @param message the message to be displayed.
     */
    public static void tellUser(String title, String message)
    {
        System.out.println(message);
        JOptionPane.showMessageDialog(null, message, title,
                                      JOptionPane.INFORMATION_MESSAGE);
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
        String title = "Program Menu";
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
                    tellUser(title, "Thank you come again.");
                    break;
                case 1:
                    tellUser(title, "You're searching books now!");
                    break;
                case 2:
                    tellUser(title, "You're placing a hold!");
                    break;
                case 3:
                    tellUser(title, "You are currently viewing a patron account");
                    break;
                case 4:
                    tellUser(title, "You are currently paying or removing a fine.");
                    break;
                case 5:
                    tellUser(title, "You are currently checking out a book.");
                    break;
                case 6:
                    tellUser(title, "You are currently returning a book.");
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
                    tellUser(title, "You are currently adding a book.");
                    break;
                case 11:
                    tellUser(title, "You are currently modifying a book.");
                    break;
                case 12:
                    tellUser(title, "You are currently removing a book.");
                    break;
                case 13:
                    tellUser(title, "You are currently configuring the system.");
                    break;
                default:
                    tellUser(title, "You really messed up this time.");
            }
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
                email,
                birthYear,
                birthMonth,
                birthDay;
        int birthDate;

        if (userLevel == 1)
        {
            String title = "Add Patron";
            firstName = getInput(title, "Enter the Patrons First Name.");
            lastName = getInput(title, "Enter the Patrons Larst Name.");
            address = getInput(title, "Enter the Patrons Address.");
            email = getInput(title, "Enter the Patrons Email.");
            phone = getInput(title, "Enter the Patrons Phone Number.");
            birthYear = getInput(title, "Enter the Year the patron was Born.");
            birthMonth = getInput(title, "Enter the Month the patron was Born.");
            birthDay = getInput(title, "Enter the Day the patron was Born.");
            birthDate = Integer.parseInt(birthYear + birthMonth + birthDay);
            barcode = "1" + Configure.getSetting("library") + Patrons.nextAvailableNumber();

            String message = "Confirm adding the following patron:\n"
                    + "Name: " + firstName + " " + lastName + "\n"
                    + "Address: " + address + "\n"
                    + "email: " + email + "\n"
                    + "phone: " + phone + "\n"
                    + "Birth Date: " + birthDate;
            if (confirm(title, message))
                Patrons.addPatron(new Patron(barcode, firstName, lastName,
                                             address,
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

    public static void remPatron(int userLevel)
    {
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
        System.out.println("Setup mode activated.");
        String setupPass = getInput("Setup Product", "Setup mode detected please enter the product\n"
                + " product key you received with this software.");
        if (setupPass == null)
            return false;
        while (!setupPass.equals("Nova-Gamma-7even-d3lt4"))
        {
            setupPass = getInput("Setup Product", "Unrecognized Password: Please"
            + " re-enter\nthe product key you received with this software.");
            if (setupPass == null)
                return false;
        }
        return true;
    }
}
