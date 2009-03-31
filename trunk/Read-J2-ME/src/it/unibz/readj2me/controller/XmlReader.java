package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.NewsItem;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import org.kxml2.io.*;
import org.xmlpull.v1.*;

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

    private InputStream getXMLInputStream() {
        
        try {
            HttpConnection conn = (HttpConnection) Connector.open(XML_URL);
            conn.setRequestMethod(HttpConnection.GET);
            conn.setRequestProperty("Content-Type", "//text plain");
            conn.setRequestProperty("Connection", "close");

            if(conn.getResponseCode() == HttpConnection.HTTP_OK){
                return conn.openInputStream();
            } else return null;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        //return this.getClass().getResourceAsStream(XML_NAME);
    }

    public Vector getEntries() {

        Vector entries = new Vector();
        KXmlParser xmlParser = new KXmlParser();
        InputStream in = getXMLInputStream();

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
                }
                //TODO: get summary, published-date and updated-date
                
                  else if(xmlParser.getName().equals(TAG_LINK)){
                    item.setLink(xmlParser.getAttributeValue(null, "href"));
                } else if(xmlParser.getName().equals(TAG_CONTENT)){
                    String content = xmlParser.nextText();
                    item.setContent(content);
                }
            }

        }

        return item;
    }
}
