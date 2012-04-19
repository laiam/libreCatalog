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
class Items {
    
}
class Item implements Serializable
{//begin class Items
    private int itemBarcode;
    private String itemTitle;
    private String itemAuthor;
    private String itemGenre;
    private double shelfLocation;
    private int dateAdded;
    private boolean checkedOut;
    
           Items(int itemBarcode,
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
        this.itemBarcode = itemBarcode;
        this.itemTitle = itemTitle;
        this.itemAuthor = itemAuthor;
        this.itemGenre = itemGenre;
        this.shelfLocation = shelfLocation;
        this.dateAdded = dateAdded;
        this.checkedOut = checkedOut;
    }
    
    
    public int getItemBarcode()
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
    
    public boolean getCheckedOut()
    {
        return checkedOut;
    }
    
    public void setItemBarcode()
    {
        String stringItemBarcode = JOptionPane.showInputDialog("Please enter\n"+ 
                                                               "the item's barcode");
        if (stringItemBarcode.length() == 12)
        {
            if (stringItemBarcode.charAt(0) == '2')
            {
                this.itemBarcode = Integer.parseInt(stringItemBarcode);
            }
        }
        JOptionPane.showMessageDialog(null, "Please enter a valid item barcode");
        this.setItemBarcode();
    }
}//end class Items

