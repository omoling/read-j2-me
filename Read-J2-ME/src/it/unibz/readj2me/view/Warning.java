package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;

public class Warning extends Alert {
    
    
    public Warning(String title, String text){
        super(title, text, null, AlertType.WARNING);
        this.setTimeout(FOREVER);
    }

    public void show(){
        ReadJ2ME.showOnDisplay(this);
    }

}
