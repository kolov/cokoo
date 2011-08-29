package net.kolov.hs.modules.hs.client.components;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * User: assen
 * Date: 6/8/11
 */
public class WaitPopup extends PopupPanel {
    private Label l;

    public WaitPopup(String txt) {
        super(false);
        l = new Label(txt);
        // PopupPanel's constructor takes 'auto-hide' as its boolean parameter.
        // If this is set, the panel closes itself automatically when the user
        // clicks outside of it.
        setWidget(l);
    }

    public void setText(String s) {
        l.setText(s);
    }

}
