
package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class Error extends Alert {
    
    public Error(String title, String text) {
         super(title, text, null, AlertType.ERROR);
    }
    
     public void show(){
        ReadJ2ME.showOnDisplay(this);
    }

}
