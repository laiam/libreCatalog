/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class fileDB<obj>
{
    private String path;
    
    /**
     * Construct the fileDB for use reading objects from a file.
     * 
     * @param path the string path to a file for reading.
     */
    public fileDB (String path) {
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
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
            for (Object obj: (Object[]) listObj.toArray()) {
                out.writeObject(obj);
            }
            out.close();
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
            in.close();
            fin.close();
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