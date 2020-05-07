package com.team34.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;/**
 * @author Morgan Karlsson
 */

public class EditEventPanel extends Stage {

    private Button btnSave;
    private Button btnCancel;
    private TextField tfEventTitle;
    private TextArea taEventDescription;
    private WindowResult windowResult;

    public EditEventPanel(Stage ownerStage) {
        setTitle("Edit Event");

        // --- GUI elements --- //

        //Label
        Label lblEventTitle = new Label("Event title:");
        Label lblEventDescription = new Label("Event description:");

        //Textfield
        tfEventTitle = new TextField();
        tfEventTitle.setPromptText("Enter event title here");
        tfEventTitle.setMaxWidth(150);

        //TextArea
        taEventDescription = new TextArea();
        taEventDescription.setPromptText("Enter event description here");

        //Buttons
        btnSave = new Button("Save");
        btnSave.setOnAction(e -> { windowResult = EditEventPanel.WindowResult.OK; close(); });

        btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> { windowResult = EditEventPanel.WindowResult.CANCEL; close(); });


        // --- Layouts --- //

        //Name Layout
        HBox titleLayout = new HBox();
        titleLayout.setMinHeight(30);
        titleLayout.setSpacing(10);
        titleLayout.getChildren().addAll(lblEventTitle, tfEventTitle);

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

        layout.add(titleLayout, 0, 0);
        layout.add(lblEventDescription, 0, 1);
        layout.add(taEventDescription, 0, 2);
        layout.add(buttonLayout, 0, 3);

        // --- Set Scene --- //
        Scene scene = new Scene(layout);
        setScene(scene);

        // --- Set ownership and modality --- //
        initModality(Modality.WINDOW_MODAL);
        initOwner(ownerStage);
    }

    /**
     * Displays the New Event dialog window.
     * @author Jim Andersson
     * @return how the user closed the window
     */
    public WindowResult showCreateEvent() {
        setTitle("New Event");

        tfEventTitle.setText("");
        taEventDescription.setText("");

        tfEventTitle.requestFocus();
        showAndWait();

        return windowResult;
    }

    /**
     * Shows the Edit Event dialog window.
     * @author Jim Andersson
     * @param title Event title
     * @param description Event description
     * @return how the user closed the window
     */
    public WindowResult showEditEvent(String title, String description) {
        setTitle("Edit Event");

        tfEventTitle.setText(title);
        taEventDescription.setText(description);

        tfEventTitle.requestFocus();
        showAndWait();

        return windowResult;
    }

    public String getEventTitle() {
        return tfEventTitle.getText();
    }

    public String getEventDescription() {
        return taEventDescription.getText();
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

