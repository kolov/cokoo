package net.kolov.hs.modules.hs.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import net.kolov.hs.modules.hs.client.ClientUser;

/**
 * Small vthin bar on top of the page.
 * User: assen
 * Date: 5/14/11
 */
public class UserBar extends HorizontalPanel {

    private Label lblUser;

    public UserBar(ClientUser clientuser) {
        lblUser = new Label();
        Button signOut = new Button("Sign out");
        signOut.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Window.Location.assign("/signout.do");
            }
        });
        add(lblUser);
        add(signOut);


        String txt = "Hello @" + clientuser.getTwitterName();


        if (clientuser.getGoogleName() == null) {
            final Button googleLogin = new GoogleInviteButton(clientuser.getGoogleLoginUrl());
            add(googleLogin);
        } else {
            txt += " [" + clientuser.getGoogleName() + "]";
        }
        txt += ".  Drag your friends around between the boxes below and watch what happens.";
        lblUser.setText(txt);

    }
}
