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
class Patron
{

    
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
                
            }
        } else {
            Patron temp = new Patron("Admin","User","","admin@domain",0,"7007",19900101);
        }
            
    }
    
    static void main (String[] args) {
        load(Configure.getProp("PatronDB"));
        System.out.println(patrons.size()+" Patron records loaded.");
    }
    
    private int phoneNumber,
            birthDate,
            barcode;
    private String firstName,
            lastName,
            address,
            email;

    static Set<Patron> patrons;

    public Patron(String firstName, String lastName, String address,
                  String email, int phone, String barcode, int birthDate)
    {
        if (validBarcode(barcode)) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthDate = birthDate;
            this.address = address;
            this.email = email;
            this.phoneNumber = phone;
            this.barcode = Integer.parseInt(barcode);
            patrons.add(this);
        }
    }

    public static Patron searchPatron(int num)
    {

        Iterator tempPatrons = patrons.iterator();
        
        while (tempPatrons.hasNext())
        {
            Patron tempP = (Patron) tempPatrons.next();
            if (tempP.barcode == num)
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
            if (tempP.barcode == cardNumber)
            {
                patrons.remove(tempP);
                return true;
            }
        }
        return false;
    }
    public String getAddress()
    {
        return address;
    }

    public int getBarcode()
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

    private boolean validBarcode(String barcode)
    {
        //barcodes start with a 1
        //four numbers for library
        //seven numbers for user
        if (barcode.length() == 12)
            if (barcode.startsWith("1"))
                    return true;
        return false;
    }
}
