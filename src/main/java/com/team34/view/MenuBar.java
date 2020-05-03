package com.team34.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Henrik Einestam
 */
public class MenuBar extends javafx.scene.control.MenuBar {

    private MenuItem fileOpen;
    private MenuItem fileExit;

    public MenuBar(Stage mainStage) {
        super();

        Menu menuFile = new Menu("File");

        fileOpen = new MenuItem("Open Project");
        fileOpen.setId(MainView.ID_MENU_OPEN);

        fileExit = new MenuItem("Exit");
        fileExit.setId(MainView.ID_MENU_EXIT);

        Menu menuEdit = new Menu("Edit");
        Menu subMenu3 = new Menu("Temp 3");
        MenuItem menuItem31 = new MenuItem("Temp 3.1");
        MenuItem menuItem32 = new MenuItem("Temp 3.2");
        MenuItem menuItem4 = new MenuItem("Temp 4");

        getMenus().add(menuFile);
        menuFile.getItems().addAll(fileOpen, fileExit);

        getMenus().add(menuEdit);
        menuEdit.getItems().add(subMenu3);
        subMenu3.getItems().add(menuItem31);
        subMenu3.getItems().add(menuItem32);
        menuEdit.getItems().add(menuItem4);

    }

    public void registerMenuBarAction(EventHandler<ActionEvent> menuActionHandler) {
        fileOpen.setOnAction(menuActionHandler);
        fileExit.setOnAction(menuActionHandler);
    }

}
