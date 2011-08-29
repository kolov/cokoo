package net.kolov.hs.modules.hs.client;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootPanel;
import net.kolov.hs.modules.hs.client.components.MainUserScreen;

/**
 * Created by IntelliJ IDEA.
 * AppUser: assen
 * Date: 4/15/11
 */
public class Hs implements EntryPoint {

    private static final String CSS_MAIN_BOUNDARY_PANEL = "main-boundary-panel";

    private PickupDragController dragController;

    public void onModuleLoad() {
        // set uncaught exception handler
        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

            public void onUncaughtException(Throwable throwable) {
                String text = "Uncaught exception: ";
                while (throwable != null) {
                    StackTraceElement[] stackTraceElements = throwable.getStackTrace();
                    text += throwable.toString() + "\n";
                    for (StackTraceElement element : stackTraceElements) {
                        text += "    at " + element + "\n";
                    }
                    throwable = throwable.getCause();
                    if (throwable != null) {
                        text += "Caused by: ";
                    }
                }
                DialogBox dialogBox = new DialogBox(true, false);
                dialogBox.getElement().getStyle().setProperty("backgroundColor", "#ABCDEF");
                System.err.print(text);
                text = text.replaceAll(" ", "&nbsp;");
                dialogBox.setHTML("<pre>" + text + "</pre>");
                dialogBox.center();
            }
        });

        // use a deferred command so that the handler catches onModuleLoad2() exceptions
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            public void execute() {
                onModuleLoad2();
            }
        });
    }


    private void onModuleLoad2() {
        RootPanel mainPanel = RootPanel.get("slot1");
        int width = Window.getClientWidth();
        int height = Window.getClientHeight();


        DOM.setInnerHTML(mainPanel.getElement(), "");

        // create the main common boundary panel to which drag operations will be restricted
        AbsolutePanel boundaryPanel = new AbsolutePanel();
        boundaryPanel.addStyleName(CSS_MAIN_BOUNDARY_PANEL);
        boundaryPanel.setPixelSize(width, height * 2);
        dragController = new HsDragController(boundaryPanel, true);
        dragController.setBehaviorMultipleSelection(false);

        boundaryPanel.add(new MainUserScreen(width, height, dragController));
        mainPanel.add(boundaryPanel);
    }

}
