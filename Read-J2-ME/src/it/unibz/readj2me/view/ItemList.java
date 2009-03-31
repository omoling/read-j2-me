package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.ImageLoader;
import it.unibz.readj2me.controller.XmlReader;
import it.unibz.readj2me.model.NewsItem;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class ItemList extends List implements CommandListener {

    private ReadJ2ME parent;

    private Command exitCommand;

    public ItemList(ReadJ2ME parent, String title){
        super(title, List.IMPLICIT);
        this.parent = parent;

        exitCommand = new Command("Exit", Command.EXIT, 0);
        this.addCommand(exitCommand);
        this.setCommandListener(this);

        //test
        XmlReader xmlReader = new XmlReader();
        Vector entries = xmlReader.getEntries();
        entries.trimToSize();
        NewsItem item;

        Enumeration enumeration = entries.elements();
        while(enumeration.hasMoreElements()){
            item = (NewsItem)enumeration.nextElement();
            this.append(item.getTitle(), ImageLoader.getImage(ImageLoader.DEFAULT_FEED));
        }
        
    }

    public void commandAction(Command c, Displayable d) {
        if (c == exitCommand){
            parent.destroyApp(true);
        }
    }

}
