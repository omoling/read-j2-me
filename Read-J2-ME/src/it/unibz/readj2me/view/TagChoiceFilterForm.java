package it.unibz.readj2me.view;

import it.unibz.readj2me.controller.PersistentManager;
import it.unibz.readj2me.model.Feed;
import it.unibz.readj2me.model.Tag;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Displayable;
import javax.microedition.rms.RecordStoreException;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class TagChoiceFilterForm extends InputForm {

    private Feed feed;
    private ChoiceGroup tagsChoice;
    private Vector items;

    public TagChoiceFilterForm(Feed feed, Displayable parent) {
        super("Filter by:", parent, parent, "Filter");
        this.feed = feed;
        this.parentDisplay = parent;

        tagsChoice = new ChoiceGroup("Select tags to filter by:", Choice.MULTIPLE);
        this.append(tagsChoice);

        try {
            items = PersistentManager.getInstance().loadTags();
            Enumeration enumeration = items.elements();
            Tag tag;
            while (enumeration.hasMoreElements()) {
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
            for (int i = 0; i < tagsChoice.size(); i++) {
                if (tagsChoice.isSelected(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void save() {
        //get tags to filter news by
        Vector filterTags = new Vector();
        for (int i = 0; i < tagsChoice.size(); i++) {
            if (tagsChoice.isSelected(i)) {
                filterTags.addElement(items.elementAt(i));
            }
        }

        //prepare title prefix
        StringBuffer title = new StringBuffer();
        for (int i = 0; i < filterTags.size(); i++) {
            title.append(((Tag) filterTags.elementAt(i)).getName());
            if (i < filterTags.size() - 1) {
                title.append(", ");
            }
        }

        NewsItemList newsList = new NewsItemList(filterTags, title.toString(), feed, parentDisplay);
        //set the new list as next displayable
        super.setNextDisplay(newsList);
    }

    protected boolean isInputValid() {
        if (isAnySelected()) {
            return true;
        } else {
            new WarningAlert("Tags", "Select any Tag to filter the News by.").show();
            return false;
        }
    }
}
