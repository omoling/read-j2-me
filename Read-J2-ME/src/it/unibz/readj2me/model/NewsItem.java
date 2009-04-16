package it.unibz.readj2me.model;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class NewsItem {

    private String id = "";
    private String title = "";
    private String published;
    private String updated;
    private String link = "";
    private String content = "";
    private String summary = "";

    public NewsItem(){
    }

    public NewsItem(byte[] recordData){
        createFromBytes(recordData);
    }

    public byte[] getBytes(){
        StringBuffer sb = new StringBuffer();
        sb.append(id);
        sb.append(Constants.FIELD_SEPARATOR);
        sb.append(title);
        sb.append(Constants.FIELD_SEPARATOR);
        sb.append(content);
        sb.append(Constants.FIELD_SEPARATOR);
        sb.append(summary);
        sb.append(Constants.FIELD_SEPARATOR);
        sb.append(link);
        sb.append(Constants.FIELD_SEPARATOR);
        sb.append(updated);
        sb.append(Constants.FIELD_SEPARATOR);
        sb.append(published);
        return sb.toString().getBytes();
    }

    private void createFromBytes(byte[] recordData){
        String recordString = new String(recordData);
        int index1, index2;
        index1 = recordString.indexOf(Constants.FIELD_SEPARATOR);
        id = recordString.substring(0, index1);
        index2 = recordString.indexOf(Constants.FIELD_SEPARATOR, index1 + 1);
        title = recordString.substring(index1 + 1, index2);
        index1 = index2;
        index2 = recordString.indexOf(Constants.FIELD_SEPARATOR, index1 + 1);
        content = recordString.substring(index1 + 1, index2);
        index1 = index2;
        index2 = recordString.indexOf(Constants.FIELD_SEPARATOR, index1 + 1);
        summary = recordString.substring(index1 + 1, index2);
        index1 = index2;
        index2 = recordString.indexOf(Constants.FIELD_SEPARATOR, index1 + 1);
        link = recordString.substring(index1 + 1, index2);
        index1 = index2;
        index2 = recordString.indexOf(Constants.FIELD_SEPARATOR, index1 + 1);
        updated = recordString.substring(index1 + 1, index2);
        index1 = index2;
        index2 = recordString.indexOf(Constants.FIELD_SEPARATOR, index1 + 1);
        published = recordString.substring(index1 + 1, recordString.length());
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the published
     */
    public String getPublished() {
        return published;
    }

    /**
     * @param published the published to set
     */
    public void setPublished(String published) {
        this.published = published;
    }

    /**
     * @return the updated
     */
    public String getUpdated() {
        return updated;
    }

    /**
     * @param updated the updated to set
     */
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

}
