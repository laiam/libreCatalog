/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.*;

/**
 * Patrons is basically it's own program.. its got it going on and all that...
 * anyway the deal is that the Record class is the data holder and the Patrons
 * class holds a more full way to modify the methods of the patron class. It's
 * almost its own show except for the minor little configure thing there to pull
 * the db location from the config class. Also it can't really do much on it's
 * own.
 *
 * @author van
 */
class Patrons
{

    private static Record selectedPatron;
    private static LinkedList<Record> patrons = new LinkedList<Record>();
    private static FileOps<Record> PatronDB = new FileOps<Record>(
            Configure.getPath(Configure.getSetting("PatronDB")));

    static void main(String[] args)
    {
        PatronDB.load(patrons);
        System.out.println(patrons.size() + " Patron records loaded.");
    }

    static void unload()
    {
        System.out.println("Unloading " + patrons.size() + " Patron records");
        PatronDB.save(patrons);
    }

    /**
     * Adds a patron to the system.
     *
     * @param record An instance of the Record class containing the record.
     * @return boolean success of operation.
     */
    public static boolean addPatron(Record record)
    {
        return patrons.add(record);
    }

    public static Record replacePatron(Record oldRecord, Record newRecord)
    {
        int position = patrons.indexOf(oldRecord);
        patrons.set(position, newRecord);
        return newRecord;
    }

    /**
     * Search for patrons in the database.
     *
     * @param type type of search to perform. 1 - search based on bar code. 2 -
     * search based on first name. 3 - search based on last name.
     * @param str value to search for.
     * @return an array of patrons matching the criteria.
     */
    public static Record[] searchPatrons(int type, String str)
    {
        Iterator patronIterator = patrons.iterator();
        LinkedList<Record> patronList = new LinkedList<Record>();
        Record tempP;
        Record[] tempArray;
        switch (type)
        {
            case 1:
            {
                while (patronIterator.hasNext())
                {
                    tempP = (Record) patronIterator.next();
                    if (tempP.getBarcode().equals(str))
                    {
                        patronList.add(tempP);
                        tempArray = new Record[patronList.size()];
                        for (int idx = 0; idx < patronList.size(); idx++)
                        {
                            tempArray[idx] = patronList.get(idx);
                        }
                        return tempArray;
                    }
                }
                break;
            }
            case 2:
                while (patronIterator.hasNext())
                {
                    tempP = (Record) patronIterator.next();
                    if (tempP.getFirstName().equals(str))
                        patronList.add(tempP);
                }
                break;
            case 3:
                while (patronIterator.hasNext())
                {
                    tempP = (Record) patronIterator.next();
                    if (tempP.getLastName().equals(str))
                        patronList.add(tempP);
                }
                break;
        }
        tempArray = new Record[patronList.size()];
        for (int idx = 0; idx < patronList.size(); idx++)
        {
            tempArray[idx] = patronList.get(idx);
        }
        return tempArray;

    }

    /**
     * Removes a patron from the system.
     *
     * @param record An instance of the Record class containing the record to be
     * removed.
     * @return boolean success of operation.
     */
    public static boolean removePatron(Record record)
    {
        return patrons.remove(record);
    }

    static String nextAvailableNumber()
    {
        Record lastPatron = patrons.peekLast();
        Record firstPatron = patrons.peekLast();
        String barcode;

        if (lastPatron != null)
        {
            if (lastPatron.equals(firstPatron))
                barcode = Integer.parseInt(
                        lastPatron.getBarcode().substring(5,
                                                          lastPatron.getBarcode().length())) + 1
                        + "";
            else
            {
                String lastBarcode = lastPatron.getBarcode();
                String firstBarcode = firstPatron.getBarcode();
                lastBarcode = lastBarcode.substring(4, lastBarcode.length());
                firstBarcode = firstBarcode.substring(4, firstBarcode.length());
                if (lastBarcode.compareTo(firstBarcode) > 0)
                    barcode = Integer.parseInt(lastBarcode) + 1 + "";
                else
                    barcode = Integer.parseInt(firstBarcode) + 1 + "";
            }
            while (barcode.length() < 7)
            {
                barcode = "0" + barcode;
            }
            return barcode;
        }
        return "0000001";
    }

    static class patronTab extends JTabbedPane
    {

