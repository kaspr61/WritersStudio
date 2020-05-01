package com.team34.view;

import com.team34.model.event.EventManager;
import com.team34.view.character.CharacterList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.team34.view.dialogs.EditEventDialog;
import com.team34.view.timeline.Timeline;

/**
 * This class represents the top layer of the view.
 * <p>
 * It manages the GUI and can only be called into; it is dependent on neither the model nor the controller.
 * @author Kasper S. Skott
 */
public class MainView {

    private static final double MIN_WINDOW_WIDTH = 800.0;
    private static final double MIN_WINDOW_HEIGHT = 600.0;
    private static final double MAX_WINDOW_WIDTH = 3840.0; // 4K Ultra HD
    private static final double MAX_WINDOW_HEIGHT = 2160.0; // 4K Ultra HD

    //// CONTROL IDs ///////////////////////////

    public static final String ID_BTN_EVENT_ADD = "BTN_EVENT_ADD";
    public static final String ID_TIMELINE_NEW_EVENT = "TIMELINE_NEW_EVENT";
    public static final String ID_TIMELINE_REMOVE_EVENT = "TIMELINE_REMOVE_EVENT";
    public static final String ID_TIMELINE_EDIT_EVENT = "TIMELINE_EDIT_EVENT";

    //// PANES /////////////////////////////////////////

    private final BorderPane rootPane;
    private final BorderPane contentBorderPane;
    private final StackPane topPane;
    private final StackPane bottomPane;
    private final SplitPane firstLayerSplit;
    private final StackPane leftPane;
    private final StackPane rightPane;
    private final SplitPane secondLayerSplit;

    //// CONTROLS //////////////////////////////////////

    private MenuBar menuBar;

    ////////////////////////////////////////////////////

    private Scene mainScene;
    private String cssMain;
    private Timeline timeline;
    private EditEventDialog editEventDialog;
    private int eventOrderList; // index to specify which order list to use

    ////////////////////////////////////////////////////

    /**
     * Constructs the GUI.
     * <p>
     * Calls {@link MainView#setupTimeline(Pane, double)} and {@link MainView#setupRightPane()}
     *
     * @param mainStage the stage associated with the {@link javafx.application.Application}.
     * @param screenW the width the window should be at
     * @param screenH the height the window should be at
     * @param maximized true if window should start maximized
     */
    public MainView(Stage mainStage, double screenW, double screenH, boolean maximized) {
        eventOrderList = 0;

        // Create the root parent pane and the main scene
        rootPane = new BorderPane();
        mainScene = new Scene(rootPane, screenW, screenH);

        // Construct the path to the main .css file, and add it to the root pane
        cssMain = com.team34.App.class.getResource("css/main.css").toExternalForm();
        rootPane.getStylesheets().add(cssMain);

        // Create and add the menu bar
        menuBar = new MenuBar();
        rootPane.setTop(menuBar);

        // Create the contentBorderPane
        contentBorderPane = new BorderPane();

        // Create the first-layer panes. These are separated horizontally
        topPane = new StackPane();
        bottomPane = new StackPane();
        firstLayerSplit = new SplitPane();

        topPane.setMinSize(screenW, 200.0);
        bottomPane.setMinSize(screenW, 120.0);

        firstLayerSplit.setOrientation(Orientation.VERTICAL);
        firstLayerSplit.getItems().addAll(topPane, bottomPane);
        firstLayerSplit.setDividerPosition(0, 0.99);

        // Create the second-layer panes, contained by centerPane. These are separated vertically
        leftPane = new StackPane();
        rightPane = new CharacterList();
        secondLayerSplit = new SplitPane();

        leftPane.setMinSize(100.0, 200.0);
        rightPane.setMinSize(250.0, 200.0);

        secondLayerSplit.setOrientation(Orientation.HORIZONTAL); // layed-out horizontally, but splitted vertically
        secondLayerSplit.getItems().addAll(leftPane, rightPane);
        secondLayerSplit.setDividerPosition(0, 0.2);
        topPane.getChildren().add(secondLayerSplit);

        // Add split the first layer split pane to the contentBorderPane
        contentBorderPane.setCenter(firstLayerSplit);

        // Add the contentBorderPane to the root pane
        rootPane.setCenter(contentBorderPane);

        // Set up timeline
        setupTimeline(bottomPane, screenW);

        // Set up right pane
        setupRightPane();

        // Finalize the stage
        mainStage.setResizable(true);
        mainStage.setMinWidth(MIN_WINDOW_WIDTH);
        mainStage.setMinHeight(MIN_WINDOW_HEIGHT);
        mainStage.setMaxWidth(MAX_WINDOW_WIDTH);
        mainStage.setMaxHeight(MAX_WINDOW_HEIGHT);
        mainStage.setMaximized(maximized);

        mainStage.setTitle("Writer's Studio");
        mainStage.setScene(mainScene);
        mainStage.show();

        // Create event dialog
        editEventDialog = new EditEventDialog(mainStage);
    }

    /**
     * Constructs and initializes the {@link Timeline}.
     * @param parentPane the pane inside which the timeline is to reside
     * @param screenW the minimum width of the timeline
     */
    private void setupTimeline(Pane parentPane, double screenW) {
        timeline = new Timeline(screenW);
        timeline.addToPane(parentPane);

        timeline.recalculateLayout();
    }

    /**
     * Constructs and initializes the right-most pane, which contains the character list.
     */
    private void setupRightPane() {

    }

    /**
     * Returns the context menu of the timeline
     * @return the context menu of the timeline
     */
    public ContextMenu getTimelineContextMenu() {
        return timeline.getContextMenu();
    }

    /**
     * Returns the index of the list specifying the order of events.
     * '0' is the default order list.
     * @return the index of the event order list
     */
    public int getEventOrderList() {
        return eventOrderList;
    }

    /**
     * Sets the index of which event order list to use.
     * '0' is the default order list.
     * @param eventOrderList the index of the event order list
     */
    public void setEventOrderList(int eventOrderList) {
        this.eventOrderList = eventOrderList;
    }

    /**
     * Hooks up the event given to buttons
     * @param buttonEventHandler the button event handler
     */
    public void registerButtonEvents(EventHandler<ActionEvent> buttonEventHandler) {

    }

    /**
     * Installs the timeline context menu, and hooks it up to the given event.
     * @param contextEventHandler the event handler for handling context menu items
     */
    public void registerContextMenuEvents(EventHandler<ActionEvent> contextEventHandler) {
        timeline.installContextMenu(contextEventHandler);
    }

    /**
     * Refreshes the GUI concerned with events with the given data.
     * See {@link EventManager#getEvents()} on how the data is formatted.
     * @param events a 2-dimensional array containing all data on every event
     * @param eventOrder the order in which the events should be displayed
     */
    public void updateEvents(Object[][] events, Long[] eventOrder) {
        timeline.clear();
        if(events != null) {
            for (int i = 0; i < events.length; i++) {
                timeline.addEvent((Long) events[i][0], (String) events[i][1]);
            }
        }
        if(eventOrder != null)
            timeline.setEventOrder(eventOrder);

        timeline.recalculateLayout();

        //TODO insert code to update event list
    }

    /**
     * Returns a reference to the {@link EditEventDialog}, to be accessed directly
     * from {@link com.team34.controller.MainController}.
     * @return the edit event dialog.
     */
    public EditEventDialog getEditEventDialog() {
        return editEventDialog;
    }

}