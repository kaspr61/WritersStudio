package com.team34.view.character;

import com.team34.view.MainView;
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

import java.util.ArrayList;

/*

//TODO: * Lägg till logiken i controller-klass
        * Se till att en dialogprompt dyker upp när användaren väljer att ta bort en karaktär, som:
        "Are you sure you want to delete [character]?"

 */


public class CharacterList extends StackPane {

    private ListView<String> characterList;
    private Button add, edit, delete;
    private Label title;

    // Panes
    BorderPane outerPane;
    BorderPane innerPane;

    //CSS
    private String cssCharacterlist;

    //Icons
    private String addCharacter;
    private String editCharacter;
    private String deleteCharacter;
    private final int ICON_SIZE = 40;

    public CharacterList() {
//        window = new Stage();

        //Panes
        outerPane = new BorderPane();
        innerPane = new BorderPane();

        //Add, Edit, Delete buttons
        HBox aedBox = new HBox();
        aedBox.setPadding(new Insets(10, 10, 10, 10));
        aedBox.setSpacing(20);

        //For the character list
        VBox characterBox = new VBox();
        characterBox.setPadding(new Insets(10, 10, 10, 10));

        //Buttons
        add = new Button();
        edit = new Button();
        delete = new Button();
        installButtonIcons();
        installButtonIds();

        //Label
        title = new Label("Characters");
        title.setPadding(new Insets(20, 0, 0, 0));

        //Character List
        ArrayList<String> characters = new ArrayList<>();
        characterList = new ListView<>();

        characterList.getItems().addAll(characters);

        //Construct
        aedBox.getChildren().addAll(add, edit, delete);
        aedBox.setAlignment(Pos.CENTER);

        characterBox.getChildren().addAll(characterList);

        innerPane.setTop(aedBox);
        innerPane.setCenter(characterBox);

        outerPane.setTop(title);
        outerPane.setCenter(innerPane);
        BorderPane.setAlignment(title, Pos.CENTER);

        getChildren().add(outerPane);

        //CSS
        cssCharacterlist = com.team34.App.class.getResource("/css/characterlist.css").toExternalForm();
    }

    public void setStyleSheets() {
        outerPane.getStyleClass().add("outerPane");
    }

    public void installButtonIcons() {
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

    public void installButtonIds() {
        add.setId(MainView.ID_BTN_CHARACTERLIST_ADD);
        edit.setId(MainView.ID_BTN_CHARACTERLIST_EDIT);
        delete.setId(MainView.ID_BTN_CHARACTERLIST_DELETE);
    }

    public void updateListView(ArrayList<String> characters) {
        characterList.getItems().addAll(characters);
    }

    public void registerButtonEvents(EventHandler<ActionEvent> buttonEventHandler) {
        add.setOnAction(buttonEventHandler);
    }
}

