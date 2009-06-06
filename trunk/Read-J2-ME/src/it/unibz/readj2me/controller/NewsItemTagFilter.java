package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.NewsItem;
import it.unibz.readj2me.model.Tag;
import java.util.Vector;
import javax.microedition.rms.RecordFilter;

/**
 * RecordFilter to filter NewsItems by tags.
 *
 * @author Anton Dignoes, Omar Moling
 */
public class NewsItemTagFilter implements RecordFilter {

    private Vector filterTags;

    /**
     * Constructor for one Tag.
     * @param tag
     */
    public NewsItemTagFilter(Tag tag){
        filterTags = new Vector();
        filterTags.addElement(tag);
    }

    /**
     * Constructor for a vector of Tags.
     * @param tags
     */
    public NewsItemTagFilter(Vector tags) {
        this.filterTags = tags;
    }

    public boolean matches(byte[] candidate) {
        NewsItem candidateNewsItem = new NewsItem(candidate);
        String nameCandidateTag, nameFilterTag;

        //check for each tag assigned to the newsitem if it matches one of the filter-tags
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
