package it.unibz.readj2me.model;

import it.unibz.readj2me.view.WarningAlert;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class Feed implements IPersistable {

    private String url;
    private String name;
    private String itemsRecordStoreName;
    private Vector knownIds = new Vector();
    private int rs_id;

    public Feed(String url, String name, String rsName, int rs_id){
        this.url = url;
        this.name = name;
        this.itemsRecordStoreName = rsName;
        this.rs_id = rs_id;
    }

    public Feed(byte[] recordData){
        createFromBytes(recordData);
    }

    public byte[] getBytes(){
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append(Constants.FIELD_SEPARATOR);
        sb.append(url);
        sb.append(Constants.FIELD_SEPARATOR);
        sb.append(itemsRecordStoreName);
        sb.append(Constants.FIELD_SEPARATOR);
        sb.append(rs_id);
        sb.append(Constants.FIELD_SEPARATOR);
        
        Enumeration enumeration = getKnownIds().elements();
        while(enumeration.hasMoreElements()){
            sb.append(enumeration.nextElement().toString());
            sb.append(Constants.FIELD_SEPARATOR);
        }
        return sb.toString().getBytes();
    }

    public void createFromBytes(byte[] recordData) {
        String dataString = new String(recordData);
        int index1, index2;
        try {
            index1= dataString.indexOf(Constants.FIELD_SEPARATOR);
            name = dataString.substring(0, index1);

            index2 = dataString.indexOf(Constants.FIELD_SEPARATOR, index1 + 1);
            url = dataString.substring(index1 + 1, index2);

            index1 = index2;
            index2 = dataString.indexOf(Constants.FIELD_SEPARATOR, index1 + 1);
            itemsRecordStoreName = dataString.substring(index1 + 1, index2);

            index1 = index2;
            index2 = dataString.indexOf(Constants.FIELD_SEPARATOR, index1 + 1);
            String tempRs_Id = dataString.substring(index1 + 1, index2);
            try {
                rs_id = Integer.parseInt(tempRs_Id);
            } catch (Throwable t) {
                //TODO
                t.printStackTrace();
                new WarningAlert("parsing feed rs_id", "parsing feed rs_id").show();
            }

            //last character must be "|"
            while(index2 < dataString.length() - 1){
                index1 = index2;
                index2 = dataString.indexOf(Constants.FIELD_SEPARATOR, index1 + 1);
                getKnownIds().addElement(dataString.substring(index1 + 1, index2));
            }

        } catch(Throwable t) {
            //TODO: 
            //throw new Exception("Parsing error in RecordStore");
            t.printStackTrace();
        }
    }

    /**
     * @return the feedUrl
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param feedUrl the feedUrl to set
     */
    public void setUrl(String feedUrl) {
        this.url = feedUrl;
    }

    /**
     * @return the feedName
     */
    public String getName() {
        return name;
    }

    /**
     * @param feedName the feedName to set
     */
    public void setName(String feedName) {
        this.name = feedName;
    }

    /**
     * @return the recordStoreName
     */
    public String getItemsRecordStoreName() {
        return itemsRecordStoreName;
    }

    /**
     * @param recordStoreName the recordStoreName to set
     */
    public void setItemsRecordStoreName(String recordStoreName) {
        this.itemsRecordStoreName = recordStoreName;
    }

    /**
     * @return the knownIds
     */
    public Vector getKnownIds() {
        return knownIds;
    }

    /**
     * @param knownIds the knownIds to set
     */
    public void setKnownIds(Vector knownIds) {
        this.knownIds = knownIds;
    }

    /**
     * @return the rs_id
     */
    public int getRs_id() {
        return rs_id;
    }

    /**
     * @param rs_id the rs_id to set
     */
    public void setRs_id(int rs_id) {
        this.rs_id = rs_id;
    }

}
