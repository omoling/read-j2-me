package it.unibz.readj2me.controller;

import it.unibz.readj2me.model.Feed;
import it.unibz.readj2me.model.NewsItem;
import java.util.Random;
import java.util.Vector;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class PersistentManager {

    public static PersistentManager persistentManager = null;

    public static final char FIELD_SEPARATOR = '|';
    public static final String recordStorePrefix = "rj2me_";
    public static final String feedRecordStoreName = "rj2me_feeds";

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

        do {
            i = generator.nextInt();
            name = recordStorePrefix + "" + i;
        } while(!isNameFree(name));

        return name;
    }

    private boolean isNameFree(String name) throws RecordStoreFullException, RecordStoreException {
        RecordStore rs;
        try {
            rs = RecordStore.openRecordStore(name, false);
            rs.closeRecordStore();
        } catch (RecordStoreNotFoundException ex) {
            return true;
        } catch (RecordStoreNotOpenException ex){
            return false;
        } 
        return false;
    }

    public Vector loadFeeds() throws RecordStoreException, Exception {
        RecordStore rs = RecordStore.openRecordStore(feedRecordStoreName, false);
        Vector items = new Vector();

        RecordEnumeration re = rs.enumerateRecords(null, null, false);
        byte[] rawRecord;
        while(re.hasNextElement()){
            rawRecord = re.nextRecord();
            items.addElement(createFeedFromRecord(rawRecord));
            //TODO handle parsing exception: delete record
        }

        rs.closeRecordStore();
        return items;
    }

    public Vector loadNewsItems(Feed feed) throws RecordStoreException, Exception {

        RecordStore rs = RecordStore.openRecordStore(feed.getItemsRecordStoreName(), false);
        Vector items = new Vector();

        RecordEnumeration re = rs.enumerateRecords(null, null, false);
        byte[] rawRecord;
        while(re.hasNextElement()) {
            rawRecord = re.nextRecord();
            //TODO items.addElement(createFeedFromRecord(rawRecord));
        }

        rs.closeRecordStore();
        return items;
    }

    public void addNewsItem(NewsItem item) {

    }

    public void removeFeed(Feed feed) {
        RecordStore rs;
        try {
            rs = RecordStore.openRecordStore(feedRecordStoreName, false);
            RecordEnumeration re = rs.enumerateRecords(new FeedFilter(feed.getUrl()), null, false);

            while (re.hasNextElement()) {
                rs.deleteRecord(re.nextRecordId());
            }
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

    public void addFeed(String name, String url) {
        String rsItemsName;
        try {
            rsItemsName = PersistentManager.getInstance().getRandomRecordStoreName();
            RecordStore rs = RecordStore.openRecordStore(feedRecordStoreName, true);
            String data = name + FIELD_SEPARATOR + url + FIELD_SEPARATOR + rsItemsName;
            byte[] row = data.getBytes();
            rs.addRecord(row, 0, row.length);
            rs.closeRecordStore();
        } catch (RecordStoreFullException ex) {
            ex.printStackTrace();
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }

    }

    private Feed createFeedFromRecord(byte[] data) {
        Feed f = new Feed();
        String recordString = new String(data);
        int index1, index2;
        try {
            index1= recordString.indexOf(FIELD_SEPARATOR);
            f.setName(recordString.substring(0, index1));

            index2 = recordString.indexOf(FIELD_SEPARATOR, index1+1);
            f.setUrl(recordString.substring(index1 + 1, index2));

            f.setItemsRecordStoreName(recordString.substring(index2 + 1, recordString.length()));
        } catch(Throwable t) {
            //TODO: should delete record if unable to parse it?
            //throw new Exception("Parsing error in RecordStore");
            t.printStackTrace();
        }
        return f;
    }

    protected class FeedFilter implements RecordFilter {

        private String urlToCheck;

        public FeedFilter(String url){
            this.urlToCheck = url;
        }

        public boolean matches(byte[] candidate) {
            Feed feed = createFeedFromRecord(candidate);
            System.out.println("name : '" + feed.getName() + "'");
            System.out.println("url  : '" + feed.getUrl() + "'");
            System.out.println("rs   : '" + feed.getItemsRecordStoreName() + "'");
            System.out.println("check: '" + urlToCheck + "'");
            if(feed.getUrl().equals(urlToCheck)){
                System.out.println("true");
                return true;
            } else {
                System.out.println("false");
                return false;
            }
        }

    }
    
}


