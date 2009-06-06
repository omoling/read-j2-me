package it.unibz.readj2me.controller;

import it.unibz.readj2me.controller.comparators.TagComparator;
import it.unibz.readj2me.controller.comparators.NewsItemTitleComparator;
import it.unibz.readj2me.controller.comparators.NewsItemDateComparator;
import it.unibz.readj2me.controller.comparators.FeedComparator;
import it.unibz.readj2me.model.Configuration;
import it.unibz.readj2me.model.Constants;
import it.unibz.readj2me.model.Feed;
import it.unibz.readj2me.model.NewsItem;
import it.unibz.readj2me.model.Tag;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;

/**
 * Class responsible for all operations related to the local storage.
 *
 * @author Anton Dignoes, Omar Moling
 */
public class PersistentManager {

    public static PersistentManager persistentManager = null;

    private PersistentManager() {
    }

    /**
     * Singleton getInstance().
     * @return
     */
    public static PersistentManager getInstance() {
        if (persistentManager == null) {
            persistentManager = new PersistentManager();
        }
        return persistentManager;
    }

    /**
     * Searches a free RecordStore name
     * @return the free RecordStore name
     * @throws javax.microedition.rms.RecordStoreFullException
     * @throws javax.microedition.rms.RecordStoreException
     */
    public String getRandomRecordStoreName() throws RecordStoreFullException, RecordStoreException {
        int i;
        String name;

        Random generator = new Random();

        //find an unused name
        do {
            i = Math.abs(generator.nextInt()) % 10000;
            name = Constants.RS_PREFIX + "" + i;
        } while (!isNameFree(name));

        //"occupy" the name
        RecordStore rs = RecordStore.openRecordStore(name, true);
        rs.closeRecordStore();

        return name;
    }

