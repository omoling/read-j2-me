package it.unibz.readj2me.model;

import it.unibz.readj2me.view.WarningAlert;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class NewsItem implements IPersistable {

    private String id = "";
    private String title = "";
    private String published = "";
    private String updated = "";
    private String link = "";
    private String content = "";
    private String summary = "";
    private boolean read = false;
    private Vector tags = new Vector();
    private int rs_id;

    public NewsItem() {
    }

    public NewsItem(byte[] recordData) {
        createFromBytes(recordData);
    }

    public byte[] getBytes() {
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
        sb.append(Constants.FIELD_SEPARATOR);
        if (read) {
            sb.append("1");
        } else {
            sb.append("0");
        }
        sb.append(Constants.FIELD_SEPARATOR);
        sb.append(rs_id);
        sb.append(Constants.FIELD_SEPARATOR);

        Tag tag;
        Enumeration enumeration = getTags().elements();
        while(enumeration.hasMoreElements()){
            tag = (Tag) enumeration.nextElement();
            sb.append(tag.getName() + Constants.VECTOR_SEPARATOR + tag.getRs_id());
            sb.append(Constants.FIELD_SEPARATOR);
        }
        return sb.toString().getBytes();
    }

    public void createFromBytes(byte[] recordData) {
        try {
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
            published = recordString.substring(index1 + 1, index2);
            index1 = index2;
            index2 = recordString.indexOf(Constants.FIELD_SEPARATOR, index1 + 1);
            String tempRead = recordString.substring(index1 + 1, index2);
            if (tempRead.equals("1")) {
                read = true;
            } else {
                read = false;
            }
            index1 = index2;
            index2 = recordString.indexOf(Constants.FIELD_SEPARATOR, index1 + 1);
            String tempRs_Id = recordString.substring(index1 + 1, index2);
            try {
                rs_id = Integer.parseInt(tempRs_Id);
            } catch (Throwable t) {
                //nothing
            }

            Tag tag;
            String tempString, tempName;
            int tempIndex, tempRsId = -1;
            //last character must be "|"
            while(index2 < recordString.length() - 1){
                index1 = index2;
                index2 = recordString.indexOf(Constants.FIELD_SEPARATOR, index1 + 1);
                tempString = recordString.substring(index1 + 1, index2);
                tempIndex = tempString.indexOf(Constants.VECTOR_SEPARATOR);
                tempName = tempString.substring(0, tempIndex);
                try {
                    tempRsId = Integer.parseInt(tempString.substring(tempIndex + 1, tempString.length()));
                } catch (NumberFormatException ex) {
                    //nothing
                }
                tag = new Tag(tempName, tempRsId);
                getTags().addElement(tag);
            }

        } catch (Throwable t) {
            //Future: could delete record since not valid
        }
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

    /**
     * @return the read
     */
    public boolean isRead() {
        return read;
    }

    /**
     * @param read the read to set
     */
    public void setRead(boolean read) {
        this.read = read;
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

    /**
     * @return the tags
     */
    public Vector getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(Vector tags) {
        this.tags = tags;
    }
}
