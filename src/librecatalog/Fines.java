/*
 * 
 */
package librecatalog;

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
 * @author van
 */
class Fines
{//begin of fines

    private static Record selectedFine;
    private static LinkedList<Fines> fines = new LinkedList<Fines>();
    private static FileOps<Fines> FineDB = new FileOps<Fines>( Configure.getPath( Configure.getSetting( "FineDB" ) ) );

    //****************
    //***** load *****
    //****************
    static void load ()
    {//begin of load

        FineDB.load( fines );
        System.out.println( fines.size() + " Fine records loaded." );

    }//end of load

    //******************
    //***** unload *****
    //******************
    static void unload ()
    {
        System.out.println( "Unloading " + fines.size() + " Fine records" );
        FineDB.save( fines );
    }

    //*******************
    //***** addFine *****
    //*******************
    public static boolean addFine ( Record record )
    {//begin of addFine
        return fines.add(record);
    }//end of addFine

    //***********************
    //***** replcaeFine *****
    //***********************
    public static Record replaceFine ( Record oldRecord, Record newRecord )
    {//begin of replaceFine
        int position = fines.indexOf( oldRecord );
        fines.set( position, newRecord );
        return newRecord;
    }//end of replaceFine

    //**********************
    //***** removeFine *****
    //**********************
    public static boolean removeFine ( Fines record )
    {// begin of removeFine
        return fines.remove( record );
    }//end of removeFine

    //***********************
    //***** searchFines *****
    //***********************
    public static Record[] searchFines(int type, String str)
    {
        Iterator fineIterator = fines.iterator();
        LinkedList<Record> fineList = new LinkedList<Record>();
        Record tempP;
        Record[] tempArray;
        switch (type)
        {
            case 1:
            {
                while (fineIterator.hasNext())
                {
                    tempP = (Record) fineIterator.next();
                    if (tempP.getBarcode().equals(str))
                    {
                        fineList.add(tempP);
                        tempArray = new Record[fineList.size()];
                        for (int idx = 0; idx < fineList.size(); idx++)
                        {
                            tempArray[idx] = fineList.get(idx);
                        }
                        return tempArray;
                    }
                }
                break;
            }
            case 2:
                while (fineIterator.hasNext())
                {
                    tempP = (Record) fineIterator.next();
                    if (tempP.getFirstName().toLowerCase().equals(str.toLowerCase()))
                        fineList.add(tempP);
                }
                break;
            case 3:
                while (fineIterator.hasNext())
                {
                    tempP = (Record) fineIterator.next();
                    if (tempP.getLastName().toLowerCase().equals(str.toLowerCase()))
                        fineList.add(tempP);
                }
                break;
        }
        tempArray = new Record[fineList.size()];
        for (int idx = 0; idx < fineList.size(); idx++)
        {
            tempArray[idx] = fineList.get(idx);
        }
        return tempArray;

    }
    
        
    static class fineTab extends JTabbedPane
    {
        fineTab(int userLevel)
        {
            add("Search", new searchFinePanel(userLevel));
            add("View", new JScrollPane(new viewFinePanel()));
            if (userLevel == 1)
            {
                add("Add", new JScrollPane(new addFinePanel()));
                add("Modify", new JScrollPane(new modFinePanel()));
                add("Remove", new remFinePanel());
            }
        }

        static class viewFinePanel extends JPanel
        {
            
            private static JTextArea FineInfo = new JTextArea("Fine Info:\n", 10, 25);
            private GroupLayout layout = new GroupLayout(this);
            
            private static void resetForm()
            {
                FineInfo.setText("Fine Info:\n"
                    + "Name: " + selectedFine.getFirstName() +" "+ selectedFine.lastName+"\n"
                    + "Birth Date: " + selectedFine.getBirthDate() +"\n"
                    + "Address:\n" + selectedFine.getAddress()+"\n"
                    + "Email: " + selectedFine.getEmail() + "\n"
                    + "Phone Number: " + selectedFine.getPhoneNumber()+"\n"
                    + "Barcode: "+selectedFine.getBarcode());
            }

