package com.team34.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import java.util.Random;

import com.team34.Debug;
import com.team34.model.Project;
import com.team34.view.MainView;

/**
 * @author Kasper S. Skott
 */
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
                case MainView.ID_BTN_EVENT_ADD:
                    model.eventManager.newEvent("Event " + new Random().nextInt(100), "test description");
                    view.updateEvents(
                            model.eventManager.getEvents(),
                            model.eventManager.getEventOrder(view.getEventOrderList())
                    );
                    break;

                default: Debug.println("Unrecognized ID: "+sourceID);
                    break;
            }

        }
    };

}
