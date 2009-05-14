package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.model.NewsItem;
import it.unibz.readj2me.model.Tag;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.Spacer;
import javax.microedition.lcdui.StringItem;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class NewsItemForm extends Form implements CommandListener, ItemCommandListener {

    private Displayable parent;
    private NewsItem newsItem;
    private Command backCommand, tagsCommand, openLinkCommand;
    private StringItem titleItem, tagsItem, contentItem, summaryItem, linkItem;
    private String itemRsName;
    private boolean changed = false;

    public NewsItemForm(NewsItem newsItem, String itemRsName, Displayable parent) {
        super(newsItem.getTitle());
        this.newsItem = newsItem;
        this.parent = parent;
        this.itemRsName = itemRsName;

        backCommand = new Command("Back", Command.BACK, 0);
        tagsCommand = new Command("Tags", Command.SCREEN, 0);

        this.addCommand(backCommand);
        this.addCommand(tagsCommand);
        this.setCommandListener(this);

        populateView();
    }

    public void populateView() {
        this.deleteAll();
        
        if (newsItem.getTitle() != null && !newsItem.getTitle().equals("")){
            titleItem = new StringItem(null, newsItem.getTitle());
            titleItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
            this.append(titleItem);
        }
        if (newsItem.getTags() != null && newsItem.getTags().size() > 0) {
            this.append(new Spacer(1, 5));
            StringBuffer sb = new StringBuffer("");
            int size = newsItem.getTags().size();
            Tag tag;
            for(int i = 0; i < size; i++) {
                tag = (Tag) newsItem.getTags().elementAt(i);
                sb.append(tag.getName());
                if (i < size -1) {
                    sb.append(", ");
                }
            }
            tagsItem = new StringItem("Tags:", sb.toString());
            tagsItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
            this.append(tagsItem);
        }
        if (newsItem.getContent() != null && !newsItem.getContent().equals("")) {
            this.append(new Spacer(1, 5));
            contentItem = new StringItem(null, newsItem.getContent());
            contentItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
            this.append(contentItem);
        }
        if (newsItem.getSummary() != null && !newsItem.getSummary().equals("")) {
            this.append(new Spacer(1, 5));
            summaryItem = new StringItem(null, newsItem.getSummary());
            summaryItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
            this.append(summaryItem);
        }
        if (newsItem.getLink() != null && !newsItem.getLink().equals("")) {
            this.append(new Spacer(1, 5));
            linkItem = new StringItem(null, "Link", Item.HYPERLINK);
            linkItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
            openLinkCommand = new Command("Browse", Command.OK, 0);
            linkItem.addCommand(openLinkCommand);
            linkItem.setItemCommandListener(this);
            this.append(linkItem);
        }
        
    }

    public void commandAction(Command c, Displayable d) {
        if (c == backCommand) {
            if(changed) {
                //TODO: not possible to update single list-entry?
                ((NewsItemList) parent).updateList();
            }
            ReadJ2ME.showOnDisplay(parent);
        } else if (c == tagsCommand) {
            TagChoiceForm tagChoiceForm = new TagChoiceForm(newsItem, itemRsName, this);
            ReadJ2ME.showOnDisplay(tagChoiceForm);
        }
    }

    public void commandAction(Command c, Item item) {
        if (item == linkItem && c == openLinkCommand){
            ReadJ2ME.platReq(newsItem.getLink());
        }
    }

    /**
     * @param changed the changed to set
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }
    
}
