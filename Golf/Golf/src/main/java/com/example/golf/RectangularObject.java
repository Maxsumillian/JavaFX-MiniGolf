package com.example.golf;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static java.lang.Math.sqrt;

public class RectangularObject extends GameObject{

    private double lenght, height;

    public RectangularObject(double x, double y, Color color, ObjectType objectType , double lenght , double height) {
        super(x, y, color,  objectType);
        this.lenght = lenght;
        this.height = height;
    }

    @Override
    public void CollisionCheck(BallObject ball, ObjectType objectType) {
        switch (objectType) {
            case WALL -> {
                //checks if the ball is within the x bounds of the box
                if( ball.getX() < getX() + getLenght() && ball.getX() > getX()){
                    // top collision
                    // this is to split the bounce logic of top vs bottom
                    if (ball.getY() < getY()){
                        if (ball.getY() + ball.getRadius() > getY()) {

                            ball.setY(ball.getY() - ball.getRadius());
                            ball.setVY(ball.getVY() * -1);
                            ball.update();
                            SoundPlayer.playHitWall(sqrt(ball.getVX()* ball.getVX() + ball.getVY()* ball.getVY()));

                        }
                    }

                    // Bottom collision doesn't work // Works now :)
                    // this is to split the bounce logic of top vs bottom
                    if  (ball.getY() > getY()) {
                        if (ball.getY() < getY() + getHeight() + ball.getRadius()) {

                            ball.setY(ball.getY() + ball.getRadius());
                            ball.setVY(ball.getVY() * -1);
                            ball.update();
                            SoundPlayer.playHitWall(sqrt(ball.getVX()* ball.getVX() + ball.getVY()* ball.getVY()));
                        }
                    }
                }

                if( ball.getY() < getY() + getHeight() && ball.getY() > getY()){

                    //right collision // Works now :)
                    if (ball.getX() < getX()) {
                        if (ball.getX() + ball.getRadius() > getX()){
                            ball.setX(ball.getX() -  ball.getRadius());
                            ball.setVX(ball.getVX() * -1);
                            ball.update();
                            SoundPlayer.playHitWall(sqrt(ball.getVX()* ball.getVX() + ball.getVY()* ball.getVY()));
                        }
                    }

//             left doesnt work // Works now :)
                    if  (ball.getX() > getX()) {
                        if (ball.getX() < getX() + getLenght() + ball.getRadius()){

                            ball.setX(ball.getX() +  ball.getRadius());
                            ball.setVX(ball.getVX() * -1);
                            ball.update();
                            SoundPlayer.playHitWall(sqrt(ball.getVX()* ball.getVX() + ball.getVY()* ball.getVY()));
                        }
                    }

                }
            }
            case FLOOR -> {
                // if the balls x pos is less than its radius that means that it hit the edge of the canvas
//                if (ball.getX() - ball.getRadius() < 0) {
//                    ball.setX(ball.getRadius());
//                    ball.setVX(ball.getVX() * -1);
//                }
                // if the balls x pos + its radius is greater than teh width of the canvas it has hit the edge of the canvas
//                if (ball.getX() + ball.getRadius() > getWidth()) {
//                    ball.setX(getWidth() - ball.getRadius());
//                    ball.setVX(ball.getVX() * -1);

//                }
//                if (ball.getY() - ball.getRadius() < 0) {
//                    ball.setY(ball.getRadius());
//                    ball.setVY(ball.getVY() * -1);
//                }
//                if (ball.getY() + ball.getRadius() > getHeight()) {
//                    ball.setY(getHeight() - ball.getRadius());
//                    ball.setVY(ball.getVY() * -1);
//                }

                // if the balls x pos is less than its radius that means that it hit the edge of the canvas
                if (ball.getX() < getX() - ball.getRadius() //checks for left collision
                        || ball.getX()  > getLenght() + ball.getRadius()// checks for right collision
                        || ball.getY()  < getY() - ball.getRadius()// checks for top colision
                        || ball.getY()  > getHeight() + ball.getRadius()){// checks for bottom
                    // if ball out of bounds of floor reset Ball
//                    ball.reset();
                    //this causese a bug where if i have multiple floors then no matter waht it resets the ball because this requires it to be in all of them at once

                }
            }
            case RESET -> {// if ball is in this area reset it
                if     (ball.getX() + ball.getRadius() > getX()
                        && ball.getX() - ball.getRadius() < getX() + getLenght()
                        && ball.getY() + ball.getRadius() > getY()
                        && ball.getY() - ball.getRadius() < getY() + getHeight()){
                    ball.reset();

                    // fun sounds :)
                    SoundPlayer.playWaterSplash();
                }

            }
            case TRAP -> {// if inside the area slow the ball down
                // Rectangle bounds
                double left = getX();
                double right = getX() + getLenght();
                double top = getY();
                double bottom = getY() + getHeight();

                // Check if ball is inside rectangle (with radius)
                if (ball.getX() + ball.getRadius() > left &&
                        ball.getX() - ball.getRadius() < right &&
                        ball.getY() + ball.getRadius() > top &&
                        ball.getY() - ball.getRadius() < bottom) {


                    double friction = 0.95;
                    ball.setVX(ball.getVX() * friction);
                    ball.setVY(ball.getVY() * friction);


                    if (Math.hypot(ball.getVX(), ball.getVY()) < 0.05) {
                        ball.setVX(0);
                        ball.setVY(0); // reset for next rectangle
                    }
                }
            }

        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(getColorAsColor());
        gc.fillRect(getX(), getY(), getLenght(), getHeight());
    }

    public double getLenght() {
        return lenght;
    }

    public void setLenght(double lenght) {
        this.lenght = lenght;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    boolean contains(double mx, double my) {
        return mx >= getX() && mx <= getX() + lenght &&
                my >= getY() && my <= getY() + height;
    }

}
