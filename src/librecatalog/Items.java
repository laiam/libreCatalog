/*
 * Name:       Team Innovation
 * Course:     CS225
 * Program:    Project Library
 * Problem:    Create a system for storing library books and patrons, provide methods
 *             for checking out books, and other library related tasks.
 * Class:      Items
 */
package librecatalog;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author David Cross
 */
public class Items
{

    private static LinkedList<Record> items = new LinkedList<Record>();
    private static fileDB<Record> ItemDB = new fileDB<Record>( Configure.getPath(Configure.getSetting( "ItemDB" )) );

    static void main( String[] args )
    {
        ItemDB.load( items );
        System.out.println( items.size() + " Item records loaded." );
    }

    static void unload()
    {
        System.out.println( "Unloading " + items.size() + " Item records" );
        ItemDB.save( items );
    }

    /**
     * Adds a item to the system.
     *
     * @param record An instance of the Record class containing the record.
     * @return boolean success of operation.
     */
    public static boolean addItem( Record record )
    {
        return items.add( record );
    }

    public static void replaceItem( Record oldRecord, Record newRecord )
    {
        int position = items.indexOf( oldRecord );
        items.set( position, newRecord );
    }

    /**
     * Search for items in the database.
     *
     * @param type type of search to perform.
     * 1 - search based on bar code.
     * 2 - search based on Title.
     * 3 - search based on Author.
     * @param str  value to search for.
     * @return an array of items matching the criteria.
     */
    public static Record[] searchItems( int type, String str )
    {
        Iterator itemIterator = items.iterator();
        LinkedList<Record> itemList = new LinkedList<Record>();
        Record tempP;
        Record[] tempArray;
        switch ( type ) {
            case 1: {
                while ( itemIterator.hasNext() ) {
                    tempP = (Record) itemIterator.next();
                    if ( tempP.getBarcode().equals( str ) ) {
                        itemList.add( tempP );
                        break;
                    }
                }
                break;
            }
            case 2:
                while ( itemIterator.hasNext() ) {
                    tempP = (Record) itemIterator.next();
                    if ( tempP.getTitle().equals( str ) )
                        itemList.add( tempP );
                }
                break;
            case 3:
                while ( itemIterator.hasNext() ) {
                    tempP = (Record) itemIterator.next();
                    if ( tempP.getAuthor().equals( str ) )
                        itemList.add( tempP );
                }
                break;
        }
        tempArray = new Record[itemList.size()];
        for ( int idx = 0; idx < itemList.size(); idx++ ) {
            tempArray[idx] = itemList.get( idx );
        }
        return tempArray;

    }

    /**
     * Removes a item from the system.
     *
     * @param record An instance of the Record class containing the record to be
     * removed.
     * @return boolean success of operation.
     */
    public static boolean removeItem( Record record )
    {
        return items.remove( record );
    }

    static String nextAvailableNumber()
    {
        Record lastItem = items.peekLast();
        Record firstItem = items.peekLast();
        String barcode;

        if ( lastItem != null ) {
            if ( lastItem.equals( firstItem ) )
                barcode = Integer.parseInt(
                        lastItem.getBarcode().substring( 5, lastItem.getBarcode().length() ) ) + 1
                        + "";
            else {
                String lastBarcode = lastItem.getBarcode();
                String firstBarcode = firstItem.getBarcode();
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

    public static Record Record( String barcode,
                          String itemTitle,
                          String itemAuthor,
                          String itemGenre,
                          String shelfLocation,
                          int date,
                          String keywords )
    {
        return new Record( barcode, itemTitle, itemAuthor, itemGenre, shelfLocation, date, keywords );
    }

    static class Record implements Serializable
    {//begin class Items

        private String barcode,
                Title,
                Author,
                Genre,
                shelfLocation;
        private String[] tags;
        private int date;

        Record( String barcode,
                String itemTitle,
                String itemAuthor,
                String itemGenre,
                String shelfLocation,
                int date,
                String keywords )
        {
            //method to check for valid barcode
            //method to check for valid shelf location/dewey decimal
            //method to check for valid yyyymmdd date format
            this.barcode = barcode;
            this.Title = itemTitle;
            this.Author = itemAuthor;
            this.Genre = itemGenre;
            this.shelfLocation = shelfLocation;
            this.date = date;
            this.tags = keywords.split( "," );
        }

        public String getBarcode()
        {
            return barcode;
        }

        public String getTitle()
        {
            return Title;
        }

        public String getAuthor()
        {
            return Author;
        }

        public String getGenre()
        {
            return Genre;
        }

        public String getShelfLocation()
        {
            return shelfLocation;
        }

        public int getdate()
        {
            return date;
        }

        public int getDate()
        {
            return date;
        }

        public String[] getTags()
        {
            return tags;
        }

        public void setAuthor( String Author )
        {
            this.Author = Author;
        }

        public void setGenre( String Genre )
        {
            this.Genre = Genre;
        }

        public void setTitle( String Title )
        {
            this.Title = Title;
        }

        public void setDate( int date )
        {
            this.date = date;
        }

        public void setShelfLocation( String shelfLocation )
        {
            this.shelfLocation = shelfLocation;
        }

        public void setTags( String tags )
        {
            this.tags = tags.split( "," );
        }
    }//end class Items
}