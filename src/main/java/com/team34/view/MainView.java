package com.team34.view;

import javafx.geometry.Orientation;
import javafx.scene.Scene;
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


    private String cssMain;
    private Timeline timeline;

    /**
     *
     * @param mainStage
     * @param screenW
     * @param screenH
     * @param maximized
     * @author Kasper S. Skott
     */
    public MainView(Stage mainStage, double screenW, double screenH, boolean maximized) {

        // Create the root parent pane and the main scene
        BorderPane rootPane = new BorderPane();
        Scene mainScene = new Scene(rootPane, screenW, screenH);

        // Construct the path to the main .css file, and add it to the root pane
        cssMain = com.team34.App.class.getResource("css/main.css").toExternalForm();
        rootPane.getStylesheets().add(cssMain);

        // Create and add the menu bar
        com.team34.MenuBar menuBar = new com.team34.MenuBar();
        rootPane.setTop(menuBar);

        // Create the contentBorderPane
        BorderPane contentBorderPane = new BorderPane();

        // Create the first-layer panes. These are separated horizontally
        StackPane centerPane = new StackPane();
        StackPane bottomPane = new StackPane();
        SplitPane firstLayerSplit = new SplitPane();

        centerPane.setMinSize(screenW, 200.0);
        bottomPane.setMinSize(screenW, 120.0);

        firstLayerSplit.setOrientation(Orientation.VERTICAL);
        firstLayerSplit.getItems().addAll(centerPane, bottomPane);
        firstLayerSplit.setDividerPosition(0, 0.99);

        // Create the second-layer panes, contained by centerPane. These are separated vertically
        StackPane leftPane = new StackPane();
        StackPane rightPane = new StackPane();
        SplitPane secondLayerSplit = new SplitPane();

        leftPane.setMinSize(100.0, 200.0);
        rightPane.setMinSize(250.0, 200.0);

        secondLayerSplit.setOrientation(Orientation.HORIZONTAL);
        secondLayerSplit.getItems().addAll(leftPane, rightPane);
        secondLayerSplit.setDividerPosition(0, 0.2);
        centerPane.getChildren().add(secondLayerSplit);

        // Add split the first layer split pane to the contentBorderPane
        contentBorderPane.setCenter(firstLayerSplit);

        // Add the contentBorderPane to the root pane
        rootPane.setCenter(contentBorderPane);

        // Setup timeline
        setupTimeline(bottomPane, screenW);

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

    public void setupTimeline(Pane parentPane, double screenW) {
        if(timeline != null)
            return;

        timeline = new Timeline(screenW);
        timeline.addToPane(parentPane);

        timeline.addEvent(0L, "Event A");
        timeline.addEvent(1L, "Event B");
        timeline.addEvent(2L, "Event C");

        Long[] order = { 0L, 1L, 2L };
        timeline.recalculateLayout(order);
    }

}