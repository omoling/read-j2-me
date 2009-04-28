package it.unibz.readj2me.view;

import it.unibz.readj2me.controller.PersistentManager;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class TagForm extends InputForm {

    private TextField tagNameField;

    public TagForm(Displayable parent){
        super("Add a new tag", parent);

        tagNameField = new TextField("Name", "Tech", 10, TextField.ANY);
        tagNameField.setLayout(Item.LAYOUT_LEFT);

        this.append(tagNameField);
    }

    protected void save() {
        PersistentManager.getInstance().addTag(getName());
        //TODO: should be put in abstract InputView??
        if(parent.getClass().equals(TagList.class)){
            ((TagList)parent).refreshList();
        } else if (parent.getClass().equals(TagChoiceForm.class)) {
            ((TagChoiceForm)parent).refreshChoice();
        }
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
