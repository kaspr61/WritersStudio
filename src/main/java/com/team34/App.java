package com.team34;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        VBox vBox = new VBox();
        vBox.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());

        Scene scene = new Scene(vBox, 1280, 720);

        stage.setTitle("Writer's Studio");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}