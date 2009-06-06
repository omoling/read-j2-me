package it.unibz.readj2me.controller.comparators;

import it.unibz.readj2me.model.Tag;
import javax.microedition.rms.RecordComparator;

/**
 * Comparator to compare Tags
 *
 * @author Anton Dignoes, Omar Moling
 */
public class TagComparator implements RecordComparator {

    public int compare(byte[] rec1, byte[] rec2) {
        Tag tag1 = new Tag(rec1);
        Tag tag2 = new Tag(rec2);

        String name1 = tag1.getName();
        String name2 = tag2.getName();

        tag1 = null;
        tag2 = null;

        if(name1.compareTo(name2) < 0) {
            return RecordComparator.PRECEDES;
        } else if (name1.compareTo(name2) > 0) {
            return RecordComparator.FOLLOWS;
        } else {
            return RecordComparator.EQUIVALENT;
        }
    }

}
