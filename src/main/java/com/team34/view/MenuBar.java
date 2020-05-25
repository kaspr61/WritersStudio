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

    private MenuItem editAddCharacter;
    private MenuItem editAddEvent;
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
        Menu editSubMenu = new Menu("New");

        editAddCharacter = new MenuItem("Character");
        editAddCharacter.setId(MainView.ID_MENU_ADD_CHARACTER);
        editAddCharacter.setAccelerator(new KeyCodeCombination(
                KeyCode.C, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

        editAddEvent = new MenuItem("Event");
        editAddEvent.setId(MainView.ID_MENU_ADD_EVENT);
        editAddEvent.setAccelerator(new KeyCodeCombination(
                KeyCode.E, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

        getMenus().add(menuFile);
        menuFile.getItems().addAll(fileNew, fileOpen, fileSave, fileSaveAs, fileExit);

        getMenus().add(menuEdit);
        menuEdit.getItems().add(editSubMenu);
        editSubMenu.getItems().add(editAddCharacter);
        editSubMenu.getItems().add(editAddEvent);

    }

    /**
     * Registers the given EventHandler on the menu items.
     * @param menuActionHandler
     */
    public void registerMenuBarAction(EventHandler<ActionEvent> menuActionHandler) {
        fileNew.setOnAction(menuActionHandler);
        fileOpen.setOnAction(menuActionHandler);
        fileSave.setOnAction(menuActionHandler);
        fileSaveAs.setOnAction(menuActionHandler);
        fileExit.setOnAction(menuActionHandler);
        editAddCharacter.setOnAction(menuActionHandler);
        editAddEvent.setOnAction(menuActionHandler);
    }

}
