package com.team34.view.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * @author Morgan Karlsson
 */

public class EditCharacterDialog extends Stage {

    private Button btnSave;
    private Button btnCancel;
    private TextField tfCharacterName;
    private TextArea taCharacterDescription;
    private WindowResult windowResult;

    public EditCharacterDialog(Stage ownerStage) {
        setTitle("Edit Character");
        setOnCloseRequest(e -> windowResult = EditCharacterDialog.WindowResult.CANCEL);

        // --- GUI elements --- //

        //Label
        Label lblCharacterName = new Label("Character name:");
        Label lblCharacterDescription = new Label("Character description:");

        //Textfield
        tfCharacterName = new TextField();
        tfCharacterName.setPromptText("Enter character name here");
        tfCharacterName.setMaxWidth(150);

        //TextArea
        taCharacterDescription = new TextArea();
        taCharacterDescription.setPromptText("Enter character description here");

        //Buttons
        btnSave = new Button("Save");
        btnSave.setOnAction(e -> { windowResult = EditCharacterDialog.WindowResult.OK; close(); });

        btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> { windowResult = EditCharacterDialog.WindowResult.CANCEL; close(); });


        // --- Layouts --- //

        //Name Layout
        HBox nameLayout = new HBox();
        nameLayout.setMinHeight(30);
        nameLayout.setSpacing(10);
        nameLayout.getChildren().addAll(lblCharacterName, tfCharacterName);

        //Add-Cancel Layout
        HBox buttonLayout = new HBox();
        buttonLayout.setSpacing(10);
        buttonLayout.getChildren().addAll(btnSave, btnCancel);

        //Overall Layout
        GridPane layout = new GridPane();
        layout.setMinSize(100, 300);
        layout.setHgap(5);
        layout.setVgap(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 10, 10, 10));

        layout.add(nameLayout, 0, 0);
        layout.add(lblCharacterDescription, 0, 1);
        layout.add(taCharacterDescription, 0, 2);
        layout.add(buttonLayout, 0, 3);

        // --- Set Scene --- //
        Scene scene = new Scene(layout);
        setScene(scene);

        // --- Set ownership and modality --- //
        initModality(Modality.WINDOW_MODAL);
        initOwner(ownerStage);
    }

    /**
     * Displays the New Character dialog window.
     * @author Jim Andersson
     * @return how the user closed the window
     */
    public WindowResult showCreateCharacter() {
        setTitle("New Character");

        tfCharacterName.setText("");
        taCharacterDescription.setText("");

        tfCharacterName.requestFocus();
        showAndWait();

        return windowResult;
    }

    /**
     * Shows the Edit Character dialog window.
     * @author Jim Andersson
     * @param name Character name
     * @param description Character description
     * @return how the user closed the window
     */
    public WindowResult showEditCharacter(String name, String description) {
        setTitle("Edit Character");

        tfCharacterName.setText(name);
        taCharacterDescription.setText(description);

        tfCharacterName.requestFocus();
        showAndWait();

        return windowResult;
    }

    public String getCharacterName() {
        return tfCharacterName.getText();
    }

    public String getCharacterDescription() {
        return taCharacterDescription.getText();
    }

    /**
     * Used to specify how the window was closed. If the user confirmed the action,
     * use OK, otherwise use CANCEL.
     * @author Kasper S. Skott
     */
    public enum WindowResult {
        OK,
        CANCEL
    }


}
