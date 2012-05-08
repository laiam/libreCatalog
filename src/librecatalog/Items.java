/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.*;

/**
 *
 * @author David Cross
 */
class Items
{

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

    static class itemTab extends JTabbedPane
    {
        itemTab(int userLevel, Record selectedItem)
        {
            add("Search", new searchItemPanel(userLevel));
            add("View", new JScrollPane(new viewItemPanel()));
            if(userLevel == 1)
            {
                add("Add", new JScrollPane(new viewItemPanel()));
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
                ItemInfo.setText("Item Info:\n" +
                                 "Title: "+selectedItem.getTitle()+"\n" + 
                                 "Author: "+selectedItem.getAuthor()+"\n " +
                                 "Genre: "+selectedItem.getGenre()+"\n" +
                                 "Self Location: "+selectedItem.getShelfLocation()+"\n" +
                                 "Date: "+selectedItem.getDate()+"\n" +
                                 "Tags: "+selectedItem.getTagsString()+"\n" +
                                 ""
                                 );
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
            };
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
        private static JTable itemListing;
        private static JPanel searchPane = new JPanel();
        private static JPanel searchResult = new JPanel();
        private static JSplitPane splitter;
        private static JLabel Barcode = new JLabel("Item Barcode: ");
        private static JTextField barcode = new JTextField();
        private static JButton submit = new JButton("Search");
        private static JSeparator Separator1 = new JSeparator();
        private static JSeparator Separator2 = new JSeparator();
        private static JLabel titleLabel = new JLabel ("Title: ");
        private static JLabel barcodeLabel = new JLabel ("Barcode: ");
        private static JLabel addedDateLabel = new JLabel ("Date added: ");
        private GroupLayout layout = new GroupLayout(this);
        
        
        searchItemPanel(int level)
        {
            {
                submit.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        selectItem();
                    }
                }};
                setLayout(layout);
                layout.setHorizontalGroup(layout
                      .createParallelGroup(GroupLayout.Alignment.LEADING)
                      .addGroup(layout
                            .createSequentialGroup()
                            .addComponent(Barcode)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(barcode, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(submit)
                            .addGap(174, 174, 174)
                        )
                        .addComponent(Separator2)
                        .addComponent(Separator1)
                        .addGroup(layout
                            .createSequentialGroup()
                            .addGroup(layout
                                .createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(titleLabel)
                                .addComponent(barcodeLabel)
                                .addComponent(addedDateLabel)
                            )
                            .addGap(0, 0, Short.MAX_VALUE)
                        )
                };
                layout.setVerticalGroup(layout
                      .createParallelGroup(GroupLayout.Alignment.LEADING)
                      .addGroup(layout
                            .createSequentialGroup()
                            .addComponent(Separator2, GroupLayout.PREFERED_SIZE, 10, GroupLayout.PREFERED_SIZE)
                            .addGap(2, 2, 2)
                            .addGroup(layout
                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(Barcode)
                                .addComponent(barcode, GroupLayout.PREFERED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERED_SIZE)
                                .addCOmponent(submit)
                            )
                            .addPreferredGap(LayoutStyle.COmponentPlacement.RELATED)
                            .addComponent(Separator1, GroupLayout.PREFERED_SIZE, 10, GroupLayout.PREFERED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(nameLabel)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(barcodeLabel)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(dateAddedLabel)
                            .addContainerGap(213, Short.MAX_VALUE)
                      )
                
            

        private void selectItem()
        {
            Record[] found = searchItems(1, barcode.getText());
            if (found.length > 0)
            {
                titleLabel.setText("Title: "+ found[0].Title);
                barcodeLabel.setText("Barcode: "+ found[0].barcode);
                addedDateLabel.setText("Date Added: "+ found[0].day+"/"+found[0].month+"/"+found[0].year);
                selectedItem = found[0];    
            }
            viewItemPanel.resetForm();
            modItemPanel.resetForm();
            remItemPanel.resetForm();
        }
        
        static void resetSearch()
        {
            barcode.setText("");
            titleLabel.setText("Title: ");
            addedDateLabel.setText("Date Added: ");
        }
    }
    
    static class addItemPanel extends JPanel
    {
        private static final int YEAR = Calendar.getInstance().get(Calendar.YEAR);
        private static JLabel barcodeLabel;
        private static JLabel titleLabel;
        private static JLabel authorLabel;
        private static JLabel genreLabel;
        private static JLabel locationLabel;
        private static JLabel addedLabel;
        private static JLabel tagsLabel;
        private static JButton submit = new JButton("Create Item");
        private static JButton reset = new JButton("Clear Form");
        private static JTextField titleField = new JTextField(64);
        private static JTextField authorField = new JTextField(64);
        private static JTextField genreField = new JTextField(32);
        private static JTextField locationField = new JTextField(7);
        private static JTextField dayField = new JTextField(2);
        private static JTextField monthField = new JTextField(2);
        private static JTextField yearField = new JTextField(4);
        private static JTextField tagsField = new JTextField(64);
        private static String barcode = 2 + Configure.getSetting("library")+nextAvailableNumber();
        
        public addItemPanel()
        {
            barcodeLabel = new JLabel("Barcode to be used: "+ barcode);
            titleLabel = new JLabel("Title of item: ");
            authorLabel = new JLabel("Author of item: ");
            genreLabel = new JLabel("Genre of item: ");
            locationLabel = new JLabel("Shelf Location: ");
            addedLabel = new JLabel("Dated Added: ");
            tagsLabel = new JLabel("Tags: ");
            
            submit.addActionListener(new ActionListener())
            {
                @Override
                public void actoinPerformed(ActionEvent e)
                {
                    addTheItem();
                }
            }};
        
            reset.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    resetForm();
                }
            }};
        
