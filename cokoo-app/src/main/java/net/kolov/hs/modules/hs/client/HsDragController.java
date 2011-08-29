package net.kolov.hs.modules.hs.client;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.util.Location;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;

/**
 * User: assen
 * Date: 5/16/11
 */
public class HsDragController extends PickupDragController {

    /**
     * Private implementation class to store widget information while dragging.
     */
    private static class SavedWidgetInfo {

        /**
         * The initial draggable index for indexed panel parents.
         */
        int initialDraggableIndex;

        /**
         * Initial draggable CSS margin.
         */
        String initialDraggableMargin;

        /**
         * Initial draggable parent widget.
         */
        Widget initialDraggableParent;

        /**
         * Initial location for absolute panel parents.
         */
        Location initialDraggableParentLocation;
    }


    private HashMap<Widget, SavedWidgetInfo> infoMap;

    public HsDragController(AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel) {
        super(boundaryPanel, allowDroppingOnBoundaryPanel);
    }

    @Override
    public boolean getBehaviorDragProxy() {
        return true;
    }

    @Override
    protected void restoreSelectedWidgetsStyle() {
        for (Widget widget : context.selectedWidgets) {
            SavedWidgetInfo info = infoMap.get(widget);
            widget.getElement().getStyle().setProperty("margin", info.initialDraggableMargin);
        }
    }

    @Override
    protected void saveSelectedWidgetsLocationAndStyle() {
        infoMap = new HashMap<Widget, SavedWidgetInfo>();
        for (Widget widget : context.selectedWidgets) {
            SavedWidgetInfo info = new SavedWidgetInfo();
            info.initialDraggableParent = widget.getParent();
            Grid g = (Grid) info.initialDraggableParent;
            for (int i = 0; i < g.getColumnCount(); i++) {
                if (g.getWidget(0, i) == widget) {
                    info.initialDraggableIndex = i;
                    break;
                }
            }


            info.initialDraggableMargin = DOM.getStyleAttribute(widget.getElement(), "margin");
            widget.getElement().getStyle().setProperty("margin", "0px");
            infoMap.put(widget, info);
        }

    }

    @Override
    protected void restoreSelectedWidgetsLocation() {
        for (Widget widget : context.selectedWidgets) {
            SavedWidgetInfo info = infoMap.get(widget);


            Grid grid = (Grid) info.initialDraggableParent;
            grid.setWidget(0, info.initialDraggableIndex, widget);

        }
    }
}
