package it.unibz.readj2me.model;

/**
 * Holds static data like image names, RecordStore names..
 *
 * @author Anton Dignoes, Omar Moling
 */
public class Constants {

    // RECORD STORE
    public static final char FIELD_SEPARATOR = '|';
    public static final char VECTOR_SEPARATOR = '*';
    public static final String RS_PREFIX = "rj2me_";
    public static final String FEED_RS_NAME = RS_PREFIX + "feeds";
    public static final String TAGS_RS_NAME = RS_PREFIX + "tags";
    public static final String CONFIG_RS_NAME = RS_PREFIX + "config";

    //IMAGES
    public static final String IMG_ICON = "/icon.png";
    public static final String IMG_DEFAULT_FEED = "/feed.png";
    public static final String IMG_GREY_FEED = "/feed_grey.png";
    public static final String IMG_DEFAULT_FEED_L = "/feed_l.png";
    public static final String IMG_GREY_FEED_L = "/feed_grey_l.png";
    public static final String IMG_TAG = "/tag_12.png";
    public static final String IMG_ERROR = "/error.png";
    public static final String IMG_WARNING = "/warning.png";

}
