package net.kolov.hs.modules.hs.client.components;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import net.kolov.hs.modules.hs.client.ClientUser;

/**
 * User: assen
 * Date: 5/16/11
 */
public class Twimage extends Image {

    private int id;
    private String screenName;
    private Grid grid;

    public Twimage(ClientUser user, Grid grid, int offsetX, int offsetY) {
        super((user.getPicurl()));
        id = user.getTwitterId();

        screenName = user.getTwitterName();
        this.grid = grid;

        addMouseListener(
                new TooltipListener(
                        screenName, 5000 /* timeout in milliseconds*/, "image-tooltip", offsetX, offsetY));


    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public Grid getGrid() {
        return grid;
    }
}
