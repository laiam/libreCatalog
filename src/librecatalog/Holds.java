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
class Holds
{//begin of holds
    private static Record selectedHold;
    private static LinkedList<Record> holds = new LinkedList<Record>();
    private static String path = Configure.getPath( Configure.getSetting( "holdsDB" ) );
    private static FileOps<Record> holdsDB = new FileOps<Record>( path );

    /**
     *
     * @param args
     */
    static void main ( String[] args )
    {//begin of main
        holdsDB.load( holds );
        System.out.println( holds.size() + " Holds/Checkouts loaded." );
    }//end main setup.

    static class Record implements Serializable
    {//begin class Items

        private String patronBarcode,
                       itemBarcode;
        private Date   created;

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
        //</editor-fold>
        
    }//end of Record
    
    public static Record[] listHolds(String patronBarcode) {
        Iterator holdIterator = holds.iterator();
        LinkedList<Record> tempList = new LinkedList<Record>();
        Record temp;
        while (holdIterator.hasNext()) {
            temp = (Record) holdIterator.next();
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
    
    public static boolean addHold(Record hold) {
        return holds.add(hold);
    }//end add hold
    
    /**
     * Find a hold by a patron for a book.
     * @param patronBarcode the patrons barcode.
     * @param bookBarcode the barcode of the item the hold is placed on
     * @return a record if found null otherwise
     */
    public static Record findHold(String patronBarcode, String bookBarcode) {
        Iterator holdIterator = holds.iterator();
        Record temp;
        while (holdIterator.hasNext())
        {
            temp = (Record) holdIterator.next();
            if (temp.getPatronBarcode().equals(patronBarcode))
                if (temp.getItemBarcode().equals(bookBarcode))
                    return temp;
        }
        return null;
    }//endfind hold
    
    
    public static Record findHold(String Barcode) {
        Iterator holdIterator = holds.iterator();
        Record temp;
        while (holdIterator.hasNext())
        {
            temp = (Record) holdIterator.next();
            if (temp.getItemBarcode().equals(Barcode))
                return temp;
            if (temp.getPatronBarcode().equals(Barcode))
                return temp;
        }
        return null;
    }//endfind hold
    
    /**
     * Removes a hold from the system.
     *
     * @param record An instance of the Record class containing the record to be
     * removed.
     *
     * @return boolean success of operation.
     */
    public static boolean removeHold ( Record record )
    {
        return holds.remove( record );
    }//end removehold
    
    public static class holdsTab extends JTabbedPane {
        public holdsTab(int userLevel) {
            add(new JScrollPane(new addHoldPanel()), "Place Hold");
            add(new JScrollPane(new remHoldPanel()), "Cancel Hold");
            if (userLevel == 3) {
                add(new JScrollPane(new listAllHoldsPanel()), "List Holds for Patron");
            } else {
                add(new JScrollPane(new listAllHoldsPanel()), "List Holds for Patron");
                add(new JScrollPane(new listHoldsPanel()), "List All Holds");
            }
        }//end constructor
        
        static void resetPanels() {
            listHoldsPanel.resetPanel();
        }
        
        static class addHoldPanel extends JPanel {
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
                    if (findHold(patronBarcode.getText(), itemBarcode.getText())==null) {
                        Date today = Calendar.getInstance().getTime();
                        addHold(new Record(patronBarcode.getText(), itemBarcode.getText(), today));
                    } else {
                        JOptionPane.showMessageDialog(null, "Hold already exists for patron.");
                    }
                }
            }
            
            addHoldPanel () {
                submit.addActionListener(new Submit());
                loadSelected();
                meh.setLayout(new GridLayout(4, 2));
                meh.add(patronLabel);
                meh.add(patronBarcode);
                meh.add(itemLabel);
                meh.add(itemBarcode);
                meh.add(submit);
                add(meh);
            }//end constructor
        }//end addHoldPanel
        
        static class remHoldPanel extends JPanel {
            remHoldPanel() {
                
            }//end constructor
        }//end remHoldPanel
        
        static class listAllHoldsPanel extends JPanel {
            static Record[] holdsList;
            static JTextArea list = new JTextArea("");
            
            void resetPanel() {
                holdsList = new Record[holds.size()];
                for (int idx = 0; idx < holds.size(); idx++)
                {
                    holdsList[idx] = holds.get(idx);
                }
                if (holdsList==null || holdsList.length == 0) {
                    list.setText("No Holds");
                } else {
                    for (int idx = 0; idx < holdsList.length; idx++) {
                        list.setText("Holds List:\n"
                                + "Book Barcode: "+holdsList[idx].getItemBarcode()+"\n"
                                + "Due Date: " +holdsList[idx].getCreated()+ "\n\n");
                    }
                }//end if
            }//end resetPanel method
            
            listAllHoldsPanel() {
                resetPanel();
                setLayout(new GridLayout(1, 1));
                list.setEditable(false);
                add(list);
            }//end constructor
        }//end listAllHoldsPanel
        
        static class listHoldsPanel extends JPanel {
            static Record[] holdsList;
            static JTextArea list = new JTextArea();
            
            static void resetPanel() {
                if (Patrons.getSelectedPatron() != null) {
                    holdsList = listHolds(Patrons.getSelectedPatron().getBarcode());
                    if (holdsList == null || holdsList.length == 0) {
                        list.setText("No Holds for User: "+Patrons.getSelectedPatron().getBarcode());
                    } else {
                        for (int idx = 0; idx < holdsList.length; idx++) {
                            list.setText("Holds for User: "
                                    +Patrons.getSelectedPatron().getBarcode()+"\n"
                                    + "Book Barcode: "+holdsList[idx].getItemBarcode()+"\n"
                                    + "Due Date: " +holdsList[idx].getCreated()+ "\n\n");
                        }
                    }//end if
                } else
                    list.setText("No Patron Selected.");
            }//end resetPanel method
            
            listHoldsPanel() {
                resetPanel();
                setLayout(new GridLayout(1, 1));
                list.setEditable(false);
                add(list);
            }//end constructor
        }//end listHoldsPanel
    }//end holdsTab
}//end Holds
