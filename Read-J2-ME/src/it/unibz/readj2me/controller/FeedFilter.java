package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.Feed;
import javax.microedition.rms.RecordFilter;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class FeedFilter implements RecordFilter {

    private String urlToCheck;

    public FeedFilter(String url) {
        this.urlToCheck = url;
    }

    public boolean matches(byte[] candidate) {
        Feed feed = new Feed(candidate);
        if (feed.getUrl().equals(urlToCheck)) {
            return true;
        } else {
            return false;
        }
    }
}
