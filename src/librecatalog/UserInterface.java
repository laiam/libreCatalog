/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package librecatalog;

import javax.swing.JOptionPane;

/**
 *
 * @author van
 */
public class UserInterface
{
    public static void Error(int err) {
        switch(err) {
            case 101:
                JOptionPane.showMessageDialog(
                    null,
                    "The configuration file was not found a default one has been created.",
                    "Configuration",
                    JOptionPane.INFORMATION_MESSAGE
                );
                break;
            case 102:
            
        }
    }
}