    /**
     * Checks whether the given name is free
     * @param name to be chacked
     * @return true if free
     */
    private boolean isNameFree(String name) {
        String[] names = RecordStore.listRecordStores();
        if (names == null) {
            return true;
        }
        for (int i = 0; i < names.length; i++) {
            if (names[i] != null && names[i].equals(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Loads all Feeds.
     * @return a vector of Feeds
     * @throws javax.microedition.rms.RecordStoreException
     */
    public Vector loadFeeds() throws RecordStoreException {
        RecordStore rs = RecordStore.openRecordStore(Constants.FEED_RS_NAME, true);
        Vector items = new Vector();

        RecordEnumeration re = rs.enumerateRecords(null, new FeedComparator(), false);
        byte[] rawRecord;
        while (re.hasNextElement()) {
            rawRecord = re.nextRecord();
            items.addElement(new Feed(rawRecord));
        }

        rs.closeRecordStore();
        return items;
    }

    /**
     * Loads all Tags.
     * @return a vector of Tagss
     * @throws javax.microedition.rms.RecordStoreException
     */
    public Vector loadTags() throws RecordStoreException {
        RecordStore rs = RecordStore.openRecordStore(Constants.TAGS_RS_NAME, true);
        Vector items = new Vector();

        RecordEnumeration re = rs.enumerateRecords(null, new TagComparator(), false);
        byte[] rawRecord;
        while (re.hasNextElement()) {
            rawRecord = re.nextRecord();
            items.addElement(new Tag(rawRecord));
        }

        rs.closeRecordStore();
        return items;
    }

    /**
     * Loads NewsItems by given Filter and Comparator on title
     * @param rsName name of RecordStore
     * @param filter to be used
     * @param titleAscendling true for ascending order
     * @return a vector of NewsItems
     * @throws javax.microedition.rms.RecordStoreException
     */
    public Vector loadNewsItemsByTitle(String rsName, RecordFilter filter, boolean titleAscendling) throws RecordStoreException {
        return loadNewsItems(rsName, filter, new NewsItemTitleComparator(titleAscendling));
    }

    /**
     * Loads NewsItems by given Filter and Comparator on date
     * @param rsName name of RecordStore
     * @param filter to be used
     * @param dateAscending true for ascending order
     * @return a vector of NewsItems
     * @throws javax.microedition.rms.RecordStoreException
     */
    public Vector loadNewsItemsByDate(String rsName, RecordFilter filter, boolean dateAscending) throws RecordStoreException {
        return loadNewsItems(rsName, filter, new NewsItemDateComparator(dateAscending));
    }

    /**
     * Loads NewsItems
     * @param rsName name of RecordStore
     * @param filter to be used
     * @param comparator to be used
     * @return a vector of NewsItems
     * @throws javax.microedition.rms.RecordStoreException
     */
    private Vector loadNewsItems(String rsName, RecordFilter filter, RecordComparator comparator) throws RecordStoreException {
        RecordStore rs = RecordStore.openRecordStore(rsName, false);
        Vector items = new Vector();

        RecordEnumeration re = rs.enumerateRecords(filter, comparator, false);
        byte[] rawRecord;
        while (re.hasNextElement()) {
            rawRecord = re.nextRecord();
            items.addElement(new NewsItem(rawRecord));
        }

        rs.closeRecordStore();
        return items;
    }

    /**
     * Adds NewsItems to a Feed-NewsItem's RecordStore
     * @param feed the Feed the news belong to
     * @param items the items to be added
     * @throws javax.microedition.rms.RecordStoreFullException
     * @throws java.lang.Exception
     */
    public void addNewsItems(Feed feed, Vector items) throws RecordStoreFullException, Exception {
        RecordStore rs;
        rs = RecordStore.openRecordStore(feed.getItemsRecordStoreName(), true);
        boolean feedChanged = false;
        Enumeration enumeration = items.elements();
        NewsItem item;
        while (enumeration.hasMoreElements()) {
            item = (NewsItem) enumeration.nextElement();
            int nextId = rs.getNextRecordID();
            item.setRs_id(nextId);
            byte[] row = item.getBytes();
            rs.addRecord(row, 0, row.length);
            feed.getKnownIds().addElement(item.getId());
            feedChanged = true;
        }
        rs.closeRecordStore();
        if (feedChanged) {
            updateFeed(feed);
        }
    }

    /**
     * Updates the given NewsItem
     * @param item the item to be updated
     * @param rsName the name of the RecordStore
     * @throws javax.microedition.rms.RecordStoreFullException
     * @throws javax.microedition.rms.RecordStoreException
     */
    public void updateNewsItem(NewsItem item, String rsName) throws RecordStoreFullException, RecordStoreException {
        RecordStore rs = RecordStore.openRecordStore(rsName, false);
        byte[] row = item.getBytes();
        rs.setRecord(item.getRs_id(), row, 0, row.length);
    }

    /**
     * Updates the given Feed
     * @param feed the feed to be updated
     * @throws javax.microedition.rms.RecordStoreFullException
     * @throws javax.microedition.rms.RecordStoreException
     */
    public void updateFeed(Feed feed) throws RecordStoreFullException, RecordStoreException {
        RecordStore rs = RecordStore.openRecordStore(Constants.FEED_RS_NAME, false);
        byte[] row = feed.getBytes();
        rs.setRecord(feed.getRs_id(), row, 0, row.length);
    }

    /**
     * Adds a Tag to the predefined RecordStore
     * @param name the name of the tag
     * @throws javax.microedition.rms.RecordStoreFullException
     * @throws javax.microedition.rms.RecordStoreException
     */
    public void addTag(String name) throws RecordStoreFullException, RecordStoreException {
        RecordStore rs = RecordStore.openRecordStore(Constants.TAGS_RS_NAME, true);
        Tag newTag = new Tag(name, rs.getNextRecordID());
        byte[] row = newTag.getBytes();
        rs.addRecord(row, 0, row.length);
        rs.closeRecordStore();
    }

    /**
     * Adds a Feed to the predefined RecordStore
     * @param name the name of the feed
     * @param url the url of the feed
     * @throws javax.microedition.rms.RecordStoreFullException
     * @throws javax.microedition.rms.RecordStoreException
     */
    public void addFeed(String name, String url) throws RecordStoreFullException, RecordStoreException {
        String rsItemsName;
        rsItemsName = getRandomRecordStoreName();
        RecordStore rs = RecordStore.openRecordStore(Constants.FEED_RS_NAME, true);
        Feed newFeed = new Feed(url, name, rsItemsName, rs.getNextRecordID());
        byte[] row = newFeed.getBytes();
        rs.addRecord(row, 0, row.length);
        rs.closeRecordStore();
    }

    /**
     * Removes the given NewsItem from the given RecordStore
     * @param item the NewsItem
     * @param rsName the name of the RecordStore
     * @throws javax.microedition.rms.RecordStoreFullException
     * @throws javax.microedition.rms.RecordStoreException
     */
    public void removeNewsItem(NewsItem item, String rsName) throws RecordStoreFullException, RecordStoreException {
        RecordStore rs;
        rs = RecordStore.openRecordStore(rsName, false);
        rs.deleteRecord(item.getRs_id());
        rs.closeRecordStore();
    }

    /**
     * Removes the given Tag from the RecordStore
     * @param tag the name of the tag to be removed
     * @throws javax.microedition.rms.RecordStoreFullException
     * @throws javax.microedition.rms.RecordStoreException
     */
    public void removeTag(Tag tag) throws RecordStoreFullException, RecordStoreException {
        RecordStore rs;
        rs = RecordStore.openRecordStore(Constants.TAGS_RS_NAME, false);
        rs.deleteRecord(tag.getRs_id());
        rs.closeRecordStore();
    }

    /**
     * Removes a feed from the RecordStore
     * @param feed the feed to be removed
     * @throws javax.microedition.rms.RecordStoreFullException
     * @throws javax.microedition.rms.RecordStoreException
     */
    public void removeFeed(Feed feed) throws RecordStoreFullException, RecordStoreException {
        RecordStore rs;
        rs = RecordStore.openRecordStore(Constants.FEED_RS_NAME, false);
        rs.deleteRecord(feed.getRs_id());
        rs.closeRecordStore();
        //removes feed's NewsItems
        RecordStore.deleteRecordStore(feed.getItemsRecordStoreName());
    }

    /**
     * Reads the Configuration RecordStore
     * @return a byte-array of the record
     * @throws javax.microedition.rms.RecordStoreException
     */
    public byte[] loadConfiguration() throws RecordStoreException {
        RecordStore rs = RecordStore.openRecordStore(Constants.CONFIG_RS_NAME, false);
        RecordEnumeration re = rs.enumerateRecords(null, null, false);
        byte[] rawRecord = null;
        while (re.hasNextElement()) {
            rawRecord = re.nextRecord();
            break;
        }
        rs.closeRecordStore();
        return rawRecord;
    }

    /**
     * Adds the configuration to the predefined RecordStore
     * @throws javax.microedition.rms.RecordStoreFullException
     * @throws javax.microedition.rms.RecordStoreException
     */
    public void addConfiguration() throws RecordStoreFullException, RecordStoreException {
        RecordStore rs = RecordStore.openRecordStore(Constants.CONFIG_RS_NAME, true);

        //delete all records if any
        RecordEnumeration re = rs.enumerateRecords(null, null, false);
        while (re.hasNextElement()) {
            rs.deleteRecord(re.nextRecordId());
        }

        //write new configuration
        byte[] row = Configuration.getInstance().getBytes();
        rs.addRecord(row, 0, row.length);
        rs.closeRecordStore();
    }
    
}
