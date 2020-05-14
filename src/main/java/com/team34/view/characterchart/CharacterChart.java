package com.team34.view.characterchart;

import com.team34.view.LabeledRectangle;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.HashMap;

public class CharacterChart {

    private final Pane pane;
    private ScrollPane scrollPane;

    private HashMap<Long, LabeledRectangle> rectMap; // Stores references to LabeledRectangles by their UID.

    public CharacterChart(double width, double height) {
        pane = new Pane();
        pane.setMinSize(width, height);
        pane.setPrefSize(width, height);

        scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setPrefViewportHeight(pane.getMinHeight());
        scrollPane.setPrefViewportWidth(pane.getMinWidth());
        scrollPane.setContent(pane);
        scrollPane.getStyleClass().add("characterchart-scrollpane");
        scrollPane.getStylesheets().add(com.team34.App.class.getResource("/css/main.css").toExternalForm());

        LabeledRectangle rect = new LabeledRectangle("Test 1", 100, 100);
        rect.setStylesheetClasses("characterchart-rect", "characterchart-text", "characterchart-tooltip");
        pane.getChildren().add(rect.getRect());
        pane.getChildren().add(rect.getText());

        rect = new LabeledRectangle("Test 2", 100, 100);
        rect.setStylesheetClasses("characterchart-rect", "characterchart-text", "characterchart-tooltip");
        rect.setX(300);
        rect.setY(300);
        pane.getChildren().add(rect.getRect());
        pane.getChildren().add(rect.getText());


    }

    /**
     * Adds the internal {@link javafx.scene.layout.Pane} as a child to the given Pane.
     * @param parentPane the Pane to which the internal Pane is to be added
     */
    public void addToPane(Pane parentPane) {
        parentPane.getChildren().add(scrollPane);
    }

}
