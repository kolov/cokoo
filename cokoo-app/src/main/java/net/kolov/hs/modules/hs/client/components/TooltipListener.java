package net.kolov.hs.modules.hs.client.components;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MouseListenerAdapter;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: assen
 * Date: 6/15/11
 */
class TooltipListener extends MouseListenerAdapter {
    private static final String DEFAULT_TOOLTIP_STYLE = "TooltipPopup";


    private static class Tooltip extends PopupPanel {
        private int delay;

        public Tooltip(Widget sender, int offsetX, int offsetY,
                       final String text, final int delay, final String styleName) {
            super(true);

            this.delay = delay;

            HTML contents = new HTML(text);
            add(contents);

            int left = sender.getAbsoluteLeft() + offsetX;
            int top = sender.getAbsoluteTop() + offsetY;

            setPopupPosition(left, top);
            setStyleName(styleName);
        }

        public void show() {
            super.show();

            Timer t = new Timer() {

                public void run() {
                    Tooltip.this.hide();
                }

            };
            t.schedule(delay);
        }
    }

    private Tooltip tooltip;
    private String text;
    private String styleName;
    private int delay;
    private int offsetX;
    private int offsetY;


    public TooltipListener(String text, int delay, String styleName, int offsetX, int offsetY) {
        this.text = text;
        this.delay = delay;
        this.styleName = styleName;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public void onMouseEnter(Widget sender) {
        if (tooltip != null) {
            tooltip.hide();
        }
        tooltip = new Tooltip(sender, offsetX, offsetY, text, delay, styleName);
        tooltip.show();
    }

    public void onMouseLeave(Widget sender) {
        if (tooltip != null) {
            tooltip.hide();
        }
    }

    public String getStyleName() {
        return styleName;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }
}