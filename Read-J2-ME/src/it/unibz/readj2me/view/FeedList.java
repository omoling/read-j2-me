package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.ImageLoader;
import it.unibz.readj2me.model.Feed;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class FeedList extends List implements CommandListener {

    private Command exitCommand, openCommand, addFeedCommand;
    private Vector items;

    public FeedList(String title, Displayable parent) {
        super(title, List.IMPLICIT);
        items = new Vector();

        exitCommand = new Command("Exit", Command.EXIT, 0);
        openCommand = new Command("Open", Command.OK, 0);
        addFeedCommand = new Command("Add new Feed", Command.SCREEN, 0);

        this.addCommand(exitCommand);
        this.addCommand(openCommand);
        this.addCommand(addFeedCommand);
        this.setCommandListener(this);

        //test
        Feed heiseFeed = new Feed();
        heiseFeed.setFeedName("Heise Mobile Atom");
        heiseFeed.setFeedUrl("http://www.heise.de/mobil/newsticker/heise-atom.xml");
        items.addElement(heiseFeed);
        Feed heiseFeed2 = new Feed();
        heiseFeed2.setFeedName("Heise Security Atom");
        heiseFeed2.setFeedUrl("http://www.heise.de/security/news/news-atom.xml");
        items.addElement(heiseFeed2);

        populateList();

    }

    private void populateList() {
        Enumeration enumeration = this.items.elements();

        while (enumeration.hasMoreElements()) {
            Feed feed = (Feed) enumeration.nextElement();
            this.append(feed.getFeedName(), ImageLoader.getImage(ImageLoader.DEFAULT_FEED));
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == exitCommand) {
            ReadJ2ME.destroy(true);
        } else if (c == addFeedCommand) {
            FeedView feedView = new FeedView(this);
            ReadJ2ME.showOnDisplay(feedView);
        } else if (c == openCommand || d == this) {
            Feed selectedFeed = (Feed) items.elementAt(this.getSelectedIndex());
            ItemList list = new ItemList(selectedFeed, this);
            ReadJ2ME.showOnDisplay(list);
        }
    }
}
