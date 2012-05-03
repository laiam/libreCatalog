/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.*;

/**
 *
 * @author David Cross
 */
class Items
{

    private static Record selectedItem = null;
    private static LinkedList<Record> items = new LinkedList<Record>();
    private static FileOps<Record> ItemDB = new FileOps<Record>( Configure.getPath( Configure.getSetting( "ItemDB" ) ) );

    static void main ( String[] args )
    {
        ItemDB.load( items );
        System.out.println( items.size() + " Item records loaded." );
    }

    static void unload ()
    {
        System.out.println( "Unloading " + items.size() + " Item records" );
        ItemDB.save( items );
    }

    /**
     * Adds a item to the system.
     *
     * @param record An instance of the Record class containing the record.
     *
     * @return boolean success of operation.
     */
    public static boolean addItem ( Record record )
    {
        return items.add( record );
    }

    public static void replaceItem ( Record oldRecord, Record newRecord )
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
     *
     * @return an array of items matching the criteria.
     */
    public static Record[] searchItems ( int type, String str )
    {
        Iterator itemIterator = items.iterator();
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
                    if ( tempP.getBarcode().equals( str ) )
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
                    if ( tempP.getTitle().equals( str ) )
                    {
                        itemList.add( tempP );
                    }
                }
                break;
            case 3:
                while ( itemIterator.hasNext() )
                {
                    tempP = (Record) itemIterator.next();
                    if ( tempP.getAuthor().equals( str ) )
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
    public static boolean removeItem ( Record record )
    {
        return items.remove( record );
    }

    static String nextAvailableNumber ()
    {
        Record lastItem = items.peekLast();
        Record firstItem = items.peekLast();
        String barcode;

        if ( lastItem != null )
        {
            if ( lastItem.equals( firstItem ) )
            {
                barcode = Integer.parseInt(
                        lastItem.getBarcode().substring( 5, lastItem.getBarcode().length() ) ) + 1
                        + "";
            } else
            {
                String lastBarcode = lastItem.getBarcode();
                String firstBarcode = firstItem.getBarcode();
                lastBarcode = lastBarcode.substring( 4, lastBarcode.length() );
                firstBarcode = firstBarcode.substring( 4, firstBarcode.length() );
                if ( lastBarcode.compareTo( firstBarcode ) > 0 )
                {
                    barcode = Integer.parseInt( lastBarcode ) + 1 + "";
                } else
                {
                    barcode = Integer.parseInt( firstBarcode ) + 1 + "";
                }
            }
            while ( barcode.length() < 7 )
            {
                barcode = "0" + barcode;
            }
            return barcode;
        }
        return "00000001";
    }

    //<editor-fold defaultstate="collapsed" desc="GUI Panels">
    static class itemTab extends JTabbedPane
    {

        itemTab(int userLevel)
        {
            add("Search", new searchItemPanel(userLevel));
            add("Detail View", new JScrollPane(new viewItemPanel()));
            if (userLevel == 1)
            {
                add("Add", new JScrollPane(new addItemPanel()));
                add("Modify", new JScrollPane(new modItemPanel()));
                add("Remove", new remItemPanel());
            }
        }

        static class viewItemPanel extends JPanel
        {
            
            private static JTextArea ItemInfo = new JTextArea("Item Info:\n", 10, 25);
            GroupLayout layout = new GroupLayout(this);
            
            private static void resetForm()
            {
                ItemInfo.setText("Item Info:\n"
                    + "Title: " + selectedItem.getTitle()+"\n"
                    + "Author: " + selectedItem.getAuthor() +"\n"
                    + "Shelf Location: " + selectedItem.getShelfLocation()+"\n"
                    + "Date Written: " + selectedItem.getDate() + "\n"
                    + "Genre: " + selectedItem.getGenre()+"\n"
                    + "Barcode: "+selectedItem.getBarcode());
            }

            public viewItemPanel()
            {
                ItemInfo.setEditable(false);
                setLayout(layout);
                layout.setHorizontalGroup(layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout
                        .createSequentialGroup()
                            .addGroup(layout
                            .createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(ItemInfo)
                        )
                    )
                );
                layout.setVerticalGroup(layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout
                        .createSequentialGroup()
                        .addComponent(ItemInfo)
                    )
                );
            }
        }

        static class searchItemPanel extends JPanel
        {
            
            private GroupLayout layout = new GroupLayout(this);

            searchItemPanel(int level)
            {
                
            }

            private void selectItem()
            {
                
            }

