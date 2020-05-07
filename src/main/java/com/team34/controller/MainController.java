package com.team34.controller;

import com.team34.view.EditCharacterPanel;
import com.team34.view.dialogs.EditEventDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

import com.team34.model.Project;
import com.team34.view.MainView;
import javafx.scene.control.MenuItem;

/**
 * This class handles logic and communication between the model and view components of the system.
 * <p>
 * This is where many of the events that communicate between components are implemented. Since
 * the model and view are completely independent of each other, their indirect interaction is
 * managed by this class. By having the components encapsulating their area of responsibility,
 * it makes it easier to implement changes in a safe manner, lowering the risk of errors.
 *
 * @author Kasper S. Skott
 */
public class MainController {

    private final MainView view;
    private final Project model;
    private final EventHandler<ActionEvent> evtButtonAction;
    private final EventHandler<ActionEvent> evtContextMenuAction;

    /**
     * Constructs the controller. Initializes member variables
     * and calls {@link MainController#registerEventsOnView()}.
     *
     * @param view  the view to control
     * @param model the model to control
     */
    public MainController(MainView view, Project model) {
        this.view = view;
        this.model = model;

        this.evtButtonAction = new EventButtonAction();
        this.evtContextMenuAction = new EventContextMenuAction();

        registerEventsOnView();
    }

    /**
     * Registers events {@link MainController#evtButtonAction} and
     * {@link MainController#evtContextMenuAction} on the view
     */
    private void registerEventsOnView() {
        view.registerButtonEvents(evtButtonAction);
        view.registerContextMenuEvents(evtContextMenuAction);
    }

    /**
     * Opens an {@link EditEventDialog}, then creates a new event if not canceled.
     * The edit event dialog will block until the dialog is closed.
     * When the dialog has been closed, the model is instructed to construct a
     * new event with the parameters specified in the edit event dialog. If creation
     * failed, a popup warning will inform the user that either the name or description
     * had an unsupported format.
     * <p>
     * This may be called from multiple events, thus allowing event manipulation
     * from different sources, eg. timeline context menu, event list.
     */
    private void createNewEvent() {
        if (view.getEditEventDialog().showCreateEvent() == EditEventDialog.WindowResult.OK) {
            long newEventUID = model.eventManager.newEvent(
                    view.getEditEventDialog().getEventName(),
                    view.getEditEventDialog().getEventDescription()
            );

            if (newEventUID == -1L) {
                // TODO Popup warning dialog, stating that either name or description has unsupported format
            }
        }
    }

    /**
     * Opens an {@link EditEventDialog}, then edits the event if not canceled.
     * The edit event dialog will block until the dialog is closed.
     * When the dialog has been closed, the model is instructed to edit the event
     * with the parameters specified in the edit event dialog. If this
     * failed, a popup warning will inform the user that either the name or
     * description had an unsupported format.
     * <p>
     * This may be called from multiple events, thus allowing event manipulation
     * from different sources, eg. timeline context menu, event list.
     */
    private void editEvent(long uid) {
        Object[] eventData = model.eventManager.getEventData(uid);

        if (view.getEditEventDialog().showEditEvent((String) eventData[0], (String) eventData[1])
                == EditEventDialog.WindowResult.OK
        ) {
            boolean success = model.eventManager.editEvent(uid,
                    view.getEditEventDialog().getEventName(),
                    view.getEditEventDialog().getEventDescription()
            );

            if (!success) {
                // TODO Popup warning dialog, stating that either name or description has unsupported format
            }
        }
    }

    /**
     * Instructs the view to update the view of events with the current state of the model.
     */
    private void refreshViewEvents() {
        view.updateEvents(
                model.eventManager.getEvents(),
                model.eventManager.getEventOrder(view.getEventOrderList())
        );
    }

