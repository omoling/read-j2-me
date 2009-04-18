package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.Constants;
import it.unibz.readj2me.model.Feed;
import it.unibz.readj2me.model.NewsItem;
import it.unibz.readj2me.view.Warning;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
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

    public Vector loadFeeds() throws RecordStoreException, Exception {
        RecordStore rs = RecordStore.openRecordStore(Constants.FEED_RS_NAME, false);
        Vector items = new Vector();

        RecordEnumeration re = rs.enumerateRecords(null, new FeedComparator(), false);
        byte[] rawRecord;
        while(re.hasNextElement()){
            rawRecord = re.nextRecord();
            items.addElement(new Feed(rawRecord));
            //TODO handle parsing exception.. delete record?
        }

        rs.closeRecordStore();
        return items;
    }

    public Vector loadNewsItems(String rsName) throws RecordStoreException, Exception {

        RecordStore rs = RecordStore.openRecordStore(rsName, false);
        Vector items = new Vector();

        RecordEnumeration re = rs.enumerateRecords(null, new NewsItemComparator(), false);
        byte[] rawRecord;
        while(re.hasNextElement()) {
            rawRecord = re.nextRecord();
            items.addElement(new NewsItem(rawRecord));
        }

        rs.closeRecordStore();
        return items;
    }

    public void addNewsItems(Feed feed, Vector items) {
        RecordStore rs;
        try {
            rs = RecordStore.openRecordStore(feed.getItemsRecordStoreName(), true);

            boolean feedChanged = false;

            Enumeration enumeration = items.elements();
            NewsItem item;
            while(enumeration.hasMoreElements()){
                item = (NewsItem)enumeration.nextElement();
                if(!feed.getKnownIds().contains(item.getId())){
                    byte[] row = item.getBytes();
                    rs.addRecord(row, 0, row.length);
                    feed.getKnownIds().addElement(item.getId());
                    feedChanged = true;
                }
            }
            rs.closeRecordStore();

            if(feedChanged){
                updateFeed(feed);
            }

        //TODO
        } catch (RecordStoreFullException ex) {
            ex.printStackTrace();
        } catch (RecordStoreNotFoundException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateNewsItem(NewsItem item, String rsName){
        removeNewsItem(item, rsName);
        addNewsItem(item, rsName);
    }

    public void updateFeed(Feed feed){
        //update feed on RS (delete and rewrite it)
        removeFeed(feed, false);
        addFeed(feed);
    }

    public void addFeed(String name, String url) {
        String rsItemsName;
        try {
            rsItemsName = getRandomRecordStoreName();
            RecordStore rs = RecordStore.openRecordStore(Constants.FEED_RS_NAME, true);
            Feed newFeed = new Feed(url, name, rsItemsName);
            byte[] row = newFeed.getBytes();
            rs.addRecord(row, 0, row.length);
            rs.closeRecordStore();
        } catch (RecordStoreFullException ex) {
            ex.printStackTrace();
            new Warning("adding feed", "1: " + ex.toString()).show();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
            new Warning("adding feed", "2: " + ex.toString()).show();
        } catch (Throwable t) {
            new Warning("adding feed", "3: " + t.toString()).show();
        }

    }

    public void addFeed(Feed feed) {
        try {
            RecordStore rs = RecordStore.openRecordStore(Constants.FEED_RS_NAME, true);
            byte[] row = feed.getBytes();
            rs.addRecord(row, 0, row.length);
            rs.closeRecordStore();
        } catch (RecordStoreFullException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    public void addNewsItem(NewsItem item, String rsName){
        try {
            RecordStore rs = RecordStore.openRecordStore(rsName, false);
            byte[] row = item.getBytes();
            rs.addRecord(row, 0, row.length);
            rs.closeRecordStore();
        } catch (RecordStoreFullException ex) {
            ex.printStackTrace();
        } catch (RecordStoreNotFoundException ex) {
            //nothing
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
    }

    public void removeNewsItem(NewsItem item, String rsName){
        RecordStore rs;
        try{
            rs = RecordStore.openRecordStore(rsName, false);
            RecordEnumeration re = rs.enumerateRecords(new NewsItemFilter(item.getId()), null, false);

            while (re.hasNextElement()) {
                rs.deleteRecord(re.nextRecordId());
            }

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

    public void removeFeed(Feed feed, boolean removeItemsRecordStore) {
        RecordStore rs;
        try {
            rs = RecordStore.openRecordStore(Constants.FEED_RS_NAME, false);
            //find the one record
            RecordEnumeration re = rs.enumerateRecords(new FeedFilter(feed.getUrl()), null, false);

            while (re.hasNextElement()) {
                rs.deleteRecord(re.nextRecordId());
            }

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


