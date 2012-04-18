/*
 * Name:       Team Innovation
 * Course:     CS225
 * Program:    Project Library
 * Problem:    Create a system for storing library books and patrons, provide methods
 *             for checking out books, and other library related tasks.
 * Class:      Conf
 */
package librecatalog;

import java.io.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *  This conf class will eventually replace configure.
 * I'm testing a different way of doing things.
 * @author van
 */
public class Conf
{
    static LinkedList<Setting> settings = new LinkedList<Setting>();
    
    static void main(String[] args)
    {
        String filename = "config.properties";
        if (args.length>0)
            for (int idx = 0; idx < args.length;idx++)
                if (args[idx].startsWith("--config")) {
                    filename = args[idx].split("=")[1];
                }
        String path = getPath(filename);
        
        load(path);
        System.out.println(settings.size() + " settings loaded.");
    }

    static void save()
    {
        try
        {
            String filepath = Configure.getProp("PatronDB");
            FileOutputStream flatDBFile = new FileOutputStream(filepath);
            ObjectOutputStream out = new ObjectOutputStream(flatDBFile);
            for (Setting s: (Setting[]) settings.toArray()) {
                out.writeObject(s);
            }
            flatDBFile.close();
        } catch (FileNotFoundException ex)
        {
            //do nothing
        } catch (IOException ex)
        {
            //do nothing
        }
    }
    
    static void load(String filepath)
    {
        File flatDBFile = new File(filepath);
        if (flatDBFile.exists())
        {
            FileInputStream fin;
            try
            {
                fin = new FileInputStream(filepath);
                ObjectInputStream in = new ObjectInputStream(fin);
                try
                {
                    while (in.available() > 0)
                    {
                        settings.add((Setting) in.readObject());
                    }
                } catch (EOFException e)
                {
                    //
                } catch (ClassNotFoundException ex)
                {
                    Logger.getLogger(Patrons.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex)
            {
                //
            } catch (IOException ex2)
            {
                //
            }
        } else
        {
            
        }

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
}
class Setting implements Serializable {
    private String key;
    private String token;
    
    Setting () {
        key = "";
        token = "";
    }
    
    public Setting (String key, String token) {
        this.key = key;
        this.token = token;
    }
    
    public String getSetting() {
        return token;
    }
}