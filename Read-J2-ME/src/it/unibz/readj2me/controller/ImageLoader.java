package it.unibz.readj2me.controller;

import java.io.IOException;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class ImageLoader {

    public static final String DEFAULT_FEED = "/feed.png";
    private Display display;

    public static Image getImage(String name) {
        Image img = null;
        try {
            if (!name.startsWith("/")) {
                name = "/" + name;
            }
            img = Image.createImage(name);
            //img = Image.createImage(img, 0, 0, 16, 16, Sprite.TRANS_NONE);
        } catch (IOException ex) {
            // TODO: do something!!
            ex.printStackTrace();
        }
        return img;
    }

    public static Image getResizedImage(String name, int width, int height) {
        //return resizeImage(getImage(name), width, height);
        return createThumbnail(getImage(name));
    }

    private static Image createThumbnail(Image image) {
        int sourceWidth = image.getWidth();
        int sourceHeight = image.getHeight();

        int thumbWidth = 16;
        //int thumbHeight = -1;
        int thumbHeight = 16;

        if (thumbHeight == -1) {
            thumbHeight = thumbWidth * sourceHeight / sourceWidth;
        }

        Image thumb = Image.createImage(thumbWidth, thumbHeight);
        Graphics g = thumb.getGraphics();

        for (int y = 0; y < thumbHeight; y++) {
            for (int x = 0; x < thumbWidth; x++) {
                g.setClip(x, y, 1, 1);
                int dx = x * sourceWidth / thumbWidth;
                int dy = y * sourceHeight / thumbHeight;
                g.drawImage(image, x - dx, y - dy, Graphics.LEFT | Graphics.TOP);
            }
        }

        Image immutableThumb = Image.createImage(thumb);

        return immutableThumb;
    }

    private static Image resizeImage(Image src, int destWidth, int destHeight) {

        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        Image tmp = Image.createImage(destWidth, srcHeight);
        Graphics g = tmp.getGraphics();
        int ratio = (srcWidth << 16) / destWidth;
        int pos = ratio / 2;

        //Horizontal Resize

        for (int x = 0; x < destWidth; x++) {
            g.setClip(x, 0, 1, srcHeight);
            g.drawImage(src, x - (pos >> 16), 0, Graphics.LEFT | Graphics.TOP);
            pos += ratio;
        }

        Image resizedImage = Image.createImage(destWidth, destHeight);
        g = resizedImage.getGraphics();
        ratio = (srcHeight << 16) / destHeight;
        pos = ratio / 2;

        //Vertical resize

        for (int y = 0; y < destHeight; y++) {
            g.setClip(0, y, destWidth, 1);
            g.drawImage(tmp, 0, y - (pos >> 16), Graphics.LEFT | Graphics.TOP);
            pos += ratio;
        }
        return resizedImage;

    }//resize image
}
