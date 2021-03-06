/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author Charlie Kaden
 */
class Checkouts
{//begin of Record

    private static LinkedList<Record> ItemAvail =
                                      new LinkedList<Record>();
    private static FileOps<Record> ItemavailabilityDB =
                                   new FileOps<Record>( Configure.getPath( Configure.getSetting( "ItemavailabilityDB" ) ) );

    /**
     *
     * @param args
     */
    static void main ( String[] args )
    {//begin of main
        ItemavailabilityDB.load( ItemAvail );
        System.out.println( ItemAvail.size() + " Holds/Checkouts loaded." );
    }//

    static void unload ()
    {
        System.out.println( "Unloading " + ItemAvail.size() + " Holds/Checkouts" );
        ItemavailabilityDB.save( ItemAvail );
    }

    /**
     * Adds a itemAvailability to the system.
     *
     * @param record An instance of the Record class containing the record.
     *
     * @return boolean success of operation.
     */
    public static boolean addAvailability ( Record record )
    {
        return ItemAvail.add( record );
    }

    public static void replaceAvailability ( Record oldRecord, Record newRecord )
    {
        int position = ItemAvail.indexOf( oldRecord );
        ItemAvail.set( position, newRecord );
    }
    
    
    static class checkoutsTab extends JTabbedPane
    {

        checkoutsTab(int userLevel)
        {
            add("Renew", new renewItemPanel());
            if (userLevel >= 1 && userLevel < 3)
            {
                add("Checkout", new JScrollPane(new checkOutPanel()));
                add("Return", new JScrollPane(new returnItemPanel()));
            }
        }
        
        private static class renewItemPanel extends JPanel {
            renewItemPanel() {
                
            }
        }
        
        private static class checkOutPanel extends JPanel {
            checkOutPanel() {
                
            }
        }
        
        private static class returnItemPanel extends JPanel {
            returnItemPanel() {
                
            }
        }
    }

    /**
     * Search for items in the database.
     *
     * @param type type of search to perform.
     * 1 - search based on DueDate.
     * 2 - search based on Record and Patron bar code.
     * 3 - search based on the date the record is created.
     * @param str  value to search for.
     *
     * @return an array of items matching the criteria.
     */
    public static Record[] searchItems ( int type, String str )
    {
        Iterator itemIterator = ItemAvail.iterator();
        LinkedList<Record> itemList = new LinkedList<Record>();
        Record tempP;
        Record[] tempArray;
        switch ( type )
        {
            case 1:
            {
                while ( itemIterator.hasNext() )
                {
                    tempP = (Record) itemIterator.next();
                    if ( tempP.getDueDate().equals( str ) )
                    {
                        itemList.add( tempP );
                    }
                    return (Record[]) itemList.toArray();
                }
                break;
            }
            case 2:
                while ( itemIterator.hasNext() )
                {
                    tempP = (Record) itemIterator.next();
                    if ( tempP.getPatronBarcode().equals( str ) )
                    {
                        itemList.add( tempP );
                    } else if ( tempP.getItemBarcode().equals( str ) )
                    {
                        itemList.add( tempP );
                    }
                }
                break;
            case 3:
                while ( itemIterator.hasNext() )
                {
                    tempP = (Record) itemIterator.next();
                    if ( tempP.getRecordCreated().equals( str ) )
                    {
                        itemList.add( tempP );
                    }
                }
                break;
        }
        tempArray = new Record[itemList.size()];
        for ( int idx = 0; idx < itemList.size(); idx++ )
        {
            tempArray[idx] = itemList.get( idx );
        }
        return tempArray;
    }

    /**
     * Removes a item from the system.
     *
     * @param record An instance of the Record class containing the record to be
     * removed.
     *
     * @return boolean success of operation.
     */
    public static boolean removeItemavailability ( Record record )
    {
        return ItemAvail.remove( record );
    }

    class Record implements Serializable
    {//begin class Items

        private String itemBarcode,
                patronBarcode,
                DueDate,
                RecordCreated;
        private int typeOf;

        public Record ( String itemBarcode, String patronBarcode, String DueDate, String RecordCreated, int typeOf )
        {
            this.itemBarcode = itemBarcode;
            this.patronBarcode = patronBarcode;
            this.DueDate = DueDate;
            this.RecordCreated = RecordCreated;
            this.typeOf = typeOf;
        }

        public String getDueDate ()
        {
            return DueDate;
        }

        public void setDueDate ( String DueDate )
        {
            this.DueDate = DueDate;
        }

        public String getRecordCreated ()
        {
            return RecordCreated;
        }

        public void setRecordCreated ( String RecordCreated )
        {
            this.RecordCreated = RecordCreated;
        }

        public String getItemBarcode ()
        {
            return itemBarcode;
        }

        public void setItemBarcode ( String itemBarcode )
        {
            this.itemBarcode = itemBarcode;
        }

        public String getPatronBarcode ()
        {
            return patronBarcode;
        }

        public void setPatronBarcode ( String patronBarcode )
        {
            this.patronBarcode = patronBarcode;
        }

        public int getTypeOf ()
        {
            return typeOf;
        }

        public void setTypeOf ( int typeOf )
        {
            this.typeOf = typeOf;
        }
    }//end of Record
}
