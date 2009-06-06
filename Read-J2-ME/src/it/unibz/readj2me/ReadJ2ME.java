package it.unibz.readj2me;

import it.unibz.readj2me.view.FeedList;
import it.unibz.readj2me.view.WarningAlert;
import it.unibz.readj2me.controller.PersistentManager;
import it.unibz.readj2me.model.Configuration;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStoreException;

/**
 * Main MIDlet class.
 *
 * @author Anton Dignoes, Omar Moling
 */
public class ReadJ2ME extends MIDlet {

    private Display display;
    private boolean initialized = false;
    private static ReadJ2ME instance;

    /**
     * Starts up the MIDlet
     */
    public void startApp() {
        if (!initialized) {
            instance = this;
            this.display = Display.getDisplay(this);

            loadConfiguration();

            showOnDisplay(new FeedList("Read-J2-Me"));
            initialized = true;
        }
    }

    /**
     * Checks for and loads the configuration, if any.
     */
    private void loadConfiguration() {
        try {
            //load Configuration if any, else default will be loaded
            byte[] config = PersistentManager.getInstance().loadConfiguration();
            if (config != null) {
                Configuration configuration = Configuration.getInstance();
                configuration.createFromBytes(config);
            }
        } catch (RecordStoreException ex) {
            // nothing, will load default configuration
        }
    }

    /**
     * Called when MIDlet is paused, here nothing to be done.
     */
    public void pauseApp() {
    }

    /**
     * Called when MIDlet is destroyed, sets display to null and calls notifyDestroyed().
     * @param unconditional
     */
    public void destroyApp(boolean unconditional) {
        this.display.setCurrent(null);
        notifyDestroyed();
    }

    /**
     * Used to show a displayable on the display
     * @param d the displayable to be shown
     */
    public static void showOnDisplay(Displayable d) {
        instance.display.setCurrent(d);
    }

    /**
     * Used internally for exiting the MIDlet.
     * @param unconditional
     */
    public static void destroy(boolean unconditional) {
        instance.destroyApp(unconditional);
    }

    /**
     * Used internally to perform a platform-request
     * @param url
     */
    public static void platReq(String url) {
        try {
            if (instance.platformRequest(url)) {
                new WarningAlert("Warning", "Either no browsing capabilities\nor the Midlet has to be quit to view the page.").show();
            }
        } catch (ConnectionNotFoundException ex) {
            new it.unibz.readj2me.view.ErrorAlert("Error", "No connection available.").show();
        }
    }
}
