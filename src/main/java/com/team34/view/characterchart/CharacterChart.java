package com.team34.view.characterchart;

import com.team34.view.LabeledRectangle;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CharacterChart {

    private final Pane pane;
    private ScrollPane scrollPane;

    private HashMap<Long, CharacterRectangle> rectMap; // Stores references to CharacterRectangles by their UID.
    private HashMap<Long, AssociationPoint> assocPoints;
    private HashMap<Long, Association> associations;
    private long lastAssocPtClicked;
    private AssociationPoint originalAssocPtClicked;
    private Rectangle currentRectDragOver;

    private final EventHandler<MouseEvent> evtRectPressed;
    private final EventHandler<MouseEvent> evtRectDragged;
    private final EventHandler<MouseEvent> evtRectReleased;

    public CharacterChart(double width, double height) {
        rectMap = new HashMap<>();
        assocPoints = new HashMap<>();
        associations = new HashMap<>();
        lastAssocPtClicked = -1L;

        evtRectPressed = new EventRectanglePressed();
        evtRectDragged = new EventRectangleDragged();
        evtRectReleased = new EventRectangleReleased();

        pane = new Pane();
//        pane.setMinSize(width, height);
//        pane.setPrefSize(width, height);
//        pane.setMaxSize(width, height);

        scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
//        scrollPane.setMinViewportHeight(height);
//        scrollPane.setMinViewportWidth(width);
//        scrollPane.setPrefViewportHeight(pane.getMinHeight());
//        scrollPane.setPrefViewportWidth(pane.getMinWidth());
        scrollPane.setContent(pane);
        scrollPane.getStyleClass().add("characterchart-scrollpane");
        scrollPane.getStylesheets().add(com.team34.App.class.getResource("/css/main.css").toExternalForm());

    }

    /**
     * Adds the internal {@link javafx.scene.layout.Pane} as a child to the given Pane.
     * @param parentPane the Pane to which the internal Pane is to be added
     */
    public void addToPane(Pane parentPane) {
        parentPane.getChildren().add(scrollPane);
    }

    public void addCharacter(long uid, String name) {
        CharacterRectangle existingRect = rectMap.get(uid);

        if(existingRect != null) { // If the event is getting overwritten, remove the old shapes first.
            pane.getChildren().removeAll(existingRect.getRect(), existingRect.getText());
            Tooltip.uninstall(existingRect.getRect(), existingRect.getTooltip());
        }

        CharacterRectangle rect = new CharacterRectangle(name, 0.0, 0.0);
        rect.setStylesheetClasses("characterchart-rect", "characterchart-text", "characterchart-tooltip");
        rectMap.put(uid, rect);

        pane.getChildren().add(rect.getRect());
        pane.getChildren().add(rect.getText());

        Tooltip.install(rect.getRect(), rect.getTooltip());

        rect.getRect().setOnMousePressed(evtRectPressed);
        rect.getRect().setOnMouseDragged(evtRectDragged);
        rect.getRect().setOnMouseReleased(evtRectReleased);
        rect.getRect().setOnMouseDragEntered(evtRectMouseDragEntered);
        rect.getRect().setOnMouseDragExited(evtRectMouseDragExited);
        rect.getRect().setOnMouseDragReleased(evtRectMouseDragReleased);

    }

    private Map.Entry<Long, CharacterRectangle> getCharacterByRectangle(Rectangle rect) {
        Iterator<Map.Entry<Long, CharacterRectangle>> it = rectMap.entrySet().iterator();
        Map.Entry<Long, CharacterRectangle> pair;

        // Find CharacterRectangle that contains the input rectangle and return its associated UID
        while (it.hasNext()) {
            pair = it.next();
            if(pair.getValue().getRect().equals(rect)) {
                return pair;
            }
        }

        return null;
    }

    private Map.Entry<Long, AssociationPoint> getAssocPointByControl(Circle circle) {
        Iterator<Map.Entry<Long, AssociationPoint>> it = assocPoints.entrySet().iterator();
        Map.Entry<Long, AssociationPoint> pair;

        // Find AssociationPoint that contains the input circle and return its associated UID
        while (it.hasNext()) {
            pair = it.next();
            if(pair.getValue().control.equals(circle)) {
                return pair;
            }
        }

        return null;
    }

    public void addAssociation(long assocUID, long startPtUID, long endPtUID, long startCharUID, long endCharUID, String label)
    {
        Association exisitingAssoc = associations.get(assocUID);
        if(exisitingAssoc != null) {
            AssociationPoint start = assocPoints.get(startPtUID);
            AssociationPoint end = assocPoints.get(endPtUID);
            pane.getChildren().removeAll(exisitingAssoc.text, exisitingAssoc.line, start.control, end.control);
        }

        AssociationPoint startPt = new AssociationPoint(false);
        AssociationPoint endPt = new AssociationPoint(true);

        Association assoc = new Association();
        assoc.uid = assocUID;
        assoc.text = new Text(label);
        assoc.text.getStyleClass().add("characterchart-assoclabel");
        assoc.line = new Line(0, 0, 0, 0);
        assoc.line.getStyleClass().add("characterchart-assocline");
        assoc.line.setViewOrder(-1.5);
        assoc.line.setMouseTransparent(true);

        startPt.assocUID = assocUID;
        startPt.rectUID = startCharUID;
        startPt.x = 0;
        startPt.y = 0;
        startPt.control = new Circle(8.0);
        startPt.control.setViewOrder(-2.0);
        startPt.control.getStyleClass().add("characterchart-line-control");
        startPt.control.setOnMousePressed(evtAPMousePressed);
        startPt.control.setOnMouseReleased(evtAPMouseReleased);
        startPt.control.setOnDragDetected(evtAPDragDetected);
        startPt.control.setOnMouseDragged(evtAPMouseDragged);

        endPt.assocUID = assocUID;
        endPt.rectUID = endCharUID;
        endPt.x = 0;
        endPt.y = 0;
        endPt.control = new Circle(8.0);
        endPt.control.setViewOrder(-2.0);
        endPt.control.getStyleClass().add("characterchart-line-control");
        endPt.control.setOnMousePressed(evtAPMousePressed);
        endPt.control.setOnMouseReleased(evtAPMouseReleased);
        endPt.control.setOnDragDetected(evtAPDragDetected);
        endPt.control.setOnMouseDragged(evtAPMouseDragged);

        associations.put(assocUID, assoc);
        assocPoints.put(startPtUID, startPt);
        assocPoints.put(endPtUID, endPt);

        pane.getChildren().addAll(assoc.line, assoc.text, startPt.control, endPt.control);

        if(startCharUID != -1L)
            rectMap.get(startCharUID).addAssociationPoint(startPtUID);

        if(endCharUID != -1L)
            rectMap.get(endCharUID).addAssociationPoint(endPtUID);

    }

    public void setAssociationPointPosition(long assocPtUID, double x, double y) {
        AssociationPoint pt = assocPoints.get(assocPtUID);
        pt.x = x;
        pt.y = y;
        updateAssociationLinePoint(pt);
    }

    public void attachAssociationPointToCharacter(long assocPtUID, long characterUID) {
        if(assocPtUID == -1L || characterUID == -1L)
            return;

        rectMap.get(characterUID).addAssociationPoint(assocPtUID);
        assocPoints.get(assocPtUID).rectUID = characterUID;
    }

    public void detachAssociationPointFromCharacter(long assocPtUID, long characterUID) {
        if(assocPtUID == -1L || characterUID == -1L)
            return;

        rectMap.get(characterUID).removeAssociationPoint(assocPtUID);
        assocPoints.get(assocPtUID).rectUID = -1L;
    }

    public void setCharacterPosition(long uid, double x, double y) {
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;

        CharacterRectangle rect = rectMap.get(uid);
        double dx = x - rect.getX();
        double dy = y - rect.getY();

        rect.setX(x);
        rect.setY(y);

        Long[] assocPts = rect.getAssociationPoints();
        if(assocPts == null)
            return;

        for (int i = 0; i < assocPts.length; i++) {
            AssociationPoint pt = assocPoints.get(assocPts[i]);
            pt.x += dx;
            pt.y += dy;
            updateAssociationLinePoint(pt);
        }

    }

    private void updateAssociationLinePoint(AssociationPoint point) {
        Line line = associations.get(point.assocUID).line;

        point.control.setCenterX(point.x);
        point.control.setCenterY(point.y);

        if(point.end) {
            line.setEndX(point.x);
            line.setEndY(point.y);
        }
        else {
            line.setStartX(point.x);
            line.setStartY(point.y);
        }
    }

    /********************* EVENT LAMBDAS ***********************/

    private EventHandler<MouseEvent> evtAPMouseDragged = e -> {
        if(lastAssocPtClicked == -1L)
            return;

        double x = e.getX();
        double y = e.getY();
        if(currentRectDragOver != null) {
            boolean invalidX = false, invalidY = false;

            if(x < currentRectDragOver.getX() + currentRectDragOver.getWidth() * 0.2) // snap vertically
                x = currentRectDragOver.getX();
            else if(x > currentRectDragOver.getX() + currentRectDragOver.getWidth() * 0.8)
                x = currentRectDragOver.getX() + currentRectDragOver.getWidth();
            else
                invalidX = true;

            if(y < currentRectDragOver.getY() + currentRectDragOver.getHeight() * 0.2) // snap horizontally
                y = currentRectDragOver.getY();
            else if(y > currentRectDragOver.getY() + currentRectDragOver.getHeight() * 0.8)
                y = currentRectDragOver.getY() + currentRectDragOver.getHeight();
            else
                invalidY = true;

            // If mouse is in the center, snap to nearest edge
            if(invalidX && invalidY) {
                double centerX = currentRectDragOver.getX() + currentRectDragOver.getWidth() * 0.5;
                double centerY = currentRectDragOver.getY() + currentRectDragOver.getHeight() * 0.5;

                // Snap to left edge
                if(x < centerX && y > currentRectDragOver.getY() + currentRectDragOver.getHeight() * 0.33 &&
                        y < currentRectDragOver.getY() + currentRectDragOver.getHeight() * 0.67) {
                    x = currentRectDragOver.getX();
                    y = centerY;
                } // Snap to right edge
                else if(x >= centerX && y > currentRectDragOver.getY() + currentRectDragOver.getHeight() * 0.33 &&
                        y < currentRectDragOver.getY() + currentRectDragOver.getHeight() * 0.67) {
                    x = currentRectDragOver.getX() + currentRectDragOver.getWidth();
                    y = centerY;
                } // Snap to upper edge
                else if(y < centerY) {
                    x = centerX;
                    y = currentRectDragOver.getY();
                } // Snap to bottom edge
                else if(y >= centerY) {
                    x = centerX;
                    y = currentRectDragOver.getY() + currentRectDragOver.getHeight();
                }

            }
        }

        setAssociationPointPosition(lastAssocPtClicked, x, y);
    };

    private EventHandler<MouseEvent> evtAPDragDetected = e -> {
        ((Circle)e.getSource()).startFullDrag();
        e.consume();
    };

    private EventHandler<MouseEvent> evtAPMouseReleased = e -> {
        Circle src = (Circle) e.getSource();

        if(originalAssocPtClicked != null) { // Reset to original point
            AssociationPoint pt = assocPoints.get(lastAssocPtClicked);
            pt.x = originalAssocPtClicked.x;
            pt.y = originalAssocPtClicked.y;
            updateAssociationLinePoint(pt);
            originalAssocPtClicked = null;
        }

        lastAssocPtClicked = -1L;
        currentRectDragOver = null;
        src.setMouseTransparent(false);
    };

    private EventHandler<MouseEvent> evtAPMousePressed = e -> {
        Circle src = (Circle) e.getSource();
        Map.Entry<Long, AssociationPoint> assocPt = getAssocPointByControl(src);
        lastAssocPtClicked = assocPt.getKey();
        src.setMouseTransparent(true);

        originalAssocPtClicked = new AssociationPoint(assocPt.getValue().end);
        originalAssocPtClicked.x = assocPt.getValue().x;
        originalAssocPtClicked.y = assocPt.getValue().y;
    };

    private EventHandler<MouseDragEvent> evtRectMouseDragReleased = e -> {
        Circle circle = (Circle) e.getGestureSource();
        Map.Entry<Long, AssociationPoint> assocPt = getAssocPointByControl(circle);
        Rectangle rectangle = (Rectangle) e.getSource();
        Map.Entry<Long, CharacterRectangle> character = getCharacterByRectangle(rectangle);

        if(assocPt.getValue().rectUID != character.getKey()) { // assoc point dropped onto new character
            if(assocPt.getValue().rectUID != -1L)
                detachAssociationPointFromCharacter(assocPt.getKey(), assocPt.getValue().rectUID);

            attachAssociationPointToCharacter(assocPt.getKey(), character.getKey());
        }

        originalAssocPtClicked = null; // Drop was valid, don't reset to original position.
        e.consume();
    };

    private EventHandler<MouseDragEvent> evtRectMouseDragEntered = e -> {
        System.out.println("onMouseDragEntered");
        currentRectDragOver = (Rectangle) e.getSource();
        e.consume();
    };

    private EventHandler<MouseDragEvent> evtRectMouseDragExited = e -> {
        System.out.println("onMouseDragExited");
        currentRectDragOver = null;
        e.consume();
    };

    /********************** EVENT CLASSES ***********************/

    private class EventRectanglePressed implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            Rectangle rectangle = (Rectangle) e.getSource();
            Map.Entry<Long, CharacterRectangle> character = getCharacterByRectangle(rectangle);
            if(character != null)
                character.getValue().setLastClick(e.getX() - rectangle.getX(), e.getY() - rectangle.getY());
        }
    }

    private class EventRectangleDragged implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            Rectangle rectangle = (Rectangle) e.getSource();
            Map.Entry<Long, CharacterRectangle> character = getCharacterByRectangle(rectangle);
            if(character != null) {
                double x = e.getX() - character.getValue().getLastClickX();
                double y = e.getY() - character.getValue().getLastClickY();

                // Snap to nearest 10
                x = (double)(((int)x / 10) * 10);
                y = (double)(((int)y / 10) * 10);

                setCharacterPosition(character.getKey(), x, y);
                rectangle.setViewOrder(-1.0);
                character.getValue().getText().setViewOrder(-1.0);
            }
        }
    }

    private class EventRectangleReleased implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            Rectangle rectangle = (Rectangle) e.getSource();
            Map.Entry<Long, CharacterRectangle> character = getCharacterByRectangle(rectangle);
            if(character != null) {
                rectangle.setViewOrder(0.0);
                character.getValue().getText().setViewOrder(0.0);
            }
        }
    }
}

class Association {
    public long uid;
    public Line line;
    public Text text;
}

class AssociationPoint {
    public long assocUID; // association line uid, shared between two association points.
    public long rectUID; // UID to which rect the point is attached to.
    public double x, y;
    public final boolean end;
    public Circle control;

    AssociationPoint(boolean isEndPoint) {
        assocUID = -1L;
        rectUID = -1L;
        end = isEndPoint;
    }
}