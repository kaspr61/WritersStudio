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
        cssMain = com.team34.App.class.getResource("css/main.css").toExternalForm();
        scene = new Scene(outerPane, 300, 500);
        scene.getStylesheets().add(cssMain);

        //Icon TODO: Hur fan får jag kompilatorn att inte ge mig null pointers för bilden?
        addCharacter = com.team34.App.class.getResource("icons/add_character.png").toExternalForm();
        FileInputStream inputAddCharacter = new FileInputStream(addCharacter);
        Image imgAddCharacter = new Image(inputAddCharacter);
        ImageView imageViewAddCharacter = new ImageView(imgAddCharacter);

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
        edit = new Button("Edit");
        delete = new Button("Delete");
        save = new Button("Save");
        cancel = new Button("Cancel");

        //Icons for Add/Edit/Delete buttons
        add.setGraphic(imageViewAddCharacter);

        //Label
        title = new Label("Characters");
        title.getStyleClass().add("character-list-headline");

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
