package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.Feed;
import javax.microedition.rms.RecordComparator;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class FeedComparator implements RecordComparator {

    public int compare(byte[] rec1, byte[] rec2) {
        Feed feed1 = new Feed(rec1);
        Feed feed2 = new Feed(rec2);

        String name1 = feed1.getName();
        String name2 = feed2.getName();

        feed1 = null;
        feed2 = null;
        
        if(name1.compareTo(name2) < 0) {
            return RecordComparator.PRECEDES;
        } else if (name1.compareTo(name2) > 0) {
            return RecordComparator.FOLLOWS;
        } else {
            return RecordComparator.EQUIVALENT;
        }
    }

}
