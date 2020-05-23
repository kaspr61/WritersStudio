package com.team34.view.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Kasper S. Skott
 */

public class EditAssociationDialog extends Stage {

    private WindowResult windowResult;

    private TextField tfAssocLabel;

    public EditAssociationDialog(Stage ownerStage) {
        setTitle("Edit Association");
        setOnCloseRequest(e -> windowResult = WindowResult.CANCEL);

        // --- GUI elements --- //

        //Label
        Label lblAssocLabel = new Label("Association label:");

        //Textfield
        tfAssocLabel = new TextField();
        tfAssocLabel.setPromptText("Enter text to be displayed, or leave it empty");
        tfAssocLabel.setMaxWidth(180);

        //Button
        Button btnAdd = new Button("Ok");
        btnAdd.setOnAction(e -> { windowResult = WindowResult.OK; close(); });
        btnAdd.setDefaultButton(true);

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> { windowResult = WindowResult.CANCEL; close(); });
        btnCancel.setCancelButton(true);

        // --- Layouts --- //

        //Name Layout
        HBox nameLayout = new HBox();
        nameLayout.setMinHeight(30);
        nameLayout.setSpacing(10);
        nameLayout.getChildren().addAll(lblAssocLabel, tfAssocLabel);
//        nameLayout.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        //Add-Cancel Layout
        HBox buttonLayout = new HBox();
        buttonLayout.setSpacing(10);
        buttonLayout.getChildren().addAll(btnAdd, btnCancel);

        //Overall layout
        GridPane layout = new GridPane();
        layout.setMinSize(200,100);
        layout.setHgap(5);
        layout.setVgap(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 10, 10, 10));

        layout.add(nameLayout, 0, 0);
        layout.add(buttonLayout, 0, 1);

        // --- Set Scene --- //
        Scene scene = new Scene(layout);
        setScene(scene);

        // --- Set ownership and modality --- //
        initModality(Modality.WINDOW_MODAL);
        initOwner(ownerStage);

    }

    /**
     * Sets the text field, before calling {@link Stage#showAndWait()}.
     * @param label the label text of the association
     * @return how the user closed the window
     */
    public WindowResult showEditAssociation(String label) {
        setTitle("Edit Association");

        tfAssocLabel.setText(label);
        tfAssocLabel.requestFocus();
        showAndWait();

        return windowResult;
    }

    /**
     * Returns the text currently inputted in {@link #tfAssocLabel}
     * @return the input text
     */
    public String getAssociationLabel() {
        return tfAssocLabel.getText();
    }

    /**
     * Used to specify how the window was closed. If the user confirmed the action,
     * use OK, otherwise use CANCEL.
     */
    public enum WindowResult {
        OK,
        CANCEL
    }

}
