/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.*;

/**
 *
 * @author Charlie Kaden
 */
class Checkouts
{//begin of Record

    private static Record selectedCheckout;
    private static LinkedList<Record> checkouts =
                                      new LinkedList<Record>();
    private static FileOps<Record> checkoutsDB =
                                   new FileOps<Record>( Configure.getPath( Configure.getSetting( "ItemavailabilityDB" ) ) );

    /**
     *
     * @param args
     */
    static void main ( String[] args )
    {//begin of main
        checkoutsDB.load( checkouts );
        System.out.println( checkouts.size() + " Holds/Checkouts loaded." );
    }//

    static void unload ()
    {
        System.out.println( "Unloading " + checkouts.size() + " Holds/Checkouts" );
        checkoutsDB.save( checkouts );
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
        return checkouts.add( record );
    }

    public static void replaceAvailability ( Record oldRecord, Record newRecord )
    {
        int position = checkouts.indexOf( oldRecord );
        checkouts.set( position, newRecord );
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
        Iterator itemIterator = checkouts.iterator();
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
                    if ( tempP.getDue().equals( str ) )
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
                    if ( tempP.getCreated().equals( str ) )
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
        return checkouts.remove( record );
    }

   static class Record implements Serializable
    {//begin class Items

        private String patronBarcode,
                       itemBarcode;
        private Date   created, due;
        private int    renews;

        public Record ( String patron, String item, Date date )
        {
            patronBarcode = patron;
            itemBarcode = item;
            created = date;
        }

        //<editor-fold defaultstate="collapsed" desc="Hold getters and setters">
        public Date getCreated() {
            return created;
        }

        public void setCreated(Date created) {
            this.created = created;
        }

        public String getItemBarcode() {
            return itemBarcode;
        }

        public void setItemBarcode(String itemBarcode) {
            this.itemBarcode = itemBarcode;
        }

        public String getPatronBarcode() {
            return patronBarcode;
        }

        public void setPatronBarcode(String patronBarcode) {
            this.patronBarcode = patronBarcode;
        }
        
        public Date getDue() {
            return due;
        }

        public void setDue(Date due) {
            this.due = due;
        }

        public int getRenews() {
            return renews;
        }

        public void setRenews(int renews) {
            this.renews = renews;
        }
        //</editor-fold>
        
    }//end of Record
    
     public static Record[] listCheckouts(String patronBarcode) {
        Iterator checkoutIterator = checkouts.iterator();
        LinkedList<Record> tempList = new LinkedList<Record>();
        Record temp;
        while (checkoutIterator.hasNext()) {
            temp = (Record) checkoutIterator.next();
            if (temp.getPatronBarcode().equals(patronBarcode))
                tempList.add(temp);
        }
        Record[] tempArray = new Record[tempList.size()];
        for (int idx = 0; idx < tempList.size(); idx++)
        {
            tempArray[idx] = tempList.get(idx);
        }
        return tempArray;
    }
    
    public static boolean addCheckout(Record checkout) {
        return checkouts.add(checkout);
    }//end add checkouts
    
    /**
     * Find a checkouts by a patron for a book.
     * @param patronBarcode the patrons barcode.
     * @param bookBarcode the barcode of the item the hold is placed on
     * @return a record if found null otherwise
     */
    public static Checkouts.Record findCheckout(String patronBarcode, String bookBarcode) {
        Iterator checkoutIterator = checkouts.iterator();
        Checkouts.Record temp;
        while (checkoutIterator.hasNext())
        {
            temp = (Checkouts.Record) checkoutIterator.next();
            if (temp.getPatronBarcode().equals(patronBarcode))
                if (temp.getItemBarcode().equals(bookBarcode))
                    return temp;
        }
        return null;
    }//endfind hold
    
    
    public static Checkouts.Record findCheckout(String Barcode) {
        Iterator checkoutIterator = checkouts.iterator();
        Checkouts.Record temp;
        while (checkoutIterator.hasNext())
        {
            temp = (Checkouts.Record) checkoutIterator.next();
            if (temp.getItemBarcode().equals(Barcode))
                return temp;
            if (temp.getPatronBarcode().equals(Barcode))
                return temp;
        }
        return null;
    }//endfind hold
    
    /**
     * Removes a checkout from the system.
     *
     * @param record An instance of the Record class containing the record to be
     * removed.
     *
     * @return boolean success of operation.
     */
    public static boolean removeCheckout ( Checkouts.Record record )
    {
        return checkouts.remove( record );
    }//end removehold
    
