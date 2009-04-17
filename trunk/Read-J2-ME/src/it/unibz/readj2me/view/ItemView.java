package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.PersistentManager;
import it.unibz.readj2me.model.NewsItem;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class ItemView extends Form implements CommandListener, ItemCommandListener {

    private Displayable parent;
    private NewsItem newsItem;
    private Command backCommand, openLinkCommand;
    private StringItem titleItem;
    private StringItem contentItem;
    private StringItem summaryItem;
    private StringItem linkItem;

    public ItemView(NewsItem newsItem, Displayable parent) {
        super(newsItem.getTitle());
        this.newsItem = newsItem;
        this.parent = parent;

        backCommand = new Command("Back", Command.BACK, 0);

        this.addCommand(backCommand);
        this.setCommandListener(this);

        populateView();
        checkItemUpdate();
    }

    private void checkItemUpdate() {
        if(!newsItem.isRead()){
            newsItem.setRead(true);
            String rsName = ((ItemList)parent).getFeedsItemsRecordStoreName();
            PersistentManager.getInstance().updateNewsItem(newsItem, rsName);
        }
    }

    private void populateView() {
        if (newsItem.getTitle() != null && !newsItem.getTitle().equals("")){
            titleItem = new StringItem(null, newsItem.getTitle());
            titleItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
            titleItem.setLayout(Item.LAYOUT_LEFT);
            this.append(titleItem);
        }
        if (newsItem.getContent() != null && !newsItem.getContent().equals("")) {
            contentItem = new StringItem(null, newsItem.getContent());
            contentItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
            contentItem.setLayout(Item.LAYOUT_LEFT);
            this.append(contentItem);
        }
        if (newsItem.getSummary() != null && !newsItem.getSummary().equals("")) {
            summaryItem = new StringItem(null, newsItem.getSummary());
            summaryItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
            summaryItem.setLayout(Item.LAYOUT_LEFT);
            this.append(summaryItem);
        }
        if (newsItem.getLink() != null && !newsItem.getLink().equals("")) {
            linkItem = new StringItem(null, "Link");
            linkItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
            linkItem.setLayout(Item.LAYOUT_LEFT);
            openLinkCommand = new Command("Browse", Command.OK, 0);
            linkItem.addCommand(openLinkCommand);
            this.append(linkItem);
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == backCommand) {
            ReadJ2ME.showOnDisplay(parent);
        }
    }

    public void commandAction(Command c, Item item) {
        if (item == linkItem && c == openLinkCommand){
            ReadJ2ME.platReq(newsItem.getLink());
        }
    }
}
