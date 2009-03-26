package it.unibz.readj2me.view;

import it.unibz.readj2me.controller.XmlReader;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class ItemList extends List implements CommandListener {

    public ItemList(){
        super("Title", List.IMPLICIT);

        //test
        XmlReader xmlReader = new XmlReader();
        xmlReader.getEntries();
    }

    public void commandAction(Command c, Displayable d) {
        //
    }

}
