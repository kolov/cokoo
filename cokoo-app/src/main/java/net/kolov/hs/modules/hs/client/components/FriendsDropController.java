package net.kolov.hs.modules.hs.client.components;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.SimpleDropController;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: assen
 * Date: 5/15/11
 */
public class FriendsDropController extends SimpleDropController {
    private static final String CSS_DRAGGABLE_ENGAGE = "dragdrop-dropTarget-engage";


    public FriendsDropController(FriendsScrollPanel dropTarget) {
        super(dropTarget);
    }

    @Override
    public void onDrop(DragContext context) {
        for (Widget widget : context.selectedWidgets) {
            Twimage twimage = (Twimage) widget;

            Grid grid = twimage.getGrid();
            Widget parent = grid.getParent();
            FriendsScrollPanel targetPanel = getTargetPanel();
            if (parent instanceof FriendsScrollPanel) {
                if (parent != targetPanel) {
                    grid.remove(widget);
                    targetPanel.dropImage((Twimage) widget);
                }
            } else if (parent instanceof TweetsPanel) {
                targetPanel.dropImage((Twimage) widget);
            }

        }
        super.onDrop(context);
    }

    private FriendsScrollPanel getTargetPanel() {
        return ((FriendsScrollPanel) getDropTarget());
    }

    @Override
    public void onEnter(DragContext context) {
        super.onEnter(context);
        for (Widget widget : context.selectedWidgets) {
            widget.addStyleName(CSS_DRAGGABLE_ENGAGE);
        }
        getTargetPanel().setEngaged(true);
    }

    @Override
    public void onLeave(DragContext context) {
        for (Widget widget : context.selectedWidgets) {
            widget.removeStyleName(CSS_DRAGGABLE_ENGAGE);
        }
        getTargetPanel().setEngaged(false);
        super.onLeave(context);
    }

    @Override
    public void onMove(DragContext context) {
        super.onMove(context);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void onPreviewDrop(DragContext context) throws VetoDragException {

        super.onPreviewDrop(context);

    }


}
