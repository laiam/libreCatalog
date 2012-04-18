/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.io.Serializable;
import javax.swing.JOptionPane;

/**
 *
 * @author van
 */
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
        String stringItemBarcode = JOptionPane.showInputDialog("Please enter\n"+ 
                                                               "the item's barcode.");
        if (stringItemBarcode.length() == 12)
        {
            if (stringItemBarcode.charAt(0) == '2')
            {
                this.itemBarcode = stringItemBarcode;
            }
        }
        this.setItemBarcode();
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
        if (decimalShelfLocation > 999.999 || decimalShelfLocation < 0.000)
        {
            JOptionPane.showMessageDialog(null, "Please enter a valid decimal system value");
            setShelfLocation();
        }
        this.shelfLocation = decimalShelfLocation;
    }
    
    public void setDateAdded()
    {
        String stringDateAdded = JOptionPane.showInputDialog("Please enter\n"+
                                                             "the date the item\n"+
                                                             "was added to inventory\n"+
                                                             "in the format YYYYMMDD");
        this.dateAdded = Integer.parseInt(stringDateAdded);
    }
}//end class Item
