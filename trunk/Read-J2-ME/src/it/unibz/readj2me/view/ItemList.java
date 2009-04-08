package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.ImageLoader;
import it.unibz.readj2me.controller.XmlReader;
import it.unibz.readj2me.model.Feed;
import it.unibz.readj2me.model.NewsItem;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class ItemList extends List implements CommandListener, Runnable {

    private Displayable parent;
    private Feed feed;
    private Vector items;
    XmlReader xmlReader;
    private Command backCommand,  openCommand;

    public ItemList(Feed feed, Displayable parent) {
        super(feed.getName(), List.IMPLICIT);
        this.parent = parent;
        this.feed = feed;
        items = new Vector();

        backCommand = new Command("Back", Command.BACK, 0);
        openCommand = new Command("Open", Command.OK, 0);
        this.addCommand(backCommand);
        this.addCommand(openCommand);
        this.setCommandListener(this);

        //test
        xmlReader = new XmlReader(feed);
        Thread localThread = new Thread(this);
        localThread.start();
        populateList();

    }

    private void populateList() {
        NewsItem item;
        Enumeration enumeration = items.elements();
        while (enumeration.hasMoreElements()) {
            item = (NewsItem) enumeration.nextElement();
            this.append(item.getTitle(), ImageLoader.getImage(ImageLoader.DEFAULT_FEED));
        }
    }

    public void run() {
        items = xmlReader.getEntries();
        populateList();
    }

    public void commandAction(Command c, Displayable d) {
        if (c == backCommand) {
            ReadJ2ME.showOnDisplay(parent);
        } else if (c == openCommand || d == this) {
            NewsItem selectedItem = (NewsItem) items.elementAt(this.getSelectedIndex());
            ItemView itemView = new ItemView(selectedItem, this);
            ReadJ2ME.showOnDisplay(itemView);
        }
    }
}
