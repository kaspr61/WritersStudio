package com.team34.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import javax.xml.stream.XMLStreamException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import com.team34.view.dialogs.EditCharacterDialog;
import com.team34.view.dialogs.EditEventDialog;
import com.team34.view.dialogs.EditAssociationDialog;
import com.team34.model.Project;
import com.team34.view.MainView;

/**
 * This class handles logic and communication between the model and view components of the system.
 * <p>
 * This is where the events that communicate between components are implemented. Since
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
    private final EventHandler<WindowEvent> evtCloseRequest;
    private final EventHandler<ActionEvent> evtMenuBarAction;
    private final EventHandler<DragEvent> evtDragDropped;

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
        this.evtCloseRequest = new EventCloseRequest();
        this.evtMenuBarAction = new EventMenuBarAction();
        this.evtDragDropped = new EventDragDropped();

        registerEventsOnView();
    }

    /**
     * Registers events {@link MainController#evtButtonAction} and
     * {@link MainController#evtContextMenuAction} on the view
     */
    private void registerEventsOnView() {
        view.registerButtonEvents(evtButtonAction);
        view.registerContextMenuEvents(evtContextMenuAction);
        view.registerCloseRequestEvent(evtCloseRequest);
        view.registerMenuBarActionEvents(evtMenuBarAction);
        view.registerDragEvent(evtDragDropped);
        view.registerCharacterChartEvents(
                new EventCharacterRectReleased(),
                new EventChartClick(),
                new EventAssociationLabelReleased()
        );
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
        refreshTitleBar();
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
        refreshTitleBar();
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
     * Updates the title of the application window.
     * Displays the name of the project, followed by an asterisk, if
     * there are any unsaved changes.
     */
    private void refreshTitleBar() {
        String title = "Writer's Studio - ";

        if (model.getProjectName().isEmpty())
            title += "untitled";
        else
            title += model.getProjectName();

        if (model.hasUnsavedChanges())
            title += "*";

        view.getMainStage().setTitle(title);
    }

    // Returns false if action should not continue

    /**
     * If there are any unsaved changes, the unsaved changes dialog will be shown.
     * If there are no unsaved changes, nothing happens, and it returns true.
     *
     * @return false if the action should not continue (user canceled)
     */
    private boolean saveBeforeContinue() {
        if (model.hasUnsavedChanges()) {
            ButtonType result = view.showUnsavedChangesDialog();
            if (result == ButtonType.YES) {
                saveProject();
            } else if (result == ButtonType.CANCEL || result == ButtonType.CLOSE) {
                return false;
            }
        }

        return true;
    }

    /**
     * Opens the file chooser, loads the project file and updates and refreshes the view.
     */
    private void openProject() {
        Project.UserPreferences userPrefs = model.getUserPreferences();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Project File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Writer's Studio Project File", "*.wsp")
        );

        File directory = Paths.get(userPrefs.projectDir).toFile();
        if (directory.exists())
            fileChooser.setInitialDirectory(Paths.get(userPrefs.projectDir).toFile());

        File file = fileChooser.showOpenDialog(view.getMainStage());
        if (file == null)
            return;

        userPrefs.projectDir = file.getParent();

        try {
            model.writeUserPrefs();
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }

        try {
            model.loadProject(file);
            refreshViewEvents();
            refreshCharacterList();
            refreshTitleBar();
        } catch (Exception e) {
            e.printStackTrace();
            // TODO popup error dialog, error reading file.
        }
    }

    /**
     * Opens the file chooser if no project file is in use, then saves the current project to that file.
     */
    private void saveProject() {
        if (model.getProjectFile() == null) {

            Project.UserPreferences userPrefs = model.getUserPreferences();

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Project File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Writer's Studio Project File", "*.wsp")
            );

            File directory = Paths.get(userPrefs.projectDir).toFile();
            if (directory.exists())
                fileChooser.setInitialDirectory(Paths.get(userPrefs.projectDir).toFile());

            File file = fileChooser.showSaveDialog(view.getMainStage());
            if (file == null)
                return;
            else {
                model.setProjectFile(file);
                model.setProjectName(file.getName());
            }
        }

        try {
            model.saveProject();
            refreshTitleBar();
        } catch (IOException | XMLStreamException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens an {@link EditCharacterDialog} dialog window. If the action is not cancelled by the user, the
     * {@link com.team34.model.character.CharacterManager} creates a new character with the user input.
     * @author Jim Andersson
     */
    private void createNewCharacter(double x, double y) {
        if (view.getEditCharacterPanel().showCreateCharacter() == EditCharacterDialog.WindowResult.OK) {
            x = view.snapTo(x, 10);
            y = view.snapTo(y, 10);

            long newCharacterUID = model.characterManager.newCharacter(
                    view.getEditCharacterPanel().getCharacterName(),
                    view.getEditCharacterPanel().getCharacterDescription(),
                    x, y
            );
            view.updateCharacterList(
                    model.characterManager.getCharacterList(),
                    model.characterManager.getAssociationData()
            );

            if (newCharacterUID == -1L) {
                // TODO Popup warning dialog, stating that either name or description has unsupported format
            }

            refreshTitleBar();
        }
    }

    /**
     * Edits character. Identifies the selected character in the list view and retrieves data from the corresponding
     * character stored in {@link com.team34.model.character.CharacterManager}. The data is then set in a new
     * {@link EditCharacterDialog} dialog window. If the action is not cancelled, updates the character with new
     * user input.
     * @author Jim Andersson
     */
    private void editCharacter(long uid) {
        Object[] characterData = model.characterManager.getCharacterData(uid);

        if (view.getEditCharacterPanel().showEditCharacter((String) characterData[0], (String) characterData[1])
                == EditCharacterDialog.WindowResult.OK
        ) {
            boolean success = model.characterManager.editCharacter(uid,
                    view.getEditCharacterPanel().getCharacterName(),
                    view.getEditCharacterPanel().getCharacterDescription()
            );

            if (!success) {
                // TODO Popup warning dialog, stating that either name or description has unsupported format
            }

            refreshTitleBar();
        }
    }

    /**
     * Deletes character. Identifies the selected character in the list view and removes the corresponding
     * character stored in {@link com.team34.model.character.CharacterManager}.
     * @author Jim Andersson
     */
    private void deleteCharacter(long uid) {
        Long[] associations = view.characterChart.getAssociationsByCharacter(uid);
        if(associations != null) {
            for (int i = 0; i < associations.length; i++)
                model.characterManager.deleteAssociation(associations[i]);
        }

        model.characterManager.deleteCharacter(uid);
        refreshTitleBar();
    }

    private void deleteAssociation(long uid) {
        if(uid == -1L)
            return;

        model.characterManager.deleteAssociation(uid);
        refreshTitleBar();
    }

    private void createAssociation(long startingCharacterUID) {
        EditAssociationDialog editAssocDlg = view.getEditAssociationDialog();

        if (editAssocDlg.showEditAssociation("")
                == EditAssociationDialog.WindowResult.OK
        ) {
            double startX = view.getLastChartMouseClickX();
            double startY = view.getLastChartMouseClickY();
            Double[] startPos = view.snapToNearestCharacterEdge(startingCharacterUID, startX, startY);

            long assocUID = model.characterManager.newAssociation(
                    startingCharacterUID, -1L, startPos[0], startPos[1], startX, startY,
                    view.getEditAssociationDialog().getAssociationLabel(), startX, startY
            );

            view.updateCharacterList(model.characterManager.getCharacterList(), model.characterManager.getAssociationData());
            view.startCharacterAssociationDrag(assocUID, false);
            refreshTitleBar();
        }
    }

    /**
     * Opens an {@link EditAssociationDialog}, then edits the association if not canceled.
     * The edit association dialog will block until the dialog is closed.
     * When the dialog has been closed, the model is instructed to change
     * the association to the text specified in the edit event dialog.
     */
    private void editAssociation(long uid) {
        Object[] assocData = model.characterManager.getAssociationData(uid);
        EditAssociationDialog editAssocDlg = view.getEditAssociationDialog();

        if (editAssocDlg.showEditAssociation((String) assocData[6])
                == EditAssociationDialog.WindowResult.OK
        ) {
            model.characterManager.editAssociation(uid,
                    (Long) assocData[0], (Long) assocData[1],
                    (Double) assocData[2], (Double) assocData[3], (Double) assocData[4], (Double) assocData[5],
                    editAssocDlg.getAssociationLabel(), (Double) assocData[7], (Double) assocData[8]
            );

            refreshCharacterList();
        }
        refreshTitleBar();
    }

    /**
     * Retrieves an updated list of characters from {@link com.team34.model.character.CharacterManager} and updates
     * the character list view.
     * @author Jim Andersson
     */
    private void refreshCharacterList() {
        view.updateCharacterList(
                model.characterManager.getCharacterList(),
                model.characterManager.getAssociationData()
        );

    }

    private void updateModelAssociationWithView(long assocUID) {
        Object[] data = view.getChartAssociationData(assocUID);
        model.characterManager.editAssociation(
                assocUID, (Long) data[0], (Long) data[1],
                (Double) data[2], (Double) data[3], (Double) data[4], (Double) data[5],
                (String) data[6], (Double) data[7], (Double) data[8]
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
            long eventUID = view.getSelectedEventUID();

            switch (sourceID) {
                case MainView.ID_BTN_EVENT_ADD:
                    createNewEvent();
                    refreshViewEvents();
                    break;

                case MainView.ID_BTN_EVENT_DELETE:
                    model.eventManager.removeEvent(eventUID);
                    refreshViewEvents();
                    refreshTitleBar();
                    break;

                case MainView.ID_BTN_EVENT_EDIT:
                    editEvent(eventUID);
                    refreshViewEvents();
                    break;

                case MainView.ID_BTN_CHARACTERLIST_ADD:
                    createNewCharacter(0.0, 0.0);
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
                    break;

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

            switch (sourceID) {
                case MainView.ID_TIMELINE_NEW_EVENT:
                    createNewEvent();
                    refreshViewEvents();
                    break;

                case MainView.ID_TIMELINE_REMOVE_EVENT:
                    if (view.getTimelineContextMenu().getUserData() instanceof Long)
                        sourceUID = (Long) view.getTimelineContextMenu().getUserData();
                    model.eventManager.removeEvent(sourceUID);
                    refreshViewEvents();
                    refreshTitleBar();
                    break;

                case MainView.ID_TIMELINE_EDIT_EVENT:
                    if (view.getTimelineContextMenu().getUserData() instanceof Long)
                        sourceUID = (Long) view.getTimelineContextMenu().getUserData();
                    editEvent(sourceUID);
                    refreshViewEvents();
                    break;

                case MainView.ID_CHART_NEW_CHARACTER:
                    createNewCharacter(view.getLastChartMouseClickX(), view.getLastChartMouseClickY());
                    break;

                case MainView.ID_CHART_EDIT_CHARACTER:
                    if (view.getChartContextMenu().getUserData() instanceof Long) {
                        sourceUID = (Long) view.getChartContextMenu().getUserData();
                        if (sourceUID != -1) {
                            editCharacter(sourceUID);
                            refreshCharacterList();
                        }
                    }
                    break;

                case MainView.ID_CHART_REMOVE_CHARACTER:
                    if (view.getChartContextMenu().getUserData() instanceof Long) {
                        sourceUID = (Long) view.getChartContextMenu().getUserData();
                        if (sourceUID != -1) {
                            deleteCharacter(sourceUID);
                            refreshCharacterList();
                        }
                    }
                    break;

                case MainView.ID_CHART_NEW_ASSOCIATION:
                    if (view.getChartContextMenu().getUserData() instanceof Long)
                        sourceUID = (Long) view.getChartContextMenu().getUserData();
                    createAssociation(sourceUID);
                    break;

                case MainView.ID_CHART_EDIT_ASSOCIATION:
                    if (view.getChartContextMenu().getUserData() instanceof Long) {
                        sourceUID = (Long) view.getChartContextMenu().getUserData();
                        editAssociation(sourceUID);
                    }
                    break;

                case MainView.ID_CHART_REMOVE_ASSOCIATION:
                    if (view.getChartContextMenu().getUserData() instanceof Long)
                        sourceUID = (Long) view.getChartContextMenu().getUserData();
                    deleteAssociation(sourceUID);
                    refreshCharacterList();
                    break;

                case MainView.ID_CHART_CENTER_ASSOCIATION_LABEL:
                    if (view.getChartContextMenu().getUserData() instanceof Long)
                        sourceUID = (Long) view.getChartContextMenu().getUserData();
                    view.characterChart.centerAssociationLabel(sourceUID);
                    updateModelAssociationWithView(sourceUID);
                    refreshTitleBar();
                    break;

                default:
                    System.out.println("Unrecognized ID: " + sourceID);
                    break;
            }

        }
    }

    ;

    /**
     * This event is fired when the application should be closed, eg. when a user exits or closes the window.
     */
    private class EventCloseRequest implements EventHandler<WindowEvent> {
        @Override
        public void handle(WindowEvent e) {
            if (!saveBeforeContinue()) // If user pressed cancel
                e.consume();

            Project.UserPreferences prefs = model.getUserPreferences();
            prefs.windowMaximized = view.getMainStage().isMaximized();
            if (!prefs.windowMaximized) {
                prefs.windowWidth = (int) view.getMainStage().getScene().getWidth();
                prefs.windowHeight = (int) view.getMainStage().getScene().getHeight();
            }

            try {
                model.writeUserPrefs();
            } catch (IOException | XMLStreamException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * This event is fired from the user clicking items in the menu bar.
     */
    private class EventMenuBarAction implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            MenuItem source = (MenuItem) e.getSource();
            String sourceID = source.getId();

            switch (sourceID) {
                case MainView.ID_MENU_NEW:
                    if (saveBeforeContinue()) {
                        model.clearProject();
                        refreshViewEvents();
                        refreshCharacterList();
                    }
                    refreshTitleBar();
                    break;

                case MainView.ID_MENU_OPEN:
                    if (saveBeforeContinue())
                        openProject();
                    break;

                case MainView.ID_MENU_SAVE:
                    saveProject();
                    break;

                case MainView.ID_MENU_SAVE_AS:
                    model.setProjectFile(null);
                    saveProject();
                    break;

                case MainView.ID_MENU_EXIT:
                    view.exitApplication();
                    break;

                case MainView.ID_MENU_ADD_CHARACTER:
                    createNewCharacter();
                    break;

                case MainView.ID_MENU_ADD_EVENT:
                    createNewEvent();
                    refreshViewEvents();
                    break;

                default:
                    System.out.println("Unrecognized ID: " + sourceID);
                    break;
            }

        }
    }

    private class EventCharacterRectReleased implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            if(e.getButton() != MouseButton.PRIMARY)
                return;

            Object[] result = view.onCharacterReleased(e);
            if(result == null)
                return;

            if((Boolean) result[1] == true) { // The character block was moved.
                Object[] characterData = view.getChartCharacterData((Long) result[0]);
                model.characterManager.editCharacter(
                        (Long) result[0],
                        (Double) characterData[0],
                        (Double) characterData[1]
                );
                Long[] associations = view.characterChart.getAssociationsByCharacter((Long) result[0]);
                if(associations != null) {
                    for (int i = 0; i < associations.length; i++) {
                        updateModelAssociationWithView(associations[i]);
                    }
                }
            }
            else { // An association was attached to the character block
                updateModelAssociationWithView((Long) result[0]);
            }

            refreshTitleBar();
        }
    }

    private class EventChartClick implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            long assocUID = view.onCharacterChartClick(e);
            if(assocUID != -1L) { // Should remove association
                    model.characterManager.deleteAssociation(assocUID);
                    refreshCharacterList();
            }
        }
    }

    private class EventAssociationLabelReleased implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            long assocUID = view.onAssociationLabelReleased(e);
            if (assocUID != -1L) {
                updateModelAssociationWithView(assocUID);
                refreshTitleBar();
            }
        }
    }

    /**
     * Fires when an event is dragged and dropped onto another event on the timeline.
     *
     * @author Jim Andersson
     */
    private class EventDragDropped implements EventHandler<DragEvent> {

        @Override
        public void handle(DragEvent dragEvent) {
            Rectangle rect = (Rectangle)dragEvent.getSource();

            long uidDragged = Long.parseLong(dragEvent.getDragboard().getString());
            long uidTarget = view.getEventUidByRectangle(rect);

            int dragged = model.eventManager.getEventIndex(view.getEventOrderList(), uidDragged);
            int target = model.eventManager.getEventIndex(view.getEventOrderList(), uidTarget);

            if (dragged != -1 && target != -1 ) {
                model.eventManager.moveEvent(view.getEventOrderList(), dragged, target);
                refreshViewEvents();
            }
        }
    }

}
