/*
 * Name:       Team Innovation
 * Course:     CS225
 * Program:    Project Library
 * Problem:    Create a system for storing library books and patrons, provide methods
 *             for checking out books, and other library related tasks.
 * Class:      Patrons
 */
package librecatalog;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Patrons is basically it's own program.. its got it going on and all that...
 * anyway the deal is that the Record class is the data holder and the Patrons
 * class holds a more full way to modify the methods of the patron class.
 * It's almost its own show except for the minor little configure thing there
 * to pull the db location from the config class. Also it can't really do much
 * on it's own.
 *
 * @author van
 */
class Patrons
{

    private static LinkedList<Record> patrons = new LinkedList<Record>();
    private static fileDB<Record> PatronDB = new fileDB<Record>( Configure.getPath(Configure.getSetting( "PatronDB" )) );

    static void main( String[] args )
    {
        PatronDB.load( patrons );
        System.out.println( patrons.size() + " Patron records loaded." );
    }

    static void unload()
    {
        System.out.println( "Unloading " + patrons.size() + " Patron records" );
        PatronDB.save( patrons );
    }

    /**
     * Adds a patron to the system.
     *
     * @param record An instance of the Record class containing the record.
     * @return boolean success of operation.
     */
    public static boolean addPatron( Record record )
    {
        return patrons.add( record );
    }

    public static void replacePatron( Record oldRecord, Record newRecord )
    {
        int position = patrons.indexOf( oldRecord );
        patrons.set( position, newRecord );
    }

    /**
     * Search for patrons in the database.
     *
     * @param type type of search to perform.
     * 1 - search based on bar code.
     * 2 - search based on first name.
     * 3 - search based on last name.
     * @param str  value to search for.
     * @return an array of patrons matching the criteria.
     */
    public static Record[] searchPatrons( int type, String str )
    {
        Iterator patronIterator = patrons.iterator();
        LinkedList<Record> patronList = new LinkedList<Record>();
        Record tempP;
        Record[] tempArray;
        switch ( type ) {
            case 1: {
                while ( patronIterator.hasNext() ) {
                    tempP = (Record) patronIterator.next();
                    if ( tempP.getBarcode().equals( str ) )
                        patronList.add( tempP );
                    tempArray = new Record[patronList.size()];
                    for ( int idx = 0; idx < patronList.size(); idx++ ) {
                        tempArray[idx] = patronList.get( idx );
                    }
                    return tempArray;
                }
                break;
            }
            case 2:
                while ( patronIterator.hasNext() ) {
                    tempP = (Record) patronIterator.next();
                    if ( tempP.getFirstName().equals( str ) )
                        patronList.add( tempP );
                }
                break;
            case 3:
                while ( patronIterator.hasNext() ) {
                    tempP = (Record) patronIterator.next();
                    if ( tempP.getLastName().equals( str ) )
                        patronList.add( tempP );
                }
                break;
        }
        tempArray = new Record[patronList.size()];
        for ( int idx = 0; idx < patronList.size(); idx++ ) {
            tempArray[idx] = patronList.get( idx );
        }
        return tempArray;

    }

    /**
     * Removes a patron from the system.
     *
     * @param record An instance of the Record class containing the record to be
     * removed.
     * @return boolean success of operation.
     */
    public static boolean removePatron( Record record )
    {
        return patrons.remove( record );
    }

    static String nextAvailableNumber()
    {
        Record lastPatron = patrons.peekLast();
        Record firstPatron = patrons.peekLast();
        String barcode;

        if ( lastPatron != null ) {
            if ( lastPatron.equals( firstPatron ) )
                barcode = Integer.parseInt(
                        lastPatron.getBarcode().substring( 5, lastPatron.getBarcode().length() ) ) + 1
                        + "";
            else {
                String lastBarcode = lastPatron.getBarcode();
                String firstBarcode = firstPatron.getBarcode();
                lastBarcode = lastBarcode.substring( 4, lastBarcode.length() );
                firstBarcode = firstBarcode.substring( 4, firstBarcode.length() );
                if ( lastBarcode.compareTo( firstBarcode ) > 0 )
                    barcode = Integer.parseInt( lastBarcode ) + 1 + "";
                else
                    barcode = Integer.parseInt( firstBarcode ) + 1 + "";
            }
            while ( barcode.length() <= 7 ) {
                barcode = "0" + barcode;
            }
            return barcode;
        }
        return "00000001";
    }

    public static Record Record(
            String barcode,
            String firstName,
            String lastName,
            String address,
            String email,
            String phone,
            int birthYear,
            int birthMonth,
            int birthDay )
    {
        return new Record( barcode,
                           firstName,
                           lastName,
                           address,
                           email,
                           phone,
                           birthDay,
                           birthMonth,
                           birthYear );
    }

    static class Record implements Serializable
    {

        private int birthDay,
                birthMonth,
                birthYear;
        private String phoneNumber,
                firstName,
                lastName,
                address,
                email,
                barcode;
        private boolean isValid = true;

        Record(
                String barcode,
                String firstName,
                String lastName,
                String address,
                String email,
                String phone,
                int birthDay,
                int birthMonth,
                int birthYear )
        {
            validBarcode( barcode );
            validateName( firstName, lastName );
            if ( isValid ) {
                this.barcode = barcode;
                this.firstName = firstName;
                this.lastName = lastName;
                this.birthDay = birthDay;
                this.birthMonth = birthMonth;
                this.birthYear = birthYear;
                this.address = address;
                this.email = email;
                this.phoneNumber = phone;
            }
        }

        //<editor-fold defaultstate="collapsed" desc="Validators" >
        public void validateBarcode( String barcode )
        {
            //barcodes start with a 1
            //four numbers for library
            //seven numbers for user
            if ( barcode.length() != 12 || barcode.startsWith( "1" ) )
                isValid = false;
        }

        private boolean validBarcode( String barcode )
        {
            //barcodes start with a 1
            //four numbers for library
            //seven numbers for user
            if ( barcode.length() == 12 )
                if ( barcode.startsWith( "1" ) )
                    return true;
            return false;
        }

        private void validateName( String firstName, String lastName )
        {
            isValid = true;
            if ( !firstName.matches( "[a-zA-Z0-9,._-]*" ) )
                isValid = false;
            if ( !lastName.matches( "[a-zA-Z0-9,._-]*" ) )
                isValid = false;
        }
        //</editor-fold>

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

        public String getPhoneNumber()
        {
            return phoneNumber;
        }

        public int getBirthDay()
        {
            return birthDay;
        }

        public int getBirthMonth()
        {
            return birthMonth;
        }

        public int getBirthYear()
        {
            return birthYear;
        }

        public String getBirthDate()
        {
            return birthMonth + "/" + birthDay + "/" + birthYear;
        }

        public void setAddress( String address )
        {
            this.address = address;
        }

        public void setBirthDay( int birthDay )
        {
            this.birthDay = birthDay;
        }

        public void setBirthMonth( int birthMonth )
        {
            this.birthMonth = birthMonth;
        }

        public void setBirthYear( int birthYear )
        {
            this.birthYear = birthYear;
        }

        public void setEmail( String email )
        {
            this.email = email;
        }

        public void setFirstName( String firstName )
        {
            this.firstName = firstName;
        }

        public void setLastName( String lastName )
        {
            this.lastName = lastName;
        }

        public void setPhoneNumber( String phoneNumber )
        {
            this.phoneNumber = phoneNumber;
        }
        //</editor-fold>
    }
}