package com.team34;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        com.team34.view.MainView view =
                new com.team34.view.MainView(stage, 1280, 720, false);

        com.team34.controller.MainController controller =
                new com.team34.controller.MainController(view);

    }

    public static void main(String[] args) {
        launch();
    }
}