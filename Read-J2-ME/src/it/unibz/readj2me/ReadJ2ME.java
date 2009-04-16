package it.unibz.readj2me;

import it.unibz.readj2me.view.FeedList;
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

    private static ReadJ2ME instance;

    public void startApp() {
        instance = this;
        this.display = Display.getDisplay(this);
        FeedList feedList = new FeedList("Read-J2-Me", null);
        showOnDisplay(feedList);
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
            instance.platformRequest(url);
        } catch (ConnectionNotFoundException ex) {
            //nothing to do
        }
    }
    
}
