/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.io.File;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author van
 */
class Patron
{
    
    private int barcode,
            phoneNumber,
            birthDate,
            userType; //0 basic user, 1 patron, 2 librarian, 3 admin
    private String firstName,
            lastName,
            address,
            email;

    static Set<Patron> patrons;

    public Patron(String firstName, String lastName, String address,
                  String email, int phone, int barcode, int birthDate)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.address = address;
        this.email = email;
        this.phoneNumber = phone;
        this.barcode = barcode;
        patrons.add(this);
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

    public int getUserType()
    {
        return userType;
    }
}
