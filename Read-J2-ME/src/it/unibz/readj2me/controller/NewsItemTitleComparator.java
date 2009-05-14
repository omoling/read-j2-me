package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.NewsItem;
import javax.microedition.rms.RecordComparator;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class NewsItemTitleComparator implements RecordComparator {

    private boolean ascending;

    public NewsItemTitleComparator(boolean ascendling) {
        this.ascending = ascendling;
    }

    public int compare(byte[] rec1, byte[] rec2) {
        NewsItem n1 = new NewsItem(rec1);
        NewsItem n2 = new NewsItem(rec2);

        String title1 = n1.getTitle();
        String title2 = n2.getTitle();

        n1 = null;
        n2 = null;

        if (ascending) {
            if (title1.toLowerCase().compareTo(title2.toLowerCase()) > 0) {
                return RecordComparator.FOLLOWS;
            } else if (title1.toLowerCase().compareTo(title2.toLowerCase()) < 0) {
                return RecordComparator.PRECEDES;
            } else {
                return RecordComparator.EQUIVALENT;
            }
        } else {
            if (title1.toLowerCase().compareTo(title2.toLowerCase()) > 0) {
                return RecordComparator.PRECEDES;
            } else if (title1.toLowerCase().compareTo(title2.toLowerCase()) < 0) {
                return RecordComparator.FOLLOWS;
            } else {
                return RecordComparator.EQUIVALENT;
            }
        }

    }

}
