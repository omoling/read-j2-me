package it.unibz.readj2me.controller;

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

    public static final char FIELD_SEPARATOR = '|';
    public static final String RS_PREFIX = "rj2me_";
    public static final String FEED_RS_NAME = RS_PREFIX + "feeds";

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
            name = RS_PREFIX + "" + i;
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
        RecordStore rs = RecordStore.openRecordStore(FEED_RS_NAME, false);
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

    public Vector loadNewsItems(String rsName) throws RecordStoreException, Exception {

        RecordStore rs = RecordStore.openRecordStore(rsName, false);
        Vector items = new Vector();

        RecordEnumeration re = rs.enumerateRecords(null, null, false);
        byte[] rawRecord;
        while(re.hasNextElement()) {
            rawRecord = re.nextRecord();
            items.addElement(createNewsItemFromRecord(rawRecord));
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
                String data = createRecordFromNewsItem(item);
                byte[] row = data.getBytes();
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
            RecordStore rs = RecordStore.openRecordStore(FEED_RS_NAME, true);
            //TODO: encapsulate in method? check whether inheritance would be interesting..
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

    public void removeFeed(Feed feed) {
        RecordStore rs;
        try {
            rs = RecordStore.openRecordStore(FEED_RS_NAME, false);
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

    private String createRecordFromNewsItem(NewsItem item){
        StringBuffer sb = new StringBuffer();
        sb.append(item.getId());
        sb.append(FIELD_SEPARATOR);
        sb.append(item.getTitle());
        sb.append(FIELD_SEPARATOR);
        sb.append(item.getContent());
        sb.append(FIELD_SEPARATOR);
        sb.append(item.getSummary());
        sb.append(FIELD_SEPARATOR);
        sb.append(item.getLink());
        /* TODO
        sb.append(FIELD_SEPARATOR);
        sb.append(item.getPublished().toString());
        sb.append(FIELD_SEPARATOR);
        sb.append(item.getUpdated().toString());
        */
        return sb.toString();
    }

    private NewsItem createNewsItemFromRecord(byte[] data){
        NewsItem item = new NewsItem();

        String recordString = new String(data);
        int index1, index2;
        index1 = recordString.indexOf(FIELD_SEPARATOR);
        item.setId(recordString.substring(0, index1));
        index2 = recordString.indexOf(FIELD_SEPARATOR, index1 + 1);
        item.setTitle(recordString.substring(index1 + 1, index2));
        index1 = index2;
        index2 = recordString.indexOf(FIELD_SEPARATOR, index1 + 1);
        item.setContent(recordString.substring(index1 + 1, index2));
        index1 = index2;
        index2 = recordString.indexOf(FIELD_SEPARATOR, index1 + 1);
        item.setSummary(recordString.substring(index1 + 1, index2));
        index1 = index2;
        
        //TODO: when dates are implemented, update string.length to index2
        //index2 = recordString.indexOf(FIELD_SEPARATOR, index1 + 1);
        //item.setLink(recordString.substring(index1 + 1, index2));
        item.setLink(recordString.substring(index1 + 1, recordString.length()));
        //TODO do the same for published and updated!!
        
        return item;
    }

    private Feed createFeedFromRecord(byte[] data) {
        Feed feed = new Feed();
        String recordString = new String(data);
        int index1, index2;
        try {
            index1= recordString.indexOf(FIELD_SEPARATOR);
            feed.setName(recordString.substring(0, index1));

            index2 = recordString.indexOf(FIELD_SEPARATOR, index1+1);
            feed.setUrl(recordString.substring(index1 + 1, index2));

            feed.setItemsRecordStoreName(recordString.substring(index2 + 1, recordString.length()));
        } catch(Throwable t) {
            //TODO: should delete record if unable to parse it?
            //throw new Exception("Parsing error in RecordStore");
            t.printStackTrace();
        }
        return feed;
    }

    //testing
    public void eraseRS(){
        String[] stores = RecordStore.listRecordStores();
        for(int i = 0; i < stores.length; i++){
            try {
                if(stores[i].startsWith(RS_PREFIX)){
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
            Feed feed = createFeedFromRecord(candidate);
            if(feed.getUrl().equals(urlToCheck)){
                return true;
            } else {
                return false;
            }
        }

    }
    
}


