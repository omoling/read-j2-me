package it.unibz.readj2me.controller;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;

/**
 * Class responsible for establishing http and https connections.
 *
 * @author Anton Dignoes, Omar Moling
 */
public class Networking {

    public Networking(){
    }

    /**
     * Gets an inputStream from the specified URL by first saving
     * the data in memory
     * @param url the URL to retrieve the data from
     * @return the data in form of an InputStream
     * @throws java.io.IOException
     */
    public InputStream getInputStream(String url) throws IOException {
        HttpConnection conn;
        if (url.startsWith("https")) {
            conn = (HttpsConnection) Connector.open(url);
        } else {
            conn = (HttpConnection) Connector.open(url);
        }
        conn.setRequestMethod(HttpConnection.GET);
        conn.setRequestProperty("Content-Type", "//text plain");
        conn.setRequestProperty("Connection", "close");
        DataInputStream is;

        if (conn.getResponseCode() != HttpConnection.HTTP_OK) {
            throw new IOException("" + conn.getResponseCode());
        }

        //
        int length = (int)conn.getLength();

        byte[] content = null;
        is = new DataInputStream(conn.openInputStream());

        if(length != -1) {
            content = new byte[length];
            is.readFully(content);
        } else {
            int chunkSize = 512;
            int index = 0;
            int readLength = 0;
            content = new byte[chunkSize];
            do {
                if (content.length < index + chunkSize) {
                    byte[] newContent = new byte[index + chunkSize];
                    System.arraycopy(content, 0, newContent, 0, content.length);
                    content = newContent;
                }
                readLength = is.read(content, index, chunkSize);
                index += readLength;
            } while (readLength != -1);
            length = index;
        }

        //check for initial space character
        String tempContent = new String(content);
        int index1 = tempContent.indexOf("<");
        if (index1 != -1 && index1 != 0) {
            tempContent = tempContent.substring(index1, tempContent.length());
        }

        return new ByteArrayInputStream(tempContent.getBytes());
    }

}
