package com.team34;

/**
 *   ||=============================================||
 *   ||        @author Erik "the Noob" Johnsson     ||
 *   ||=============================================||
 */

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;


public class EventMainHomeView extends Application {
    Stage window;

    public static BorderPane createBorderPane(){
        BorderPane borderPane = new BorderPane();
        borderPane.getStyleClass().add("borderPane");
        borderPane.setPadding(new Insets(5));


        Label top = createLabel("top", "#151515"); //label högst upp
        borderPane.setTop(MenuBar.getClassCssMetaData().add(setUserAgentStylesheet()));
        top.setMaxHeight(150);

        Label left = createLabel("Left", "#efefef");
        borderPane.setLeft(left);
        left.setMaxSize(300,600);

        Label center = createLabel("center", "#151515");
        borderPane.setCenter(center);


        Label right = createLabel("right", "#efefef");
        borderPane.setRight(right);
        right.setMaxSize(300, 600);

        Label bottom = createLabel("bottom", "#252525");
        borderPane.setBottom(bottom);


        return borderPane;
    }

    private static Label createLabel(String text, String styleClass){
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        BorderPane.setMargin(label, new Insets(5));
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return label;
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(createBorderPane(), 1270,710);
        String styleSheet = getClass().getResource("/main.css").toExternalForm();
        scene.getStylesheets().add(styleSheet);
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Writer's Studio");


        MenuBar menuBar;
        menuBar = new MenuBar();
        menuBar.getMenus().addAll();
        menuBar.getStylesheets().add(styleSheet);
        createBorderPane().getChildren().add(menuBar);
        createBorderPane().getStylesheets().add(MenuBar.positionInArea(setUserAgentStylesheet(s)));



        VBox vBox = new VBox(menuBar);
        vBox.getStylesheets().add(getClass().getResource("css/main.css").toExternalForm());
        vBox.getStylesheets().add(styleSheet);

    }


    public static void main(String[] args) {
        launch(args);
    }

}