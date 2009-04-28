package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.PersistentManager;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class FeedView extends InputView {

    private TextField feedNameField,  feedUrlField;

    public FeedView(Displayable parent) {
        super("Add a new feed", parent);

        feedNameField = new TextField("Name", "heise mobil atom", 30, TextField.ANY);
        feedNameField.setLayout(Item.LAYOUT_LEFT);
        feedUrlField = new TextField("Url", null, 300, TextField.URL);
        feedUrlField.setString("http://www.heise.de/mobil/newsticker/heise-atom.xml");
        feedUrlField.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
        feedUrlField.setLayout(Item.LAYOUT_LEFT);
        this.append(feedNameField);
        this.append(feedUrlField);
    }

    protected void save() {
        PersistentManager.getInstance().addFeed(getName(), getUrl());
        ((FeedList) parent).refreshList();
    }

    protected boolean isInputValid() {
        if (getName() != null && getUrl() != null && !getName().equals("") && !getUrl().equals("")) {
            return true;
        } else {
            new Warning("Adding a feed", "Define both name and url..").show();
            return false;
        }
    }

    private String getName() {
        return feedNameField.getString();
    }

    private String getUrl() {
       return feedUrlField.getString();
    }
    
}
