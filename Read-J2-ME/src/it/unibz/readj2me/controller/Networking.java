package it.unibz.readj2me.controller;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class Networking {

    public Networking(){

    }

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

        if (conn.getResponseCode() == HttpConnection.HTTP_OK) {
            return conn.openInputStream();
        } else {
            throw new IOException("" + conn.getResponseCode());
        }
    }

}
