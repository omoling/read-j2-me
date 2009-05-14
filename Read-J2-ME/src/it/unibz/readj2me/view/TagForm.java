package it.unibz.readj2me.view;

import it.unibz.readj2me.controller.PersistentManager;
import it.unibz.readj2me.model.Tag;
import java.util.Enumeration;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class TagForm extends InputForm {

    private TextField tagNameField;

    public TagForm(Displayable parent){
        super("Add a new tag", parent, parent, "Save");

        tagNameField = new TextField("Name", "Tech", 10, TextField.ANY);
        tagNameField.setLayout(Item.LAYOUT_LEFT);

        this.append(tagNameField);
    }

    protected void save() {
        try {
            PersistentManager.getInstance().addTag(getName());
            //TODO: should be put in abstract InputView??
            if (parentDisplay.getClass().equals(TagList.class)) {
                ((TagList) parentDisplay).refreshList();
            } else if (parentDisplay.getClass().equals(TagChoiceForm.class)) {
                ((TagChoiceForm) parentDisplay).refreshChoice();
            }
        } catch (RecordStoreFullException ex) {
            new ErrorAlert("Memory", "Memory full!").show();
        } catch (RecordStoreException ex) {
            new ErrorAlert("Memory", "Sorry, memory error!").show();
        }
    }

    protected boolean isInputValid() {
        //check on empty and wheter name already taken
        boolean tagIsFree = true;
        try {
            Enumeration enumeration = PersistentManager.getInstance().loadTags().elements();
            Tag tag;
            while(enumeration.hasMoreElements()){
                tag = (Tag) enumeration.nextElement();
                if (tag.getName().equals(getName())){
                    tagIsFree = false;
                }
            }
            //TODO: review after RecordStore exceptions are handles in one way!!
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (getName() != null && !getName().equals("") && tagIsFree) {
            return true;
        } else {
            if(!tagIsFree){
                new WarningAlert("Tag", "Tag name already taken, please choose another name.").show();
            } else {
                new WarningAlert("Tag", "Please insert a name.").show();
            }
            return false;
        }
    }

    private String getName() {
        return tagNameField.getString();
    }

}
