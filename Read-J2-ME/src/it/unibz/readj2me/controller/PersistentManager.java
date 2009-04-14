package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.Constants;
import it.unibz.readj2me.model.Feed;
import it.unibz.readj2me.model.NewsItem;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;

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
        Random generator = new Random();
        
        int i;
        String name;

        //find an unused name
        do {
            i = generator.nextInt(1000000);
            name = Constants.RS_PREFIX + "" + i;
        } while(!isNameFree(name));

        //"occupy" the name
        RecordStore rs = RecordStore.openRecordStore(name, true);
        rs.closeRecordStore();

        return name;
    }

    private boolean isNameFree(String name) {
        String[] names = RecordStore.listRecordStores();
        for(int i = 0; i < names.length; i++){
            if (names[i].equals(name)){
                return false;
            }
        }
        return true;
    }

    public Vector loadFeeds() throws RecordStoreException, Exception {
        RecordStore rs = RecordStore.openRecordStore(Constants.FEED_RS_NAME, false);
        Vector items = new Vector();

        RecordEnumeration re = rs.enumerateRecords(null, null, false);
        byte[] rawRecord;
        while(re.hasNextElement()){
            rawRecord = re.nextRecord();
            items.addElement(new Feed(rawRecord));
            //TODO handle parsing exception: delete record
        }

        rs.closeRecordStore();
        return items;
    }

    public Vector loadNewsItems(String rsName) throws RecordStoreException, Exception {

        RecordStore rs = RecordStore.openRecordStore(rsName, false);
        Vector items = new Vector();

        RecordEnumeration re = rs.enumerateRecords(null, null, false);
        byte[] rawRecord;
        while(re.hasNextElement()) {
            rawRecord = re.nextRecord();
            items.addElement(new NewsItem(rawRecord));
        }

        rs.closeRecordStore();
        return items;
    }

    public void addNewsItems(String rsName, Vector items) {
        RecordStore rs;
        try {
            rs = RecordStore.openRecordStore(rsName, true);

            Enumeration enumeration = items.elements();
            NewsItem item;
            while(enumeration.hasMoreElements()){
                item = (NewsItem)enumeration.nextElement();
                byte[] row = item.getBytes();
                rs.addRecord(row, 0, row.length);
            }

            rs.closeRecordStore();
            
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
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }

    }

    public void removeFeed(Feed feed) {
        RecordStore rs;
        try {
            rs = RecordStore.openRecordStore(Constants.FEED_RS_NAME, false);
            //find the one record
            RecordEnumeration re = rs.enumerateRecords(new FeedFilter(feed.getUrl()), null, false);

            while (re.hasNextElement()) {
                rs.deleteRecord(re.nextRecordId());
            }

            RecordStore.deleteRecordStore(feed.getItemsRecordStoreName());
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

    protected class FeedFilter implements RecordFilter {

        private String urlToCheck;

        public FeedFilter(String url){
            this.urlToCheck = url;
        }

        public boolean matches(byte[] candidate) {
            Feed feed = new Feed(candidate);
            if(feed.getUrl().equals(urlToCheck)){
                return true;
            } else {
                return false;
            }
        }

    }
    
}