    public static class checkoutsTab extends JTabbedPane
    {

        checkoutsTab(int userLevel)
        {
            add("Renew", new renewCheckoutPanel());
            if (userLevel >= 1 && userLevel < 3)
            {
                add("Checkout", new JScrollPane(new addCheckoutPanel()));
                add("Return", new JScrollPane(new remCheckoutPanel()));
            }
            if (userLevel == 3) {
                add(new JScrollPane(new listCheckoutsPanel()), "List Checkouts for Patron");
            } else {
                add(new JScrollPane(new listAllCheckoutsPanel()), "List All Checkouts");
                add(new JScrollPane(new listCheckoutsPanel()), "List Checkouts for Patron");
            }
        }//end constructor
        
        static void resetPanels() {
            addCheckoutPanel.loadSelected();
            remCheckoutPanel.loadSelected();
            renewCheckoutPanel.loadSelected();
            listCheckoutsPanel.resetPanel();
            listAllCheckoutsPanel.resetPanel();
        }
        
        static class addCheckoutPanel extends JPanel {
            static JPanel meh = new JPanel();
            static JLabel patronLabel = new JLabel("Patron Barcode: ");
            static JLabel itemLabel = new JLabel("Item Barcode");
            static JTextField patronBarcode = new JTextField();
            static JTextField itemBarcode = new JTextField();
            static JButton submit = new JButton("Place Hold");
            
            public static void loadSelected() {
                if (Patrons.getSelectedPatron()!=null) {
                    patronBarcode.setText(Patrons.getSelectedPatron().getBarcode());
                }
                if (Items.getSelectedItem()!=null) {
                    itemBarcode.setText(Items.getSelectedItem().getBarcode());
                }
            }
            
            public static class Submit implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    submit();
                }
                public static void submit () {
                    if (findCheckout(patronBarcode.getText(), itemBarcode.getText())==null) {
                        Date today = Calendar.getInstance().getTime();
                        selectedCheckout = new Record(patronBarcode.getText(), itemBarcode.getText(), today);
                        addCheckout(selectedCheckout);
                        resetPanels();
                    } else {
                        JOptionPane.showMessageDialog(null, "Checkouts already exists for patron.");
                        //in Checkouts this actually removes the hold and places the checkout.
                    }
                }
            }
            
