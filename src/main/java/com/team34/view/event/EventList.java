package com.team34.view.event;

import com.team34.model.event.EventListObject;
import com.team34.view.MainView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;


public class EventList extends StackPane {

    private ListView<EventListObject> list;
    private Button add, edit, delete;
    private Label title;

    // Panes
    BorderPane outerPane;
    BorderPane innerPane;

    //CSS
    private String cssEventlist;

    //Icons
    private String addEvent;
    private String editEvent;
    private String deleteEvent;
    private final int ICON_SIZE = 40;

    /**
     * Initializes StackPane.
     */
    public EventList() {
//        window = new Stage();

        //Panes
        outerPane = new BorderPane();
        innerPane = new BorderPane();

        //Add, Edit, Delete buttons
        HBox aedBox = new HBox();
        aedBox.setPadding(new Insets(10, 10, 10, 10));
        aedBox.setSpacing(20);

        //For the event list
        VBox eventBox = new VBox();
        eventBox.setPadding(new Insets(10, 10, 10, 10));

        //Buttons
        add = new Button();
        edit = new Button();
        delete = new Button();
        installButtonIcons();
        installButtonIds();

        //Label
        title = new Label("Events");
        title.setPadding(new Insets(20, 0, 0, 0));
        title.getStyleClass().add("list-headline");

        //Event List
        list = new ListView<>();

        //Construct
        aedBox.getChildren().addAll(add, edit, delete);
        aedBox.setAlignment(Pos.CENTER);

        eventBox.getChildren().addAll(list);

        innerPane.setTop(aedBox);
        innerPane.setCenter(eventBox);

        outerPane.setTop(title);
        outerPane.setCenter(innerPane);
        BorderPane.setAlignment(title, Pos.CENTER);

        getChildren().add(outerPane);

        //CSS
        cssEventlist = com.team34.App.class.getResource("/css/characterlist.css").toExternalForm();
    }

    public void setStyleSheets() {
        outerPane.getStyleClass().add("outerPane");
    }

    /**
     * Sets the icon graphics for the Add-, Edit- and Delete buttons.
     */
    public void installButtonIcons() {
        addEvent = com.team34.App.class.getResource("/icons/add_event.png").toExternalForm(); //Filestream for icon
        Image imgAddEvent = new Image(addEvent);
        ImageView imageViewAddEvent = new ImageView(imgAddEvent);

        editEvent = com.team34.App.class.getResource("/icons/edit_event.png").toExternalForm();
        Image imgEditEvent = new Image(editEvent);
        ImageView imageViewEditEvent = new ImageView(imgEditEvent);

        deleteEvent = com.team34.App.class.getResource("/icons/delete_event.png").toExternalForm();
        Image imgDeleteEvent = new Image(deleteEvent);
        ImageView imageViewDeleteEvent = new ImageView(imgDeleteEvent);

        imageViewAddEvent.setFitHeight(ICON_SIZE); // Set size for icon
        imageViewAddEvent.setFitWidth(ICON_SIZE);

        imageViewEditEvent.setFitHeight(ICON_SIZE);
        imageViewEditEvent.setFitWidth(ICON_SIZE);

        imageViewDeleteEvent.setFitHeight(ICON_SIZE);
        imageViewDeleteEvent.setFitWidth(ICON_SIZE);

        add.setGraphic(imageViewAddEvent);
        edit.setGraphic(imageViewEditEvent);
        delete.setGraphic(imageViewDeleteEvent);
    }


    /**
     * Sets button IDs for the Add-, Edit- and Delete buttons. The IDs are used in the
     * {@link com.team34.controller.MainController} class for event handling.
     */
    public void installButtonIds() {
        add.setId(MainView.ID_BTN_EVENT_ADD);
        edit.setId(MainView.ID_BTN_EVENT_EDIT);
        delete.setId(MainView.ID_BTN_EVENT_DELETE);
    }

    public void updateListView(Object[][] events) {
        if (events == null || events.length < 1){
            list.getItems().clear();
            return;
        }
        ObservableList<EventListObject> ol = FXCollections.observableArrayList();
        for(int i = 0; i < events.length; i++){
            ol.add(new EventListObject((String) events[i][1], (Long) events[i][0]));
        }list.setItems(ol);
    }

    /**
     * Registers the Add-, Edit- and Delete buttons to the event handler in the
     * {@link com.team34.controller.MainController} class.
     * @param buttonEventHandler
     */
    public void registerButtonEvents(EventHandler<ActionEvent> buttonEventHandler) {
        add.setOnAction(buttonEventHandler);
        edit.setOnAction(buttonEventHandler);
        delete.setOnAction(buttonEventHandler);
    }

    /**
     * If a character is selected in the list view, returns the character's UID. Else, returns -1.
     * @return long
     */
    public long getEventUID() {
        if (list.getSelectionModel().getSelectedItem() != null) {
            return list.getSelectionModel().getSelectedItem().getUid();
        }

        return -1;
    }
}
