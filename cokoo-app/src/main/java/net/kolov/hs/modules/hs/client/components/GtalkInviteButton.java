package net.kolov.hs.modules.hs.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import net.kolov.hs.modules.hs.client.FriendsService;

/**
 * User: assen
 * Date: 6/8/11
 */
public class GtalkInviteButton extends Button {
    private final CheckBox checkBoxNotify;

    public GtalkInviteButton(CheckBox cb) {


        super("GTalk Invite");
        this.checkBoxNotify = cb;


        addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent
                                        clickEvent) {
                FriendsService.App.getInstance().sendInvite(new AsyncCallback() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        // network error, slikken
                    }

                    @Override
                    public void onSuccess(Object o) {
                        setVisible(false);
                        checkBoxNotify.setEnabled(true);
                    }
                });

            }
        });
    }
}
