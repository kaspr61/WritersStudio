package com.team34.view;

import com.team34.view.characterchart.CharacterChart;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;
import java.util.ArrayList;

import com.team34.model.event.EventManager;
import com.team34.view.character.CharacterList;
import com.team34.view.event.EventList;
import com.team34.view.dialogs.EditEventDialog;
import com.team34.view.dialogs.EditCharacterDialog;
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

    public static final String ID_BTN_CHARACTERLIST_ADD = "ID_BTN_CHARACTERLIST_ADD";
    public static final String ID_BTN_CHARACTERLIST_EDIT = "ID_BTN_CHARACTERLIST_EDIT";
    public static final String ID_BTN_CHARACTERLIST_DELETE = "ID_BTN_CHARACTERLIST_DELETE";
    public static final String ID_BTN_EVENT_ADD = "BTN_EVENT_ADD";
    public static final String ID_BTN_EVENT_EDIT = "BTN_EVENT_EDIT";
    public static final String ID_BTN_EVENT_DELETE = "BTN_EVENT_DELETE";

    public static final String ID_TIMELINE_NEW_EVENT = "TIMELINE_NEW_EVENT";
    public static final String ID_TIMELINE_REMOVE_EVENT = "TIMELINE_REMOVE_EVENT";
    public static final String ID_TIMELINE_EDIT_EVENT = "TIMELINE_EDIT_EVENT";

    public static final String ID_MENU_NEW = "MENU_NEW_PROJECT";
    public static final String ID_MENU_OPEN = "MENU_OPEN_PROJECT";
    public static final String ID_MENU_SAVE = "MENU_SAVE";
    public static final String ID_MENU_SAVE_AS = "MENU_SAVE_AS";
    public static final String ID_MENU_EXIT = "MENU_EXIT";

    public static final String ID_CHART_NEW_ASSOCIATION = "CHART_NEW_ASSOCIATION";
    public static final String ID_CHART_EDIT_CHARACTER = "CHART_EDIT_CHARACTER";
    public static final String ID_CHART_REMOVE_CHARACTER = "CHART_REMOVE_CHARACTER";
    public static final String ID_CHART_NEW_CHARACTER = "CHART_NEW_CHARACTER";

    //// PANES /////////////////////////////////////////

    private final BorderPane rootPane;
    private final BorderPane contentBorderPane;
    private final StackPane topPane;
    private final StackPane bottomPane;
    private final SplitPane firstLayerSplit;
    private final EventList leftPane;
    private final StackPane centerPane;
    private final CharacterList rightPane;
    private final SplitPane secondLayerSplit;

    //// CONTROLS //////////////////////////////////////

    private MenuBar menuBar;

    ////////////////////////////////////////////////////

    private Stage mainStage;
    private Scene mainScene;
    private String cssMain;
    private Timeline timeline;
    private CharacterChart characterChart;
    private EditEventDialog editEventDialog;
    private EditCharacterDialog editCharacterPanel;
    private int eventOrderList; // index to specify which order list to use
    private double lastChartMouseClickX;
    private double lastChartMouseClickY;

