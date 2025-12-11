package com.example.golf;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameObject {
    private double x, y;
    private String color;;
    private ObjectType objectType;

    public  GameObject(double x, double y, Color color, ObjectType objectType) {
        this.x = x;
        this.y = y;
        this.color = String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
        this.objectType = objectType;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }

    public Color getColorAsColor() {
        return Color.web(color);
    }

    public String getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public ObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectType objectType) {
        this.objectType = objectType;
    }

    public void CollisionCheck(BallObject ballObject,ObjectType objectType){
        //using polymorpism This behavoir will change based on object type
    }

    public double getRadius() {
        return 0;
    }

    public void setRadius(double radius) {
    }

    public double getCenterY() {
        return 0;
    }

    public void setCenterY(double centerY) {
    }

    public double getCenterX() {
        return 0;
    }

    public void setCenterX(double centerX) {
    }

    public double getLenght() {
        return 0;
    }

    public double getHeight() {
        return 0;
    }

    public void setHeight(double height) {
    }

    public void setLenght(double height) {
    }

    public void draw(GraphicsContext gc){
    }

    boolean contains(double mx, double my) {
        return false;
    }
}
