package it.unibz.readj2me.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import org.kxml2.io.*;
import org.xmlpull.v1.*;

public class XmlReader {

    private final String XML_NAME = "/sampleatom.xml";
    private final String ENCODING = "utf-8";

    private InputStream getXMLInputStream() {
        return this.getClass().getResourceAsStream(XML_NAME);
    }

    public Vector getEntries() {

        Vector entries = new Vector();
        KXmlParser xmlParser = new KXmlParser();
        InputStream in = getXMLInputStream();

        if (in != null) {
            try {

                xmlParser.setInput(in, ENCODING);

                while (true) {
                    int event = xmlParser.next();

                    if (event == XmlPullParser.END_DOCUMENT) {

                        System.out.println("XML read!");

                        break;
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
}
