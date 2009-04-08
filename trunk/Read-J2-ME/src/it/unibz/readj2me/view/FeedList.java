package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.ImageLoader;
import it.unibz.readj2me.controller.PersistentManager;
import it.unibz.readj2me.model.Feed;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.rms.RecordStoreException;

public class FeedList extends List implements CommandListener {

    private Command exitCommand, openCommand, addFeedCommand, deleteFeedCommand;
    private Vector items;

    public FeedList(String title, Displayable parent) {
        super(title, List.IMPLICIT);
        items = new Vector();

        exitCommand = new Command("Exit", Command.EXIT, 0);
        openCommand = new Command("Open", Command.SCREEN, 0);
        addFeedCommand = new Command("Add new Feed", Command.SCREEN, 1);
        deleteFeedCommand = new Command("Delete Feed", Command.SCREEN, 2);

        this.addCommand(exitCommand);
        this.addCommand(openCommand);
        this.addCommand(addFeedCommand);
        this.addCommand(deleteFeedCommand);
        this.setCommandListener(this);

        //test
        /*
        Feed heiseFeed = new Feed();
        heiseFeed.setName("Heise Mobile Atom");
        heiseFeed.setUrl("http://www.heise.de/mobil/newsticker/heise-atom.xml");
        items.addElement(heiseFeed);
        Feed heiseFeed2 = new Feed();
        heiseFeed2.setName("Heise Security Atom");
        heiseFeed2.setUrl("http://www.heise.de/security/news/news-atom.xml");
        items.addElement(heiseFeed2);
        */
        
        refershList();

    }

    public void refershList() {
        //test
        //Enumeration enumeration = this.items.elements();

        this.deleteAll();
        items.removeAllElements();

        PersistentManager pm = PersistentManager.getInstance();
        Enumeration enumeration;

        //TODO: handle exceptions: show view or alert
        //TODO: put in items and show by iterating items?
        try {
            enumeration = pm.loadFeeds().elements();
            while (enumeration.hasMoreElements()) {
            Feed feed = (Feed) enumeration.nextElement();
            items.addElement(feed);
            this.append(feed.getName(), ImageLoader.getImage(ImageLoader.DEFAULT_FEED));
        }
        } catch (RecordStoreException ex) {
            //TODO
            //ex.printStackTrace();
        } catch (Exception ex) {
            //TODO
            //ex.printStackTrace();
        }

    }

    public void commandAction(Command c, Displayable d) {
        if (c == exitCommand) {
            ReadJ2ME.destroy(true);
        } else if (c == addFeedCommand) {
            FeedView feedView = new FeedView(this);
            ReadJ2ME.showOnDisplay(feedView);
        } else if (c == deleteFeedCommand) {
            Feed selectedFeed = (Feed) items.elementAt(this.getSelectedIndex());

            //TODO remove manually single removed element?
            PersistentManager.getInstance().removeFeed(selectedFeed);
            refershList();
            
        } else if (c == openCommand || d == this) {
            Feed selectedFeed = (Feed) items.elementAt(this.getSelectedIndex());
            ItemList list = new ItemList(selectedFeed, this);
            ReadJ2ME.showOnDisplay(list);
        }
    }
}
