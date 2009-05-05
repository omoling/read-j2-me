package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.NewsItem;
import it.unibz.readj2me.model.Tag;
import java.util.Vector;
import javax.microedition.rms.RecordFilter;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class NewsItemTagFilter implements RecordFilter {

    //private Tag filterTag;
    private Vector filterTags;

    public NewsItemTagFilter(Tag tag){
        //this.filterTag = tag;
        filterTags = new Vector();
        filterTags.addElement(tag);
    }

    public NewsItemTagFilter(Vector tags) {
        this.filterTags = tags;
    }

    public boolean matches(byte[] candidate) {
        NewsItem candidateNewsItem = new NewsItem(candidate);
        //String nameFilter = filterTag.getName();
        String nameCandidateTag, nameFilterTag;
        for (int i = 0; i < candidateNewsItem.getTags().size(); i++) {
            nameCandidateTag = ((Tag) candidateNewsItem.getTags().elementAt(i)).getName();
            if (filterTags != null && filterTags.size() > 0) {
                for (int j = 0; j < filterTags.size(); j++) {
                    nameFilterTag = ((Tag) filterTags.elementAt(j)).getName();
                    if (nameCandidateTag.equals(nameFilterTag)) {
                        return true;
                    }
                } 
            } else {
                return true;
            }
        }
        return false;
    }

}
