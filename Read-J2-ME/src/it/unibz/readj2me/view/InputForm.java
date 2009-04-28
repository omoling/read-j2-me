package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public abstract class InputForm extends Form implements CommandListener {

    protected Displayable parent;
    protected Command backCommand, saveCommand;

    public InputForm(String title, Displayable parent) {
        super(title);
        this.parent = parent;

        backCommand = new Command("Back", Command.BACK, 0);
        saveCommand = new Command("Save", Command.SCREEN, 0);

        this.addCommand(backCommand);
        this.addCommand(saveCommand);
        this.setCommandListener(this);
    }

    protected abstract void save();

    protected abstract boolean isInputValid();

    public void commandAction(Command c, Displayable d) {
        if (c == backCommand) {
            ReadJ2ME.showOnDisplay(parent);
        } else if (c == saveCommand) {
            if(isInputValid()){
                save();
                //TODO: if lists extend abstract class: call abstract refersh method
                ReadJ2ME.showOnDisplay(parent);
            }
            /* what to show to the user if the input is not valid should be
             * defined in the implementation's isInputValid()
             */
        }
    }
    
}
