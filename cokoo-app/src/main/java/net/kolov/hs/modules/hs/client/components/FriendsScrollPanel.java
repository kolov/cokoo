package net.kolov.hs.modules.hs.client.components;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import net.kolov.hs.modules.hs.client.ClientUser;
import net.kolov.hs.modules.hs.client.FriendsService;

/**
 * User: assen
 * Date: 5/11/11
 */
public class FriendsScrollPanel extends ScrollPanel {


    private static final String CSS_BIN_ENGAGE = "friendspanel-engaged";
    private static final int SIZE_ICON = 48;
    private static final String PANEL_HEIGHT = "80";
    private Grid grid;
    private int ixHighlighted = -1;

    private Integer level;
    private PickupDragController dragController;

    private TweetsPanel tweetsPanel;


    public FriendsScrollPanel(int width, PickupDragController dragController, Integer level, TweetsPanel tweetsPanel) {
        setSize(Integer.toString(width), PANEL_HEIGHT);

        this.dragController = dragController;
        addStyleName("friendsPanel");
        this.level = level;
        this.tweetsPanel = tweetsPanel;

    }

    public void setEngaged(boolean engaged) {
        if (engaged) {
            addStyleName(CSS_BIN_ENGAGE);
        } else {
            removeStyleName(CSS_BIN_ENGAGE);
        }
    }


    @Override
    protected void onLoad() {

        FriendsService.App.getInstance().askFriends(level, level + 1, new AsyncCallback<ClientUser[]>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(ClientUser[] friends) {
                grid = new Grid(1, friends.length);
                for (int i = 0; i < friends.length; i++) {
                    ClientUser friend = friends[i];
                    Twimage image = new Twimage(friend, grid, 5, 55);
                    image.setPixelSize(SIZE_ICON, SIZE_ICON);
                    if (dragController != null) {
                        dragController.makeDraggable(image);
                    }
                    grid.setWidget(0, i, image);
                }
                add(grid);
            }
        });
        super.onLoad();
    }


    public void dropImage(final Twimage twimage) {
        FriendsService.App.getInstance().moveFriend(twimage.getId(), level, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
            }

            @Override
            public void onSuccess(Void aVoid) {
                grid.resize(1, grid.getColumnCount() + 1);
                grid.setWidget(0, grid.getColumnCount() - 1, twimage);
                tweetsPanel.onLoad();
            }
        });
    }

    /**
     * highlight a user ucon
     *
     * @param twitterId user twitter id
     */


    public void unhighlight() {
        if (ixHighlighted != -1) {
            grid.getWidget(0, ixHighlighted).removeStyleDependentName("highlighted");
            ixHighlighted = -1;
        }
    }

    public int indexOf(int twitterId) {
        int cols = grid.getColumnCount();
        for (int i = 0; i < cols; i++) {
            Twimage twimage = (Twimage) grid.getWidget(0, i);
            if (twimage == null) {
                //???
                continue;
            }
            if (twimage.getId() == twitterId) {
                return i;
            }
        }
        return -1;
    }

    public void highlightIndex(int ix) {
        unhighlight();
        ixHighlighted = ix;
        final Widget widget = grid.getWidget(0, ix);
        int w = widget.getOffsetWidth() + grid.getCellPadding() + grid.getCellSpacing();
        final int position = (w * ix) - this.getOffsetWidth() / 2;
        this.setHorizontalScrollPosition(Math.min(position, 0));
        widget.addStyleName("highlighted");
    }
}

