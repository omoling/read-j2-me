package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.Constants;
import it.unibz.readj2me.model.Feed;
import it.unibz.readj2me.model.NewsItem;
import it.unibz.readj2me.model.Tag;
import it.unibz.readj2me.view.WarningAlert;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class PersistentManager {

    public static PersistentManager persistentManager = null;

    private PersistentManager(){
    }

    public static PersistentManager getInstance(){
        if(persistentManager == null){
            persistentManager = new PersistentManager();
        }
        return persistentManager;
    }

    public String getRandomRecordStoreName() throws RecordStoreFullException, RecordStoreException {
        int i;
        String name;

        Random generator = new Random();
        
        //find an unused name
        do {
            i = Math.abs(generator.nextInt()) % 10000;
            name = Constants.RS_PREFIX + "" + i;
        } while(!isNameFree(name));

        //"occupy" the name
        RecordStore rs = RecordStore.openRecordStore(name, true);
        rs.closeRecordStore();

        return name;
    }

    private boolean isNameFree(String name) {
        String[] names = RecordStore.listRecordStores();
        if(names == null) { 
            return true;
        }
        for(int i = 0; i < names.length; i++){
            if (names[i] != null && names[i].equals(name)){
                return false;
            }
        }
        return true;
    }

    public Vector loadFeeds() throws RecordStoreException {
        RecordStore rs = RecordStore.openRecordStore(Constants.FEED_RS_NAME, true);
        Vector items = new Vector();

        RecordEnumeration re = rs.enumerateRecords(null, new FeedComparator(), false);
        byte[] rawRecord;
        while(re.hasNextElement()){
            rawRecord = re.nextRecord();
            items.addElement(new Feed(rawRecord));
        }

        rs.closeRecordStore();
        return items;
    }

    public Vector loadTags() throws RecordStoreException {
        RecordStore rs = RecordStore.openRecordStore(Constants.TAGS_RS_NAME, true);
        Vector items = new Vector();

        RecordEnumeration re = rs.enumerateRecords(null, new TagComparator(), false);
        byte[] rawRecord;
        while(re.hasNextElement()) {
            rawRecord = re.nextRecord();
            items.addElement(new Tag(rawRecord));
        }

        rs.closeRecordStore();
        return items;
    }

    public Vector loadNewsItems(String rsName, RecordFilter filter) throws RecordStoreException {
        RecordStore rs = RecordStore.openRecordStore(rsName, false);
        Vector items = new Vector();

        RecordEnumeration re = rs.enumerateRecords(filter, new NewsItemComparator(), false);
        byte[] rawRecord;
        while(re.hasNextElement()) {
            rawRecord = re.nextRecord();
            items.addElement(new NewsItem(rawRecord));
        }

        rs.closeRecordStore();
        return items;
    }

    public Vector loadNewsItems(String rsName) throws RecordStoreException {
        return loadNewsItems(rsName, null);
    }

    public void addNewsItems(Feed feed, Vector items) throws RecordStoreFullException, Exception {
        RecordStore rs;
        rs = RecordStore.openRecordStore(feed.getItemsRecordStoreName(), true);
        boolean feedChanged = false;
        Enumeration enumeration = items.elements();
        NewsItem item;
        while (enumeration.hasMoreElements()) {
            item = (NewsItem) enumeration.nextElement();
            if (!feed.getKnownIds().contains(item.getId())) {
                int nextId = rs.getNextRecordID();
                item.setRs_id(nextId);
                byte[] row = item.getBytes();
                rs.addRecord(row, 0, row.length);
                feed.getKnownIds().addElement(item.getId());
                feedChanged = true;
            }
        }
        rs.closeRecordStore();
        if (feedChanged) {
            updateFeed(feed);
        }
    }

    public void updateNewsItem(NewsItem item, String rsName){
        try {
            RecordStore rs = RecordStore.openRecordStore(rsName, false);
            byte[] row = item.getBytes();
            rs.setRecord(item.getRs_id(), row, 0, row.length);
        } catch (RecordStoreFullException ex) {
            ex.printStackTrace();
        } catch (RecordStoreNotFoundException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        } 
    }

    public void updateFeed(Feed feed){
        //update feed on RS (delete and rewrite it)
        //removeFeed(feed, false);
        //addFeed(feed);
        try {
            RecordStore rs = RecordStore.openRecordStore(Constants.FEED_RS_NAME, false);
            byte[] row = feed.getBytes();
            rs.setRecord(feed.getRs_id(), row, 0, row.length);
        } catch (RecordStoreFullException ex) {
            ex.printStackTrace();
        } catch (RecordStoreNotFoundException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    public void addTag(String name) {
        try {
            RecordStore rs = RecordStore.openRecordStore(Constants.TAGS_RS_NAME, true);
            Tag newTag = new Tag(name, rs.getNextRecordID());
            byte[] row = newTag.getBytes();
            rs.addRecord(row, 0, row.length);
            rs.closeRecordStore();
        } catch (RecordStoreFullException ex) {
            ex.printStackTrace();
            new WarningAlert("adding tag", "1: " + ex.toString()).show();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
            new WarningAlert("adding tag", "2: " + ex.toString()).show();
        } catch (Throwable t) {
            new WarningAlert("adding tag", "3: " + t.toString()).show();
        }
    }

    public void addFeed(String name, String url) {
        String rsItemsName;
        try {
            rsItemsName = getRandomRecordStoreName();
            RecordStore rs = RecordStore.openRecordStore(Constants.FEED_RS_NAME, true);
            Feed newFeed = new Feed(url, name, rsItemsName, rs.getNextRecordID());
            byte[] row = newFeed.getBytes();
            rs.addRecord(row, 0, row.length);
            rs.closeRecordStore();
        } catch (RecordStoreFullException ex) {
            ex.printStackTrace();
            new WarningAlert("adding feed", "1: " + ex.toString()).show();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
            new WarningAlert("adding feed", "2: " + ex.toString()).show();
        } catch (Throwable t) {
            new WarningAlert("adding feed", "3: " + t.toString()).show();
        }

    }

    public void removeNewsItem(NewsItem item, String rsName){
        RecordStore rs;
        try{
            rs = RecordStore.openRecordStore(rsName, false);
            rs.deleteRecord(item.getRs_id());
            rs.closeRecordStore();
        } catch (RecordStoreFullException ex) {
            ex.printStackTrace();
            return;
        } catch (RecordStoreNotFoundException ex) {
            // nothing..
            return;
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    public void removeTag(Tag tag) {
        RecordStore rs;
        try {
            rs = RecordStore.openRecordStore(Constants.TAGS_RS_NAME, false);
            rs.deleteRecord(tag.getRs_id());
            rs.closeRecordStore();
        } catch (RecordStoreFullException ex) {
            ex.printStackTrace();
            return;
        } catch (RecordStoreNotFoundException ex) {
            ex.printStackTrace();
            return;
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    public void removeFeed(Feed feed, boolean removeItemsRecordStore) {
        RecordStore rs;
        try {
            rs = RecordStore.openRecordStore(Constants.FEED_RS_NAME, false);
            rs.deleteRecord(feed.getRs_id());
            rs.closeRecordStore();
            if(removeItemsRecordStore){
                RecordStore.deleteRecordStore(feed.getItemsRecordStoreName());
            }
            
        } catch (RecordStoreFullException ex) {
            ex.printStackTrace();
            return;
        } catch (RecordStoreNotFoundException ex) {
            ex.printStackTrace();
            return;
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }


    // ****************

    //testing
    public void eraseRS(){
        String[] stores = RecordStore.listRecordStores();
        for(int i = 0; i < stores.length; i++){
            try {
                if(stores[i].startsWith(Constants.RS_PREFIX)){
                    System.out.println("going to remove: " + stores[i]);
                    RecordStore.deleteRecordStore(stores[i]);
                    System.out.println("removed        : " + stores[i]);
                }
            } catch (RecordStoreNotFoundException ex) {
                ex.printStackTrace();
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            } 
        }
    }

    //testing
    public void listRS(){
        String[] stores = RecordStore.listRecordStores();
        for(int i = 0; i < stores.length; i++){
            System.out.println(stores[i]);
        }
    }

}
