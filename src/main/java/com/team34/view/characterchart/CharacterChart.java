package com.team34.view.characterchart;

import com.team34.view.LabeledRectangle;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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

    private final EventHandler<MouseEvent> evtRectPressed;
    private final EventHandler<MouseEvent> evtRectDragged;

    public CharacterChart(double width, double height) {
        rectMap = new HashMap<>();
        assocPoints = new HashMap<>();
        associations = new HashMap<>();

        evtRectPressed = new EventRectanglePressed();
        evtRectDragged = new EventRectangleDragged();

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

    public void addAssociation(long assocUID, long startPtUID, long endPtUID, long startCharUID, long endCharUID, String label)
    {
        Association exisitingAssoc = associations.get(assocUID);
        if(exisitingAssoc != null) {
            pane.getChildren().removeAll(exisitingAssoc.text, exisitingAssoc.line);
        }

        AssociationPoint startPt = new AssociationPoint(false);
        AssociationPoint endPt = new AssociationPoint(true);

        Association assoc = new Association();
        assoc.uid = assocUID;
        assoc.text = new Text(label);
        assoc.text.getStyleClass().add("characterchart-assoclabel");
        assoc.line = new Line(0, 0, 0, 0);
        assoc.line.getStyleClass().add("characterchart-assocline");

        startPt.assocUID = assocUID;
        startPt.rectUID = startCharUID;
        startPt.x = 0;
        startPt.y = 0;

        endPt.assocUID = assocUID;
        endPt.rectUID = endCharUID;
        endPt.x = 0;
        endPt.y = 0;

        associations.put(assocUID, assoc);
        assocPoints.put(startPtUID, startPt);
        assocPoints.put(endPtUID, endPt);

        pane.getChildren().addAll(assoc.line, assoc.text);

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
        rectMap.get(characterUID).addAssociationPoint(assocPtUID);
    }

    public void detachAssociationPointFromCharacter(long assocPtUID, long characterUID) {
        rectMap.get(characterUID).removeAssociationPoint(assocPtUID);
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

        if(point.end) {
            line.setEndX(point.x);
            line.setEndY(point.y);
        }
        else {
            line.setStartX(point.x);
            line.setStartY(point.y);
        }
    }

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
                setCharacterPosition(character.getKey(), x, y);
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

    AssociationPoint(boolean isEndPoint) {
        assocUID = -1L;
        rectUID = -1L;
        end = isEndPoint;
    }
}