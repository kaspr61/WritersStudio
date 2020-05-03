package com.team34;

import com.team34.model.Project;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        com.team34.model.Project model =
                new com.team34.model.Project();

        Project.UserPreferences userPrefs = model.getUserPreferences();

        com.team34.view.MainView view =
                new com.team34.view.MainView(stage, userPrefs.windowWidth, userPrefs.windowHeight, userPrefs.windowMaximized);

        com.team34.controller.MainController controller =
                new com.team34.controller.MainController(view, model);

    }

    public static void main(String[] args) {
        launch();
    }
}