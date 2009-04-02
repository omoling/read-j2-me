package it.unibz.readj2me.controller;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class Networking {

    public Networking(){

    }

    //TODO review!!!
    public InputStream getInputStream(String url){
        try {
            HttpConnection conn = (HttpConnection) Connector.open(url);
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
    }

}
