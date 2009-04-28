package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.ImageLoader;
import it.unibz.readj2me.controller.PersistentManager;
import it.unibz.readj2me.model.Constants;
import it.unibz.readj2me.model.Tag;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.rms.RecordStoreException;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class TagList extends List implements CommandListener {

    private Displayable parent;
    private Command backCommand, addTagCommand, deleteTagCommand;
    private Vector items;

    public TagList(String title, Displayable parent){
        super(title, List.IMPLICIT);
        this.parent = parent;
        this.items = new Vector();

        backCommand = new Command("Back", Command.EXIT, 0);
        addTagCommand = new Command("Add tag", Command.SCREEN, 0);
        deleteTagCommand = new Command("Delete tag", Command.SCREEN, 1);

        this.addCommand(backCommand);
        this.addCommand(addTagCommand);
        this.addCommand(deleteTagCommand);

        this.setCommandListener(this);
        refreshList();
    }

    public void refreshList() {
        this.deleteAll();
        items.removeAllElements();

        PersistentManager pm = PersistentManager.getInstance();
        Enumeration enumeration;

        try {
            enumeration = pm.loadTags().elements();
            Tag tag;
            while (enumeration.hasMoreElements()) {
                tag = (Tag) enumeration.nextElement();
                items.addElement(tag);
                this.append(tag.getName(), ImageLoader.getImage(Constants.IMG_TAG));
            }
        } catch (RecordStoreException ex) {
            new WarningAlert("Error", "Some error while loading occurred.. " + ex.toString()).show();
        } catch (Throwable ex) {
            new WarningAlert("Error", "Some error occurred.." + ex.toString()).show();
        }
    }

    public void commandAction(Command c, Displayable d) {
        if (c == backCommand) {
            ReadJ2ME.showOnDisplay(parent);
        } else if (c == addTagCommand) {
            TagForm tagView = new TagForm(this);
            ReadJ2ME.showOnDisplay(tagView);
        } else if (c == deleteTagCommand) {

            //TODO: remove tag from all newsitems...

            int index = this.getSelectedIndex();
            if (index >= 0) {
                Tag selectedTag = (Tag) items.elementAt(index);
                PersistentManager.getInstance().removeTag(selectedTag);
                //remove from vector and list
                items.removeElementAt(index);
                this.delete(index);
            }
        }
    }

}
