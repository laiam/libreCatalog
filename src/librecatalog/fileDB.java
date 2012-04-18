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
    
    fileDB (String path) {
        this.path = path;
    }
    
    void save(LinkedList<obj> listObj)
    {
        try
        {
            FileOutputStream flatDBFile = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(flatDBFile);
            for (Object obj: (Object[]) listObj.toArray()) {
                out.writeObject(obj);
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
    
    LinkedList<obj> load(LinkedList<obj> listObj)
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
        return listObj;
    }
}
