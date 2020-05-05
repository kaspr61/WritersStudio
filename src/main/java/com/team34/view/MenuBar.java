package com.team34.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * @author Henrik Einestam
 */
public class MenuBar extends javafx.scene.control.MenuBar {

    private MenuItem fileNew;
    private MenuItem fileOpen;
    private MenuItem fileSave;
    private MenuItem fileSaveAs;
    private MenuItem fileExit;

    public MenuBar(Stage mainStage) {
        super();

        Menu menuFile = new Menu("File");

        fileNew = new MenuItem("New");
        fileNew.setId(MainView.ID_MENU_NEW);
        fileNew.setAccelerator(new KeyCodeCombination(
                KeyCode.N, KeyCombination.CONTROL_DOWN));

        fileOpen = new MenuItem("Open");
        fileOpen.setId(MainView.ID_MENU_OPEN);
        fileOpen.setAccelerator(new KeyCodeCombination(
                KeyCode.O, KeyCombination.CONTROL_DOWN));

        fileSave = new MenuItem("Save");
        fileSave.setId(MainView.ID_MENU_SAVE);
        fileSave.setAccelerator(new KeyCodeCombination(
                KeyCode.S, KeyCombination.CONTROL_DOWN));

        fileSaveAs = new MenuItem("Save As");
        fileSaveAs.setId(MainView.ID_MENU_SAVE_AS);
        fileSaveAs.setAccelerator(new KeyCodeCombination(
                KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

        fileExit = new MenuItem("Exit");
        fileExit.setId(MainView.ID_MENU_EXIT);

        Menu menuEdit = new Menu("Edit");
        Menu subMenu3 = new Menu("Temp 3");
        MenuItem menuItem31 = new MenuItem("Temp 3.1");
        MenuItem menuItem32 = new MenuItem("Temp 3.2");
        MenuItem menuItem4 = new MenuItem("Temp 4");

        getMenus().add(menuFile);
        menuFile.getItems().addAll(fileNew, fileOpen, fileSave, fileSaveAs, fileExit);

        getMenus().add(menuEdit);
        menuEdit.getItems().add(subMenu3);
        subMenu3.getItems().add(menuItem31);
        subMenu3.getItems().add(menuItem32);
        menuEdit.getItems().add(menuItem4);

    }

    public void registerMenuBarAction(EventHandler<ActionEvent> menuActionHandler) {
        fileNew.setOnAction(menuActionHandler);
        fileOpen.setOnAction(menuActionHandler);
        fileSave.setOnAction(menuActionHandler);
        fileSaveAs.setOnAction(menuActionHandler);
        fileExit.setOnAction(menuActionHandler);
    }

}
