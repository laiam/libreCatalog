/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author van
 */
class Patrons
{
    private static LinkedList<Patron> patrons = new LinkedList<>();
    
    static void main (String[] args) {
        load(Configure.getProp("PatronDB"));
        System.out.println(patrons.size()+" Patron records loaded.");
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
    
    /**
     * Search for patrons in the database.
     * @param type type of search to perform.
     *             1 - search based on bar code.
     *             2 - search based on first name.
     *             3 - search based on last name.
     * @param str  value to search for.
     * @return an array of patrons matching the criteria.
     */
    public static Patron[] searchPatron(int type, String str)
    {
        Iterator patronIterator = patrons.iterator();
        LinkedList<Patron> patronList = new LinkedList<>();
        Patron tempP;
        switch (type) {
            case 1: {
                while (patronIterator.hasNext()) {
                    tempP = (Patron) patronIterator.next();
                    if (tempP.getBarcode().equals(str))
                        patronList.add(tempP);
                        return (Patron[]) patronList.toArray();
                }
                break;
            }
            case 2:
        }
        throw new UnsupportedOperationException("Not yet implemented");

    }

    public static boolean removePatron(Patron record)
    {
        return patrons.remove(record);
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
    Patron(
            String barcode,
            String firstName,
            String lastName,
            String address,
            String email,
            int phone,
            int birthDate
          )
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