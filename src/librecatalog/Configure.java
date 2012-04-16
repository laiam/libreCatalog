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
    
    Configure (String filename)
    {
        String path = getPath(filename);
        try
        {
            FileInputStream propFile = new FileInputStream( path );
            config.loadFromXML(propFile);
            firstRun=false;
        }
        catch (FileNotFoundException fnfe)
        {
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
    }
    
    void set(String key) {
        
    }

    String get(String key) {
        return config.getProperty(key);
    }
    
    static String getPath(String filename)
    {
        String path = System.getProperty("java.class.path");
        if (path.endsWith(".jar"))
        {
            int lastSlash = path.lastIndexOf(System.getProperty("file.separator"));
            path = path.substring(0, lastSlash);
        }
        if (!path.endsWith(System.getProperty("file.separator")))
            path += System.getProperty("file.separator");
        return path+filename;
    }

    private void createConfig(String filename)
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
