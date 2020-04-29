package com.team34.view.timeline;

import com.team34.model.event.Event;
import com.team34.view.MainView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

    private static final int CONTEXT_MENU_ITEM_EDIT = 0;
    private static final int CONTEXT_MENU_ITEM_ADD = 1;
    private static final int CONTEXT_MENU_ITEM_REMOVE = 2;

    private double posX;
    private double posY;
    private double width;
    private final double minWidth;
    private final Pane pane;
    private ScrollPane scrollPane;
    private TimelineLine line;
    private ContextMenu contextMenu;
    private MenuItem[] contextMenuItem;

    private EventHandler<ContextMenuEvent> evtShowContextEvent; // Fires when an event is right-clicked
    private EventHandler<ContextMenuEvent> evtShowContextPane; // Fires when the pane is right-clicked

    private HashMap<Long, EventRectangle> eventRectMap; // Stores references to EventRectangles by their eventUID.
    private Long[] eventUIDOrder; // This is a reference to the order of the events.

    /**
     * Creates a new instance of Timeline with the given minimum width in pixels.
     * @param minWidth the minimum width in pixels
     */
    public Timeline(double minWidth) {

        evtShowContextPane = new EventContextRequestPane();
        evtShowContextEvent = new EventContextRequestEvent();

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
        scrollPane.setOnContextMenuRequested(evtShowContextPane);

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
            existingRect.getRect().setOnContextMenuRequested(null);
            Tooltip.uninstall(existingRect.getRect(), existingRect.getTooltip());
        }

        EventRectangle rect = new EventRectangle(label, width);
        eventRectMap.put(eventUID, rect);

        pane.getChildren().add(rect.getRect());
        pane.getChildren().add(rect.getText());

        rect.getRect().setOnContextMenuRequested(evtShowContextEvent);

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
            rect.getRect().setOnContextMenuRequested(null);
            Tooltip.uninstall(rect.getRect(), rect.getTooltip());
        });

        eventRectMap.clear();
        eventUIDOrder = null;
    }

    public Long getEventUIDByRectangle(Rectangle rectangle) {
        Iterator<Map.Entry<Long, EventRectangle>> it = eventRectMap.entrySet().iterator();
        Map.Entry<Long, EventRectangle> pair;

        // Find EventRectangle that contains the input rectangle and return its associated UID
        while (it.hasNext()) {
            pair = it.next();
            if(pair.getValue().getRect().equals(rectangle)) {
                return pair.getKey();
            }
        }

        return -1L;
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

    public void installContextMenu(EventHandler<ActionEvent> contextEventHandler) {
        if(contextMenu != null)
            return;

        contextMenu = new ContextMenu();
        contextMenuItem = new MenuItem[3];

        //// Edit Event
        contextMenuItem[CONTEXT_MENU_ITEM_EDIT] = new MenuItem("Edit Event");
        contextMenuItem[CONTEXT_MENU_ITEM_EDIT].setId(MainView.ID_TIMELINE_EDIT_EVENT);
        contextMenuItem[CONTEXT_MENU_ITEM_EDIT].setOnAction(contextEventHandler);

        //// New Event
        contextMenuItem[CONTEXT_MENU_ITEM_ADD] = new MenuItem("New Event");
        contextMenuItem[CONTEXT_MENU_ITEM_ADD].setId(MainView.ID_TIMELINE_NEW_EVENT);
        contextMenuItem[CONTEXT_MENU_ITEM_ADD].setOnAction(contextEventHandler);

        //// Remove Event
        contextMenuItem[CONTEXT_MENU_ITEM_REMOVE] = new MenuItem("Remove Event");
        contextMenuItem[CONTEXT_MENU_ITEM_REMOVE].setId(MainView.ID_TIMELINE_REMOVE_EVENT);
        contextMenuItem[CONTEXT_MENU_ITEM_REMOVE].setOnAction(contextEventHandler);

        /////////////////////////////

        contextMenu.getItems().addAll(contextMenuItem);
        scrollPane.setContextMenu(contextMenu);
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    ////// EVENTS ////////////////////////////////////////////////////////////

    private class EventContextRequestPane implements EventHandler<ContextMenuEvent> {
        @Override
        public void handle(ContextMenuEvent e) {
            contextMenuItem[CONTEXT_MENU_ITEM_EDIT].setVisible(false);
            contextMenuItem[CONTEXT_MENU_ITEM_REMOVE].setVisible(false);
        }
    }

    private class EventContextRequestEvent implements EventHandler<ContextMenuEvent> {
        @Override
        public void handle(ContextMenuEvent e) {
            Long uid = getEventUIDByRectangle((Rectangle) e.getSource());
            if(uid == -1)
                return;

            contextMenu.setUserData(uid);
            contextMenuItem[CONTEXT_MENU_ITEM_EDIT].setVisible(true);
            contextMenuItem[CONTEXT_MENU_ITEM_REMOVE].setVisible(true);
            contextMenu.show((Node)e.getSource(), e.getScreenX(), e.getScreenY());
            e.consume();
        }
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

