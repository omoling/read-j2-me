package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.PersistentManager;
import it.unibz.readj2me.model.Feed;
import it.unibz.readj2me.model.Tag;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.rms.RecordStoreException;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class TagChoiceFilterForm extends Form implements CommandListener {

    private Displayable parent;
    private Feed feed;
    private Command backCommand, filterCommand;
    private ChoiceGroup tagsChoice;
    private Vector items;

    public TagChoiceFilterForm(Feed feed, Displayable parent) {
        super("Filter by:");
        this.feed = feed;
        this.parent = parent;

        backCommand = new Command("Back", Command.BACK, 0);
        filterCommand = new Command("Filter", Command.OK, 0);

        this.addCommand(backCommand);
        this.addCommand(filterCommand);

        this.setCommandListener(this);

        tagsChoice = new ChoiceGroup("Select tags to filter by:", Choice.MULTIPLE);
        this.append(tagsChoice);

        try {
            items = PersistentManager.getInstance().loadTags();
            Enumeration enumeration = items.elements();
            Tag tag;
            while(enumeration.hasMoreElements()) {
                tag = (Tag) enumeration.nextElement();
                tagsChoice.append(tag.getName(), null);
            }
            
        } catch (RecordStoreException ex) {
            new WarningAlert("Error", "Was unable to retrieve tags.").show();
        } catch (Throwable ex) {
            new WarningAlert("Error", "Sorry, an error occurred.").show();
        }
    }

    private boolean isAnySelected() {
        if (tagsChoice != null && tagsChoice.size() > 0) {
            for(int i = 0; i < tagsChoice.size(); i++) {
                if (tagsChoice.isSelected(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void commandAction(Command c, Displayable d) {
        if (c == backCommand) {
            ReadJ2ME.showOnDisplay(parent);
        } else if (c == filterCommand) {
            if (isAnySelected()) {
                //get tags to filter news by
                Vector filterTags = new Vector();
                for (int i = 0; i < tagsChoice.size(); i++) {
                    if (tagsChoice.isSelected(i)) {
                        filterTags.addElement(items.elementAt(i));
                    }
                }

                //prepare title prefix
                StringBuffer title = new StringBuffer();
                for(int i = 0; i < filterTags.size(); i++) {
                    title.append(((Tag) filterTags.elementAt(i)).getName());
                    if (i < filterTags.size() - 1) { title.append(", "); }
                }
                
                NewsItemList newsList = new NewsItemList(filterTags, title.toString(), feed, parent);
                ReadJ2ME.showOnDisplay(newsList);
            } else {
                new WarningAlert("Tags", "Select any Tag to filter the News by.").show();
            }
        }
    }

}