////////////////////////////////////////////////////

    /**
     * Constructs the GUI.
     * <p>
     * Calls {@link MainView#setupTimeline(Pane, double)}.
     *
     * @param mainStage the stage associated with the {@link javafx.application.Application}.
     * @param screenW the width the window should be at
     * @param screenH the height the window should be at
     * @param maximized true if window should start maximized
     */
    public MainView(Stage mainStage, double screenW, double screenH, boolean maximized) {
        eventOrderList = 0;
        this.mainStage = mainStage;
        lastChartMouseClickX = 0.0;
        lastChartMouseClickY = 0.0;

        // Create the root parent pane and the main scene
        rootPane = new BorderPane();
        mainScene = new Scene(rootPane, screenW, screenH);

        // Construct the path to the main .css file, and add it to the root pane
        cssMain = com.team34.App.class.getResource("/css/main.css").toExternalForm();
        rootPane.getStylesheets().add(cssMain);

        // Create and add the menu bar
        menuBar = new MenuBar(mainStage);
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
        leftPane = new EventList(); // Contains event list
        centerPane = new StackPane(); // Contains character chart
        rightPane = new CharacterList(); // Contains character list
        secondLayerSplit = new SplitPane();

        leftPane.setMinSize(250.0, 200.0);
        rightPane.setMinSize(250.0, 200.0);

        secondLayerSplit.setOrientation(Orientation.HORIZONTAL); // layed-out horizontally, but splitted vertically
        secondLayerSplit.getItems().addAll(leftPane, centerPane, rightPane);
        secondLayerSplit.setDividerPosition(0, 0.25);
        secondLayerSplit.setDividerPosition(1, 0.99);
        topPane.getChildren().add(secondLayerSplit);

        // Add split the first layer split pane to the contentBorderPane
        contentBorderPane.setCenter(firstLayerSplit);

        // Add the contentBorderPane to the root pane
        rootPane.setCenter(contentBorderPane);

        // Set up character chart
        characterChart = new CharacterChart(centerPane.getWidth(), centerPane.getHeight());
        characterChart.addToPane(centerPane);

        // Set up timeline
        setupTimeline(bottomPane, screenW);

        // Register mouse event to keep track of mouse position when clicked
        centerPane.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            lastChartMouseClickX = e.getX();
            lastChartMouseClickY = e.getY();
        });

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

        // Create character dialog
        editCharacterPanel = new EditCharacterDialog(mainStage);
    }

    /**
     * Returns a reference to the main stage.
     * @return the main stage
     */
    public Stage getMainStage() {
        return mainStage;
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
     * Returns a reference to the {@link EditCharacterDialog}, to be accessed directly
     * from {@link com.team34.controller.MainController}.
     * @return the edit character dialog
     */
    public EditCharacterDialog getEditCharacterPanel() {
        return editCharacterPanel;
    }

    /**
     * Returns a reference to the {@link EditEventDialog}, to be accessed directly
     * from {@link com.team34.controller.MainController}.
     * @return the edit event dialog.
     */
    public EditEventDialog getEditEventDialog() {
        return editEventDialog;
    }

    public double getLastChartMouseClickX() {
        return lastChartMouseClickX;
    }

    public double getLastChartMouseClickY() {
        return lastChartMouseClickY;
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
     * Returns the context menu of the timeline
     * @return the context menu of the timeline
     */
    public ContextMenu getTimelineContextMenu() {
        return timeline.getContextMenu();
    }

    /**
     * Returns the context menu of the character chart
     * @return the context menu of the character chart
     */
    public ContextMenu getChartContextMenu() {
        return characterChart.getContextMenu();
    }

    public Double[] snapToNearestCharacterEdge(long startingCharacterUID, double x, double y) {
        return characterChart.snapToNearestCharacterEdge(startingCharacterUID, x, y);
    }

    /**
     * Hooks up the event given to buttons
     * @param buttonEventHandler the button event handler
     */
    public void registerButtonEvents(EventHandler<ActionEvent> buttonEventHandler) {
        rightPane.registerButtonEvents(buttonEventHandler);
        leftPane.registerButtonEvents(buttonEventHandler);
    }

    /**
     * Installs the timeline context menu, and hooks it up to the given event.
     * @param contextEventHandler the event handler for handling context menu items
     */
    public void registerContextMenuEvents(EventHandler<ActionEvent> contextEventHandler) {
        timeline.installContextMenu(contextEventHandler);
        characterChart.installContextMenu(contextEventHandler);
    }

    /**
     * Registers the given EventHandler on the mainStage.
     * @param windowEventHandler the event handler
     */
    public void registerCloseRequestEvent(EventHandler<WindowEvent> windowEventHandler) {
        mainStage.setOnCloseRequest(windowEventHandler);
    }

    /**
     * Registers the given EventHandler on the menuBar
     * @param menuEventHandler the event handler
     */
    public void registerMenuBarActionEvents(EventHandler<ActionEvent> menuEventHandler) {
        menuBar.registerMenuBarAction(menuEventHandler);
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

        leftPane.updateListView(events);
    }

    /**
     * Fires a close request event on the main stage.
     */
    public void exitApplication() {
        mainStage.fireEvent(new WindowEvent(mainStage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    /**
     * Shows a dialog that waits for the user to click one of the buttons.
     * The user is prompted with "Would you like to save your project?".
     * The choices are Yes, No, and Cancel.
     * @return the clicked button type. Either YES, NO, CANCEL, or CLOSE.
     */
    public ButtonType showUnsavedChangesDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Unsaved changes");
        dialog.setContentText("Would you like to save your project?");
        dialog.getDialogPane().getButtonTypes().addAll(
                ButtonType.YES, ButtonType.NO, ButtonType.CANCEL
        );

        Optional<ButtonType> result = dialog.showAndWait();
        if(!result.isPresent())
            return ButtonType.CLOSE;
        else
            return result.get();
    }

    /**
     * Sends an array list of object arrays containing character data to the CharacterList class.
     * @param characters ArrayList of Object[]
     */
    public void updateCharacterList(ArrayList<Object[]> characters, Object[][] associations) {
        rightPane.updateListView(characters);
        characterChart.updateCharacters(characters, associations);
    }

    /**
     * Returns the UID of the selected character in the character list
     * @author Jim Andersson
     * @return UID
     */
    public long getCharacterUID() {
        return rightPane.getCharacterUID();
    }

    public long getSelectedEventUID() {
        return leftPane.getEventUID();
    }

    public long onCharacterPlaced(Object source) {
        return characterChart.onCharacterPlaced(source);
    }

    public void registerCharacterChartEvents(EventHandler<MouseEvent> evtCharacterReleased) {
        characterChart.registerEvents(evtCharacterReleased);
    }

    public Object[] getChartCharacterData(long uid) {
        return characterChart.getChartCharacterData(uid);
    }

    public void startCharacterAssociationDrag(long assocUID, boolean endPoint) {
        characterChart.startAssociationPointClickedDrag(assocUID, endPoint);
    }

}