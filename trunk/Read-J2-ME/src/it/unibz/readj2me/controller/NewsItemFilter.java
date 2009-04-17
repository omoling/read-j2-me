package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.NewsItem;
import javax.microedition.rms.RecordFilter;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class NewsItemFilter implements RecordFilter {

    String id;

    public NewsItemFilter(String id){
        this.id = id;
    }

    public boolean matches(byte[] candidate) {
        NewsItem item = new NewsItem(candidate);
        if(item.getId().equals(id)){
            return true;
        } else {
            return false;
        }
    }

}
