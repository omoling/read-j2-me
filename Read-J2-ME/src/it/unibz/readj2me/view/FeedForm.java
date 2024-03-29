package it.unibz.readj2me.view;

import it.unibz.readj2me.controller.PersistentManager;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.Spacer;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;

/**
 * Form used to add a new feed.
 *
 * @author Anton Dignoes, Omar Moling
 */
public class FeedForm extends InputForm {

    private TextField feedNameField,  feedUrlField;

    public FeedForm(Displayable parent) {
        super("Add a new feed", parent, parent, "Save");

        feedNameField = new TextField("Name", "heise mobil atom", 30, TextField.ANY);
        feedNameField.setLayout(Item.LAYOUT_LEFT);
        this.append(new Spacer(1, 5));
        feedUrlField = new TextField("Url", null, 300, TextField.URL);
        feedUrlField.setString("http://www.heise.de/mobil/newsticker/heise-atom.xml");
        feedUrlField.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
        feedUrlField.setLayout(Item.LAYOUT_LEFT);
        this.append(feedNameField);
        this.append(feedUrlField);
    }

    protected void save() {
        try {
            PersistentManager.getInstance().addFeed(getName(), getUrl());
            //could be put in abstract InputView
            ((FeedList) parentDisplay).refreshList();
        } catch (RecordStoreFullException ex) {
            new ErrorAlert("Memory", "Memory full!").show();
        } catch (RecordStoreException ex) {
            new ErrorAlert("Memory", "Sorry, memory error!").show();
        }
    }

    protected boolean isInputValid() {
        if (getName() != null && getUrl() != null && !getName().equals("") && !getUrl().equals("")) {
            return true;
        } else {
            new WarningAlert("Adding a feed", "Define both name and url..").show();
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
