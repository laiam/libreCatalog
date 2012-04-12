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
                    "The configuration file was not found.",
                    "Configuration Info",
                    JOptionPane.WARNING_MESSAGE
                );
                break;
            case 102:
                JOptionPane.showMessageDialog(
                    null,
                    "Unexpected file input error.",
                    "Configuration Error",
                    JOptionPane.WARNING_MESSAGE
                );
                break;
            case 103:
                JOptionPane.showMessageDialog(
                    null,
                    "Error: Invalid Permission level.",
                    "Configuration Warning",
                    JOptionPane.WARNING_MESSAGE
                );
                break;
            default:
                JOptionPane.showMessageDialog(
                    null,
                    "Error "+err+": undefined error code generated.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
                );
        }
    }
}
