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
class ItemAvailability
{
    private static LinkedList<UnItem> unavailableItems = new LinkedList<UnItem>();
    private static fileDB<UnItem> AvailabilityDB = new fileDB<UnItem>(Configure.getSetting("AvailabilityDB"));
    
    public static String patronBarcode;
    public static String itemBarcode;
    
    static void load()
    {
        AvailabilityDB.load(unavailableItems);
        System.out.println(unavailableItems.size()+" checked out items loaded.");
    }
    
    static void unload() 
    {
        System.out.println("Unloading "+unavailableItems.size()+" Fine records");
        AvailabilityDB.save(unavailableItems);
    }
    
    private static boolean addCheckedOut(Item record)
    {
        return unavailableItems.add(record);
    }
    
    public static void replaceCheckedOut(Item oldRecord, Item newRecord) 
    {
        int position = unavailableItems.indexOf(oldRecord);
        unavailableItems.set(position, newRecord);
    }
    
    public static Item[] searchCheckedOut(int type, String str)
    {
        Iterator availabilityIterator = unavailableItems.iterator();
        LinkedList<Item> availabilityList = new LinkedList<Item>();
        Fine tempI;
        Item[] tempArray;
        tempArray = new Item[availabilityList.size()];
        for (int idx = 0; idx < availabilityList.size();idx++)
            tempArray[idx]=availabilityList.get(idx);
        return tempArray;
    }
    
    public static boolean removeCheckedOut(Item record)
    {
        return unavailableItems.remove(record);
    }
    
    static String nextAvailableNumber()
    {
        
    }
    
    class UnItem implements Serializable
    {
        private String patronBarcode;
        private String itemBarcode;
        
        UnItem(String availPatronBarcode, String availItemBarcode)
        {
            this.patronBarcode = availPatronBarcode;
            this.itemBarcode = availItemBarcode;
        }
        
    public String getPatronBarcode()
    {
        return patronBarcode;
    }
    
    public String getItemBarcode()
    {
        return itemBarcode;
    }
    }
}
