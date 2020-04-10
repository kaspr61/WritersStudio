package com.team34.view.timeline;

import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * EventRectangle represents an event on the timeline. It contains all graphical
 * information needed to display the event using {@link javafx.scene.shape.Shape}s.
 * This class is only to be used internally by {@link Timeline}.
 * @author Kasper S. Skott
 */
class EventRectangle {

    private static final double TOOLTIP_SHOW_DELAY_MS = 500.0;
    static final double DEFAULT_WIDTH = 80.0;
    static final double DEFAULT_HEIGHT = 50.0;

    private final Text text;
    private final Rectangle rect;
    private final Rectangle clipRect;
    private final Tooltip tooltip;
    private double textOffsetX;
    private double textOffsetY;

    /**
     * Creates a new instance of EventRectangle with the given text label and width.
     * Also creates a tooltip, but doesn't install it. Tooltip installation is managed
     * by the {@link Timeline} when adding and clearing events.
     * @param label the text to be displayed within the rectangle
     * @param width the width of the rectangle. Set to 0.0 to use default width.
     */
    EventRectangle(String label, double width) {

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

    /**
     * Returns a reference to the internal {@link javafx.scene.text.Text} object.
     * @return the Text object
     */
    Text getText() {
        return text;
    }

    /**
     * Returns a reference to the internal {@link javafx.scene.shape.Rectangle} object.
     * @return the Rectangle object
     */
    Rectangle getRect() {
        return rect;
    }

    /**
     * Sets the x-position.
     * @param x the new x-position
     */
    void setX(double x) {
        rect.setX(x);
        text.setX(x + textOffsetX);
    }

    /**
     * Sets the y-position
     * @param y the new y-position
     */
    void setY(double y) {
        rect.setY(y);
        text.setY(y + textOffsetY);
    }

    /**
     * Returns the bounds, relative to the parent.
     * @return bounds relative to parent
     */
    Bounds getBoundsInParent() {
        return rect.getBoundsInParent();
    }

    /**
     * Returns the local bounds.
     * @return local bounds
     */
    Bounds getBoundsInLocal() {
        return rect.getBoundsInLocal();
    }

    /**
     * Returns a reference to the internal {@link javafx.scene.control.Tooltip} object.
     * @return the Tooltip object
     */
    Tooltip getTooltip() {
        return tooltip;
    }


}