            public viewFinePanel()
            {
                FineInfo.setEditable( false );
                setLayout(layout);
                layout.setHorizontalGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING ).addGroup( layout.createSequentialGroup().addGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING ).addComponent( FineInfo ) ) ) );
                layout.setVerticalGroup( layout.createParallelGroup( GroupLayout.Alignment.LEADING ).addGroup( layout.createSequentialGroup().addComponent( FineInfo ) ) );
            }
        }

        static class searchFinePanel extends JPanel
        {

            private static JTable patronListing;
            private static JPanel searchPane = new JPanel();
            private static JPanel searchResult = new JPanel();
            private static JSplitPane splitter;
            private static JLabel searchLabel = new JLabel("Search:");
            private static JComboBox searchType;
            private static JTextField barcode = new JTextField();
            private static JButton submit = new JButton("Find");
            private static JSeparator Separator1 = new JSeparator();
            private static JSeparator Separator2 = new JSeparator();
            private static JLabel nameLabel = new JLabel("Name: "),
                    barcodeLabel = new JLabel("Barcode: "),
                    birthdateLabel = new JLabel("Birth Date: ");
            private GroupLayout layout = new GroupLayout(this);

            searchFinePanel(int level)
            {
                if (level != 3) {
                    searchType =new JComboBox(new DefaultComboBoxModel(new String[] {
                        "Barcode", "First Name", "Last Name"
                    }));
                } else {
                    searchType =new JComboBox(new DefaultComboBoxModel(new String[] {
                        "Barcode"
                    }));
                }
                submit.addActionListener(new ActionListener()
                {

                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        selectFine();
                    }
                });
                setLayout(layout);
                layout.setHorizontalGroup(layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout
                        .createSequentialGroup()
                        .addComponent(searchLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchType)
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
                            .addComponent(nameLabel)
                            .addComponent(barcodeLabel)
                            .addComponent(birthdateLabel)
                        )
                        .addGap(0, 0, Short.MAX_VALUE)
                    )
                );
                layout.setVerticalGroup(layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout
                        .createSequentialGroup()
                        .addComponent(Separator2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addGroup(layout
                            .createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(searchLabel)
                            .addComponent(searchType)
                            .addComponent(barcode, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(submit)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Separator1, GroupLayout.PREFERRED_SIZE,10,GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(barcodeLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(birthdateLabel)
                        .addContainerGap(213, Short.MAX_VALUE)
                    )
                );
            }//end of search patron constructor

            private void selectFine()
            {
                Record[] found = searchFines(searchType.getSelectedIndex()+1, barcode.getText());
                if (found.length == 1)
                {
                    nameLabel.setText(
                            "Name: " + found[0].lastName + ", " + found[0].firstName);
                    barcodeLabel.setText(
                            "Barcode: " + found[0].getBarcode());
                    birthdateLabel.setText(
                            "Birth Date: " + found[0].getBirthDate());
                    selectedFine = found[0];
                }
                else if (found.length > 1) {
                    String title = "Searching...";
                    String message = "The following patrons where found:\n";
                    for (int idx = 0; idx < found.length; idx++)
                    {
                        message += (idx + 1) + " - " + found[idx].getBarcode()
                        + " " + found[idx].getFirstName()
                        + " " + found[idx].getLastName() +"\n";
                    }
                    message += "choose one";
                    int response = Graphical.askUserForInt(title, message)-1;
                    if (response > found.length-1)
                    {
                        Graphical.tellUser(title, "Search canceled.");
                    } else {
                        nameLabel.setText( "Name: " + found[response].lastName + ", " + found[response].firstName);
                        barcodeLabel.setText( "Barcode: " + found[response].getBarcode());
                        birthdateLabel.setText( "Birth Date: " + found[response].getBirthDate());
                        selectedFine = found[response];
                    }
                }
                viewFinePanel.resetForm();
                modFinePanel.resetForm();
                remFinePanel.resetForm();
            }//end of select patron method.

            static void resetSearch()
            {
                barcode.setText("");
                nameLabel.setText("Name: ");
                birthdateLabel.setText("Birth Date:");
                
            }//end of reset search method
        }//end of search patron panel

        static class addFinePanel extends JPanel
        {
            private static JLabel barcodeLabel,
                    firstNameLabel,
                    lastNameLabel,
                    addressLabel,
                    emailLabel,
                    phoneLabel,
                    phoneAreaEndLabel,
                    phoneMidLabel,
                    birthDateLabel;
            private static JButton submit = new JButton("Create User");
            private static JButton reset = new JButton("Clear Form");
            private static JTextField firstName = new JTextField(10),
                    lastName = new JTextField(10),
                    email = new JTextField(10),
                    phoneAreaCode = new JTextField(3),
                    phoneFirstThree = new JTextField(3),
                    phoneLastFour = new JTextField(4);
            private static JSpinner birthDate;
            private static SpinnerDateModel birthDateModel;
            private static JTextArea address = new JTextArea(3, 4);
            private static JScrollPane addressPane = new JScrollPane(address);
            private static String barcode = 1 + Configure.getSetting("library");
            GroupLayout layout = new GroupLayout(this);

            public addFinePanel()
            {
                Calendar calendar = Calendar.getInstance();
                Date initDate = calendar.getTime();
                Date latestDate = calendar.getTime();
                calendar.add(Calendar.YEAR, -100);
                Date earliestDate = calendar.getTime();
                birthDateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);
                birthDate = new JSpinner(birthDateModel);
                birthDate.setEditor(new JSpinner.DateEditor(birthDate, "MM/dd/yyyy"));
                barcodeLabel = new JLabel("Barcode to be used: " + barcode);
                firstNameLabel = new JLabel("First Name:");
                lastNameLabel = new JLabel("Last Name:");
                addressLabel = new JLabel("Address:");
                emailLabel = new JLabel("Email:");
                phoneLabel = new JLabel("Phone: (");
                phoneAreaEndLabel = new JLabel(")");
                phoneMidLabel = new JLabel("-");
                birthDateLabel = new JLabel("Birth Date:");

                submit.addActionListener(new ActionListener()
                {

                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        addTheFine();
                    }
                });

                reset.addActionListener(new ActionListener()
                {

                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        resetForm();
                    }
                });

                setLayout(layout);
                layout.setHorizontalGroup(layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout
                        .createSequentialGroup()
                            .addGroup(layout
                            .createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(barcodeLabel)
                            .addComponent(firstNameLabel)
                            .addComponent(firstName)
                            .addComponent(lastNameLabel)
                            .addComponent(lastName)
                            .addComponent(addressLabel)
                            .addComponent(addressPane)
                            .addComponent(emailLabel)
                            .addComponent(email)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(phoneLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(phoneAreaCode)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(phoneAreaEndLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(phoneFirstThree)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(phoneMidLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(phoneLastFour)
                                .addContainerGap(165, Short.MAX_VALUE)
                            )
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(birthDateLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(birthDate)
                            )
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(reset)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(submit)
                            )
                        )
                        .addContainerGap(165, Short.MAX_VALUE)
                    )
                );
                layout.setVerticalGroup(layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout
                        .createSequentialGroup()
                        .addComponent(barcodeLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(firstNameLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(firstName)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lastNameLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lastName)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addressLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addressPane)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emailLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(email)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(phoneLabel)
                            .addComponent(phoneAreaCode)
                            .addComponent(phoneAreaEndLabel)
                            .addComponent(phoneFirstThree)
                            .addComponent(phoneMidLabel)
                            .addComponent(phoneLastFour)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(birthDateLabel)
                            .addComponent(birthDate)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(reset)
                            .addComponent(submit)
                        )
                        .addContainerGap(165, Short.MAX_VALUE)
                    )
                );
            }//end of add patron panel constructor.

            public void addTheFine()
            {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime((Date) birthDate.getValue());
                
                int Day=calendar.get( Calendar.DAY_OF_MONTH ),
                    Month=calendar.get( Calendar.MONTH ),
                    Year=calendar.get( Calendar.YEAR );
//                selectedFine = new Record(
//                    barcode,
//                    firstName.getText(),
//                    lastName.getText(),
//                    address.getText(),
//                    email.getText(),
//                    Integer.parseInt(phoneAreaCode.getText()),
//                    Integer.parseInt(phoneFirstThree.getText()),
//                    Integer.parseInt(phoneLastFour.getText()),
//                    Day,Month,Year);
                addFine(selectedFine);
                viewFinePanel.resetForm();
                modFinePanel.resetForm();
                remFinePanel.resetForm();
                resetForm();
            }//end add patron

            public static void resetForm()
            {
                
                Calendar calendar = Calendar.getInstance();
                Date initDate = calendar.getTime();
                birthDate.setValue( initDate );
                barcode = 1 + Configure.getSetting("library");
                barcodeLabel.setText("Barcode to be used: " + barcode);
                firstName.setText("");
                lastName.setText("");
                address.setText("");
                email.setText("");
                phoneAreaCode.setText("");
                phoneFirstThree.setText("");
                phoneLastFour.setText("");
            }//end reset form.

        }//end of add patron panel

        static class modFinePanel extends JPanel
        {
            
            private static JLabel barcodeLabel,
                    firstNameLabel,
                    lastNameLabel,
                    addressLabel,
                    emailLabel,
                    phoneLabel,
                    phoneAreaEndLabel,
                    phoneMidLabel,
                    birthDateLabel;
            private static JButton submit = new JButton("Modify User");
            private static JButton reset = new JButton("Clear Changes");
            private static JTextField firstName = new JTextField(10),
                    lastName = new JTextField(10),
                    email = new JTextField(10),
                    phoneAreaCode = new JTextField(3),
                    phoneFirstThree = new JTextField(3),
                    phoneLastFour = new JTextField(4);
            private static JSpinner birthDate;
            private static SpinnerDateModel birthDateModel;
            private static JTextArea address = new JTextArea(3, 4);
            private static JScrollPane addressPane = new JScrollPane(address);
            private static String barcode = "";
            GroupLayout layout = new GroupLayout(this);

            public modFinePanel()
            {
                Calendar calendar = Calendar.getInstance();
                Date initDate = calendar.getTime();
                Date latestDate = calendar.getTime();
                calendar.add(Calendar.YEAR, -100);
                Date earliestDate = calendar.getTime();
                birthDateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);
                birthDate = new JSpinner(birthDateModel);
                birthDate.setEditor(new JSpinner.DateEditor(birthDate, "MM/dd/yyyy"));
                barcodeLabel = new JLabel("Editing Record for Patron with barcode: " + barcode);
                firstNameLabel = new JLabel("First Name:");
                lastNameLabel = new JLabel("Last Name:");
                addressLabel = new JLabel("Address:");
                emailLabel = new JLabel("Email:");
                phoneLabel = new JLabel("Phone: (");
                phoneAreaEndLabel = new JLabel(")");
                phoneMidLabel = new JLabel("-");
                birthDateLabel = new JLabel("Birth Date:");

                submit.addActionListener(new ActionListener()
                {

                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        modThePatron();
                    }
                });

                reset.addActionListener(new ActionListener()
                {

                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        resetForm();
                    }
                });

                setLayout(layout);
                layout.setHorizontalGroup(layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout
                        .createSequentialGroup()
                            .addGroup(layout
                            .createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(barcodeLabel)
                            .addComponent(firstNameLabel)
                            .addComponent(firstName)
                            .addComponent(lastNameLabel)
                            .addComponent(lastName)
                            .addComponent(addressLabel)
                            .addComponent(addressPane)
                            .addComponent(emailLabel)
                            .addComponent(email)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(phoneLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(phoneAreaCode)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(phoneAreaEndLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(phoneFirstThree)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(phoneMidLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(phoneLastFour)
                                .addContainerGap(165, Short.MAX_VALUE)
                            )
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(birthDateLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(birthDate)
                            )
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(reset)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(submit)
                            )
                        )
                        .addContainerGap(165, Short.MAX_VALUE)
                    )
                );
                layout.setVerticalGroup(layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout
                        .createSequentialGroup()
                        .addComponent(barcodeLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(firstNameLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(firstName)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lastNameLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lastName)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addressLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addressPane)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emailLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(email)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(phoneLabel)
                            .addComponent(phoneAreaCode)
                            .addComponent(phoneAreaEndLabel)
                            .addComponent(phoneFirstThree)
                            .addComponent(phoneMidLabel)
                            .addComponent(phoneLastFour)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(birthDateLabel)
                            .addComponent(birthDate)
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(reset)
                            .addComponent(submit)
                        )
                        .addContainerGap(165, Short.MAX_VALUE)
                    )
                );
            }//end modify patron panel constructor

            public void modThePatron()
            {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime((Date) birthDate.getValue());
                
                int Day=calendar.get( Calendar.DAY_OF_MONTH ),
                    Month=calendar.get( Calendar.MONTH ),
                    Year=calendar.get( Calendar.YEAR );
                selectedFine = replaceFine(
                    selectedFine,
                    new Fines.Record(
                        barcode,
                        firstName.getText(),
                        lastName.getText(),
                        address.getText(),
                        email.getText(),
                        Integer.parseInt(phoneAreaCode.getText()),
                        Integer.parseInt(phoneFirstThree.getText()),
                        Integer.parseInt(phoneLastFour.getText()),
                        Day,Month,Year
                    )
                );
                resetForm();
                viewFinePanel.resetForm();
                remFinePanel.resetForm();
            }//method to modify a patron.

            public static void resetForm()
            {
                if (selectedFine != null) {
                    barcode = selectedFine.barcode;
                    barcodeLabel.setText("Barcode to be used: "+selectedFine.barcode);
                    firstName.setText(selectedFine.getFirstName());
                    lastName.setText(selectedFine.getLastName());
                    address.setText(selectedFine.getAddress());
                    email.setText(selectedFine.getEmail());
                    phoneAreaCode.setText(selectedFine.getPhoneAreaCode());
                    phoneFirstThree.setText(selectedFine.getPhoneFirstThree());
                    phoneLastFour.setText(selectedFine.getPhoneLastFour());
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(
                            selectedFine.getBirthYear(),
                            selectedFine.getBirthMonth(),
                            selectedFine.getBirthDay()
                    );
                    birthDateModel.setValue( calendar.getTime() );
                    submit.setEnabled(true);
                }//if patron is selected supply details then enable submit button
                else
                {
                    submit.setEnabled(false);
                }//end of enable submit button
            }//end of reset modification form
        }//end of modify patron panel gui

        static class remFinePanel extends JPanel
        {

            private static JLabel nameLabel = new JLabel("Name: ");
            private static JLabel barcodeLabel = new JLabel("Barcode: ");
            private static JLabel emailLabel = new JLabel("Email: ");
            private static JLabel finesLabel = new JLabel("Fines: ");
            private static JLabel holdsLabel = new JLabel("Holds: ");
            private static JLabel checkoutsLabel = new JLabel("Checkouts: ");
            private static JButton remove = new JButton("Remove Patron");
            GroupLayout layout = new GroupLayout(this);
            public remFinePanel()
            {
                remove.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        remTheFine();
                    }
                });
                
                setLayout(layout);
                layout.setHorizontalGroup(layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout
                        .createSequentialGroup()
                        .addGroup(layout
                            .createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel)
                            .addComponent(barcodeLabel)
                            .addComponent(emailLabel)
                            .addComponent(finesLabel)
                            .addComponent(holdsLabel)
                            .addComponent(checkoutsLabel)
                            .addComponent(remove)
                        )
                    )
                );
                layout.setVerticalGroup(layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout
                        .createSequentialGroup()
                        .addComponent(nameLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(barcodeLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emailLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(finesLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(holdsLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkoutsLabel)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(remove)
                    )
                );
            }//end patron panel constructor

            public void remTheFine()
            {
                if (selectedFine != null)
                {
                    //if () {
                    //TODO
                    //check to see if there are any checkouts and alert the user
                    //if there are any
                    //} else 
                    if (
                        Graphical.confirm("Remove Patron",
                                          "Are you sure you want to remove the "
                                        + "patron?\nThis will remove all fines "
                                        + "on and holds placed for this account.")
                       )
                    {
                        //remove fines for patron
                        //remove holds for patron
                        //last measure just in case remove any records of books
                        //checkedout.
                        removeFine(selectedFine);
                        selectedFine = null;
                        for (int idx = 0; idx <= 999999; idx++)
                        {//waiting...
                        }//seriously it sucks that i needed this.
                        resetForm();
                        addFinePanel.resetForm();
                        viewFinePanel.resetForm();
                        modFinePanel.resetForm();
                    }//end of error check to see if patron successfully removed
                }//end of if selected patron is selected
            }//end of the remove patron method

            public static void resetForm()
            {
                if (selectedFine == null) {
                    nameLabel.setText("Name: ");
                    barcodeLabel.setText("Barcode: ");
                    emailLabel.setText("Email: ");
                    finesLabel.setText("Fines: ");
                    checkoutsLabel.setText("Checkouts: ");
                }//end of if selectedpatron isn't selected.
                else
                {
                    nameLabel.setText("Name: "+selectedFine.getFirstName()
                        +" "+selectedFine.getLastName());
                    barcodeLabel.setText("Barcode: "+selectedFine.getBarcode());
                    emailLabel.setText("Email: "+selectedFine.getEmail());
                    finesLabel.setText("Fines: Not yet Implemented"); //TODO fines search for fines under patron
                    holdsLabel.setText("Holds: "+Checkouts.searchItems(3, selectedFine.getBarcode()).length);
                    checkoutsLabel.setText("Checkouts: "+Checkouts.searchItems(3, selectedFine.getBarcode()).length);
                }//end of if seletec patron exists
            }//end of reset form method
        }//end of remove patron panel
        
    }//End of patronTab GUI
    
    static class Record implements Serializable
    {//begin class Record

        private String[] tags;
        private int date,
                birthDay,
                birthMonth,
                birthYear,
                phoneAreaCode,
                phoneFirstThree,
                phoneLastFour;;
        private String firstName,
                lastName,
                address,
                email,
                barcode,
                phoneNumber,
                birthDate,
                stringBirthDate,
                phoneNumberAreaCode,
                phoneNumberFirstThree,
                phoneNumberLastFour;
        private boolean isValid = true;

        Record ( String barcode,
                 String fineTitle,
                 String fineAuthor,
                 int date,
                 String keywords,
                 String firstName,
                 String lastName,
                 String address,
                 String email,
                 int phoneArea,
                 int phoneFirstThree,
                 int phoneLastFour,
                 int birthDay,
                 int birthMonth,
                 int birthYear)
        {
            //method to check for valid barcode
            //method to check for valid shelf location/dewey decimal
            //method to check for valid yyyymmdd date format
            this.barcode = barcode;
            this.date = date;
            this.tags = keywords.split( "," );
            validBarcode(barcode);
            validateName(firstName, lastName);
            if (isValid)
            {
                this.barcode = barcode;
                this.firstName = firstName;
                this.lastName = lastName;
                this.birthDay = birthDay;
                this.birthMonth = birthMonth;
                this.birthYear = birthYear;
                this.address = address;
                this.email = email;
                this.phoneAreaCode = phoneArea;
                this.phoneFirstThree = phoneFirstThree;
                this.phoneLastFour = phoneLastFour;
                this.stringBirthDate = birthMonth+1 + "/" + birthDay + "/" + birthYear;
            }
        }
        
        //<editor-fold defaultstate="collapsed" desc="Validators" >
        public void validateBarcode(String barcode)
        {
            //barcodes start with a 1
            //four numbers for library
            //seven numbers for user
            if (barcode.length() != 12 || barcode.startsWith("1"))
                isValid = false;
        }

        private boolean validBarcode(String barcode)
        {
            //barcodes start with a 1
            //four numbers for library
            //seven numbers for user
            if (barcode.length() == 12)
                if (barcode.startsWith("1"))
                    return true;
            return false;
        }

        private void validateName(String firstName, String lastName)
        {
            isValid = true;
            if (!firstName.matches("[a-zA-Z0-9,._-]*"))
                isValid = false;
            if (!lastName.matches("[a-zA-Z0-9,._-]*"))
                isValid = false;
        }
        //</editor-fold>

        public String getBarcode ()
        {
            return barcode;
        }

        public int getdate ()
        {
            return date;
        }

        public int getDate ()
        {
            return date;
        }

        public String[] getTags ()
        {
            return tags;
        }

        public void setDate ( int date )
        {
            this.date = date;
        }

        public void setTags ( String tags )
        {
            this.tags = tags.split( "," );
        }

        private String getFirstName() {
            return firstName;
        }
        
        private String getLastName() {
            return lastName;
        }

        private String getBirthDate() {
            return birthDate;
        }

        private String getAddress() {
            return address;
        }

        private String getEmail() {
            return email;
        }

        private String getPhoneNumber() {
            return phoneNumber;
        }

        private int getBirthDay() {
            return birthDay;
        }

        private int getBirthMonth() {
            return birthMonth;
        }

        private int getBirthYear() {
            return birthYear;
        }
        private String getPhoneAreaCode() {
            return phoneNumberAreaCode;
        }
        private String getPhoneFirstThree() {
            return phoneNumberFirstThree;
        }
        private String getPhoneLastFour() {
            return phoneNumberLastFour;
        }
    }//end class Record
}//end of fines