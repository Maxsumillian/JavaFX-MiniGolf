package com.example.golf;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class BallObject extends CircularObject{

    private double initialX, initialY;
    private double vx;
    private double vy;
    private double friction;
    private double fricitonBase;
    private Boolean InHole = false;


    public void setInitialX(double initialX) {
        this.initialX = initialX;
    }
    public void setInitialY(double initialY) {
        this.initialY = initialY;
    }

    public BallObject(double x, double y, Color color,ObjectType objectType, double Radius) {
        super(x, y, color, objectType, Radius);
        fricitonBase = 0.98;
        friction = fricitonBase;
        initialX = x;
        initialY = y;
    }

    //Sets position of ball based on velocity
    public void update() {
//        x += vx;
//        y += vy;
        super.setX(super.getX() + vx);
        super.setY(super.getY() + vy);

        // slow down
        vx *= friction;
        vy *= friction;


        // stop when very slow
        if (Math.abs(vx) < 0.01) vx = 0;
        if (Math.abs(vy) < 0.01) vy = 0;

    }

    @Override
    public void draw(GraphicsContext gc) {
        super.draw(gc);
    }

    public void reset(){
        setX(initialX);
        setY(initialY);
        vx = 0;
        vy = 0;
    }

    public double getVY() {
        return vy;
    }

    public void setVY(double vy) {
        this.vy = vy;
    }

    public double getVX() {
        return vx;
    }

    public void setVX(double vx) {
        this.vx = vx;
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public Boolean getInHole() {
        return InHole;
    }

    public void setInHole(Boolean inHole) {
        InHole = inHole;
    }
}
