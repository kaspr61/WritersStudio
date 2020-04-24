package com.team34.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.team34.view.timeline.Timeline;

public class MainView {

    private static final double MIN_WINDOW_WIDTH = 800.0;
    private static final double MIN_WINDOW_HEIGHT = 600.0;
    private static final double MAX_WINDOW_WIDTH = 3840.0; // 4K Ultra HD
    private static final double MAX_WINDOW_HEIGHT = 2160.0; // 4K Ultra HD

    //// CONTROL IDs ///////////////////////////

    public static final String ID_BTN_EVENT_ADD = "BTN_EVENT_ADD";

    //// PANES /////////////////////////////////////////

    private final BorderPane rootPane;
    private final BorderPane contentBorderPane;
    private final StackPane centerPane;
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
    private int eventOrderList; // index to specify which order list to use

    ////////////////////////////////////////////////////

    /**
     *
     * @param mainStage
     * @param screenW
     * @param screenH
     * @param maximized
     * @author Kasper S. Skott
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
        centerPane = new StackPane();
        bottomPane = new StackPane();
        firstLayerSplit = new SplitPane();

        centerPane.setMinSize(screenW, 200.0);
        bottomPane.setMinSize(screenW, 120.0);

        firstLayerSplit.setOrientation(Orientation.VERTICAL);
        firstLayerSplit.getItems().addAll(centerPane, bottomPane);
        firstLayerSplit.setDividerPosition(0, 0.99);

        // Create the second-layer panes, contained by centerPane. These are separated vertically
        leftPane = new StackPane();
        rightPane = new StackPane();
        secondLayerSplit = new SplitPane();

        leftPane.setMinSize(100.0, 200.0);
        rightPane.setMinSize(250.0, 200.0);

        secondLayerSplit.setOrientation(Orientation.HORIZONTAL); // layed-out horizontally, but splitted vertically
        secondLayerSplit.getItems().addAll(leftPane, rightPane);
        secondLayerSplit.setDividerPosition(0, 0.2);
        centerPane.getChildren().add(secondLayerSplit);

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
    }

    private void setupTimeline(Pane parentPane, double screenW) {
        timeline = new Timeline(screenW);
        timeline.addToPane(parentPane);

        timeline.recalculateLayout();
    }

    private void setupRightPane() {

    }

    public int getEventOrderList() {
        return eventOrderList;
    }

    public void setEventOrderList(int eventOrderList) {
        this.eventOrderList = eventOrderList;
    }

    public void registerButtonEvents(EventHandler<ActionEvent> buttonEventHandler) {

    }

    public void updateEvents(Object[][] events, Long[] eventOrder) {
        timeline.clear();
        for (int i = 0; i < events.length; i++) {
            timeline.addEvent((Long)events[i][0], (String)events[i][1]);
        }
        timeline.setEventOrder(eventOrder);
        timeline.recalculateLayout();

        //TODO insert code to update event list
    }

}