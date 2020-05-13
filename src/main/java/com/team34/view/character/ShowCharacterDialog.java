package com.team34.view.character;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Shows a dialog window containing a summary of the character data.
 * @author Jim Andersson
 */
public class ShowCharacterDialog extends Stage {

    private Button btnOk;
    private Label lblCharacter;
    private Label lblName;
    private Label lblDescription;
    private Text txtDescription;

    /**
     * Initializes dialog window.
     */
    public ShowCharacterDialog(Stage ownerStage) {
        setTitle("Edit Character");

        // --- GUI elements --- //

        //Label
        lblCharacter = new Label("Name: ");
        lblName = new Label();
        lblDescription = new Label("Description:");

        //Textfield
        lblName = new Label();
        lblName.setMaxWidth(150);

        //TextArea
        txtDescription = new Text();

        //Buttons
        btnOk = new Button("OK");
        btnOk.setOnAction(e -> { close(); });


        // --- Layouts --- //

        //Name Layout
        HBox nameLayout = new HBox();
        nameLayout.setMinHeight(30);
        nameLayout.setSpacing(10);
        nameLayout.getChildren().addAll(lblCharacter, lblName);

        //Description layout
        VBox descriptionLayout = new VBox();
        descriptionLayout.setMinHeight(40);
        nameLayout.setSpacing(10);
        nameLayout.getChildren().addAll(lblDescription, txtDescription);

        //Button Layout
        HBox buttonLayout = new HBox();
        buttonLayout.setSpacing(10);
        buttonLayout.getChildren().addAll(btnOk);

        //Overall Layout
        GridPane layout = new GridPane();
        layout.setMinSize(300, 300);
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
    public void showCharacter(Object[] data) {
        String name, description;
        name = (String)data[0];
        description = (String)data[1];

        lblName.setText(name);
        txtDescription.setText(description);

        showAndWait();
    }

}
