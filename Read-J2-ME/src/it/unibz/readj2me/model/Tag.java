package it.unibz.readj2me.model;


/**
 * Represents a Tag.
 *
 * @author Anton Dignoes, Omar Moling
 */
public class Tag implements IPersistable {

    private String name;
    private int rs_id;

    public Tag(String name, int rs_id){
        this.name = name;
        this.rs_id = rs_id;
    }

    public Tag(byte[] recordData) {
        createFromBytes(recordData);
    }

    public byte[] getBytes() {
        StringBuffer sb = new StringBuffer();
        sb.append(name);
        sb.append(Constants.FIELD_SEPARATOR);
        sb.append(rs_id);
        sb.append(Constants.FIELD_SEPARATOR);
        return sb.toString().getBytes();
    }

    public void createFromBytes(byte[] recordData) {
        try {
            String recordString = new String(recordData);
            int index1, index2;
            index1 = recordString.indexOf(Constants.FIELD_SEPARATOR);
            name = recordString.substring(0, index1);
            index2 = recordString.indexOf(Constants.FIELD_SEPARATOR, index1 + 1);
            String tempRs_Id = recordString.substring(index1 + 1, index2);
            try {
                rs_id = Integer.parseInt(tempRs_Id);
            } catch (Throwable t) {
                //nothing
            }
        } catch (Throwable t) {
            //Future: could delete record since not valid
        }
    }

    /**
     * Custom equals
     */
    public boolean equals(Tag tag) {
        if (tag.getName().equals(name) && tag.getRs_id() == rs_id) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