            static void resetSearch()
            {
                
            }
        }

        static class addItemPanel extends JPanel
        {
            
            GroupLayout layout = new GroupLayout(this);

            public addItemPanel()
            {
                
            }

            public void addTheItem()
            {
                
            }

            public static void resetForm()
            {
                
            }
        }

        static class modItemPanel extends JPanel
        {
            
            GroupLayout layout = new GroupLayout(this);

            public modItemPanel()
            {
                
            }

            public void modTheItem()
            {
                
            }

            public static void resetForm()
            {
                
            }
        }

        static class remItemPanel extends JPanel
        {
            
            GroupLayout layout = new GroupLayout(this);
            
            public remItemPanel()
            {
                
            }

            public void remTheItem()
            {
                if (selectedItem != null)
                {
                    //if () {
                    //TODO
                    //check to see if there are any checkouts and alert the user
                    //if there are any
                    //} else 
                    if (
                        Graphical.confirm("Remove Item",
                                          "Are you sure you want to remove the "
                                        + "item?\nThis will remove all checkouts "
                                        + "on and holds placed for this book.")
                       )
                    {
                        //remove fines for item
                        //remove holds for item
                        //last measure just in case remove any records of books
                        //checkedout.
                        removeItem(selectedItem);
                        selectedItem = null;
                        for (int idx = 0; idx <= 999999; idx++)
                        {//waiting...
                        }
                        resetForm();
                        addItemPanel.resetForm();
                        viewItemPanel.resetForm();
                        modItemPanel.resetForm();
                    }
                }
            }

            public static void resetForm()
            {
                if (selectedItem == null) {
                    
                }
                else
                {
                    
                }
            }
        }
    }
    
    //</editor-fold>
    
    public static Record Record ( String barcode,
                                  String itemTitle,
                                  String itemAuthor,
                                  String itemGenre,
                                  String shelfLocation,
                                  int year,
                                  int month,
                                  int day,
                                  String keywords )
    {
        return new Record(barcode, itemTitle, itemAuthor, itemGenre, shelfLocation,
                year, month, day, keywords);
    }

    static class Record implements Serializable
    {//begin class Items

        private String barcode,
                Title,
                Author,
                Genre,
                shelfLocation,
                tagsString;
        private String[] tags;
        private int year, month, day;

        Record ( String barcode,
                 String itemTitle,
                 String itemAuthor,
                 String itemGenre,
                 String shelfLocation,
                 int year,
                 int month,
                 int day,
                 String keywords )
        {
            //todo
            //method to check for valid barcode
            //method to check for valid shelf location/dewey decimal
            this.barcode = barcode;
            this.Title = itemTitle;
            this.Author = itemAuthor;
            this.Genre = itemGenre;
            this.shelfLocation = shelfLocation;
            this.year = year;
            this.month = month;
            this.day = day;
            this.tagsString = keywords;
            this.tags = keywords.split( "," );
        }

        public String getAuthor ()
        {
            return Author;
        }

        public String getGenre ()
        {
            return Genre;
        }

        public String getTitle ()
        {
            return Title;
        }

        public String getBarcode ()
        {
            return barcode;
        }

        public String getDate () {
            return month + "/" + day + "/" + year;
        }
        
        public int getDay ()
        {
            return day;
        }

        public int getMonth ()
        {
            return month;
        }

        public String getShelfLocation ()
        {
            return shelfLocation;
        }

        public String getTagsString ()
        {
            return tagsString;
        }
        
        public String[] getTags ()
        {
            return tags;
        }

        public int getYear ()
        {
            return year;
        }

        public void setAuthor ( String Author )
        {
            this.Author = Author;
        }

        public void setGenre ( String Genre )
        {
            this.Genre = Genre;
        }

        public void setTitle ( String Title )
        {
            this.Title = Title;
        }

        public void setBarcode ( String barcode )
        {
            this.barcode = barcode;
        }

        public void setDay ( int day )
        {
            this.day = day;
        }

        public void setMonth ( int month )
        {
            this.month = month;
        }

        public void setYear ( int year )
        {
            this.year = year;
        }
        
        public void setDate (int y, int m, int d) {
            this.year = y;
            this.month = m;
            this.day = d;
        }

        public void setShelfLocation ( String shelfLocation )
        {
            this.shelfLocation = shelfLocation;
        }

        public void setTags ( String tags )
        {
            this.tagsString = tags;
            this.tags = tags.split( "," );
        }
    }//end class Items
}