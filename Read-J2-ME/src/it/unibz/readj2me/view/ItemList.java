package it.unibz.readj2me.view;

import it.unibz.readj2me.controller.XmlReader;
import it.unibz.readj2me.model.NewsItem;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class ItemList extends List implements CommandListener {

    public ItemList(){
        super("Title", List.IMPLICIT);

        //test
        XmlReader xmlReader = new XmlReader();
        Vector entries = xmlReader.getEntries();
        entries.trimToSize();
        NewsItem item;

        Enumeration enumeration = entries.elements();
        while(enumeration.hasMoreElements()){
            item = (NewsItem)enumeration.nextElement();
            this.append(item.getTitle(), null);
        }
        
    }

    public void commandAction(Command c, Displayable d) {
        //
    }

}
