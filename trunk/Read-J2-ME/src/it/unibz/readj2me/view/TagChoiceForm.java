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
    private boolean changed = false;

    public TagChoiceForm(NewsItem newsItem, Displayable parent){
        super("Available Tags, parent", parent);
        this.newsItem = newsItem;
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
        items.removeAllElements();
        tagsChoice.deleteAll();

        PersistentManager pm = PersistentManager.getInstance();
        Enumeration enumeration;

        try {
            enumeration = pm.loadTags().elements();
            Tag tag;
            while (enumeration.hasMoreElements()) {
                tag = (Tag) enumeration.nextElement();
                items.addElement(tag);
                tagsChoice.append(tag.getName(), null);
            }
        } catch (RecordStoreException ex) {
            new WarningAlert("Error", "Some error while loading occurred.. " + ex.toString()).show();
        } catch (Throwable ex) {
            new WarningAlert("Error", "Some error occurred.." + ex.toString()).show();
        }

        //TODO: if newsitem tagged with tag, set selected
        //iterate items and set in tagsChoice
        //tagsChoice.setSelectedIndex(elementNum, true);

        
    }

    protected void save() {
        if (changed) {
            //TODO: save tags in newsitem
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected boolean isInputValid() {
        //TODO: always true?
        throw new UnsupportedOperationException("Not supported yet.");
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
}
