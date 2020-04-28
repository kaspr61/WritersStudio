package com.team34.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import java.util.Random;

import com.team34.Debug;
import com.team34.model.Project;
import com.team34.view.MainView;
import javafx.scene.control.MenuItem;

/**
 * @author Kasper S. Skott
 */
public class MainController {

    private final MainView view;
    private final Project model;
    private final EventHandler<ActionEvent> evtButtonAction;
    private final EventHandler<ActionEvent> evtContextMenuAction;

    public MainController(MainView view, Project model) {
        this.view = view;
        this.model = model;

        this.evtButtonAction = new EventButtonAction();
        this.evtContextMenuAction = new EventContextMenuAction();

        registerEventsOnView();
    }

    private void registerEventsOnView() {
        view.registerButtonEvents(evtButtonAction);
        view.registerContextMenuEvents(evtContextMenuAction);
    }

    private void createNewEvent() {
        // TODO open new event dialog and get arguments for the new event.
        model.eventManager.newEvent("Event " + new Random().nextInt(1000), "test description");
    }

    private void refreshViewEvents() {
        view.updateEvents(
                model.eventManager.getEvents(),
                model.eventManager.getEventOrder(view.getEventOrderList())
        );
    }

    ////// ALL EVENTS ARE LISTED HERE //////////////////////////////////////////////

    private class EventButtonAction implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            Node source = (Node) e.getSource();
            String sourceID = source.getId();

            switch(sourceID) {
                case MainView.ID_BTN_EVENT_ADD:
                    createNewEvent();
                    refreshViewEvents();
                    break;

                default: Debug.println("Unrecognized ID: "+sourceID);
                    break;
            }

        }
    };

    ////////////////////////////////////////////////////////////////////////////

    private class EventContextMenuAction implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            MenuItem source = (MenuItem) e.getSource();
            String sourceID = source.getId();

            switch(sourceID) {
                case MainView.ID_TIMELINE_NEW_EVENT:
                    createNewEvent();
                    refreshViewEvents();
                    break;

                default: Debug.println("Unrecognized ID: "+sourceID);
                    break;
            }

        }
    };

}
