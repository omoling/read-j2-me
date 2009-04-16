package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import it.unibz.readj2me.controller.PersistentManager;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class FeedView extends Form implements CommandListener{

    private Displayable parent;
    private TextField feedNameField, feedUrlField;

    private Command backCommand, saveCommand;

    public FeedView(Displayable parent){
        super("Add a new feed");
        this.parent = parent;

        backCommand = new Command("Back", Command.BACK, 0);
        saveCommand = new Command("Save", Command.SCREEN, 0);

        this.addCommand(backCommand);
        this.addCommand(saveCommand);
        this.setCommandListener(this);

        feedNameField = new TextField("Name", "heise mobil atom", 30, TextField.ANY);
        feedNameField.setLayout(Item.LAYOUT_LEFT);
        feedUrlField = new TextField("Url", null, 100, TextField.URL);
        feedUrlField.setString("http://www.heise.de/mobil/newsticker/heise-atom.xml");
        feedUrlField.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
        feedUrlField.setLayout(Item.LAYOUT_LEFT);
        this.append(feedNameField);
        this.append(feedUrlField);

    }

    public void commandAction(Command c, Displayable d) {
        if (c == backCommand) {
            ReadJ2ME.showOnDisplay(parent);
        } else if (c == saveCommand) {
            PersistentManager.getInstance().addFeed(feedNameField.getString(), feedUrlField.getString());
            ((FeedList)parent).refreshList();
            ReadJ2ME.showOnDisplay(parent);
        }
    }
    
}
