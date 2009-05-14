/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibz.readj2me.view;

import it.unibz.readj2me.controller.PersistentManager;
import it.unibz.readj2me.model.Configuration;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.StringItem;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class ConfigurationForm extends InputForm implements ItemStateListener {

    private Gauge maxNewsGauge;
    private StringItem maxNewsGaugeItem, maxNewsInfoItem;
    private boolean changed = false;
    int originalMaxNewsValue;

    public ConfigurationForm(Displayable parentDisplay) {
        super("Configuration", parentDisplay, parentDisplay, "Save");
        originalMaxNewsValue = Configuration.getInstance().getMaxNewsItems();
        
        maxNewsGauge = new Gauge("Max items:", true, 20, originalMaxNewsValue);
        this.append(maxNewsGauge);

        maxNewsGaugeItem = new StringItem("Value:", "" + maxNewsGauge.getValue() * 10);
        maxNewsGaugeItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
        this.append(maxNewsGaugeItem);

        maxNewsInfoItem = new StringItem("Info:", "put 0 for no limit.");
        maxNewsInfoItem.setLayout(Item.LAYOUT_NEWLINE_BEFORE);
        this.append(maxNewsInfoItem);

        this.setItemStateListener(this);
    }

    protected void save() {
        if (changed) {
            try {
                //update Configuration instance
                Configuration.getInstance().setMaxNewsItems(maxNewsGauge.getValue());
                //update RecordStore
                PersistentManager.getInstance().addConfiguration();
            } catch (RecordStoreFullException ex) {
                new ErrorAlert("Configuration", "Configuration could not be stored, your memory is full!").show();
                Configuration.getInstance().setMaxNewsItems(originalMaxNewsValue);
            } catch (RecordStoreException ex) {
                new ErrorAlert("Configuration", "Sorry, configuration could not be stored!").show();
                Configuration.getInstance().setMaxNewsItems(originalMaxNewsValue);
            }
        }
    }

    protected boolean isInputValid() {
        return true;
    }

    public void itemStateChanged(Item item) {
        if (item == maxNewsGauge) {
            maxNewsGaugeItem.setText("" + maxNewsGauge.getValue() * 10);
            changed = true;
        }
    }

}
