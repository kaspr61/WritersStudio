package com.team34.view.character;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Shows a dialog window containing a summary of the character data.
 * @author Jim Andersson
 */
public class ShowCharacterDialog extends Stage {

    private Button btnEdit;
    private Button btnBack;
    private boolean edit;
    private Label lblName;
    private Label lblCharacterName;
    private Label lblDescription;
    private Text txtDescription;

    /**
     * Initializes dialog window.
     */
    public ShowCharacterDialog(Stage ownerStage) {
        setTitle("Character Summary");
        setOnCloseRequest( e-> edit = false);
        edit = false;

        // --- GUI elements --- //

        //Label
        lblName = new Label("Name: ");
        lblName.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        lblCharacterName = new Label();

        lblDescription = new Label("Description:");
        lblDescription.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

        //Textfield
        lblCharacterName = new Label();
        lblCharacterName.setMaxWidth(150);

        //TextArea
        txtDescription = new Text();


        //Buttons
        btnBack = new Button("Back");
        btnBack.setOnAction(e -> { edit = false; close(); });

        btnEdit = new Button("Edit");
        btnEdit.setOnAction(e -> { edit = true; close();});



        // --- Layouts --- //

        //Name Layout
        HBox nameLayout = new HBox();
        nameLayout.setMinHeight(30);
        nameLayout.setSpacing(10);
        nameLayout.setAlignment(Pos.CENTER_LEFT);
        nameLayout.getChildren().addAll(lblName, lblCharacterName);
        nameLayout.getStyleClass().add("name");

        //Description layout
        VBox descriptionLayout = new VBox();
        descriptionLayout.setMinHeight(40);
        descriptionLayout.setSpacing(10);
        descriptionLayout.getChildren().addAll(lblDescription, txtDescription);

        //Button Layout
        HBox buttonLayout = new HBox();
        buttonLayout.setSpacing(10);
        buttonLayout.setPadding(new Insets(20, 0, 0, 0));
        buttonLayout.getChildren().addAll(btnEdit, btnBack);

        //Overall Layout
        GridPane layout = new GridPane();
        layout.setMinSize(250, 300);
        layout.setMaxSize(500, 500);
        layout.setHgap(5);
        layout.setVgap(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 10, 10, 10));

        layout.add(nameLayout, 0, 0);
        layout.add(lblDescription, 0, 2);
        layout.add(txtDescription, 0, 3);
        layout.add(buttonLayout, 0, 4);

        // --- Set Scene --- //
        Scene scene = new Scene(layout);
        setScene(scene);

        // --- Set ownership and modality --- //
        initModality(Modality.WINDOW_MODAL);
        initOwner(ownerStage);
    }

    /**
     * Sets the character data displayed in the dialog window.
     * @param data array containing character information
     */
    public boolean showCharacter(Object[] data) {
        String name, description;
        name = (String)data[0];
        description = (String)data[1];

        lblCharacterName.setText(name);
        txtDescription.setText(description);
        setTitle(name);

        showAndWait();
        return edit;
    }
}
