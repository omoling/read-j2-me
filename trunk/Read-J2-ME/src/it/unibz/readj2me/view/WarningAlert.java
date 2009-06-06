package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.ImageLoader;
import it.unibz.readj2me.model.Constants;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;

/**
 * Alert to inform the user about warnings, i.e. the impossibilit to execute a command.
 *
 * @author Anton Dignoes, Omar Moling
 */
public class WarningAlert extends Alert {
    
    public WarningAlert(String title, String text){
        super(title, text, ImageLoader.getImage(Constants.IMG_WARNING), AlertType.WARNING);
        this.setTimeout(FOREVER);
    }

    public void show(){
        ReadJ2ME.showOnDisplay(this);
    }

}
