/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JOptionPane;

/**
 *
 * @author van
 */
class Items
{
    private static LinkedList<Item> items = new LinkedList<Item>();
    private static fileDB<Patron> ItemDB = new fileDB<Patron>(Configure.getSetting("ItemDB"));
    
    static void main(String[] args)
    {
        ItemDB.load(items);
        System.out.println(items.size() + " Patron records loaded.");
    }
    
    static void unload() 
    {
        ItemDB.save(items);
    }
    
    public static boolean addItem(Item record) 
    {
        return Items.add(record);
    }

    public static Patron[] searchPatron(int type, String str)
    {
        Iterator itemIterator = items.iterator();
        LinkedList<Item> itemList = new LinkedList<Item>();
        Item tempI;
        switch (type)
        {
            case 1:
            {
                while (itemIterator.hasNext())
                {
                    tempI = (Item) itemIterator.next();
                    if (tempI.getItemBarcode().equals(str))
                    {
                        itemList.add(tempI);
                    }
                    return (Patron[]) itemList.toArray();
                }
                break;
            }
            case 2:
                while (itemIterator.hasNext())
                {
                    tempI = (Item) itemIterator.next();
                    if (tempI.getItemTitle().equals(str))
                    {
                        itemList.add(tempI);
                    }
                }
                break;
            case 3:
                while (itemIterator.hasNext())
                {
                    tempI = (Item) itemIterator.next();
                    if (tempI.getItemAuthor().equals(str))
                    {
                        itemList.add(tempI);
                    }
                }
                break;
        }
        return (Patron[]) itemList.toArray();
    }
    
    public static boolean removePatron(Patron record)
    {
        return items.remove(record);
    }
class Item implements Serializable
{//begin class Item
    private String itemBarcode;
    private String itemTitle;
    private String itemAuthor;
    private String itemGenre;
    private double shelfLocation;
    private int dateAdded;
    

           Item(String itemBarcode,
                String itemTitle,
                String itemAuthor,
                String itemGenre,
                double shelfLocation,
                int dateAdded,
                boolean checkedOut)
    {
        this.itemBarcode = itemBarcode;
        this.itemTitle = itemTitle;
        this.itemAuthor = itemAuthor;
        this.itemGenre = itemGenre;
        this.shelfLocation = shelfLocation;
        this.dateAdded = dateAdded;
    }
    
    
    public String getItemBarcode()
    {
        return itemBarcode;
    }
    
    public String getItemTitle()
    {
        return itemTitle;
    }
    
    public String getItemAuthor()
    {
        return itemAuthor;
    }
    
    public String getItemGenre()
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
    
    public void setItemBarcode()
    {
        String stringItemBarcode = JOptionPane.showInputDialog(null, "Please enter the item's barcode");
        if (stringItemBarcode.length() == 12)
        {
            if (stringItemBarcode.charAt(0) == '2')
            {
                this.itemBarcode = stringItemBarcode;
            }
        }
        else
        {    
            JOptionPane.showMessageDialog(null, "Please enter a valid 12 digit barcode\n"+
                                                "Barcodes for items should begin with '2'");
            this.setItemBarcode();
        }
    }
    
    public void setItemTitle()
    {
        this.itemTitle = JOptionPane.showInputDialog("Please enter\n"+
                                                     "the title of the item.");
    }
    
    public void setItemAuthor()
    {
        this.itemAuthor = JOptionPane.showInputDialog("Please enter\n"+
                                                      "the title of the item");
    }
    
    public void setItemGenre()
    {
        this.itemGenre = JOptionPane.showInputDialog("Please enter\n"+
                                                     "the genre of the item");
    }
    
    public void setShelfLocation()
    {
        String stringShelfLocation = JOptionPane.showInputDialog("Please enter\n"+
                                                                 "the decimal shelf location");
        double decimalShelfLocation = Double.parseDouble(stringShelfLocation);
        if (decimalShelfLocation < 999.999 && decimalShelfLocation > 0.000)
        {
            this.shelfLocation = decimalShelfLocation;
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please enter a valid decimal system value");
            this.setShelfLocation();
        }
    }
    
    public void setDateAdded()
    {
        String stringDateAdded = JOptionPane.showInputDialog("Please enter\n"+
                                                             "the date the item\n"+
                                                             "was added to inventory\n"+
                                                             "in the format YYYYMMDD");
        
        if (stringDateAdded.length() == 8)
        {
            this.dateAdded = Integer.parseInt(stringDateAdded);
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Please enter a valid date in the format YYYYMMDD");
            this.setDateAdded();
        }
    }
}
}//end class Item
