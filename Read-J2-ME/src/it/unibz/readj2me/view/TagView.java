package it.unibz.readj2me.view;

import it.unibz.readj2me.controller.PersistentManager;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class TagView extends InputView {

    private TextField tagNameField;

    public TagView(Displayable parent){
        super("Add a new tag", parent);

        tagNameField = new TextField("Name", "Tech", 10, TextField.ANY);
        tagNameField.setLayout(Item.LAYOUT_LEFT);

        this.append(tagNameField);
    }

    protected void save() {
        PersistentManager.getInstance().addTag(getName());
        //TODO: should be put in abstract InputView
        ((TagList)parent).refreshList();
    }

    protected boolean isInputValid() {
        if (getName() != null && !getName().equals("")) {
            //TODO: check in items if name already occupied
            return true;
        } else {
            return false;
        }
    }

    private String getName() {
        return tagNameField.getString();
    }

}
