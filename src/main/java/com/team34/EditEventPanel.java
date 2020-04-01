package com.team34;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditEventPanel extends Application {
    Stage window;
    Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        window.setTitle("Edit Event");

        //Label
        Label lblEventName = new Label("Event name:");
        Label lblEventDescription = new Label("Event description:");

        //Textfield
        TextField tfEventName = new TextField();
        tfEventName.setPromptText("Enter event name here");

        //TextArea
        TextArea taEventDescription = new TextArea();
        taEventDescription.setPromptText("Enter event description here");

        //Button
        Button btnAdd = new Button("Add");
        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> window.close());

        //Layout
        HBox buttonLayout = new HBox();
        buttonLayout.setSpacing(10);
        buttonLayout.getChildren().addAll(btnAdd, btnCancel);

        GridPane layout = new GridPane();
        layout.setMinSize(100,300);
        layout.setHgap(5);
        layout.setVgap(5);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 10, 10, 10));

        layout.add(lblEventName, 0, 0);
        layout.add(tfEventName, 0, 1);
        layout.add(lblEventDescription, 0, 2);
        layout.add(taEventDescription, 0,3);
        layout.add(buttonLayout, 0, 4);

        scene = new Scene(layout);
        window.setScene(scene);
        window.show();

    }
}
