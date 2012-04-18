/*
 * Name:       Team Innovation
 * Course:     CS225
 * Program:    Project Library
 * Problem:    Create a system for storing library books and patrons, provide methods
 *             for checking out books, and other library related tasks.
 * Class:      Conf
 */
package librecatalog;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *  This conf class will eventually replace configure.
 * I'm testing a different way of doing things.
 * @author van
 */
public class Conf
{
    static LinkedList<Setting> settings = new LinkedList<Setting>();
    static fileDB<Setting> SettingDB;
    
    static void main(String[] args)
    {
        String filename = "config.properties";
        if (args.length>0)
            for (int idx = 0; idx < args.length;idx++)
                if (args[idx].startsWith("--config")) {
                    filename = args[idx].split("=")[1];
                }
        String path = getPath(filename);
        SettingDB = new fileDB<Setting>(path);
        SettingDB.load(settings);
        System.out.println(settings.size() + " settings loaded.");
        if (settings.size()==0)
            loadDefaults();
    }
    
    static void unload() {
        SettingDB.save(settings);
    }
    
    static void loadDefaults() {
        settings.add(new Setting("", ""));
        settings.add(new Setting("PatronDB", getPath("Patrons.dbflat")));
        settings.add(new Setting("ItemDB", getPath("Items.dbflat")));
        settings.add(new Setting("FineDB", getPath("Fines.dbflat")));
        settings.add(new Setting("AvailabilityDB", getPath("ItemAvailability.dbflat")));
        unload();
    }
    
    static void addSetting (String key, String token) {
        settings.add(new Setting("", ""));
    }
    
    
    /**
     * Get an absolute path for a file.
     * Calculate an absolute path relative to the location of the .jar or class
     * files.
     * 
     * @param filename the name of the file to access absolutely.
     * @return a full absolute path.
     */
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
    
    public void getToken(String newToken) {
        token = newToken;
    }
    
    public String getKey() {
        return key;
    }
    public String getToken() {
        return token;
    }
}