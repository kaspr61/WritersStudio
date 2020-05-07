package com.team34.view.event;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Shows a dialog window containing a summary of the event data.
 * @author Jim Andersson
 */
public class ShowEventDialog extends Stage {

    private Button btnOk;
    private Label lblEventTitle;
    private Text txtEventDescription;

    /**
     * Initializes dialog window.
     */
    public ShowEventDialog(Stage ownerStage) {
        setTitle("Edit Event");

        // --- GUI elements --- //

        //Label
        lblEventTitle = new Label("Event title:");

        //Textfield
        lblEventTitle = new Label();
        lblEventTitle.setMaxWidth(150);

        //TextArea
        txtEventDescription = new Text();

        //Buttons
        btnOk = new Button("OK");
        btnOk.setOnAction(e -> { close(); });


        // --- Layouts --- //

        //Title Layout
        HBox nameLayout = new HBox();
        nameLayout.setMinHeight(30);
        nameLayout.setSpacing(10);
        nameLayout.getChildren().addAll(lblEventTitle);

        //Button Layout
        HBox buttonLayout = new HBox();
        buttonLayout.setSpacing(10);
        buttonLayout.getChildren().addAll(btnOk);

        //Overall Layout
        GridPane layout = new GridPane();
        layout.setMinSize(100, 300);
        layout.setHgap(5);
        layout.setVgap(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10, 10, 10, 10));

        layout.add(nameLayout, 0, 0);
        layout.add(txtEventDescription, 0, 2);
        layout.add(buttonLayout, 0, 3);

        // --- Set Scene --- //
        Scene scene = new Scene(layout);
        setScene(scene);

        // --- Set ownership and modality --- //
        initModality(Modality.WINDOW_MODAL);
        initOwner(ownerStage);
    }

    /**
     * Sets the character data displayed in the dialog window.
     * @param title Event title.
     * @param description Event description.
     */
    public void setData(String title, String description) {
        lblEventTitle.setText(title);
        txtEventDescription.setText(description);
    }

}
