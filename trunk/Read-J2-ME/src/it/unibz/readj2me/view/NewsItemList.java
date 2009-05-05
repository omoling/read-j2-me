package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.ImageLoader;
import it.unibz.readj2me.controller.NewsItemTagFilter;
import it.unibz.readj2me.controller.PersistentManager;
import it.unibz.readj2me.controller.XmlReader;
import it.unibz.readj2me.model.Constants;
import it.unibz.readj2me.model.Feed;
import it.unibz.readj2me.model.NewsItem;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Ticker;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class NewsItemList extends List implements CommandListener, Runnable {

    private Displayable parent;
    private Feed feed;
    private Vector items = new Vector();
    private XmlReader xmlReader;
    private Command backCommand,  openCommand,  updateCommand,  deleteItemCommand,  markUnreadCommand,  filterByTagCommand,  searchCommand;
    private RecordFilter newsItemsFilter = null;
    private boolean filtered = false;

    public NewsItemList(Feed feed, Displayable parent) {
        super(feed.getName(), List.IMPLICIT);
        filtered = false;
        this.parent = parent;
        this.feed = feed;

        addCommands();
        this.setCommandListener(this);

        this.setFitPolicy(Choice.TEXT_WRAP_OFF);
        updateList();
    }

    public NewsItemList(Vector filterTags, String titlePrefix, Feed feed, Displayable parent) {
        super(titlePrefix + " for " + feed.getName(), List.IMPLICIT);
        filtered = true;
        this.feed = feed;
        this.parent = parent;

        newsItemsFilter = new NewsItemTagFilter(filterTags);

        addCommands();
        this.setCommandListener(this);

        this.setFitPolicy(Choice.TEXT_WRAP_OFF);
        updateList();
    }

    private void addCommands() {
        backCommand = new Command("Back", Command.BACK, 0);
        this.addCommand(backCommand);
        openCommand = new Command("Open", Command.SCREEN, 0);
        this.addCommand(openCommand);
        markUnreadCommand = new Command("Mark unread", Command.SCREEN, 2);
        this.addCommand(markUnreadCommand);
        deleteItemCommand = new Command("Delete Item", Command.SCREEN, 3);
        this.addCommand(deleteItemCommand);

        if (!filtered) {
            updateCommand = new Command("Update", Command.SCREEN, 1);
            this.addCommand(updateCommand);
            filterByTagCommand = new Command("Filter by Tag", Command.SCREEN, 4);
            this.addCommand(filterByTagCommand);
            searchCommand = new Command("Search", Command.SCREEN, 5);
            this.addCommand(searchCommand);
        }
    }

    private void updateList() {
        //empty list and vector
        items.removeAllElements();
        this.deleteAll();

        try {
            //TODO: handle d*** exceptions
            items = PersistentManager.getInstance().loadNewsItems(getFeed().getItemsRecordStoreName(), newsItemsFilter);

            NewsItem item;
            items.trimToSize();
            Enumeration enumeration = items.elements();

            while (enumeration.hasMoreElements()) {
                item = (NewsItem) enumeration.nextElement();
                if (item.isRead()) {
                    this.append(item.getTitle(), ImageLoader.getImage(Constants.IMG_GREY_FEED));
                } else {
                    this.append(item.getTitle(), ImageLoader.getImage(Constants.IMG_DEFAULT_FEED));
                }
            }

            //set bold-font if unread
            /*had to be removed because of emulator bug on setFont
            for (int i = 0; i < items.size(); i++) {
            item = (NewsItem) items.elementAt(i);

            if (!item.isRead()) {
            this.setFont(i, Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
            }
            }
             */
            item = null;
            
        } catch (RecordStoreException ex) {
            new WarningAlert("Error", "Was unable to retrieve news from memory.").show();
        } catch (Throwable ex) {
            new WarningAlert("Error", "Sorry, an error occurred.").show();
        }
    }

    public void run() {
        Ticker updateTicker = new Ticker("updating..");

        this.setTicker(updateTicker);
        xmlReader = XmlReader.getInstance();
        Vector newItems;
        try {
            newItems = xmlReader.getEntries(getFeed().getUrl());
            PersistentManager.getInstance().addNewsItems(getFeed(), newItems);
            updateList();
            
            //TODO: remove from feed's IDs-vector those IDs that are not present in the feed anymore

        } catch (IOException ex) {
            new ErrorAlert("Network", "Could not retrieve any data. Check the URL on its correctness.").show();
        } catch (RecordStoreFullException ex) {
                new WarningAlert("Error", "The memory of the device is full!").show();
        } catch (Exception ex) {
                new WarningAlert("Error", "Sorry, an error occurred!").show();
        } finally {
            xmlReader = null;
            this.setTicker(null);
        }
    }

    public void commandAction(Command c, Displayable d) {

        //avoid initialization of objects if going to update or exit the view
        if (c == backCommand) {
            if (filtered) {
                //if filtered list, refresh parent list first (shall be a NewsItemList)
                ((NewsItemList) parent).updateList();
            }
            ReadJ2ME.showOnDisplay(parent);
            return;
        } else if (c == updateCommand) {
            Thread updateThread = new Thread(this);
            updateThread.start();
            return;
        } else if (c == filterByTagCommand) {
            TagChoiceFilterForm filterForm = new TagChoiceFilterForm(feed, this);
            ReadJ2ME.showOnDisplay(filterForm);
            return;
        } else if (c == searchCommand) {
            //TODO
            return;
        }

        int index = this.getSelectedIndex();
        if (index >= 0) {
            NewsItem selectedItem = (NewsItem) items.elementAt(index);

            if (c == markUnreadCommand) {
                selectedItem.setRead(false);
                PersistentManager.getInstance().updateNewsItem(selectedItem, getFeed().getItemsRecordStoreName());
                //update list and vector
                this.set(index, selectedItem.getTitle(), ImageLoader.getImage(Constants.IMG_DEFAULT_FEED));
            //had to be removed because of emulator bug on setFont
            //this.setFont(index, Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
            } else if (c == deleteItemCommand) {
                PersistentManager.getInstance().removeNewsItem(selectedItem, getFeed().getItemsRecordStoreName());
                //update list and vector
                this.delete(index);
                items.removeElementAt(index);

            } else if (c == openCommand || d == this) {
                //show view
                NewsItemForm itemView = new NewsItemForm(selectedItem, feed.getItemsRecordStoreName(), this);
                ReadJ2ME.showOnDisplay(itemView);
                //update item's read-status
                if (!selectedItem.isRead()) {
                    selectedItem.setRead(true);
                    PersistentManager.getInstance().updateNewsItem(selectedItem, getFeed().getItemsRecordStoreName());
                    //had to be removed because of emulator bug on setFont
                    //this.setFont(index, Font.getDefaultFont());
                    this.set(index, selectedItem.getTitle(), ImageLoader.getImage(Constants.IMG_GREY_FEED));
                }
            }

        } else {
            if(filtered) {
                new WarningAlert("Info", "No news matching!").show();
            } else {
                new WarningAlert("Info", "No news! Perform an update..").show();
            }
        }
    }

    /**
     * @return the feed
     */
    public Feed getFeed() {
        return feed;
    }
}
