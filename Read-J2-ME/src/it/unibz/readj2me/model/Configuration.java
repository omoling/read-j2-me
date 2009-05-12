package it.unibz.readj2me.model;

import it.unibz.readj2me.view.WarningAlert;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class Configuration implements IPersistable {

    private static Configuration instance = null;
    private int maxNewsItems = 100;

    private Configuration() {
    }

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

    public byte[] getBytes() {
        StringBuffer sb = new StringBuffer();
        sb.append(maxNewsItems);
        sb.append(Constants.FIELD_SEPARATOR);

        return sb.toString().getBytes();
    }

    public void createFromBytes(byte[] recordData) {
        try {
            String recordString = new String(recordData);
            int index1;
            index1 = recordString.indexOf(Constants.FIELD_SEPARATOR, 0);
            String tempMax = recordString.substring(0, index1);
            try {
                maxNewsItems = Integer.parseInt(tempMax);
            } catch (Throwable t) {
                //TODO: remove then..
                new WarningAlert("parsing settings maxNewsItems", "parsing settings maxNewsITems").show();
                t.printStackTrace();
            }
        } catch (Throwable t) {
            //TODO:
            //throw new Exception("Parsing error in RecordStore");
            t.printStackTrace();
        }
    }

}
