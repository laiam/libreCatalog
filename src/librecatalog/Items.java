/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author van
 */
class Items {
    
    private static LinkedList<Item> items = new LinkedList<Item>();
    private static fileDB<Item> ItemDB = new fileDB<Item>(Configure.getSetting("ItemDB"));
    
    static void main(String[] args)
    {
        ItemDB.load(items);
        System.out.println(items.size() + " Item records loaded.");
    }

    static void unload() {
        ItemDB.save(items);
    }
    
    /**
     * Adds a patron to the system.
     * @param record  An instance of the Item class containing the record.
     * @return boolean success of operation.
     */
    public static boolean addItem(Item record) {
        return items.add(record);
    }
    
    public static void replaceItem(Item oldRecord, Item newRecord) {
        int position = items.indexOf(oldRecord);
        items.set(position, newRecord);
    }
    
    /**
     * Search for items in the database.
     *
     * @param type type of search to perform.
     *             1 - search based on bar code.
     *             2 - search based on Title.
     *             3 - search based on Author.
     * @param str value to search for.
     * @return an array of items matching the criteria.
     */
    public static Item[] searchItems(int type, String str)
    {
        Iterator patronIterator = items.iterator();
        LinkedList<Item> patronList = new LinkedList<Item>();
        Item tempP;
        Item[] tempArray;
        switch (type)
        {
            case 1:
            {
                while (patronIterator.hasNext())
                {
                    tempP = (Item) patronIterator.next();
                    if (tempP.getBarcode().equals(str))
                    {
                        patronList.add(tempP);
                    }
                    return (Item[]) patronList.toArray();
                }
                break;
            }
            case 2:
                while (patronIterator.hasNext())
                {
                    tempP = (Item) patronIterator.next();
                    if (tempP.getTitle().equals(str))
                    {
                        patronList.add(tempP);
                    }
                }
                break;
            case 3:
                while (patronIterator.hasNext())
                {
                    tempP = (Item) patronIterator.next();
                    if (tempP.getAuthor().equals(str))
                    {
                        patronList.add(tempP);
                    }
                }
                break;
        }
        tempArray = new Item[patronList.size()];
        for (int idx = 0; idx < patronList.size();idx++)
            tempArray[idx]=patronList.get(idx);
        return tempArray;

    }

    /**
     * Removes a patron from the system.
     * @param record An instance of the Item class containing the record to be removed.
     * @return boolean success of operation.
     */
    public static boolean removeItem(Item record)
    {
        return items.remove(record);
    }

    static String nextAvailableNumber()
    {
        Item lastItem = items.peekLast();
        Item firstItem = items.peekLast();
        String barcode;
        
        if (lastItem != null) {
            if (lastItem.equals(firstItem)) {
                barcode = Integer.parseInt(
                    lastItem.getBarcode().substring(4, lastItem.getBarcode().length() )
                )+1
                +"";
            } else {
                String lastBarcode = lastItem.getBarcode();
                String firstBarcode = firstItem.getBarcode();
                lastBarcode = lastBarcode.substring(4, lastBarcode.length());
                firstBarcode = firstBarcode.substring(4, firstBarcode.length());
                if (lastBarcode.compareTo(firstBarcode)>0) {
                    barcode = Integer.parseInt(lastBarcode)+1+"";
                } else {
                    barcode = Integer.parseInt(firstBarcode)+1+"";
                }
            }
            while (barcode.length() < 7) {
                barcode = "0"+barcode;
            }
            return barcode;
        }
        return "00000001";
    }
}
class Item implements Serializable
{//begin class Items
    private String barcode;
    private String itemTitle;
    private String itemAuthor;
    private String itemGenre;
    private double shelfLocation;
    private int dateAdded;
    private boolean checkedOut;
    
           Item(String barcode,
                String itemTitle,
                String itemAuthor,
                String itemGenre,
                double shelfLocation,
                int dateAdded,
                boolean checkedOut)
    {
        //method to check for valid barcode
        //method to check for valid shelf location/dewey decimal
        //method to check for valid yyyymmdd date format
        this.barcode = barcode;
        this.itemTitle = itemTitle;
        this.itemAuthor = itemAuthor;
        this.itemGenre = itemGenre;
        this.shelfLocation = shelfLocation;
        this.dateAdded = dateAdded;
        this.checkedOut = checkedOut;
    }
    
    
    public String getBarcode()
    {
        return barcode;
    }
    
    public String getTitle()
    {
        return itemTitle;
    }
    
    public String getAuthor()
    {
        return itemAuthor;
    }
    
    public String getGenre()
    {
        return itemGenre;
    }
    
    public double getShelfLocation()
    {
        return shelfLocation;
    }
    
    public int getDateAdded()
    {
        return dateAdded;
    }
    
    public boolean getCheckedOut()
    {
        return checkedOut;
    }
}//end class Items

