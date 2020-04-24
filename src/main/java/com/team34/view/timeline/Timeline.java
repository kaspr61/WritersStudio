package com.team34.view.timeline;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import java.util.HashMap;

/**
 * Timeline is the main com.team34.view class of the timeline feature.
 * It acts as a wrapper of various objects defined in the javafx.scene package.
 * Events are represented as rectangles, containing text. To add an event,
 * the eventUID of the event must be provided along with the name. Events are not, however,
 * displayed in the order they are added. In order to allow flexibility the order is represented
 * by an array of eventUIDs that are inputted as parameters to the Timeline when needed.
 * <p>
 * In order to use this class, consider the following:
 * <ol>
 *     <li>Construct the list, and provide a minimum width in pixels
 *     <li>Add events
 *     <li>Add the timeline to a {@link javafx.scene.layout.Pane}.
 *     <li>Either call {@link Timeline#setEventOrder(Long[])}, then call {@link Timeline#recalculateLayout()},
 *     or simply call the method {@link Timeline#recalculateLayout(Long[])}
 * </ol>
 * <p>
 * Example usage:
 * <code style=display:block;white-space:pre-wrap>
 * VBox vBox = new vBox();
 * Timeline timeline = new Timeline(300.0);
 *
 * timeline.addEvent(0L, "Event A");
 * timeline.addEvent(1L, "Event B");
 * timeline.addEvent(2L, "Event C");
 * timeline.addEvent(3L, "Event D");
 *
 * timeline.addToPane(vBox);
 *
 * Long[] eventOrder = {2L,1L,3L,0L};
 * timeline.recalculateLayout(eventOrder);
 * </code>
 * @author Kasper S. Skott
 */
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

    /**
     * Creates a new instance of Timeline with the given minimum width in pixels.
     * @param minWidth the minimum width in pixels
     */
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

    /**
     * Adds an event to be shown in the timeline.
     * This does not correctly set the layout of the associated rectangle.
     * To correctly display the events, {@link Timeline#recalculateLayout()} must be
     * called after all events have been added.
     * @param eventUID the unique ID, associated with the event throughout the project
     * @param label the text that is to be displayed within the rectangle
     * @param width the width of the rectangle. Set to 0.0 to use default
     */
    public void addEvent(long eventUID, String label, double width) {
        EventRectangle existingRect = eventRectMap.get(eventUID);

        if(existingRect != null) { // If the event is getting overwritten, remove the old shapes first.
            pane.getChildren().removeAll(existingRect.getRect(), existingRect.getText());
            Tooltip.uninstall(existingRect.getRect(), existingRect.getTooltip());
        }

        EventRectangle rect = new EventRectangle(label, width);
        eventRectMap.put(eventUID, rect);

        pane.getChildren().add(rect.getRect());
        pane.getChildren().add(rect.getText());

        Tooltip.install(rect.getRect(), rect.getTooltip());
    }

    /**
     * Adds an event to be shown in the timeline, with the default width.
     * See {@link Timeline#addEvent(long, String, double)} for details.
     * @param eventUID the unique ID, associated with the event throughout the project
     * @param label the text that is to be displayed within the rectangle
     */
    public void addEvent(long eventUID, String label) {
        addEvent(eventUID, label, 0.0);
    }

    /**
     * Clears the events that have been added, uninstalls their tooltip, and clears the order of events.
     */
    public void clear() {
        eventRectMap.forEach((uid, rect) -> {
            pane.getChildren().removeAll(rect.getRect(), rect.getText());
            Tooltip.uninstall(rect.getRect(), rect.getTooltip());
        });

        eventRectMap.clear();
        eventUIDOrder = null;
    }

    /**
     * Adds the internal {@link javafx.scene.layout.Pane} as a child to the given Pane.
     * @param parentPane the Pane to which the internal Pane is to be added
     */
    public void addToPane(Pane parentPane) {
        parentPane.getChildren().add(scrollPane);
    }

    /**
     * Sets the order of which the events are displayed in the timeline.
     * This order is stored for use when recalculating the layout. This method also gets called
     * in {@link Timeline#recalculateLayout(Long[])}.
     * @param eventUIDs the array of eventUIDs, in the order that they are to be displayed
     */
    public void setEventOrder(Long[] eventUIDs) {
        eventUIDOrder = eventUIDs;
    }

    /**
     * Recalculates and sets the correct positions and layout of all graphical elements.
     * Call this method after adding, clearing, or changing the order of events.
     * If events have been added that would overflow the current width of the timeline,
     * it will be resized to fit the current events. However, if events have been removed,
     * the timeline will never shrink to less than the minimum width, specified in the constructor.
     * @param eventUIDs the array of eventUIDs, in the order that they are to be displayed
     */
    public void recalculateLayout(Long[] eventUIDs) {
        if(eventUIDs != null)
            setEventOrder(eventUIDs);

        // Recalculate position
        posX = LAYOUT_SPACING;
        posY = pane.getMinHeight() / 2.0;

        // Reset positions of event rectangles according to the given order.
        double y = posY - EventRectangle.DEFAULT_HEIGHT / 2.0;
        double nextX = posX + LAYOUT_SPACING;

        if(eventUIDOrder != null) {
            for (int i = 0; i < eventUIDOrder.length; i++) {
                EventRectangle rect = eventRectMap.get(eventUIDOrder[i]);
                if (rect == null)
                    continue;

                rect.setX(nextX);
                rect.setY(y);

                nextX += rect.getBoundsInLocal().getWidth() + LAYOUT_SPACING; // take individual width into account
            }
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

    /**
     * Recalculates and sets the correct positions and layout of all graphical elements,
     * using the event order that has been set beforehand.
     * See {@link Timeline#recalculateLayout(Long[])} for details.
     */
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

