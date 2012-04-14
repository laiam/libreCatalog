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
            createConfig(filename);
            UserInterface.Error(101);
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
        UserInterface.setupMode(config);
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
}