            setLayout(layout);
            layout.setHorizontalGroup(GroupLayout.Alignment.LEADING)
                  .addGroup(layout
                        .createSequentialGroup()
                            .addGroup(layout
                            .createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(barcodeLabel)
                            .addComponent(titleLabel)
                            .addComponent(authorLabel)
                            .addComponent(genreLabel)
                            .addComponent(locationLabel)
                            .addComponent(addedLabel)
                            .addComponent(tagsLabel)
                        )
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(reset)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATEED)
                            .addComponent(submit)
                        )
                   )
                   .addContainerGap(165, Short.MAX_VALUE)
            )
        );
        layout.setVerticalGroup(layout
              .createParallelGroup(GroupLayout.Alignment.RELATED)
              .addGroup(layout.
                    .createSequentialGroup()
                    .addComponent(barcodeLabel)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(titleLabel)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(authorLabel)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(genreLabel)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(locationLabel)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(addedLabel)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(tagsLabel)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    )
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(reset)
                        .addComponent(submit)
                    )
                    .addComtainerGap(165, Short.MAX_VALUE)
               )
        );
        )
                    
        public void addTheItem()
        {
            selectedItem = new Record(
                    barcode,
                    titleField.getText(),
                    authorField.getText(),
                    genreField.getText(),
                    locationField.getText(),
                    yearField.getText(),
                    monthField.getText(),
                    dayField.getText(),
                    tagsField.getText(),
            );
            addItem(selectedItem);
            viewItemPanel.resetForm();
            modItemPanel.resetForm();
            remItemPanel.resetForm();
            resetForm();
        }
        
        public static void resetForm()
        {
            barcode = 2 + Configure.setSetting("library") + nextAvailableNumber();
            barcodeLabel.setText("Barcode to be used : "+barcode);
            titleField.setText("");
            authorField.setText("");
            genreField.setText("");
            locationField.setText("");
            addedField.setText("");
            tagsField.setText("");
        }
    }

    static class modItemPanel extends JPanel
    {
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
        public void remTheItem()
        {
            
        }
        
        public static void resetForm()
        {
            
        }
    }
    
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
    }//end class Items