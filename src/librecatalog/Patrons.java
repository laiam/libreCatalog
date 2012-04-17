/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author van
 */
class Patrons
{
    private static Set<Patron> patrons;
    
    static void main (String[] args) {
        load(Configure.getProp("PatronDB"));
        System.out.println("Patron records loaded.");
    }
    
    static void load(String filepath)
    {
        File flatDBFile = new File(filepath);
        if (flatDBFile.exists()) {
            try {
                String input, keygroups[];
                Scanner flatDB = new Scanner(flatDBFile);
                while (flatDB.hasNext()) {
                    input = flatDB.next();
                    keygroups = input.split(":next:");
                    for (int idx = 0; idx < keygroups.length; idx++) {
                        
                    }
                }
            }
            catch (FileNotFoundException fnfe) {
                //this section should never be executed.
                UserInterface.Error(301);
            }
        } else {
            Patron temp = new Patron("10085000254877","James","Brown","","admin@domain",0,19900101);
            patrons.add(temp);
        }
            
    }
    
    public static Patron searchPatron(int num)
    {

        Iterator tempPatrons = patrons.iterator();
        
        while (tempPatrons.hasNext())
        {
            Patron tempP = (Patron) tempPatrons.next();
            if (tempP.getBarcode().equals(num))
                return tempP;
        }
        return null;

    }
    
    public static Patron searchPatron(String str)
    {

        throw new UnsupportedOperationException("Not yet implemented");

    }

    public static boolean removePatron(int cardNumber)
    {

        Iterator tempPatrons = patrons.iterator();
        while (tempPatrons.hasNext())
        {
            Patron tempP = (Patron) tempPatrons.next();
            if (tempP.getBarcode().equals(cardNumber))
            {
                patrons.remove(tempP);
                return true;
            }
        }
        return false;
    }

}

class Patron {
    private int phoneNumber,
            birthDate;
    private String firstName,
            lastName,
            address,
            email,
            barcode;
    Patron(String barcode,
           String firstName,
           String lastName,
           String address,
           String email,
           int phone,
           int birthDate)
    {
        if (validBarcode(barcode)) {
            this.barcode=barcode;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthDate = birthDate;
            this.address = address;
            this.email = email;
            this.phoneNumber = phone;
        }
    }
    
    boolean validBarcode(String barcode)
    {
        //barcodes start with a 1
        //four numbers for library
        //seven numbers for user
        if (barcode.length() == 12)
            if (barcode.startsWith("1"))
                    return true;
        return false;
    }
    public String getAddress()
    {
        return address;
    }

    public String getBarcode()
    {
        return barcode;
    }

    public String getEmail()
    {
        return email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public int getPhoneNumber()
    {
        return phoneNumber;
    }

    public int getBirthDate()
    {
        return birthDate;
    }
}