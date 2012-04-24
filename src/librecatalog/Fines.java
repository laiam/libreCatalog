/*
 * My attempt at the Fine class file.  Most of this was copied from the Items
 * Class and I tried to rename variables to use for Fines.  I think code still 
 * needs to be added or removed to make it function for the Fines class prperly.
 * I'll be gone on a campout for this weekend.  I'm going to try and push this
 * to get up so that it is availble to everyone.  This Sunday I should be able
 * start in on this agian.
 */
package librecatalog;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
/**
 *
 * @author van
 */
class Fines
{//begin of fines

    private static LinkedList<Fine> fines = new LinkedList<Fine>();
    private static FileDB<Fine> FineDB = new FileDB<Fine>(Configure.getSetting("FineDB"));
    
    public static double fine;
    public static String patronBarCode;
    public static String bookBarCode;
    public static Boolean finePaid = false;
    
    //****************
    //***** load *****
    //****************
    static void load()
    {//begin of load
        
        FineDB.load(fines);
        System.out.println(fines.size() + " Fine records loaded.");
        
    }//end of load
    
    //******************
    //***** unload *****
    //******************
    static void unload() 
    {
        System.out.println("Unloading "+fines.size()+" Fine records");
        FineDB.save(fines);
    }

    //*******************
    //***** addFine *****
    //*******************
    private static boolean addFine(Fine record)
    {//begin of addFine

        return fines.add(record);
        
    }//end of addFine
    
    //***********************
    //***** replcaeFine *****
    //***********************
    public static void replaceFine(Fine oldRecord, Fine newRecord) {
        int position = fines.indexOf(oldRecord);
        fines.set(position, newRecord);
    }
    
    //***********************
    //***** searchFines *****
    //***********************
    public static Fine[] searchFines(int type, String str)
    {//begin of searchFines
        Iterator fineIterator = fines.iterator();
        LinkedList<Fine> fineList = new LinkedList<Fine>();
        Fine tempP;
        Fine[] tempArray;
        tempArray = new Fine[fineList.size()];
        for (int idx = 0; idx < fineList.size();idx++)
            tempArray[idx]=fineList.get(idx);
        return tempArray;

    }//end of searchFines
    
    //**********************
    //***** removeFine *****
    //**********************
    public static boolean removeFine(Fine record)
    {// begin of removeFine
        return fines.remove(record);
    }//end of removeFine
    
    //*******************************
    //***** nextAvailablenumber *****
    //*******************************
    static String nextAvailableNumber()
    {//begin of nextAvailableNumber
        Fine lastFine = fines.peekLast();
        Fine firstFine = fines.peekLast();
        String barcode;
        
        if (lastFine != null) {
            if (lastFine.equals(firstFine)) {
                barcode = Integer.parseInt(
                    lastFine.getBarcode().substring(5, lastFine.getBarcode().length() )
                )+1
                +"";
            } else {
                String lastBarcode = lastFine.getBarcode();
                String firstBarcode = firstFine.getBarcode();
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
    }//end of nextAvailableNumber
    
}//end of fines

class Fine implements Serializable
{//begin class Fine
    private String barcode,
                   Title,
                   Author;
    private String[] tags;
    private int    date;
    
           Fine(String barcode,
                String fineTitle,
                String fineAuthor,
                int date,
                String keywords)
    {
        //method to check for valid barcode
        //method to check for valid shelf location/dewey decimal
        //method to check for valid yyyymmdd date format
        this.barcode = barcode;
        this.date = date;
        this.tags = keywords.split(",");
        this.Title = fineTitle;
        this.Author = fineAuthor;
    }
    
    
    public String getBarcode()
    {
        return barcode;
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

    public void getTitle(String Title)
    {
        this.Title = Title;
    }
    
    public void getAuthor(String Author)
    {
        this.Author = Author;
    }

    public void setDate(int date)
    {
        this.date = date;
    }

    public void setTags(String tags)
    {
        this.tags = tags.split(",");
    }
    
}//end class Fine