package com.team34;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class MenuBar extends javafx.scene.control.MenuBar {

    public MenuBar() {

        super();

        Menu menuFile = new Menu("File");
        Menu subMenu1 = new Menu("Temp 1");
        MenuItem menuItem11 = new MenuItem("Temp 1.1");
        MenuItem menuItem12 = new MenuItem("Temp 1.2");
        MenuItem menuItem2 = new MenuItem("Temp 2");
        Menu menuEdit = new Menu("Edit");
        Menu subMenu3 = new Menu("Temp 3");
        MenuItem menuItem31 = new MenuItem("Temp 3.1");
        MenuItem menuItem32 = new MenuItem("Temp 3.2");
        MenuItem menuItem4 = new MenuItem("Temp 4");

        getMenus().add(menuFile);
        menuFile.getItems().add(subMenu1);
        subMenu1.getItems().add(menuItem11);
        subMenu1.getItems().add(menuItem12);
        menuFile.getItems().add(menuItem2);
        getMenus().add(menuEdit);
        menuEdit.getItems().add(subMenu3);
        subMenu3.getItems().add(menuItem31);
        subMenu3.getItems().add(menuItem32);
        menuEdit.getItems().add(menuItem4);
    }
}
