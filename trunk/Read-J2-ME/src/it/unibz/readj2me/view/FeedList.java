package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.ImageLoader;
import it.unibz.readj2me.controller.PersistentManager;
import it.unibz.readj2me.model.Constants;
import it.unibz.readj2me.model.Feed;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;

/**
 * List to show all feeds and also entry point for the user, so it contains
 * other commands, i.e. to manage tags or edit the configuration.
 *
 * @author Anton Dignoes, Omar Moling
 */
public class FeedList extends List implements CommandListener {

    private Command exitCommand,  openCommand,  addFeedCommand,  deleteFeedCommand;
    private Command manageTagCommand,  configCommand;

    private Vector items;

    public FeedList(String title) {
        super(title, List.IMPLICIT);
        items = new Vector();

        exitCommand = new Command("Exit", Command.EXIT, 0);
        openCommand = new Command("Open", Command.SCREEN, 0);
        addFeedCommand = new Command("Add new Feed", Command.SCREEN, 1);
        deleteFeedCommand = new Command("Delete Feed", Command.SCREEN, 2);
        manageTagCommand = new Command("Manage Tags", Command.SCREEN, 3);
        configCommand = new Command("Configuration", Command.SCREEN, 4);

        this.addCommand(exitCommand);
        this.addCommand(openCommand);
        this.addCommand(addFeedCommand);
        this.addCommand(deleteFeedCommand);
        this.addCommand(manageTagCommand);
        this.addCommand(configCommand);

        this.setCommandListener(this);
        refreshList();
    }

    public void refreshList() {
        this.deleteAll();
        items.removeAllElements();

        PersistentManager pm = PersistentManager.getInstance();
        Enumeration enumeration;

        try {
            enumeration = pm.loadFeeds().elements();
            Feed feed;
            while (enumeration.hasMoreElements()) {
                feed = (Feed) enumeration.nextElement();
                items.addElement(feed);
                this.append(feed.getName(), ImageLoader.getImage(Constants.IMG_DEFAULT_FEED));
            }
        } catch (RecordStoreException ex) {
            new WarningAlert("Error", "Some error while loading occurred!" + ex.toString()).show();
        } catch (Throwable ex) {
            new WarningAlert("Error", "Sorry, some general occurred!" + ex.toString()).show();
        }

    }

    public void commandAction(Command c, Displayable d) {
        //if-else for operations that do not require inst.of objects
        if (c == exitCommand) {
            ReadJ2ME.destroy(true);
            return;
        } else if (c == manageTagCommand) {
            TagList tagList = new TagList("Tags", this);
            ReadJ2ME.showOnDisplay(tagList);
            return;
        } else if (c == configCommand) {
            ConfigurationForm configForm = new ConfigurationForm(this);
            ReadJ2ME.showOnDisplay(configForm);
            return;
        } else if (c == addFeedCommand) {
            FeedForm feedView = new FeedForm(this);
            ReadJ2ME.showOnDisplay(feedView);
            return;
        }
       
        int index = this.getSelectedIndex();

        if (index >= 0) {
            Feed selectedFeed = (Feed) items.elementAt(index);

            try {
                if (c == deleteFeedCommand) {
                    PersistentManager.getInstance().removeFeed(selectedFeed);
                    //remove from vector and list
                    items.removeElementAt(index);
                    this.delete(index);
                } else if (c == openCommand || d == this) {
                    NewsItemList list = new NewsItemList(selectedFeed, this);
                    ReadJ2ME.showOnDisplay(list);
                }
            } catch (RecordStoreFullException ex) {
                new ErrorAlert("Memory", "Memory full!").show();
            } catch (RecordStoreException ex) {
                new ErrorAlert("Memory", "Sorry, memory error!").show();
            }
        } else {
            new WarningAlert("Info", "Add a feed first..").show();
        }
    }
}
