package com.team34.controller;

import com.team34.Debug;
import com.team34.model.Project;
import com.team34.view.MainView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

public class MainController {

    private final MainView view;
    private final Project model;
    private final EventHandler<ActionEvent> evtButtonAction;

    public MainController(MainView view, Project model) {
        this.view = view;
        this.model = model;

        this.evtButtonAction = new EventButtonAction();

        registerEventsOnView();
    }

    private void registerEventsOnView()
    {
        view.registerButtonEvents(evtButtonAction);
    }

    ////// ALL EVENTS ARE LISTED HERE //////////////////////////////////////////////

    private class EventButtonAction implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            Node source = (Node) e.getSource();
            String sourceID = source.getId();

            switch(sourceID) {
                case "BTN_EVENT_ADD":
                    model.eventManager.newEvent("TEST", "test");
                    view.updateEvents(model.eventManager.getEvents(0));
                    // TODO what class stores and manages the event order?
                    break;

                default: Debug.println("Unrecognized ID: "+sourceID);
                    break;
            }

        }
    };

}
