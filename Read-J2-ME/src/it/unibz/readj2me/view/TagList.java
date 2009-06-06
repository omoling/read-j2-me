package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.ImageLoader;
import it.unibz.readj2me.controller.NewsItemTagFilter;
import it.unibz.readj2me.controller.PersistentManager;
import it.unibz.readj2me.model.Constants;
import it.unibz.readj2me.model.Feed;
import it.unibz.readj2me.model.NewsItem;
import it.unibz.readj2me.model.Tag;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Ticker;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;

/**
 * List containing all tags.
 *
 * @author Anton Dignoes, Omar Moling
 */
public class TagList extends List implements CommandListener {

    private Displayable parent;
    private Command backCommand,  addTagCommand,  deleteTagCommand;
    private Vector items;

    public TagList(String title, Displayable parent) {
        super(title, List.IMPLICIT);
        this.parent = parent;
        this.items = new Vector();

        backCommand = new Command("Back", Command.EXIT, 0);
        addTagCommand = new Command("Add tag", Command.SCREEN, 0);
        deleteTagCommand = new Command("Delete tag", Command.SCREEN, 1);

        this.addCommand(backCommand);
        this.addCommand(addTagCommand);
        this.addCommand(deleteTagCommand);

        this.setCommandListener(this);
        refreshList();
    }

    public void refreshList() {
        this.deleteAll();
        getItems().removeAllElements();

        PersistentManager pm = PersistentManager.getInstance();
        Enumeration enumeration;

        try {
            enumeration = pm.loadTags().elements();
            Tag tag;
            while (enumeration.hasMoreElements()) {
                tag = (Tag) enumeration.nextElement();
                getItems().addElement(tag);
                this.append(tag.getName(), ImageLoader.getImage(Constants.IMG_TAG));
            }
        } catch (RecordStoreException ex) {
            new WarningAlert("Error", "Some error while loading occurred.. " + ex.toString()).show();
        } catch (Throwable ex) {
            new WarningAlert("Error", "Some error occurred.." + ex.toString()).show();
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == backCommand) {
            ReadJ2ME.showOnDisplay(parent);
        } else if (c == addTagCommand) {
            TagForm tagView = new TagForm(this);
            ReadJ2ME.showOnDisplay(tagView);
        } else if (c == deleteTagCommand) {
            Ticker deletingTicker = new Ticker("deleting tag from newsitems..");
            this.setTicker(deletingTicker);

            int index = this.getSelectedIndex();
            try {
                if (index >= 0) {
                    Tag selectedTag = (Tag) getItems().elementAt(index);
                    PersistentManager pm = PersistentManager.getInstance();

                    //remove tag from ALL newsitems
                    Vector newsItems, tags, feeds = pm.loadFeeds();
                    Enumeration enumNewsItems, enumFeeds = feeds.elements();
                    Feed currentFeed;
                    NewsItem currentNewsItem;
                    while (enumFeeds.hasMoreElements()) {
                        currentFeed = (Feed) enumFeeds.nextElement();
                        newsItems = pm.loadNewsItemsByDate(currentFeed.getItemsRecordStoreName(),
                                new NewsItemTagFilter(selectedTag), false);
                        enumNewsItems = newsItems.elements();
                        while (enumNewsItems.hasMoreElements()) {
                            currentNewsItem = (NewsItem) enumNewsItems.nextElement();
                            tags = currentNewsItem.getTags();
                            int tagIndex = -1;
                            for (int i = 0; i < tags.size(); i++) {
                                if (((Tag) tags.elementAt(i)).equals(selectedTag)) {
                                    tagIndex = i;
                                    break;
                                }
                            }
                            tags.removeElementAt(tagIndex);
                            currentNewsItem.setTags(tags);
                            pm.updateNewsItem(currentNewsItem, currentFeed.getItemsRecordStoreName());
                        }
                    }

                    pm.removeTag(selectedTag);
                    //remove from vector and list
                    getItems().removeElementAt(index);
                    this.delete(index);
                }
            } catch (RecordStoreFullException ex) {
                new ErrorAlert("Memory", "Memory full!").show();
            } catch (RecordStoreException ex) {
                new ErrorAlert("Memory", "Sorry, memory error!").show();
            } finally {
                this.setTicker(null);
            }
        }
    }

    /**
     * @return the items
     */
    public Vector getItems() {
        return items;
    }
}
