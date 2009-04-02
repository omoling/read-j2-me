package it.unibz.readj2me;

import it.unibz.readj2me.view.FeedList;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.*;

public class ReadJ2ME extends MIDlet {

    private Display display;

    public void startApp() {

        this.display = Display.getDisplay(this);

        FeedList feedList = new FeedList("My Feeds", this, null);

        showOnDisplay(feedList);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        this.display.setCurrent(null);
        notifyDestroyed();
    }

    public void showOnDisplay(Displayable d){
        display.setCurrent(d);
    }

    /*
    public int getDisplayImageWidth(int type){
        return display.getBestImageWidth(type);
    }

    public int getDisplayImageHeight(int type){
        return display.getBestImageHeight(type);
    }
    */
    
}
