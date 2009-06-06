package it.unibz.readj2me.view;

import it.unibz.readj2me.ReadJ2ME;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

/**
 * Abstract class used by all classes that require some input from the user.
 * The class provides the basic commands to save/accept or go back and
 * the basic structure to check whether the input is valid and to save/accept
 * the input. The actual implementation has to be done by the subclasses.
 *
 * @author Anton Dignoes, Omar Moling
 */
public abstract class InputForm extends Form implements CommandListener {
    protected Displayable parentDisplay;
    private Displayable nextDisplay;
    protected Command backCommand, saveCommand;

    public InputForm(String title, Displayable parent, Displayable nextDisplay, String okString) {
        super(title);
        this.parentDisplay = parent;
        this.nextDisplay = nextDisplay;

        backCommand = new Command("Back", Command.BACK, 0);
        saveCommand = new Command(okString, Command.SCREEN, 0);

        this.addCommand(backCommand);
        this.addCommand(saveCommand);
        this.setCommandListener(this);
    }

    protected abstract void save();

    protected abstract boolean isInputValid();

    public void commandAction(Command c, Displayable d) {
        if (c == backCommand) {
            ReadJ2ME.showOnDisplay(parentDisplay);
        } else if (c == saveCommand) {
            if(isInputValid()){
                save();
                //if lists would extend abstract class: call abstract refersh method
                ReadJ2ME.showOnDisplay(nextDisplay);
            }
            /* what to show to the user if the input is not valid should be
             * defined in the implementation's isInputValid()
             */
        }
    }

    /**
     * @param nextDisplay the nextDisplay to set
     */
    public void setNextDisplay(Displayable nextDisplay) {
        this.nextDisplay = nextDisplay;
    }
    
}
