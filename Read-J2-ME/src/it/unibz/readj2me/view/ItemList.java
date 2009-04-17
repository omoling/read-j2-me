package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.ImageLoader;
import it.unibz.readj2me.controller.PersistentManager;
import it.unibz.readj2me.controller.XmlReader;
import it.unibz.readj2me.model.Feed;
import it.unibz.readj2me.model.NewsItem;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Ticker;
import javax.microedition.rms.RecordStoreException;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class ItemList extends List implements CommandListener, Runnable {

    private Displayable parent;
    private Feed feed;
    private Vector items;
    XmlReader xmlReader;
    private Command backCommand,  openCommand,  updateCommand;

    public ItemList(Feed feed, Displayable parent) {
        super(feed.getName(), List.IMPLICIT);
        this.parent = parent;
        this.feed = feed;
        items = new Vector();

        backCommand = new Command("Back", Command.BACK, 0);
        openCommand = new Command("Open", Command.SCREEN, 0);
        updateCommand = new Command("Update", Command.SCREEN, 1);
        
        this.addCommand(backCommand);
        this.addCommand(openCommand);
        this.addCommand(updateCommand);
        this.setCommandListener(this);

        this.setFitPolicy(Choice.TEXT_WRAP_OFF);
        updateList();
    }

    private void updateList() {
        //empty list and vector
        items.removeAllElements();
        this.deleteAll();
        
        try {
            //TODO: handle d*** exceptions
            items = PersistentManager.getInstance().loadNewsItems(feed.getItemsRecordStoreName());
            
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        NewsItem item;
        items.trimToSize();
        Enumeration enumeration = items.elements();
        while (enumeration.hasMoreElements()) {
            item = (NewsItem) enumeration.nextElement();
            if(item.isRead()){
                this.append(item.getTitle(), ImageLoader.getImage(ImageLoader.GREY_FEED));
            } else {
                this.append(item.getTitle(), ImageLoader.getImage(ImageLoader.DEFAULT_FEED));
            }
        }

        //set bold-font if unread
        for(int i = 0; i < this.size(); i++){
            item = (NewsItem) items.elementAt(i);

            if(!item.isRead()){
                this.setFont(i, Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
            }
        }
        item = null;
    }

    public void run() {
        Ticker updateTicker = new Ticker("updating..");

        this.setTicker(updateTicker);
        xmlReader = XmlReader.getInstance();
        Vector newItems = xmlReader.getEntries(feed.getUrl());
        PersistentManager.getInstance().addNewsItems(feed, newItems);
        updateList();

        this.setTicker(null);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == backCommand) {
            ReadJ2ME.showOnDisplay(parent);
        } else if (c == updateCommand) {
            Thread updateThread = new Thread(this);
            updateThread.start();
        } else if (c == openCommand || d == this) {
            int index = this.getSelectedIndex();
            if (index >= 0) {
                NewsItem selectedItem = (NewsItem) items.elementAt(index);
                if (!selectedItem.isRead()) {
                    this.setFont(index, Font.getDefaultFont());
                    this.set(index, selectedItem.getTitle(), ImageLoader.getImage(ImageLoader.GREY_FEED));
                }
                ItemView itemView = new ItemView(selectedItem, this);
                ReadJ2ME.showOnDisplay(itemView);
            } else {
                new Warning("Info", "No item to be shown, perform an update..").show();
            }
        }
    }

    public String getFeedsItemsRecordStoreName(){
        return feed.getItemsRecordStoreName();
    }

}
