package it.unibz.readj2me.model;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public interface IPersistable {

    public byte[] getBytes();

    public void createFromBytes(byte[] recordData);

}
