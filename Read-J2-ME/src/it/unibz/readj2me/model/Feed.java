package it.unibz.readj2me.model;


public class Feed {

    private String url;
    private String name;
    private String itemsRecordStoreName;

    public Feed(String url, String name, String rsName){
        this.url = url;
        this.name = name;
        this.itemsRecordStoreName = rsName;
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
        return sb.toString().getBytes();
    }

    private void createFromBytes(byte[] recordData) {
        String dataString = new String(recordData);
        int index1, index2;
        try {
            index1= dataString.indexOf(Constants.FIELD_SEPARATOR);
            name = dataString.substring(0, index1);

            index2 = dataString.indexOf(Constants.FIELD_SEPARATOR, index1+1);
            url = dataString.substring(index1 + 1, index2);

            itemsRecordStoreName = dataString.substring(index2 + 1, dataString.length());
        } catch(Throwable t) {
            //TODO: should delete record if unable to parse it?
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

}
