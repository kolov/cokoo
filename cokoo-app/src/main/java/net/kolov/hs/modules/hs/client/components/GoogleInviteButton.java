package net.kolov.hs.modules.hs.client.components;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;

/**
 * User: assen
 * Date: 6/8/11
 */
public class GoogleInviteButton extends Button {

    public GoogleInviteButton(final String url) {
        super("Google Login");
        addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent
                                        clickEvent) {
                Window.Location.assign(url);
            }
        });
    }
}
