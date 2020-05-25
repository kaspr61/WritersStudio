package com.team34.view.character;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import java.util.ArrayList;

import com.team34.view.MainView;



/**
 * StackPane for the {@link MainView} that contains add-, edit-, and create character buttons,
 * and a list of all characters.
 * @author Jim Andersosn
 */
public class CharacterList extends StackPane {

    private static int ICON_SIZE = 40;

    private ArrayList<CharacterListObject> chListObjArray;
    private ListView<CharacterListObject> list;
    private Button add, edit, delete;
    private Label title;

    // Panes
    private BorderPane outerPane;
    private BorderPane innerPane;

    //CSS
    private String css;

    //Icons
    private String addCharacter;
    private String editCharacter;
    private String deleteCharacter;

    /**
     * Initializes StackPane.
     */
    public CharacterList() {
//        window = new Stage();

        //Panes
        outerPane = new BorderPane();
        innerPane = new BorderPane();

        //Add, Edit, Delete buttons
        HBox aedBox = new HBox();
        aedBox.setPadding(new Insets(10, 10, 0, 10));
        aedBox.setSpacing(20);

        //For the character list
        VBox characterBox = new VBox();
        characterBox.setPadding(new Insets(5, 10, 10, 10));

        //Buttons
        add = new Button();
        edit = new Button();
        delete = new Button();
        installButtonIcons();
        installButtonIds();

        //Label
        title = new Label("Characters");
        title.setPadding(new Insets(20, 0, 0, 0));
        title.getStyleClass().add("list-headline");

        //Character List
        list = new ListView<>();

        //Construct
        aedBox.getChildren().addAll(add, edit, delete);
        aedBox.setAlignment(Pos.CENTER);

        characterBox.getChildren().addAll(list);

        innerPane.setTop(aedBox);
        innerPane.setCenter(characterBox);

        outerPane.setTop(title);
        outerPane.setCenter(innerPane);
        BorderPane.setAlignment(title, Pos.CENTER);

        getChildren().add(outerPane);

        //Character list objects
        chListObjArray = new ArrayList<>();
    }

    /**
     * Sets the icon graphics for the Add-, Edit- and Delete buttons.
     */
    private void installButtonIcons() {
        addCharacter = com.team34.App.class.getResource("/icons/add_character.png").toExternalForm(); //Filestream for icon
        Image imgAddCharacter = new Image(addCharacter);
        ImageView imageViewAddCharacter = new ImageView(imgAddCharacter);

        editCharacter = com.team34.App.class.getResource("/icons/edit_character.png").toExternalForm();
        Image imgEditCharacter = new Image(editCharacter);
        ImageView imageViewEditCharacter = new ImageView(imgEditCharacter);

        deleteCharacter = com.team34.App.class.getResource("/icons/delete_character.png").toExternalForm();
        Image imgDeleteCharacter = new Image(deleteCharacter);
        ImageView imageViewDeleteCharacter = new ImageView(imgDeleteCharacter);

        imageViewAddCharacter.setFitHeight(ICON_SIZE); // Set size for icon
        imageViewAddCharacter.setFitWidth(ICON_SIZE);

        imageViewEditCharacter.setFitHeight(ICON_SIZE);
        imageViewEditCharacter.setFitWidth(ICON_SIZE);

        imageViewDeleteCharacter.setFitHeight(ICON_SIZE);
        imageViewDeleteCharacter.setFitWidth(ICON_SIZE);

        add.setGraphic(imageViewAddCharacter);
        edit.setGraphic(imageViewEditCharacter);
        delete.setGraphic(imageViewDeleteCharacter);
    }

    /**
     * Sets button IDs for the Add-, Edit- and Delete buttons. The IDs are used in the
     * {@link com.team34.controller.MainController} class for event handling.
     */
    private void installButtonIds() {
        add.setId(MainView.ID_BTN_CHARACTERLIST_ADD);
        edit.setId(MainView.ID_BTN_CHARACTERLIST_EDIT);
        delete.setId(MainView.ID_BTN_CHARACTERLIST_DELETE);
    }

    /**
     * Updates the list view to correspond with currently existing characters in the project.
     *
     * Extracts the name and UID information from the object arrays, and instantiates new {@link CharacterListObject}s
     * with the information. The character list view is then populated with the new character list objects.
     * This makes retrieving and displaying character information easier and more manageable.
     * @param characters Array list object arrays, containing character name and UID.
     */
    public void updateListView(ArrayList<Object[]> characters) {
        chListObjArray.clear();
        for (Object[] ch : characters) {
            CharacterListObject chObj = new CharacterListObject((String)ch[0], (long)ch[1]);
            chListObjArray.add(chObj);
        }
        ObservableList<CharacterListObject> ol = FXCollections.observableArrayList();
        ol.addAll(chListObjArray);
        SortedList sl = new SortedList(ol); //Sorts list alphabetically
        list.setItems(sl.sorted());
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
     * Registers the character list to the mouse event handler in the
     * {@link com.team34.controller.MainController} class.
     * @param listEventHandler
     */
    public void registerMouseEvents(EventHandler<MouseEvent> listEventHandler) {
        list.setOnMouseClicked(listEventHandler);
    }

    /**
     * If a character is selected in the list view, returns the character's UID. Else, returns -1.
     * @return long
     */
    public long getCharacterUID() {
        if (list.getSelectionModel().getSelectedItem() != null) {
            return list.getSelectionModel().getSelectedItem().getUid();
        }

        return -1;
    }

    /**
     * Checks if a list item is selected
     * @return boolean
     */
    public boolean listItemSelected() {
        return list.getSelectionModel().getSelectedIndex() >= 0;
    }
}

