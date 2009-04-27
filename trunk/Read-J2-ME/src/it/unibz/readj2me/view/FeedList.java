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

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class FeedList extends List implements CommandListener {

    private Command exitCommand, openCommand, addFeedCommand, deleteFeedCommand;
    private Command manageTagCommand;

    //test commands
    private Command eraseRSCommand, listRSCommand, testCommand;
    private Vector items;

    public FeedList(String title) {
        super(title, List.IMPLICIT);
        items = new Vector();

        exitCommand = new Command("Exit", Command.EXIT, 0);
        openCommand = new Command("Open", Command.SCREEN, 0);
        addFeedCommand = new Command("Add new Feed", Command.SCREEN, 1);
        deleteFeedCommand = new Command("Delete Feed", Command.SCREEN, 2);
        manageTagCommand = new Command("Manage Tags", Command.SCREEN, 3);

        //test commands
        eraseRSCommand = new Command("Erase RS", Command.SCREEN, 5);
        listRSCommand = new Command("List RS", Command.SCREEN, 4);
        testCommand = new Command("Test", Command.SCREEN, 6);

        this.addCommand(exitCommand);
        this.addCommand(openCommand);
        this.addCommand(addFeedCommand);
        this.addCommand(deleteFeedCommand);
        this.addCommand(manageTagCommand);

        //test commands
        this.addCommand(eraseRSCommand);
        this.addCommand(listRSCommand);
        this.addCommand(testCommand);

        this.setCommandListener(this);
        refreshList();
    }

    public void refreshList() {
        //test
        //Enumeration enumeration = this.items.elements();

        this.deleteAll();
        items.removeAllElements();

        PersistentManager pm = PersistentManager.getInstance();
        Enumeration enumeration;

        //TODO: put in items and show by iterating items?
        try {
            enumeration = pm.loadFeeds().elements();
            Feed feed;
            while (enumeration.hasMoreElements()) {
                feed = (Feed) enumeration.nextElement();
                items.addElement(feed);
                this.append(feed.getName(), ImageLoader.getImage(Constants.IMG_DEFAULT_FEED));
            }
        } catch (RecordStoreException ex) {
            new Warning("Error", "Some error while loading occurred.. " + ex.toString()).show();
        } catch (Throwable ex) {
            new Warning("Error", "Some error occurred.." + ex.toString()).show();
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
        }

        int index = this.getSelectedIndex();

        if (c == addFeedCommand) {
            FeedView feedView = new FeedView(this);
            ReadJ2ME.showOnDisplay(feedView);
        } else if (c == deleteFeedCommand) {
            
            if(index >= 0){
                Feed selectedFeed = (Feed) items.elementAt(index);     
                PersistentManager.getInstance().removeFeed(selectedFeed, true);
                //TODO remove manually only single removed element? (avoid reloading??)
                refreshList();
            }
        } else if (c == listRSCommand) {
            //TODO: to be removed after development
            PersistentManager.getInstance().listRS();
        } else if (c == eraseRSCommand){
            //TODO: to be removed after development
            PersistentManager.getInstance().eraseRS();
            refreshList();
        } else if (c == testCommand) {
            //TODO: to be removed after development
            PersistentManager.getInstance().addFeed("heise security", "http://www.heise.de/security/news/news-atom.xml");
            refreshList();
            
        } else if (c == openCommand || d == this) {
            if (index >= 0) {
                Feed selectedFeed = (Feed) items.elementAt(index);
                ItemList list = new ItemList(selectedFeed, this);
                ReadJ2ME.showOnDisplay(list);
            } else {
                new Warning("Info", "Add a feed first..").show();
            }
        }
    }
}