            addCheckoutPanel () {
                submit.addActionListener(new Checkouts.checkoutsTab.remCheckoutPanel.Submit());
                loadSelected();
                meh.setLayout(new GridLayout(4, 2));
                meh.add(patronLabel);
                meh.add(patronBarcode);
                meh.add(itemLabel);
                meh.add(itemBarcode);
                meh.add(submit);
                add(meh);
            }//end constructor
        }//end addCheckoutPanel
        
        
        static class renewCheckoutPanel extends JPanel {
            static JPanel meh = new JPanel();
            static JLabel patronLabel = new JLabel("Patron Barcode: ");
            static JLabel itemLabel = new JLabel("Item Barcode");
            static JTextField patronBarcode = new JTextField();
            static JTextField itemBarcode = new JTextField();
            static JButton submit = new JButton("Place Hold");
            
            public static void loadSelected() {
                if (Patrons.getSelectedPatron()!=null) {
                    patronBarcode.setText(Patrons.getSelectedPatron().getBarcode());
                }
//                if (Items.getSelectedItem()!=null) {
//                    itemBarcode.setText(Items.getSelectedItem().getBarcode());
//                }
            }
            
            public static class Submit implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    submit();
                }
                public static void submit () {
                    if (findCheckout(patronBarcode.getText(), itemBarcode.getText())==null) {
                        Date today = Calendar.getInstance().getTime();
                        selectedCheckout = new Record(patronBarcode.getText(), itemBarcode.getText(), today);
                        addCheckout(selectedCheckout);
                        resetPanels();
                    } else {
                        JOptionPane.showMessageDialog(null, "Checkouts already exists for patron.");
                        //in Checkouts this actually removes the hold and places the checkout.
                    }
                }
            }
            
            renewCheckoutPanel () {
                submit.addActionListener(new Checkouts.checkoutsTab.remCheckoutPanel.Submit());
                loadSelected();
                meh.setLayout(new GridLayout(4, 2));
                meh.add(patronLabel);
                meh.add(patronBarcode);
                meh.add(itemLabel);
                meh.add(itemBarcode);
                meh.add(submit);
                add(meh);
            }//end constructor
        }//end addCheckoutPanel
        
        static class remCheckoutPanel extends JPanel {
            static JPanel meh = new JPanel();
            static JLabel patronLabel = new JLabel("Patron Barcode: ");
            static JLabel itemLabel = new JLabel("Item Barcode");
            static JTextField patronBarcode = new JTextField();
            static JTextField itemBarcode = new JTextField();
            static JButton submit = new JButton("Remove Checkout");
            
            public static void loadSelected() {
                if (Patrons.getSelectedPatron()!=null) {
                    patronBarcode.setText(Patrons.getSelectedPatron().getBarcode());
                }
                if (Items.getSelectedItem()!=null) {
                    itemBarcode.setText(Items.getSelectedItem().getBarcode());
                }
            }
            
            public static class Submit implements ActionListener {

                @Override
                public void actionPerformed(ActionEvent e) {
                    submit();
                }
                public static void submit () {
                    Checkouts.Record selectedCheckout = findCheckout(patronBarcode.getText(), itemBarcode.getText());
                    if (selectedCheckout!=null) {
                        if (Graphical.confirm("Remove Checkout", "Are you sure you want to remove the Checkout?")) {
                            removeCheckout(selectedCheckout);
                            selectedCheckout=null;
                            Graphical.tellUser("Remove Checkout", "Checkout Removed.");
                            resetPanels();
                        }
                        
                    } else {
                        JOptionPane.showMessageDialog(null, "No checkout found.");
                        //in Checkouts this actually removes the checkout and places the checkout.
                    }
                }
            }
            
            remCheckoutPanel () {
                submit.addActionListener(new Checkouts.checkoutsTab.remCheckoutPanel.Submit());
                loadSelected();
                meh.setLayout(new GridLayout(4, 2));
                meh.add(patronLabel);
                meh.add(patronBarcode);
                meh.add(itemLabel);
                meh.add(itemBarcode);
                meh.add(submit);
                add(meh);
            }//end constructor
        }//end remCheckoutPanel
        
        static class listAllCheckoutsPanel extends JPanel {
            static Checkouts.Record[] checkoutsList;
            static JTextArea list = new JTextArea("");
            
            static void resetPanel() {
                checkoutsList = new Checkouts.Record[checkouts.size()];
                for (int idx = 0; idx < checkouts.size(); idx++)
                {
                    checkoutsList[idx] = checkouts.get(idx);
                }
                if (checkoutsList==null || checkoutsList.length == 0) {
                    list.setText("No checkout");
                } else {
                    for (int idx = 0; idx < checkoutsList.length; idx++) {
                        list.setText("Holds List:\n"
                                + "Book Barcode: "+checkoutsList[idx].getItemBarcode()+"\n"
                                + "Due Date: " +checkoutsList[idx].getDue()+ "\n\n");
                    }
                }//end if
            }//end resetPanel method
            
            listAllCheckoutsPanel() {
                resetPanel();
                setLayout(new GridLayout(1, 1));
                list.setEditable(false);
                add(list);
            }//end constructor
        }//end listAllHoldsPanel
        
        static class listCheckoutsPanel extends JPanel {
            static Checkouts.Record[] checkoutsList;
            static JTextArea list = new JTextArea();
            
            static void resetPanel() {
                if (Patrons.getSelectedPatron() != null) {
                    checkoutsList = listCheckouts(Patrons.getSelectedPatron().getBarcode());
                    if (checkoutsList == null || checkoutsList.length == 0) {
                        list.setText("No checkouts for User: "+Patrons.getSelectedPatron().getBarcode());
                    } else {
                        for (int idx = 0; idx < checkoutsList.length; idx++) {
                            list.setText("Checkouts for User: "
                                    +Patrons.getSelectedPatron().getBarcode()+"\n"
                                    + "Book Barcode: "+checkoutsList[idx].getItemBarcode()+"\n"
                                    + "Due Date: " +checkoutsList[idx].getDue()+ "\n\n");
                        }
                    }//end if
                } else
                    list.setText("No Patron Selected.");
            }//end resetPanel method
            
            listCheckoutsPanel() {
                resetPanel();
                setLayout(new GridLayout(1, 1));
                list.setEditable(false);
                add(list);
            }//end constructor
        }//end listCheckoutsPanel
    }//end checkoutsTab
}//end Checkouts