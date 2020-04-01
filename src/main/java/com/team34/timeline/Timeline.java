package com.team34.timeline;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import java.util.HashMap;

public class Timeline {

    private static final int INITIAL_EVENT_CAPACITY = 20;
    private static final double LAYOUT_SPACING = 20.0;

    private double posX;
    private double posY;
    private double width;
    private final double minWidth;
    private final Pane pane;
    private Pane parentPane;
    private Line lineShape;

    private HashMap<Long, EventRectangle> eventRectMap; // Stores references to EventRectangles by their eventUID.
    Long[] eventUIDOrder; // This is a reference to the order of the events.

    public Timeline(double posX, double posY, double minWidth) {
        pane = new Pane();

        this.posX = posX;
        this.posY = posY;
        this.width = minWidth;
        this.minWidth = minWidth;

        lineShape = new Line(posX, posY, posX+width, posY);
        lineShape.getStyleClass().add("timeline-line");

        pane.getChildren().add(lineShape);

        eventRectMap = new HashMap<>(INITIAL_EVENT_CAPACITY);

    }

/*
    private void eventOnMouseDragged(javafx.scene.input.MouseEvent e, EventRectangle rect) {
        rect.setX(e.getX());
        rect.setY(e.getY());
        for(Node n : pane.getChildren()) {
            if(!(n instanceof EventRectangle) || n.equals(rect))
                continue;

            Shape shape = (Shape) n;
            if(Shape.intersect(rect, shape).getBoundsInLocal().getWidth() != -1) {
                // Collision detected
            }
            else {
                // No collision detected
                shape.applyCss();
            }
        }

    }
*/

/*
    private void eventOnMouseDraggedEntered(javafx.scene.input.DragEvent e, EventRectangle rect) {
        rect.setViewOrder(-1.0);
        rect.getText().setViewOrder(-1.0);
        System.out.println("enter");
    }
*/

/*
    private void eventOnMouseDraggedReleased(javafx.scene.input.DragEvent e, EventRectangle rect) {
        rect.setViewOrder(0.0);
        rect.getText().setViewOrder(0.0);
        System.out.println("released");

    }
*/

    public void addEvent(long eventUID, String label, double width) {
        EventRectangle existingRect = eventRectMap.get(eventUID);

        if(existingRect != null) { // If the event is getting overwritten, remove the old shapes first.
            pane.getChildren().removeAll(existingRect.getRect(), existingRect.getText());
        }

        EventRectangle rect = new EventRectangle(label, width);
        eventRectMap.put(eventUID, rect);

        pane.getChildren().add(rect.getRect());
        pane.getChildren().add(rect.getText());

        Tooltip.install(rect.getRect(), rect.getTooltip());

        //TODO rework drag events
//        rect.setOnMouseDragged((e) -> eventOnMouseDragged(e, rect));
//        rect.setOnDragEntered((e) -> eventOnMouseDraggedEntered(e, rect));
//        rect.setOnDragExited((e) -> eventOnMouseDraggedReleased(e, rect));
    }

    public void clear() {

        eventRectMap.forEach((uid, rect) -> {
            pane.getChildren().removeAll(rect.getRect(), rect.getText());
            Tooltip.uninstall(rect.getRect(), rect.getTooltip());
        });

        eventRectMap.clear();
        eventUIDOrder = null;

    }

    public void addEvent(long eventUID, String label) {
        addEvent(eventUID, label, 0.0);
    }

    public void addToParentPane(Pane parentPane) {
        this.parentPane = parentPane;
        this.parentPane.getChildren().add(this.pane);
    }

    public void setEventOrder(Long[] eventUIDs) {
        eventUIDOrder = eventUIDs;
    }

    public void recalculateLayout() {
        recalculateLayout(eventUIDOrder);
    }

    public void recalculateLayout(Long[] eventUIDs) {
        if(eventUIDs == null)
            throw new NullPointerException("eventUIDs was null");

        eventUIDOrder = eventUIDs;

        double y = posY - EventRectangle.DEFAULT_HEIGHT / 2.0;
        double nextX = posX + LAYOUT_SPACING;

        // Reset positions of event rectangles according to the given order.
        for (int i = 0; i < eventUIDOrder.length; i++) {
            EventRectangle rect = eventRectMap.get(eventUIDOrder[i]);
            if(rect == null) {
                continue;
            }

            rect.setX(nextX);
            rect.setY(y);

            nextX += rect.getBoundsInLocal().getWidth() + LAYOUT_SPACING; // take individual width into account
        }

        // Adjust timeline length (width) if necessary


        if(nextX - posX > minWidth)
            width = nextX - posX;
        else
            width = minWidth;

        // Reset the timeline line shape.
        lineShape.setStartX(posX);
        lineShape.setStartY(posY);
        lineShape.setEndX(posX+width);
        lineShape.setEndY(posY);

    }


}