    /**
     * Opens an {@link EditCharacterPanel} dialog window. If the action is not cancelled by the user, the
     * {@link com.team34.model.character.CharacterManager} creates a new character with the user input.
     */
    private void createNewCharacter() {
        if (view.getEditCharacterPanel().showCreateCharacter() == EditCharacterPanel.WindowResult.OK) {
            long newCharacterUID = model.characterManager.newCharacter(
                    view.getEditCharacterPanel().getCharacterName(),
                    view.getEditCharacterPanel().getCharacterDescription()
            );
            view.updateCharacterList(model.characterManager.getCharacterList());

            if (newCharacterUID == -1L) {
                // TODO Popup warning dialog, stating that either name or description has unsupported format
            }
        }
    }

    /**
     * Edits character. Identifies the selected character in the list view and retrieves data from the corresponding
     * character stored in {@link com.team34.model.character.CharacterManager}. The data is then set in a new
     * {@link EditCharacterPanel} dialog window. If the action is not cancelled, updates the character with new
     * user input.
     */
    private void editCharacter(long uid) {
        Object[] characterData = model.characterManager.getCharacterData(uid);

        if (view.getEditCharacterPanel().showEditCharacter((String) characterData[0], (String) characterData[1])
                == EditCharacterPanel.WindowResult.OK
        ) {
            boolean success = model.characterManager.editCharacter(uid,
                    view.getEditCharacterPanel().getCharacterName(),
                    view.getEditCharacterPanel().getCharacterDescription()
            );

            if (!success) {
                // TODO Popup warning dialog, stating that either name or description has unsupported format
            }
        }
    }

    /**
     * Deletes character. Identifies the selected character in the list view and removes the corresponding
     * character stored in {@link com.team34.model.character.CharacterManager}.
     */
    private void deleteCharacter(long uid) {
        model.characterManager.deleteCharacter(uid);
    }

    /**
     * Retrieves an updated list of characters from {@link com.team34.model.character.CharacterManager} and updates
     * the character list view.
     */
    private void refreshCharacterList() {
        view.updateCharacterList(
                model.characterManager.getCharacterList()
        );
    }

    ////// ALL EVENTS ARE LISTED HERE //////////////////////////////////////////////

    /**
     * This event is fired from the user interacting with buttons.
     */
    private class EventButtonAction implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            Node source = (Node) e.getSource();
            String sourceID = source.getId();

            switch (sourceID) {
                case MainView.ID_BTN_EVENT_ADD:
                    createNewEvent();
                    refreshViewEvents();
                    break;

                case MainView.ID_BTN_CHARACTERLIST_ADD:
                    createNewCharacter();
                    break;

                case MainView.ID_BTN_CHARACTERLIST_EDIT:
                    if (view.getCharacterUID() != -1) {
                        editCharacter(view.getCharacterUID());
                        refreshCharacterList();
                    }
                    break;

                case MainView.ID_BTN_CHARACTERLIST_DELETE:
                    if (view.getCharacterUID() != -1) {
                        deleteCharacter(view.getCharacterUID());
                        refreshCharacterList();
                    }

                default:
                    System.out.println("Unrecognized ID: " + sourceID);
                    break;
            }

        }
    }


    ////////////////////////////////////////////////////////////////////////////

    /**
     * This event is fired from the user clicking context menu items.
     */
    private class EventContextMenuAction implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            MenuItem source = (MenuItem) e.getSource();
            String sourceID = source.getId();

            Long sourceUID = -1L;
            if (view.getTimelineContextMenu().getUserData() instanceof Long)
                sourceUID = (Long) view.getTimelineContextMenu().getUserData();

            switch (sourceID) {
                case MainView.ID_TIMELINE_NEW_EVENT:
                    createNewEvent();
                    refreshViewEvents();
                    break;

                case MainView.ID_TIMELINE_REMOVE_EVENT:
                    model.eventManager.removeEvent(sourceUID);
                    refreshViewEvents();
                    break;

                case MainView.ID_TIMELINE_EDIT_EVENT:
                    editEvent(sourceUID);
                    refreshViewEvents();
                    break;

                default:
                    System.out.println("Unrecognized ID: " + sourceID);
                    break;
            }

        }
    }

    ;

}
