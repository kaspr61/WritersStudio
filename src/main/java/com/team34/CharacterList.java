package com.team34;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;


public class CharacterList extends Application {
    private ListView<String> characterList;
    private Button add, edit, delete;
    private Button save, cancel;
    private Label title;

    // Test purposes
    private Stage window;
    private Scene scene;

    //CSS
    private String cssMain;

    //Icons
    private String addCharacter;
    private String editCharacter;
    private String deleteCharacter;
    private final int ICON_SIZE = 40;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;

        //Panes
        BorderPane outerPane = new BorderPane();
        BorderPane innerPane = new BorderPane();

        //CSS
        cssMain = com.team34.App.class.getResource("css/characterlist.css").toExternalForm();
        scene = new Scene(outerPane, 300, 500);
        scene.getStylesheets().add(cssMain);

        //Add, Edit, Delete buttons
        HBox aedBox = new HBox();
        aedBox.setPadding(new Insets(10, 10, 10, 10));
        aedBox.setSpacing(20);

        //Save, Cancel buttons
        HBox sdBox = new HBox();
        sdBox.setPadding(new Insets(10, 10, 10, 10));
        sdBox.setSpacing(40);
        sdBox.setAlignment(Pos.CENTER);

        //For the character list
        VBox characterBox = new VBox();
        characterBox.setPadding(new Insets(10, 10, 10, 10));

        //Buttons
        add = new Button();
        edit = new Button();
        delete = new Button();
        save = new Button("Save");
        cancel = new Button("Cancel");

        save.getStyleClass().add("button-save-cancel"); //TODO: Fixa i stylesheet så att save och cancel behåller default
        cancel.getStyleClass().add("button-save-cancel");

        //Icons for Add/Edit/Delete
        addCharacter = com.team34.App.class.getResource("icons/add_character.png").getPath(); //Filestream for icon
        FileInputStream inputAddCharacter = new FileInputStream(addCharacter);
        Image imgAddCharacter = new Image(inputAddCharacter);
        ImageView imageViewAddCharacter = new ImageView(imgAddCharacter);

        editCharacter = com.team34.App.class.getResource("icons/edit_character.png").getPath();
        FileInputStream inputEditCharacter = new FileInputStream(editCharacter);
        Image imgEditCharacter = new Image(inputEditCharacter);
        ImageView imageViewEditCharacter = new ImageView(imgEditCharacter);

        deleteCharacter = com.team34.App.class.getResource("icons/delete_character.png").getPath();
        FileInputStream inputDeleteCharacter = new FileInputStream(deleteCharacter);
        Image imgDeleteCharacter = new Image(inputDeleteCharacter);
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

        //Label
        title = new Label("Characters");
        title.setPadding(new Insets(20, 0, 0, 0));

        //Character List
        ArrayList<String> characters = new ArrayList<>();
        characterList = new ListView<>();
        characters.add("Hamlet");
        characters.add("Claudius");
        characters.add("Ophelia");
        characterList.getItems().addAll(characters);

        //Construct
        aedBox.getChildren().addAll(add, edit, delete);
        aedBox.setAlignment(Pos.CENTER);

        characterBox.getChildren().addAll(characterList);
        sdBox.getChildren().addAll(save, cancel);

        innerPane.setTop(aedBox);
        innerPane.setCenter(characterBox);

        outerPane.setTop(title);
        outerPane.setCenter(innerPane);
        outerPane.setBottom(sdBox);
        BorderPane.setAlignment(title, Pos.CENTER);


        /**
         * For test purposes
         */


        window.setScene(scene);
        window.show();

    }


}
