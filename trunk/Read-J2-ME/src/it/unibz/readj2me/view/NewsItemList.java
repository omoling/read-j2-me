package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.ImageLoader;
import it.unibz.readj2me.controller.NewsItemSearchFilter;
import it.unibz.readj2me.controller.NewsItemTagFilter;
import it.unibz.readj2me.controller.PersistentManager;
import it.unibz.readj2me.controller.XmlReader;
import it.unibz.readj2me.model.Configuration;
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
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Ticker;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import org.xmlpull.v1.XmlPullParserException;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class NewsItemList extends List implements CommandListener, Runnable {

    private Displayable parent;
    private Feed feed;
    private Vector items = new Vector();
    private XmlReader xmlReader;
    private Command backCommand, openCommand, updateCommand, deleteItemCommand,
            markUnreadCommand, filterByTagCommand, searchCommand;
    private RecordFilter newsItemsFilter = null;
    private boolean filtered = false, changed = false;

    public NewsItemList(Feed feed, Displayable parent) {
        super(feed.getName(), List.IMPLICIT);
        filtered = false;
        this.parent = parent;
        this.feed = feed;
        init();
    }

    public NewsItemList(Vector filterTags, String titlePrefix, Feed feed, Displayable parent) {
        super(titlePrefix + " for " + feed.getName(), List.IMPLICIT);
        filtered = true;
        this.feed = feed;
        this.parent = parent;

        newsItemsFilter = new NewsItemTagFilter(filterTags);
        init();
    }

    public NewsItemList(String searchString, Feed feed, Displayable parent) {
        super("\"" + searchString + "\" in " + feed.getName(), List.IMPLICIT);
        filtered = true;
        this.feed = feed;
        this.parent = parent;

        newsItemsFilter = new NewsItemSearchFilter(searchString);
        init();
    }

    private void init() {
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

    protected void updateList() {
        //empty list and vector
        items.removeAllElements();
        this.deleteAll();

        try {
            //TODO: handle d*** exceptions
            items = PersistentManager.getInstance().loadNewsItems(getFeed().getItemsRecordStoreName(), newsItemsFilter, false);

            NewsItem item;
            items.trimToSize();
            Enumeration enumeration = items.elements();

            while (enumeration.hasMoreElements()) {
                item = (NewsItem) enumeration.nextElement();
                this.append(item.getTitle(), getNewsItemIcon(item));
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
        boolean[] knownIndexes;
        try {
            newItems = xmlReader.getEntries(getFeed().getUrl());
            newItems.trimToSize();

            if (newItems != null && newItems.size() > 0) {

                //remove already known items
                knownIndexes = new boolean[newItems.size()];
                NewsItem item;
                for(int i = 0; i < newItems.size(); i++) {
                    item = (NewsItem) newItems.elementAt(i);
                    if (feed.getKnownIds().contains(item.getId())) {
                        knownIndexes[i] = true;
                    } else {
                        knownIndexes[i] = false;
                    }
                }
                for(int i = knownIndexes.length - 1; i >= 0; i--) {
                    if (knownIndexes[i]) {
                        newItems.removeElementAt(i);
                    }
                }
                knownIndexes = null;

                //add new items
                PersistentManager.getInstance().addNewsItems(getFeed(), newItems);
                newItems = null;
                
                //if limit > 0 delete old items if there are more than limit
                if(Configuration.getInstance().getMaxNewsItems() > 0) {
                    Vector currentItems = PersistentManager.getInstance().loadNewsItems(feed.getItemsRecordStoreName(), null, true);
                    int toBeDeleted = currentItems.size() - (Configuration.getInstance().getMaxNewsItems() * 10);
                    if (toBeDeleted > 0) {
                        Enumeration e = currentItems.elements();
                        for (int i = 0; i < toBeDeleted; i++) {
                            if (e.hasMoreElements()) {
                                item = (NewsItem) e.nextElement();
                                if (item.getTags().size() == 0) {
                                    PersistentManager.getInstance().removeNewsItem(item, feed.getItemsRecordStoreName());
                                } else {
                                    //avoid deleting this news by decrementing i
                                    i--;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                }

                updateList();
            }
            
        } catch (IOException ex) {
            new ErrorAlert("Network", "Could not retrieve any data. Check the URL on its correctness.").show();
        } catch (RecordStoreFullException ex) {
                new WarningAlert("Error", "The memory of the device is full!").show();
        } catch (XmlPullParserException ex) {
                new WarningAlert("Error", "Error in parsing data, XML format not supported!").show();
                //TODO
                System.out.println(ex.getMessage());
                ex.printStackTrace();
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
                if (changed && parent instanceof NewsItemList) {
                    ((NewsItemList) parent).updateList();
                }
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
            NewsItemSearchForm searchForm = new NewsItemSearchForm(feed, this);
            ReadJ2ME.showOnDisplay(searchForm);
            return;
        }

        int index = this.getSelectedIndex();
        if (index >= 0) {
            NewsItem selectedItem = (NewsItem) items.elementAt(index);

            if (c == markUnreadCommand) {
                selectedItem.setRead(false);
                PersistentManager.getInstance().updateNewsItem(selectedItem, getFeed().getItemsRecordStoreName());
                //update list and vector
                this.set(index, selectedItem.getTitle(), getNewsItemIcon(selectedItem));
                //had to be removed because of emulator bug on setFont
                //this.setFont(index, Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
                changed = true;
            } else if (c == deleteItemCommand) {
                PersistentManager.getInstance().removeNewsItem(selectedItem, getFeed().getItemsRecordStoreName());
                //update list and vector
                this.delete(index);
                items.removeElementAt(index);
                changed = true;
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
                    this.set(index, selectedItem.getTitle(), getNewsItemIcon(selectedItem));
                    changed = true;
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

    private Image getNewsItemIcon(NewsItem newsItem) {
        if (newsItem.getTags().size() > 0) {
            if (newsItem.isRead()) {
                return ImageLoader.getImage(Constants.IMG_GREY_FEED_L);
            } else {
                return ImageLoader.getImage(Constants.IMG_DEFAULT_FEED_L);
            }
        } else {
            if (newsItem.isRead()) {
                return ImageLoader.getImage(Constants.IMG_GREY_FEED);
            } else {
                return ImageLoader.getImage(Constants.IMG_DEFAULT_FEED);
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
