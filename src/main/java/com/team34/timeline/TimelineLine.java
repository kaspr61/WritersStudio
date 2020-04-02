package com.team34.timeline;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class TimelineLine {

    private static final double VERTICAL_STOP_LENGTH = 20.0;

    private final Line timeline;
    private final Line verticalStart;
    private final Path arrow;

    protected TimelineLine() {
        timeline = new Line();
        timeline.getStyleClass().add("timeline-line");
        verticalStart = new Line();
        verticalStart.getStyleClass().add("timeline-line");
        arrow = new Path();
        arrow.getStyleClass().add("timeline-line");

        // Draw arrow path in local coordinates, then set layout position later.
        arrow.getElements().addAll(
                new MoveTo(-20.0, 12.0), new LineTo(-20.0, -12.0), new LineTo(0.0, 0.0), new LineTo(-20.0, 12.0)
        );
    }

    protected void addToPane(Pane pane) {
        pane.getChildren().addAll(timeline, verticalStart, arrow);
    }

    protected void recalculate(double posX, double posY, double length) {
        timeline.setStartX(posX);
        timeline.setStartY(posY);
        timeline.setEndX(posX+length-20.0);
        timeline.setEndY(posY);

        verticalStart.setStartX(posX);
        verticalStart.setStartY(posY-VERTICAL_STOP_LENGTH*0.5);
        verticalStart.setEndX(posX);
        verticalStart.setEndY(posY+VERTICAL_STOP_LENGTH*0.5);

        arrow.setLayoutX(posX+length);
        arrow.setLayoutY(posY);

    }

}
