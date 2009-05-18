package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.NewsItem;
import javax.microedition.rms.RecordFilter;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class NewsItemSearchFilter implements RecordFilter {

    private String searchString;

    public NewsItemSearchFilter(String searchString) {
        this.searchString = searchString;
    }

    public boolean matches(byte[] candidate) {
        NewsItem candidateNewsItem = new NewsItem(candidate);
        if (candidateNewsItem.getTitle().indexOf(searchString) >= 0) {
            return true;
        } else {
            return false;
        }
    }

}
