package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class WarningAlert extends Alert {
    
    public WarningAlert(String title, String text){
        super(title, text, null, AlertType.WARNING);
        this.setTimeout(FOREVER);
    }

    public void show(){
        ReadJ2ME.showOnDisplay(this);
    }

}