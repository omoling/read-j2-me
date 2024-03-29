package it.unibz.readj2me.model;

/**
 * Class containing the application's configuration
 * @author Anton Dignoes, Omar Moling
 */
public class Configuration implements IPersistable {

    private static Configuration instance = null;
    private int maxNewsItems = 2;

    private Configuration() {
    }

    /**
     * Singleton class.
     * @return the only instance.
     */
    public static Configuration getInstance(){
        if(instance == null) {
            instance = new Configuration();
        }
        return instance;
    }

    /**
     * @return the maxNewsItems
     */
    public int getMaxNewsItems() {
        return maxNewsItems;
    }

    /**
     * @param maxNewsItems the maxNewsItems to set
     */
    public void setMaxNewsItems(int maxNewsItems) {
        this.maxNewsItems = maxNewsItems;
    }

    /**
     * @return a byte-array representaion of the object
     */
    public byte[] getBytes() {
        StringBuffer sb = new StringBuffer();
        sb.append(maxNewsItems);
        sb.append(Constants.FIELD_SEPARATOR);

        return sb.toString().getBytes();
    }

    /**
     * Builds the object from a byte array
     * @param recordData object in form of byte array
     */
    public void createFromBytes(byte[] recordData) {
        try {
            String recordString = new String(recordData);
            int index1;
            index1 = recordString.indexOf(Constants.FIELD_SEPARATOR, 0);
            String tempMax = recordString.substring(0, index1);
            try {
                instance.setMaxNewsItems(maxNewsItems = Integer.parseInt(tempMax));
            } catch (Throwable t) {
                //nothing
            }
        } catch (Throwable t) {
            //Future: could delete record since not valid
        }
    }

}
