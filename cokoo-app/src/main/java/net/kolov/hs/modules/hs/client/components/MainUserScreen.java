package net.kolov.hs.modules.hs.client.components;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import net.kolov.hs.modules.hs.client.ClientUser;
import net.kolov.hs.modules.hs.client.FriendsService;
import net.kolov.hs.modules.hs.model.Levels;

/**
 * Created by IntelliJ IDEA.
 * AppUser: assen
 * Date: 4/16/11
 * Time: 5:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainUserScreen extends VerticalPanel {


    private static final int POPUP_WIDTH = 250;
    private static final int POPUP_HEIGHT = 100;

    public MainUserScreen(final int width, final int height, final PickupDragController dragController) {

        final WaitPopup popup = new WaitPopup("Wait while loading your twitter contacts");
        popup.setSize(Integer.toString(POPUP_WIDTH) + "px", Integer.toString(POPUP_HEIGHT) + "px");
        popup.setPopupPosition((width - POPUP_WIDTH) / 2, (height - POPUP_HEIGHT) / 2);

        popup.show();

        FriendsService.App.getInstance().syncFriends(new AsyncCallback<ClientUser>() {
            @Override
            public void onFailure(Throwable throwable) {
                popup.setText("Failed. Try again later");
            }

            @Override
            public void onSuccess(ClientUser clientUser) {
                popup.hide();
                addElements(width, dragController, clientUser);
            }
        });


    }

    /**
     * Creates all page elements.
     *
     * @param width          available width
     * @param dragController
     */
    private void addElements(int width, PickupDragController dragController, ClientUser clientUser) {

        VerticalPanel vp = this;

        vp.add(new UserBar(clientUser));

        int groupWidth = (width - 300) / 3;

        Grid categories = new Grid(3, Levels.COUNT);
        int column = 0;
        FriendPanels panels = new FriendPanels();
        TweetsPanel tweetsPanel = new TweetsPanel(dragController, panels);
        for (int i = Levels.MAX; i >= Levels.MIN; i--) {

            final Label label = new Label(Levels.getLabel(i));
            label.addStyleName("labelCategory");


            categories.setWidget(0, column, label);
            categories.setWidget(1, column, createLegend(clientUser, i));
            final FriendsScrollPanel friendsPanel = new FriendsScrollPanel(groupWidth, dragController, i, tweetsPanel);
            panels.add(i, friendsPanel);
            categories.setWidget(2, column, friendsPanel);

            dragController.registerDropController(new FriendsDropController(friendsPanel));
            column++;
        }

        vp.add(categories);


        vp.add(tweetsPanel);


    }

    /**
     * Create the UI above each Friends panel
     *
     * @param user
     * @param level
     * @return
     */
    private Widget createLegend(final ClientUser user, int level) {
        switch (level) {
            case Levels.LIVE: {
                VerticalPanel vp = new VerticalPanel();
                CheckBox cbShowOnTop = new CheckBox("Show on top");
                cbShowOnTop.setEnabled(false);
                cbShowOnTop.setValue(true);
                vp.add(cbShowOnTop);

                CheckBox cbShowLast = new CheckBox("Show >1 tweet");
                cbShowLast.setEnabled(false);
                cbShowLast.setValue(false);
                vp.add(cbShowLast);

                HorizontalPanel gtalkPanel = new HorizontalPanel();
                final boolean registeredOnGoogle = user.getGoogleName() != null;
                final boolean canNotify = user.isXmppContact() && registeredOnGoogle;
                final boolean doesNotify = canNotify && user.isNotifyStarTweets();

                CheckBox checkBoxNotify = new CheckBox("Resend tweets on GTalk");
                checkBoxNotify.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        user.setNotifyStarTweets(!user.isNotifyStarTweets());
                        FriendsService.App.getInstance().updateUserFromClient(user, new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                            }

                            @Override
                            public void onSuccess(Void clientUser) {
                            }
                        });
                    }
                });
                checkBoxNotify.setEnabled(canNotify);
                checkBoxNotify.setValue(doesNotify);
                gtalkPanel.add(checkBoxNotify);
                if (!registeredOnGoogle) {
                    gtalkPanel.add(new GoogleInviteButton(user.getGoogleLoginUrl()));
                    vp.add(gtalkPanel);
                }
                if (registeredOnGoogle && !canNotify) {
                    final GtalkInviteButton gtalkInviteButton = new GtalkInviteButton(checkBoxNotify);
                    gtalkInviteButton.setEnabled(registeredOnGoogle);
                    gtalkPanel.add(gtalkInviteButton);
                }
                vp.add(gtalkPanel);
                return vp;
            }
            case Levels.IGNORE: {
                VerticalPanel vp = new VerticalPanel();
                CheckBox cb = new CheckBox("Don't want to hear from them");
                cb.setEnabled(false);
                cb.setValue(true);
                vp.add(cb);
                return vp;
            }
            case Levels.REGULAR: {
                VerticalPanel vp = new VerticalPanel();
                CheckBox cbShoOnTop = new CheckBox("Follow");
                cbShoOnTop.setEnabled(false);
                cbShoOnTop.setValue(true);
                vp.add(cbShoOnTop);
                return vp;
            }
            default:
                throw new RuntimeException("Unexpected");

        }
    }


}
