package com.team34;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        com.team34.MenuBar menuBar = new com.team34.MenuBar();
        menuBar.getMenus().addAll();

        VBox vBox = new VBox(menuBar);
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