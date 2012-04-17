/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author van
 */
class Patrons
{

    private static LinkedList<Patron> patrons = new LinkedList<>();

    static void main(String[] args)
    {
        load(Configure.getProp("PatronDB"));

        System.out.println(patrons.size() + " Patron records loaded.");
    }

    static void unload()
    {
        try
        {
            String filepath = Configure.getProp("PatronDB");
            FileOutputStream flatDBFile = new FileOutputStream(filepath);
            ObjectOutputStream out = new ObjectOutputStream(flatDBFile);
            for (Patron p: (Patron[]) patrons.toArray()) {
                out.writeObject(p);
            }
            flatDBFile.close();
        } catch (FileNotFoundException ex)
        {
            //do nothing
        } catch (IOException ex)
        {
            //do nothing
        }
    }

    static void load(String filepath)
    {
        File flatDBFile = new File(filepath);
        if (flatDBFile.exists())
        {
            FileInputStream fin;
            try
            {
                fin = new FileInputStream(filepath);
                ObjectInputStream in = new ObjectInputStream(fin);
                try
                {
                    while (in.available() > 0)
                    {
                        patrons.add((Patron) in.readObject());
                    }
                } catch (EOFException e)
                {
                    //
                } catch (ClassNotFoundException ex)
                {
                    Logger.getLogger(Patrons.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex)
            {
                //
            } catch (IOException ex2)
            {
                //
            }
        } else
        {
            patrons.add(new Patron("10085000254877", "James", "Brown", "", "admin@domain", 0, 19900101));
        }

    }

    /**
     * Search for patrons in the database.
     *
     * @param type type of search to perform. 1 - search based on bar code. 2 -
     * search based on first name. 3 - search based on last name.
     * @param str value to search for.
     * @return an array of patrons matching the criteria.
     */
    public static Patron[] searchPatron(int type, String str)
    {
        Iterator patronIterator = patrons.iterator();
        LinkedList<Patron> patronList = new LinkedList<>();
        Patron tempP;
        switch (type)
        {
            case 1:
            {
                while (patronIterator.hasNext())
                {
                    tempP = (Patron) patronIterator.next();
                    if (tempP.getBarcode().equals(str))
                    {
                        patronList.add(tempP);
                    }
                    return (Patron[]) patronList.toArray();
                }
                break;
            }
            case 2:
                while (patronIterator.hasNext())
                {
                    tempP = (Patron) patronIterator.next();
                    if (tempP.getFirstName().equals(str))
                    {
                        patronList.add(tempP);
                    }
                    return (Patron[]) patronList.toArray();
                }
            case 3:
                while (patronIterator.hasNext())
                {
                    tempP = (Patron) patronIterator.next();
                    if (tempP.getLastName().equals(str))
                    {
                        patronList.add(tempP);
                    }
                    return (Patron[]) patronList.toArray();
                }
        }
        throw new UnsupportedOperationException("Not yet implemented");

    }

    public static boolean removePatron(Patron record)
    {
        return patrons.remove(record);
    }
}

class Patron implements Serializable
{

    private int phoneNumber,
            birthDate;
    private String firstName,
            lastName,
            address,
            email,
            barcode;
    private boolean isValid = true;

    Patron(
            String barcode,
            String firstName,
            String lastName,
            String address,
            String email,
            int phone,
            int birthDate)
    {
        validBarcode(barcode);
        validateName(firstName, lastName);
        if (isValid)
        {
            this.barcode = barcode;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthDate = birthDate;
            this.address = address;
            this.email = email;
            this.phoneNumber = phone;
            isValid = true;
        }
    }

    public void validateBarcode(String barcode)
    {
        //barcodes start with a 1
        //four numbers for library
        //seven numbers for user
        if (barcode.length() != 12 || barcode.startsWith("1"))
        {
            isValid = false;
        }
    }

    private boolean validBarcode(String barcode)
    {
        //barcodes start with a 1
        //four numbers for library
        //seven numbers for user
        if (barcode.length() == 12)
        {
            if (barcode.startsWith("1"))
            {
                return true;
            }
        }
        return false;
    }

    private void validateName(String firstName, String lastName)
    {
        isValid = true;
        if (!firstName.matches("[a-zA-Z0-9,._-]*"))
        {
            isValid = false;
        }
        if (!lastName.matches("[a-zA-Z0-9,._-]*"))
        {
            isValid = false;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="getters and setters" >
    public boolean isValid()
    {
        return isValid;
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
    //</editor-fold>
}