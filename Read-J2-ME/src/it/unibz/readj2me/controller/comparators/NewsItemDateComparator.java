package it.unibz.readj2me.controller.comparators;

import it.unibz.readj2me.model.NewsItem;
import javax.microedition.rms.RecordComparator;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class NewsItemDateComparator implements RecordComparator {

    private boolean ascending;

    public NewsItemDateComparator(boolean ascending) {
        this.ascending = ascending;
    }

    public int compare(byte[] rec1, byte[] rec2) {
        NewsItem n1 = new NewsItem(rec1);
        NewsItem n2 = new NewsItem(rec2);

        //                                                 0                 18    ...
        //                                                 |                 |     |
        //18 is the index of the last seconds-value (i.e.: 2009-02-12T15:04:38+01:00)
        String date1 = n1.getUpdated().substring(0, 18);
        String date2 = n2.getUpdated().substring(0, 18);

        n1 = null;
        n2 = null;

        if (ascending) {
            if (date1.compareTo(date2) > 0) {
                return RecordComparator.FOLLOWS;
            } else if (date1.compareTo(date2) < 0) {
                return RecordComparator.PRECEDES;
            } else {
                return RecordComparator.EQUIVALENT;
            }
        } else {
            if (date1.compareTo(date2) > 0) {
                return RecordComparator.PRECEDES;
            } else if (date1.compareTo(date2) < 0) {
                return RecordComparator.FOLLOWS;
            } else {
                return RecordComparator.EQUIVALENT;
            }
        }
    }
}
