/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author van
 */
class Configuration
{
    private static Properties config;
    
    Configuration (String filename)
    {
        String path = getPath(filename);
        try
        {
            FileInputStream propFile = new FileInputStream( path );
            config = new Properties();
            config.loadFromXML(propFile);
        }
        catch (FileNotFoundException fnfe)
        {
            System.out.println("First run: or config file failure.");
            //UserInterface.Error(101);
        }
        catch (IOException ioe)
        {
            System.out.println("unexpected error:");
            ioe.printStackTrace(System.out);
            //UserInterface.Error(102);
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
    
}
