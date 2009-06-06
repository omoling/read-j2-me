package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.NewsItem;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import org.kxml2.io.*;
import org.xmlpull.v1.*;

/**
 * Class responsible for parsing the Atom-XML data.
 *
 * @author Anton Dignoes, Omar Moling
 */
public class XmlReader {

    private final static String ENCODING = "utf-8";
    private final static String TAG_ENTRY = "entry";
    private final static String TAG_ID = "id";
    private final static String TAG_TITLE = "title";
    private final static String TAG_PUBLISHED = "published";
    private final static String TAG_UPDATED = "updated";
    private final static String TAG_LINK = "link";
    private final static String TAG_CONTENT = "content";
    private final static String TAG_SUMMARY = "summary";
    private Networking networking;
    private static XmlReader xmlReader = null;

    private XmlReader() {
        networking = new Networking();
    }

    /**
     * Singleton: to get the one instance
     * @return the only instance
     */
    public static XmlReader getInstance() {
        if (xmlReader == null) {
            xmlReader = new XmlReader();
        }
        return xmlReader;
    }

    /**
     * Gets all news entries contained in the given url
     * @param feedUrl the url fo the feed
     * @return a vector of NewsItems
     * @throws java.io.IOException
     * @throws org.xmlpull.v1.XmlPullParserException
     */
    public Vector getEntries(String feedUrl) throws IOException, XmlPullParserException {

        Vector entries = new Vector();
        KXmlParser xmlParser = new KXmlParser();
        InputStream in = networking.getInputStream(feedUrl);

        if (in != null) {

            xmlParser.setInput(in, ENCODING);
            int event;
            while ((event = xmlParser.next()) != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG && xmlParser.getName().equals(TAG_ENTRY)) {
                    NewsItem item;
                    item = populateItem(xmlParser);

                    entries.addElement(item);
                }
            }

        }

        return entries;
    }

    /**
     * Parses the <entry> tag
     * @param xmlParser
     * @return
     * @throws org.xmlpull.v1.XmlPullParserException
     * @throws java.io.IOException
     */
    private NewsItem populateItem(KXmlParser xmlParser) throws XmlPullParserException, IOException {
        NewsItem item = new NewsItem();

        while (true) {
            int event = xmlParser.next();

            if (event == XmlPullParser.END_DOCUMENT ||
                    (event == XmlPullParser.END_TAG && xmlParser.getName().equals(TAG_ENTRY))) {
                break;
            }

            if (event == XmlPullParser.START_TAG) {
                String name = xmlParser.getName();
                if (name.equals(TAG_ID)) {
                    String id = xmlParser.nextText();
                    item.setId(id);
                } else if (name.equals(TAG_TITLE)) {
                    String title = xmlParser.nextText();
                    item.setTitle(title);
                } else if (name.equals(TAG_PUBLISHED)) {
                    String published = xmlParser.nextText();
                    item.setPublished(published);
                } else if (name.equals(TAG_UPDATED)) {
                    String updated = xmlParser.nextText();
                    item.setUpdated(updated);
                } else if (name.equals(TAG_LINK)) {
                    item.setLink(xmlParser.getAttributeValue(null, "href"));
                } else if (name.equals(TAG_CONTENT)) {
                    String content = xmlParser.nextText();
                    item.setContent(content);
                } else if (name.equals(TAG_SUMMARY)) {
                    String summary = xmlParser.nextText();
                    item.setSummary(summary);
                }
            }

        }

        return item;
    }
}
