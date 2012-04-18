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
    
    <obj> void save(LinkedList<obj> listObj)
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
    
    <obj> void load(LinkedList<obj> listObj)
    {
        File flatDBFile = new File(path);
        if (flatDBFile.exists())
        {
            FileInputStream fin;
            try
            {
                fin = new FileInputStream(path);
                ObjectInputStream in = new ObjectInputStream(fin);
                try
                {
                    while (in.available() > 0)
                    {
                        obj o = (obj) in.readObject();
                        listObj.add(o);
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
        }
    }
}
