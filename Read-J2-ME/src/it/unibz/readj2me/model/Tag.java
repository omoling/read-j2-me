package it.unibz.readj2me.model;

/**
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

    public byte[] getBytes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void createFromBytes(byte[] recordData) {
        throw new UnsupportedOperationException("Not supported yet.");
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
