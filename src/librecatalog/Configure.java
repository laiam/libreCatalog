/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.io.*;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author van
 */
class Configure
{
    private static Properties config = new Properties();
    static boolean firstRun = true;
    
    public static String[] main (String[] args)
    {
        String filename = "config.properties";
        if (args.length<0)
            for (int idx = 0; idx < args.length;idx++)
                if (args[idx].startsWith("--config")) {
                    filename = args[idx].split("=")[1];
                }
        String path = getPath(filename);
        try
        {
            FileInputStream propFile = new FileInputStream( path );
            config.loadFromXML(propFile);
            firstRun=false;
        }
        catch (FileNotFoundException fnfe)
        {
            if (args.length == 0) {
                args    = new String[1];
                args[0] = "--first-run";
            } else {
                boolean flagSet = false;
                for (int idx=0;idx < args.length; idx++)
                    if (args[idx].equals("--first-run")||args[idx].equals("-F")) {
                        flagSet=true;
                        break;
                    }
                if (!flagSet) {
                    String[] temp = new String[args.length+1];
                    System.arraycopy(args, 0, temp, 0, args.length);
                    temp[args.length]="--first-run";
                    args = temp;
                }
            }
            System.out.println("First run: or config file failure.");
            UserInterface.Error(101);
            if (UserInterface.productSetupKey())
                createConfig(filename);
            else
                System.exit(0);
        }
        catch (IOException ioe)
        {
            System.out.println("unexpected error:");
            ioe.printStackTrace(System.out);
            UserInterface.Error(102);
        }
        return args;
    }
    
    void set(String key) {
        
    }

    String get(String key) {
        return config.getProperty(key);
    }
    
    static String getPath(String filename)
    {
        String path = "";
        if (!filename.startsWith("/")||!filename.startsWith(".")||!filename.startsWith(":\\",1)) {
            path = System.getProperty("java.class.path");
            if (path.endsWith(".jar"))
            {
                int lastSlash = path.lastIndexOf(System.getProperty("file.separator"));
                path = path.substring(0, lastSlash);
            }
            if (!path.endsWith(System.getProperty("file.separator")))
                path += System.getProperty("file.separator");
        }
        return path+filename;
    }

    private static void createConfig(String filename)
    {
        String path = getPath(filename);
        config.setProperty("PatronDB",Configure.getPath("Patrons.dbflat"));
        config.setProperty("ItemDB",Configure.getPath("Items.dbflat"));
        config.setProperty("FineDB",Configure.getPath("Fines.dbflat"));
        config.setProperty("AvailabilityDB",Configure.getPath("ItemAvailability.dbflat"));
        try {
            FileOutputStream propFile = new FileOutputStream( path );
            config.storeToXML(propFile, "");
        }
        catch (FileNotFoundException fnfe)
        {
            UserInterface.Error(104);
        }
        catch (IOException ioe)
        {
            UserInterface.Error(104);
        }
    }
    
    public static String getProp(String key) {
        return config.getProperty(key);
    }

    static void setProp(String key, String value)
    {
        config.setProperty(key,value);
    }
}
