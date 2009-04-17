package it.unibz.readj2me.controller;

import java.io.IOException;
import javax.microedition.lcdui.Image;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class ImageLoader {

    public static final String DEFAULT_FEED = "/feed.png";
    public static final String GREY_FEED = "/feed_grey.png";

    public static Image getImage(String name) {
        Image img = null;
        try {
            if (!name.startsWith("/")) {
                name = "/" + name;
            }
            img = Image.createImage(name);
        } catch (IOException ex) {
            //image is missing, nothing to do..
        }
        return img;
    }

}
