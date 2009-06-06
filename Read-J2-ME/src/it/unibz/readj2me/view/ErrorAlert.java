package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.ImageLoader;
import it.unibz.readj2me.model.Constants;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;

/**
 * Alert to inform the user about an error.
 *
 * @author Anton Dignoes, Omar Moling
 */
public class ErrorAlert extends Alert {
    
    public ErrorAlert(String title, String text) {
         super(title, text, ImageLoader.getImage(Constants.IMG_ERROR), AlertType.ERROR);
         this.setTimeout(FOREVER);
    }
    
     public void show(){
        ReadJ2ME.showOnDisplay(this);
    }

}
