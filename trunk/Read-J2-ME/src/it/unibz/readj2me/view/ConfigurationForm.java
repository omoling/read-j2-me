/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unibz.readj2me.view;

import it.unibz.readj2me.model.Configuration;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Gauge;

/**
 *
 * @author omoling
 */
public class ConfigurationForm extends InputForm {

    private Gauge maxNewsGauge;

    public ConfigurationForm(Displayable parentDisplay) {
        super("Configuration", parentDisplay, parentDisplay, "Save");

        maxNewsGauge = new Gauge("Max items:", true, 200, Configuration.getInstance().getMaxNewsItems());
        this.append(maxNewsGauge);

        //TODO
    }

    protected void save() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected boolean isInputValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
