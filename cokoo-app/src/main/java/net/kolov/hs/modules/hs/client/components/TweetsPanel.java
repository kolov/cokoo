package net.kolov.hs.modules.hs.client.components;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import net.kolov.hs.modules.hs.client.CategorizedStatus;
import net.kolov.hs.modules.hs.client.ClientStatus;
import net.kolov.hs.modules.hs.client.FriendsService;
import net.kolov.hs.modules.hs.client.SortedStatus;

/**
 * User: assen
 * Date: 5/14/11
 */
public class TweetsPanel extends VerticalPanel {
    private static final int SMALL_ICON_SIZE = 24;
    private Grid grid = new Grid(0, 2);

    private PickupDragController dragController;

    private WaitPopup waitPopup;

    private final FriendPanels friendPanels;

    public TweetsPanel(PickupDragController dragController, FriendPanels friendPanels) {
        this.dragController = dragController;
        this.friendPanels = friendPanels;
        waitPopup = new WaitPopup("Retrieveing yout timeline...");
    }

    @Override
    protected void onLoad() {

        FriendsService.App.getInstance().getHomeTimeline(new AsyncCallback<SortedStatus>() {
            @Override
            public void onFailure(Throwable throwable) {
                waitPopup.setText("Failed, tty later");
            }

            @Override
            public void onSuccess(SortedStatus sortedStatus) {
                waitPopup.hide();
                grid.clear();
                grid.resize(sortedStatus.recordsSize() + sortedStatus.categoriesSize(), 2);
                GWT.log(" SortedStatus " + sortedStatus.recordsSize() + "," + sortedStatus.categoriesSize(), null);
                int ix = 0;
                for (CategorizedStatus cs : sortedStatus) {
                    GWT.log(" CategorizedStatus " + cs.getStates().size(), null);
                    if (cs.getStates().size() > 0) {
                        // non-empty category
                        // first label
                        final Label label = new Label(cs.getName());
                        label.addStyleName("labelCategory");
                        grid.setWidget(ix, 0, label);
                        ix++;
                        // then all messages
                        for (final ClientStatus state : cs.getStates()) {

                            Twimage image = new Twimage(state.getUser(), grid, 50, 0);
                            image.setPixelSize(SMALL_ICON_SIZE, SMALL_ICON_SIZE);
                            ClickHandler ch = new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent clickEvent) {
                                    friendPanels.highlight(state.getUser().getTwitterId());
                                }
                            };
                            image.addClickHandler(ch);
                            image.setTitle(state.getUser().getTwitterName());

                            grid.setWidget(ix, 0, image);


                            String text = state.getText();
                            //parts[] = text.split("((mailto\\\\:|(news|(ht|f)tp(s?))\\\\://){1}\\\\S+)")      ;
                            final Label label1 = new Label(text);
                            label1.addClickHandler(ch);
                            grid.setWidget(ix, 1, label1);
                            // dragController.makeDraggable(image);
                            ix++;
                        }
                    }

                }
                add(grid);
            }
        });
        super.onLoad();
    }

    private void refresh() {

    }
}

