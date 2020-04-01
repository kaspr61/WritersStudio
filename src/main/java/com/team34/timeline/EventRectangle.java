package com.team34.timeline;

import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class EventRectangle {

    private static final double TOOLTIP_SHOW_DELAY_MS = 500;
    protected static final double DEFAULT_WIDTH = 80.0;
    protected static final double DEFAULT_HEIGHT = 50.0;

    private final Text text;
    private final Rectangle rect;
    private final Rectangle clipRect;
    private final Tooltip tooltip;
    private double textOffsetX;
    private double textOffsetY;

    protected EventRectangle(String label, double width) {

        double w = width < 1.0 ? DEFAULT_WIDTH : width;

        rect = new Rectangle(w, DEFAULT_HEIGHT);

        clipRect = new Rectangle(w, DEFAULT_HEIGHT);
        clipRect.xProperty().bind(rect.xProperty());
        clipRect.yProperty().bind(rect.yProperty());
        clipRect.widthProperty().bind(rect.widthProperty().multiply(0.9f));
        clipRect.heightProperty().bind(rect.heightProperty().multiply(0.9f));

        text = new Text(label);
        text.prefWidth(w);
        text.prefHeight(DEFAULT_HEIGHT);
        text.setLineSpacing(-3.0); // Set line space to less than specified in font.
        text.setTextAlignment(TextAlignment.CENTER);
        text.setTextOrigin(VPos.CENTER);
        text.setMouseTransparent(true); // Set to ignore mouse events
        text.setClip(clipRect);
        text.wrappingWidthProperty().bind(clipRect.widthProperty());

        textOffsetX = rect.getBoundsInParent().getCenterX() - text.getBoundsInParent().getCenterX();
        textOffsetY = rect.getBoundsInParent().getCenterY();

        rect.getStyleClass().add("timeline-event-rect");
        text.getStyleClass().add("timeline-event-text");

        tooltip = new Tooltip(label);
        tooltip.setShowDelay(Duration.millis(TOOLTIP_SHOW_DELAY_MS));
        tooltip.setHideDelay(Duration.millis(300.0));
        tooltip.getStyleClass().add("timeline-tooltip");

    }

    protected Text getText() {
        return text;
    }

    protected Rectangle getRect() {
        return rect;
    }

    protected void setX(double x) {
        rect.setX(x);
        text.setX(x + textOffsetX);
    }

    protected void setY(double y) {
        rect.setY(y);
        text.setY(y + textOffsetY);
    }

    protected Bounds getBoundsInParent() {
        return rect.getBoundsInParent();
    }

    protected Bounds getBoundsInLocal() {
        return rect.getBoundsInLocal();
    }

    protected Tooltip getTooltip() {
        return tooltip;
    }


}
