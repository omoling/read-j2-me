package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.model.NewsItem;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.StringItem;

public class ItemView extends Form implements CommandListener {

    private Displayable parent;
    private NewsItem newsItem;
    private Command backCommand;
    private StringItem contentItem;
    private StringItem summaryItem;

    public ItemView(NewsItem newsItem, Displayable parent) {
        super(newsItem.getTitle());
        this.newsItem = newsItem;
        this.parent = parent;

        backCommand = new Command("Back", Command.BACK, 0);

        this.addCommand(backCommand);
        this.setCommandListener(this);

        populateView();
    }

    private void populateView() {
        if (newsItem.getContent() != null) {
            contentItem = new StringItem(null, newsItem.getContent());
            contentItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
            contentItem.setLayout(Item.LAYOUT_LEFT);
            this.append(contentItem);
        }
        if (newsItem.getSummary() != null) {
            summaryItem = new StringItem(null, newsItem.getSummary());
            summaryItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
            summaryItem.setLayout(Item.LAYOUT_LEFT);
            this.append(summaryItem);
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == backCommand) {
            ReadJ2ME.showOnDisplay(parent);
        }
    }
}
