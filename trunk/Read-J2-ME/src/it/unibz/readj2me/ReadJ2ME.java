package it.unibz.readj2me;

import it.unibz.readj2me.view.FeedList;
import it.unibz.readj2me.view.Warning;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.*;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class ReadJ2ME extends MIDlet {

    private Display display;

    private boolean initialized = false;
    private static ReadJ2ME instance;

    public void startApp() {
        if (!initialized) {
            instance = this;
            this.display = Display.getDisplay(this);
            FeedList feedList = new FeedList("Read-J2-Me");
            showOnDisplay(feedList);
            initialized = true;
        }
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        this.display.setCurrent(null);
        notifyDestroyed();
    }

    public static void showOnDisplay(Displayable d){
        instance.display.setCurrent(d);
    }

    public static void destroy(boolean unconditional){
        instance.destroyApp(unconditional);
    }

    public static void platReq(String url){
        try {
          if(instance.platformRequest("omar moling")) {
             new Warning("Warning", "Either no browsing capabilities\nor the Midlet has to be quit to view the page.").show(); 
          }
        } catch (ConnectionNotFoundException ex) {
            new it.unibz.readj2me.view.Error("Error", "No connection available.").show();
        }
    }
    
}