        patronTab(int userLevel, Record selectedPatron)
        {
            add("Search", new searchPatronPanel(userLevel));
            add("View", new viewPatronPanel());
            if (userLevel == 1)
            {
                add("Add", new JScrollPane(new addPatronPanel()));
                add("Modify", new modPatronPanel());
                add("Remove", new remPatronPanel());
            }


        }

        static class viewPatronPanel extends JScrollPane
        {

            private static void resetForm()
            {
            }

            public viewPatronPanel()
            {
            }
        }

        static class searchPatronPanel extends JPanel
        {

            private static JTable patronListing;
            private static JPanel searchPane = new JPanel();
            private static JPanel searchResult = new JPanel();
            private static JSplitPane splitter;
            private static JLabel Barcode = new JLabel("Patron Barcode:");
            private static JTextField barcode = new JTextField();
            private static JButton submit = new JButton("Find");
            private static JSeparator Separator1 = new javax.swing.JSeparator();
            private static JSeparator Separator2 = new javax.swing.JSeparator();
            private static JLabel nameLabel = new JLabel("Name: "),
                    barcodeLabel = new JLabel("Barcode: "),
                    birthdateLabel = new JLabel("Birth Date: ");
            private GroupLayout layout = new GroupLayout(this);

            searchPatronPanel(int level)
            {
                {
                    submit.addActionListener(new ActionListener()
                    {

                        public void actionPerformed(ActionEvent e)
                        {
                            selectPatron();
                        }
                    });
                    setLayout(layout);
                    layout.setHorizontalGroup(
                            layout.createParallelGroup(
                            javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(
                            Barcode).addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                            barcode, javax.swing.GroupLayout.DEFAULT_SIZE, 175,
                                                                                             Short.MAX_VALUE).addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                            submit).addGap(174, 174, 174)).addComponent(
                            Separator2).addComponent(Separator1).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(
                            javax.swing.GroupLayout.Alignment.LEADING).addComponent(
                            nameLabel).addComponent(barcodeLabel).addComponent(
                            birthdateLabel)).addGap(0, 0, Short.MAX_VALUE)));
                    layout.setVerticalGroup(
                            layout.createParallelGroup(
                            javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                            layout.createSequentialGroup().addComponent(
                            Separator2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                        10,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE).addGap(
                            2, 2, 2).addGroup(layout.createParallelGroup(
                            javax.swing.GroupLayout.Alignment.BASELINE).addComponent(
                            Barcode).addComponent(barcode,
                                                  javax.swing.GroupLayout.PREFERRED_SIZE,
                                                  javax.swing.GroupLayout.DEFAULT_SIZE,
                                                  javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(
                            submit)).addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                            Separator1, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                             10,
                                                                                             javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                            nameLabel).addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                            barcodeLabel).addPreferredGap(
                            javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                            birthdateLabel).addContainerGap(213, Short.MAX_VALUE)));
                    //            } else
                    //            {
                    //                splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT, searchPane,
                    //                                          searchResult);
                    //                add(splitter);
                }
            }

            private void selectPatron()
            {
                Record[] found = searchPatrons(1, barcode.getText());
                if (found.length > 0)
                {
                    nameLabel.setText(
                            "Name: " + found[0].lastName + ", " + found[0].firstName);
                    barcodeLabel.setText(
                            "Barcode: " + found[0].getBarcode());
                    birthdateLabel.setText(
                            "Birth Date: " + found[0].getBirthDate());
                    selectedPatron = found[0];
                }
                viewPatronPanel.resetForm();
                modPatronPanel.resetForm();
                remPatronPanel.resetForm();
            }

            static void resetSearch()
            {
                barcode.setText("");
                nameLabel.setText("Name: ");
                birthdateLabel.setText("Birth Date:");
                
            }
        }

        static class addPatronPanel extends JPanel
        {

            private static final int YEAR = Calendar.getInstance().get(
                    Calendar.YEAR);
            private static JLabel barcodeLabel,
                    firstNameLabel,
                    lastNameLabel,
                    addressLabel,
                    emailLabel,
                    phoneLabel,
                    birthDateLabel;
            private static JButton submit = new JButton("Create User");
            private static JButton reset = new JButton("Clear Form");
            private static JTextField firstName = new JTextField(10),
                    lastName = new JTextField(10),
                    email = new JTextField(10),
                    phone = new JTextField(10);
            private static JComboBox birthMonth = new JComboBox(
                    new javax.swing.DefaultComboBoxModel(new String[]
                    {
                        "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"
                    }));
            private static JSpinner birthDay = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
            private static JSpinner birthYear =new JSpinner(new SpinnerNumberModel(YEAR, YEAR-100,YEAR,1));
            private static JTextArea address = new JTextArea(3, 4);
            private static JScrollPane addressPane = new JScrollPane(address);
            private static String barcode = 1 + Configure.getSetting("library") + nextAvailableNumber();
            GroupLayout layout = new GroupLayout(this);

            public addPatronPanel()
            {
                barcodeLabel = new JLabel("Barcode to be used: " + barcode);
                firstNameLabel = new JLabel("First Name:");
                lastNameLabel = new JLabel("Last Name:");
                addressLabel = new JLabel("Address:");
                emailLabel = new JLabel("Email:");
                phoneLabel = new JLabel("Phone:");
                birthDateLabel = new JLabel("Birth Date:");

                submit.addActionListener(new ActionListener()
                {

                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        addThePatron();
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
                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout
                        .createSequentialGroup()
                            .addGroup(layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(barcodeLabel)
                            .addComponent(firstNameLabel)
                            .addComponent(firstName)
                            .addComponent(lastNameLabel)
                            .addComponent(lastName)
                            .addComponent(addressLabel)
                            .addComponent(addressPane)
                            .addComponent(emailLabel)
                            .addComponent(email)
                            .addComponent(phoneLabel)
                            .addComponent(phone)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(birthDateLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(birthMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(birthDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(birthYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            )
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(reset)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(submit)
                            )
                        )
                    )
                );
                layout.setVerticalGroup(layout
                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout
                        .createSequentialGroup()
                        .addComponent(barcodeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(firstNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(firstName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lastNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lastName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addressLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addressPane)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emailLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(email)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(phoneLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(phone)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(birthDateLabel)
                            .addComponent(birthMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(birthDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(birthYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        )
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(reset)
                            .addComponent(submit)
                        )
                    )
                );
            }

            public void addThePatron()
            {
                selectedPatron = new Record(barcode, firstName.getText(),
                                            lastName.getText(),
                                            address.getText(),
                                            email.getText(), phone.getText(),
                                            birthMonth.getSelectedIndex(),
                                            Integer.parseInt(birthDay.getValue().toString()),
                                            Integer.parseInt(birthYear.getValue().toString()));
                addPatron(selectedPatron);
                viewPatronPanel.resetForm();
                modPatronPanel.resetForm();
                remPatronPanel.resetForm();
                resetForm();
            }

            public static void resetForm()
            {
                barcode = 1 + Configure.getSetting("library") + nextAvailableNumber();
                barcodeLabel.setText("Barcode to be used: " + barcode);
                firstName.setText("");
                lastName.setText("");
                address.setText("");
                email.setText("");
                phone.setText("");
            }
        }

        static class modPatronPanel extends JPanel
        {

            private static JLabel barcodeLabel,
                    firstNameLabel,
                    lastNameLabel,
                    addressLabel,
                    emailLabel,
                    phoneLabel,
                    birthDateLabel;
            private static JButton submit = new JButton("Create User");
            private static JButton reset = new JButton("Clear Form");
            private static JTextField barcodeField = new JTextField(12),
                    firstName = new JTextField(10),
                    lastName = new JTextField(10),
                    email = new JTextField(10),
                    phone = new JTextField(10),
                    birthDay = new JTextField(2),
                    birthMonth = new JTextField(2),
                    birthYear = new JTextField(4);
            private static JTextArea address = new JTextArea(3, 4);
            private static JScrollPane addressPane = new JScrollPane(address);
            private static String barcode = 1 + Configure.getSetting("library") + nextAvailableNumber();
            GroupLayout layout = new GroupLayout(this);

            public modPatronPanel()
            {
                barcodeLabel = new JLabel("Barcode to be used: " + barcode);
                firstNameLabel = new JLabel("First Name:");
                lastNameLabel = new JLabel("Last Name:");
                addressLabel = new JLabel("Address:");
                emailLabel = new JLabel("Email:");
                phoneLabel = new JLabel("Phone:");
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
                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout
                        .createSequentialGroup()
                            .addGroup(layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(barcodeLabel)
                            .addComponent(firstNameLabel)
                            .addComponent(firstName)
                            .addComponent(lastNameLabel)
                            .addComponent(lastName)
                            .addComponent(addressLabel)
                            .addComponent(addressPane)
                            .addComponent(emailLabel)
                            .addComponent(email)
                            .addComponent(phoneLabel)
                            .addComponent(phone)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(birthDateLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(birthMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(birthDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(birthYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            )
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(reset)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(submit)
                            )
                        )
                    )
                );
                layout.setVerticalGroup(layout
                    .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout
                        .createSequentialGroup()
                        .addComponent(barcodeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(firstNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(firstName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lastNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lastName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addressLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addressPane)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(emailLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(email)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(phoneLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(phone)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(birthDateLabel)
                            .addComponent(birthMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(birthDay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(birthYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        )
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(reset)
                            .addComponent(submit)
                        )
                    )
                );
            }

            public void modThePatron()
            {
                selectedPatron = replacePatron(selectedPatron, new Record(
                        barcode, firstName.getText(),
                                                                          lastName.getText(),
                                                                          address.getText(),
                                                                          email.getText(),
                                                                          phone.getText(),
                                                                          Integer.parseInt(
                        birthDay.getText()),
                                                                          Integer.parseInt(
                        birthMonth.getText()),
                                                                          Integer.parseInt(
                        birthYear.getText())));
                resetForm();
                viewPatronPanel.resetForm();
                remPatronPanel.resetForm();
            }

            public static void resetForm()
            {
                barcode = selectedPatron.barcode;
                barcodeLabel.setText("Barcode to be used: "+selectedPatron.barcode);
                firstName.setText(selectedPatron.getFirstName());
                lastName.setText(selectedPatron.getLastName());
                address.setText(selectedPatron.getAddress());
                email.setText(selectedPatron.getEmail());
                phone.setText(selectedPatron.getPhoneNumber());
                birthDay.setText(selectedPatron.getBirthDay() + "");
                birthMonth.setText(selectedPatron.getBirthMonth() + "");
                birthYear.setText(selectedPatron.getBirthYear() + "");

            }
        }

        static class remPatronPanel extends JPanel
        {

            public remPatronPanel()
            {
            }

            public void remThePatron()
            {
                resetForm();
            }

            public static void resetForm()
            {
            }
        }
    }

    static public Record Record(
            String barcode,
            String firstName,
            String lastName,
            String address,
            String email,
            String phone,
            int birthDay,
            int birthMonth,
            int birthYear)
    {
        return new Record(barcode, firstName, lastName, address, email, phone,
                          birthDay, birthMonth, birthYear);
    }

    static class Record implements Serializable
    {

        private int birthDay,
                birthMonth,
                birthYear;
        private String phoneNumber,
                firstName,
                lastName,
                address,
                email,
                barcode,
                stringBirthDate;
        private boolean isValid = true;

        Record(
                String barcode,
                String firstName,
                String lastName,
                String address,
                String email,
                String phone,
                int birthDay,
                int birthMonth,
                int birthYear)
        {
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
                this.phoneNumber = phone;
                this.stringBirthDate = birthMonth + "/" + birthDay + "/" + birthYear;
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

        //<editor-fold defaultstate="collapsed" desc="getters and setters" >
        public boolean isValid()
        {
            return isValid;
        }

        public String getAddress()
        {
            return address;
        }

        public String getBarcode()
        {
            return barcode;
        }

        public String getEmail()
        {
            return email;
        }

        public String getFirstName()
        {
            return firstName;
        }

        public String getLastName()
        {
            return lastName;
        }

        public String getPhoneNumber()
        {
            return phoneNumber;
        }

        public String getBirthDate()
        {
            return stringBirthDate;
        }

        public int getBirthDay()
        {
            return birthDay;
        }

        public int getBirthMonth()
        {
            return birthMonth;
        }

        public int getBirthYear()
        {
            return birthYear;
        }

        public void setAddress(String address)
        {
            this.address = address;
        }

        public void setBirthDate(int birthDay, int birthMonth, int birthYear)
        {
            this.birthDay = birthDay;
            this.birthMonth = birthMonth;
            this.birthYear = birthYear;
            this.stringBirthDate = birthMonth + "/" + birthDay + "/" + birthYear;
        }

        public void setEmail(String email)
        {
            this.email = email;
        }

        public void setFirstName(String firstName)
        {
            this.firstName = firstName;
        }

        public void setLastName(String lastName)
        {
            this.lastName = lastName;
        }

        public void setPhoneNumber(String phoneNumber)
        {
            this.phoneNumber = phoneNumber;
        }
        //</editor-fold>
    }
}