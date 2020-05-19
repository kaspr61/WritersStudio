package com.team34.model.character;

/**
 * @author Morgan Karlsson
 */

public class Character {

    private String name = "";
    private String description = "";
    private double chartPositionX = 0.0;
    private double chartPositionY = 0.0;


    public Character(String name, String description, double posX, double posY){
        this.name = name;
        this.description = description;
        chartPositionX = posX;
        chartPositionY = posY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getChartPositionX() {
        return chartPositionX;
    }

    public void setChartPositionX(double chartPositionX) {
        this.chartPositionX = chartPositionX;
    }

    public double getChartPositionY() {
        return chartPositionY;
    }

    public void setChartPositionY(double chartPositionY) {
        this.chartPositionY = chartPositionY;
    }
}
