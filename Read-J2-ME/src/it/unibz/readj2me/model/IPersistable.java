package it.unibz.readj2me.model;

/**
 * Defines which methods an object that should be stored has to define
 *
 * @author Anton Dignoes, Omar Moling
 */
public interface IPersistable {

    /**
     * Creates a byte array of the object
     * @return an array of bytes
     */
    public byte[] getBytes();

    /**
     * Builds the object from a byte array
     * @param recordData the byte data
     */
    public void createFromBytes(byte[] recordData);

}
