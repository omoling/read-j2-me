package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.NewsItem;
import it.unibz.readj2me.model.Tag;
import javax.microedition.rms.RecordFilter;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class NewsItemTagFilter implements RecordFilter {

    Tag filterTag;

    public NewsItemTagFilter(Tag tag){
        this.filterTag = tag;
    }

    public boolean matches(byte[] candidate) {
        NewsItem item = new NewsItem(candidate);
        String nameFilter = filterTag.getName();
        String nameTag;
        for (int i = 0; i < item.getTags().size(); i++) {
            nameTag = ((Tag) item.getTags().elementAt(i)).getName();
            if(nameTag.equals(nameFilter)) {
                return true;
            }
        }
        return false;
    }

}
