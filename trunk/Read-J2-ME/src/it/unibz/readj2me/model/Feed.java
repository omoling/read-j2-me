package it.unibz.readj2me.model;

public class Feed {

    private String url;
    private String name;
    private String itemsRecordStoreName;

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
