package com.team34.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Jim Andersson
 */

public class EditEventPanel extends Stage {

    public EditEventPanel(Stage ownerStage) {
        setTitle("Edit Event");

        // --- GUI elements --- //

        //Label
        Label lblEventName = new Label("Event name:");
        Label lblEventGroup = new Label("Event group:");
        Label lblEventDescription = new Label("Event description:");

        //Textfield
        TextField tfEventName = new TextField();
        tfEventName.setPromptText("Enter event name here");
        tfEventName.setMaxWidth(150);

        //TextArea
        TextArea taEventDescription = new TextArea();
        taEventDescription.setPromptText("Enter event description here");

        //Drop down
        ComboBox<String> cbEventGroup = new ComboBox<>();
        cbEventGroup.setPromptText("Choose event group");

        //Button
        Button btnAdd = new Button("Add");
        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> close());

        // --- Layouts --- //

        //Name Layout
        HBox nameLayout = new HBox();
        nameLayout.setMinHeight(30);
        nameLayout.setSpacing(10);
        nameLayout.getChildren().addAll(lblEventName, tfEventName);
//        nameLayout.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        //EventGroup layout
        HBox eventGroupLayout = new HBox();
        eventGroupLayout.setSpacing(10);
        eventGroupLayout.getChildren().addAll(lblEventGroup, cbEventGroup);

        //Add-Cancel Layout
        HBox buttonLayout = new HBox();
        buttonLayout.setSpacing(10);
        buttonLayout.getChildren().addAll(btnAdd, btnCancel);

        //Overall layout
        GridPane layout = new GridPane();
        layout.setMinSize(100,300);
        layout.setHgap(5);
        layout.setVgap(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 10, 10, 10));

        layout.add(nameLayout, 0, 0);
        layout.add(eventGroupLayout, 0, 1);
        layout.add(lblEventDescription, 0, 2);
        layout.add(taEventDescription, 0,3);
        layout.add(buttonLayout, 0, 4);

        // --- Set Scene --- //
        Scene scene = new Scene(layout);
        setScene(scene);

        // --- Set ownership and modality --- //
        initModality(Modality.WINDOW_MODAL);
        initOwner(ownerStage);

    }

}
