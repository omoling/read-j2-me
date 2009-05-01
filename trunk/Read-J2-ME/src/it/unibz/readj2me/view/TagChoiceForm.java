package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.PersistentManager;
import it.unibz.readj2me.model.NewsItem;
import it.unibz.readj2me.model.Tag;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.rms.RecordStoreException;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class TagChoiceForm extends InputForm implements CommandListener, ItemStateListener {

    private ChoiceGroup tagsChoice;
    private Command addTagCommand;
    private Vector items;
    private NewsItem newsItem;
    private String itemRsName;
    private boolean changed = false;

    public TagChoiceForm(NewsItem newsItem, String itemRsName, Displayable parent){
        super("Available Tags", parent);
        this.newsItem = newsItem;
        this.itemRsName = itemRsName;
        items = new Vector();

        tagsChoice = new ChoiceGroup("Select tags:", Choice.MULTIPLE);
        this.append(tagsChoice);

        addTagCommand = new Command("Add new Tag", Command.SCREEN, -1);
        this.addCommand(addTagCommand);
        this.setCommandListener(this);

        this.setItemStateListener(this);

        refreshChoice();
    }

    public void refreshChoice() {
        getItems().removeAllElements();
        tagsChoice.deleteAll();

        PersistentManager pm = PersistentManager.getInstance();
        Enumeration enumeration;
        Tag tag;

        try {
            enumeration = pm.loadTags().elements();
            while (enumeration.hasMoreElements()) {
                tag = (Tag) enumeration.nextElement();
                getItems().addElement(tag);
                tagsChoice.append(tag.getName(), null);
            }
        } catch (RecordStoreException ex) {
            new WarningAlert("Error", "Some error while loading occurred.. " + ex.toString()).show();
        } catch (Throwable ex) {
            new WarningAlert("Error", "Some error occurred.." + ex.toString()).show();
        }

        //set tagged tags to selected on the Choice
        if (!getItems().isEmpty()) {
            for (int i = 0; i < getItems().size(); i++) {
                tag = (Tag) getItems().elementAt(i);
                if (newsItem.getTags().contains(tag)) {
                    tagsChoice.setSelectedIndex(i, true);
                }
            }
        }

        
    }

    protected void save() {
        if (changed) {
            boolean[] settings = new boolean[tagsChoice.size()];
            tagsChoice.getSelectedFlags(settings);
            if(settings != null && settings.length > 0) {
                Vector newTags = new Vector();
                for (int i = 0; i < settings.length; i++) {
                    if (settings[i]) {
                        newTags.addElement(getItems().elementAt(i));
                    }
                }
                newsItem.setTags(newTags);
                PersistentManager.getInstance().updateNewsItem(newsItem, itemRsName);
                ((NewsItemForm) parent).populateView();
            }
        }
    }

    protected boolean isInputValid() {
        return true;
    }

    public void commandAction(Command c, Displayable d) {
        if (c == addTagCommand) {
            TagForm tagForm = new TagForm(this);
            ReadJ2ME.showOnDisplay(tagForm);
        } else {
            super.commandAction(c, d);
        }
    }

    public void itemStateChanged(Item item) {
        if (item == tagsChoice) {
            changed = true;
        }
    }

    /**
     * @return the items
     */
    public Vector getItems() {
        return items;
    }
}
