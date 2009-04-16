package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.NewsItem;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import org.kxml2.io.*;
import org.xmlpull.v1.*;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class XmlReader {

    private final static String XML_NAME = "/sampleatom.xml";
    private final static String XML_URL = "http://www.heise.de/mobil/newsticker/heise-atom.xml";
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

    private XmlReader(){
        networking = new Networking();
    }

    public static XmlReader getInstance(){
        if (xmlReader == null){
            xmlReader = new XmlReader();
        }
        return xmlReader;
    }

    public Vector getEntries(String feedUrl) {

        Vector entries = new Vector();
        KXmlParser xmlParser = new KXmlParser();
        InputStream in = networking.getInputStream(feedUrl);

        if (in != null) {
            try {

                xmlParser.setInput(in, ENCODING);
                int event;
                while ((event = xmlParser.next()) != XmlPullParser.END_DOCUMENT) {

                    if (event == XmlPullParser.START_TAG && xmlParser.getName().equals(TAG_ENTRY)) {
                        NewsItem item;
                        item = populateItem(xmlParser);
                        entries.addElement(item);
                    }

                }

            } catch (XmlPullParserException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return entries;
    }

    private NewsItem populateItem(KXmlParser xmlParser) throws XmlPullParserException, IOException {
        NewsItem item = new NewsItem();

        while (true) {
            int event = xmlParser.next();

            if (event == XmlPullParser.END_DOCUMENT ||
                    (event == XmlPullParser.END_TAG && xmlParser.getName().equals(TAG_ENTRY))) {
                break;
            }

            if (event == XmlPullParser.START_TAG) {
                if (xmlParser.getName().equals(TAG_ID)) {
                    String id = xmlParser.nextText();
                    item.setId(id);
                } else if (xmlParser.getName().equals(TAG_TITLE)) {
                    String title = xmlParser.nextText();
                    item.setTitle(title);
                } else if (xmlParser.getName().equals(TAG_PUBLISHED)) {
                    String published = xmlParser.nextText();
                    item.setPublished(published);
                } else if (xmlParser.getName().equals(TAG_UPDATED)) {
                    String updated = xmlParser.nextText();
                    item.setUpdated(updated);
                } else if(xmlParser.getName().equals(TAG_LINK)){
                    item.setLink(xmlParser.getAttributeValue(null, "href"));
                } else if(xmlParser.getName().equals(TAG_CONTENT)){
                    String content = xmlParser.nextText();
                    item.setContent(content);
                } else if(xmlParser.getName().equals(TAG_SUMMARY)){
                    String summary = xmlParser.nextText();
                    item.setSummary(summary);
                }
            }

        }

        return item;
    }
}
