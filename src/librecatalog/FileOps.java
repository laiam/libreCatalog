/*
 * Name:       Team Innovation
 * Course:     CS225
 * Program:    Project Library
 * Problem:    Create a system for storing library books and patrons, provide methods
 *             for checking out books, and other library related tasks.
 * Class:      FileOps
 */
package librecatalog;

import java.io.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author van
 */
public class FileOps<obj>
{
    private String path;
    
    /**
     * Construct the FileOps for use reading objects from a file.
     * 
     * @param path the string path to a file for reading.
     */
    public FileOps (String path) {
        this.path = path;
    }
    
    /**
     * Save the list of objects to a file.
     * @param listObj the list of objects you created in the program.
     */
    public void save(LinkedList<obj> listObj)
    {
        try
        {
            FileOutputStream flatDBFile = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(flatDBFile);
            for (Object obj: (Object[]) listObj.toArray()) {
                out.writeObject(obj);
            }
            flatDBFile.close();
            //<editor-fold defaultstate="collapsed" desc="java idiocy">
        } catch (FileNotFoundException ex)
        {
            //do nothing
        } catch (IOException ex)
        {
            //do nothing
            //</editor-fold>
        }
    }
    
    /**
     * Read the file from the constructor and add the items to the list.
     * @param listObj the list of objects you'll be using in your program.
     * @return if needed will return the list of objects read in. just in case.
     */
    public LinkedList<obj> load(LinkedList<obj> listObj)
    {
        try
        {
            FileInputStream fin = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fin);
            try
            {
                while (true)
                {
                    obj o = (obj) in.readObject();
                    boolean offer = listObj.offer(o);
                }
                //<editor-fold defaultstate="collapsed" desc="java idiocy">
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
        }//</editor-fold>
        return listObj;
    }
}