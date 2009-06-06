package it.unibz.readj2me.controller;

import java.io.IOException;
import javax.microedition.lcdui.Image;

/**
 * Utility class to load images.
 *
 * @author Anton Dignoes, Omar Moling
 */
public class ImageLoader {

    /**
     * Loads an image with specified name
     * @param name the name of the image
     * @return the image object
     */
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
