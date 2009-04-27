package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.ImageLoader;
import it.unibz.readj2me.model.Constants;
import it.unibz.readj2me.model.Tag;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

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

    private void refreshList() {

        this.deleteAll();
        items.removeAllElements();
        Enumeration enumeration;

        // add test data
        items.addElement(new Tag("tag1", 1));
        items.addElement(new Tag("tag2", 2));

        // TODO get from RS
        enumeration = items.elements();

        Tag tag;
        while(enumeration.hasMoreElements()){
            tag = (Tag) enumeration.nextElement();
            //items append...
            this.append(tag.getName(), ImageLoader.getImage(Constants.IMG_TAG));
        }

    }

    public void commandAction(Command c, Displayable d) {
        if (c == backCommand) {
            ReadJ2ME.showOnDisplay(parent);
        }
        // TODO
    }

}
