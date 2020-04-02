package com.team34.timeline;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import java.util.HashMap;

public class Timeline {

    private static final int INITIAL_EVENT_CAPACITY = 20;
    private static final double LAYOUT_SPACING = 20.0;

    private double posX;
    private double posY;
    private double width;
    private final double minWidth;
    private final Pane pane;
    private ScrollPane scrollPane;
    private TimelineLine line;

    private HashMap<Long, EventRectangle> eventRectMap; // Stores references to EventRectangles by their eventUID.
    private Long[] eventUIDOrder; // This is a reference to the order of the events.

    public Timeline(double minWidth) {
        pane = new Pane();

        pane.setMinSize(minWidth, EventRectangle.DEFAULT_HEIGHT + LAYOUT_SPACING + LAYOUT_SPACING);
        pane.setPrefSize(minWidth, pane.getMinHeight());
        pane.setMaxHeight(pane.getMinHeight());

        scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setMinViewportHeight(pane.getMinHeight());
        scrollPane.setPrefViewportHeight(pane.getMinHeight());
        scrollPane.setContent(pane);


        this.posX = 0.0;
        this.posY = 0.0;
        this.width = minWidth;
        this.minWidth = minWidth;

        line = new TimelineLine();
        line.addToPane(pane);

        eventRectMap = new HashMap<>(INITIAL_EVENT_CAPACITY);

    }

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

//        rect.setOnMouseDragged((e) -> eventOnMouseDragged(e, rect));
    }

    public void addEvent(long eventUID, String label) {
        addEvent(eventUID, label, 0.0);
    }

    public void clear() {

        eventRectMap.forEach((uid, rect) -> {
            pane.getChildren().removeAll(rect.getRect(), rect.getText());
            Tooltip.uninstall(rect.getRect(), rect.getTooltip());
        });

        eventRectMap.clear();
        eventUIDOrder = null;

    }

    public void addToPane(Pane parentPane) {
        parentPane.getChildren().add(scrollPane);
    }

    public void setEventOrder(Long[] eventUIDs) {
        eventUIDOrder = eventUIDs;
    }

    public void recalculateLayout(Long[] eventUIDs) {
        if(eventUIDs == null)
            throw new NullPointerException("eventUIDs was null");

        if(scrollPane == null)
            throw new NullPointerException("a timeline must be added to a ScrollPane");

        eventUIDOrder = eventUIDs;

        // Recalculate position
        posX = LAYOUT_SPACING;
        posY = pane.getMinHeight() / 2.0;

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
        nextX += LAYOUT_SPACING; // end should have more space
        if(nextX - posX > minWidth)
            width = nextX - posX;
        else
            width = minWidth;

        // Readjust pane sizes
        pane.setMinWidth(width + LAYOUT_SPACING + LAYOUT_SPACING);
        pane.setPrefSize(pane.getMinWidth(), pane.getMinHeight());

        scrollPane.setMinViewportHeight(pane.getMinHeight());
        scrollPane.setPrefViewportHeight(pane.getMinHeight());

        // Recalculate the timeline line shapes.
        line.recalculate(posX, posY, width);

    }

    public void recalculateLayout() {
        recalculateLayout(eventUIDOrder);
    }

}


  ///////////////////////////////////////////////////////////////
 ////// Reference for later when implementing drag events //////
///////////////////////////////////////////////////////////////

/*  private void eventOnMouseDragged(javafx.scene.input.MouseEvent e, EventRectangle rect) {
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

    }*/

