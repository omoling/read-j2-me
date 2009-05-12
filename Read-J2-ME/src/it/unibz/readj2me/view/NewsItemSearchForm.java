package it.unibz.readj2me.view;

import it.unibz.readj2me.model.Feed;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.TextField;

/**
 *
 * @author Anton Dignoes, Omar Moling
 */
public class NewsItemSearchForm extends InputForm {

    private Feed feed;
    private Displayable parentDisplay;
    private TextField searchField;

    public NewsItemSearchForm(Feed feed, Displayable parentDisplay) {
        super("Search for:", parentDisplay, parentDisplay, "Search");
        this.feed = feed;
        this.parentDisplay = parentDisplay;

        searchField = new TextField("Search string", "Android", 20, TextField.ANY);
        searchField.setLayout(Item.LAYOUT_LEFT);

        this.append(searchField);
    }

    protected void save() {
        NewsItemList newsList = new NewsItemList(getSearchString(), feed, parentDisplay);
        //set the new list as next displayable
        super.setNextDisplay(newsList);
    }

    protected boolean isInputValid() {
        if (getSearchString() != null && !getSearchString().equals("")) {
            return true;
        } else {
            new WarningAlert("Search", "Please insert a search string.").show();
            return false;
        }
    }

    private String getSearchString() {
        return searchField.getString();
    }

}
