package com.example.golf;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CircularObject extends GameObject {

    private double centerX, centerY, radius;

    public CircularObject(double x, double y, Color color, ObjectType objectType, double radius) {
        super(x, y, color,  objectType);
        this.radius = radius;
        this.centerX = x - radius;
        this.centerY = y -  radius;
    }


    Boolean inTrap = false;

    public void CollisionCheck(BallObject ball, ObjectType objectType) {
        switch (objectType) {
            case WALL -> {
                double dx = ball.getX() - getX();
                double dy = ball.getY() - getY();
                double distSq = dx*dx + dy*dy;
                double radiusSum = ball.getRadius() + radius;

                if (distSq < radiusSum * radiusSum) {

                    double distance = Math.sqrt(distSq);

                    //Prevents divide by zero stops weird collision bugs
                    if (distance == 0) {
                        distance = 0.01;
                    }

                    double overlap = radiusSum - distance;

                    // 1. Push ball out
                    ball.setY(ball.getY() + (dy / distance) * overlap);
                    ball.setX(ball.getX() + (dx / distance) * overlap);

                    // 2. Reflect velocity
                    double nx = dx / distance;
                    double ny = dy / distance;

                    double dot = ball.getVX() * nx + ball.getVY() * ny;

                    ball.setVX(ball.getVX() - 2 * dot * nx);
                    ball.setVY(ball.getVY() - 2 * dot * ny);
                    SoundPlayer.playHitWall();
                }
            }
            case FLOOR -> {
                ;
            }
            case RESET -> {
                double dist = Math.hypot(ball.getX() - getX(), ball.getY() - getY());
                if (dist + ball.getRadius() < radius) {
                    ball.reset();
                    SoundPlayer.playWaterSplash();
                }
            }
            case TRAP -> {// if inside the area slow the ball down
                double dist = Math.hypot(ball.getX() - getX(), ball.getY() - getY());
                //friction apporach
//                if (dist + ball.getRadius() < radius) {
//                    double friction = 0.95;  // slows ball by 5% each frame
//                    ball.setVX(ball.getVX() * friction);
//                    ball.setVY(ball.getVY() * friction);
//
//                    // Stop ball if it's very slow
//                    if (Math.hypot(ball.getVX(), ball.getVY()) < 0.05) {
//                        ball.setVX(0);
//                        ball.setVY(0);
//                    }
//                }
                if (dist + ball.getRadius() > radius) {
                    inTrap = false;
                }

                if (dist + ball.getRadius() < radius ) {

                    double friction = 0.95;
                    ball.setVX(ball.getVX() * friction);
                    ball.setVY(ball.getVY() * friction);

                    // Optional: tiny pull toward trap center
                    double dx = getX() - ball.getX();
                    double dy = getY() - ball.getY();
                    double dist2 = Math.hypot(dx, dy);

                    //if the ball is inside the trap for the first time it pulls slightly
                    //this makes it so that the ball doesn't get magnetically pulled to the center in all frames
                    if (dist2 > 0 && !inTrap) {
                        inTrap = true;
                        ball.setVX(ball.getVX() + dx * 0.015);
                        ball.setVY(ball.getVY() + dy * 0.015);
                    }

                    if (Math.hypot(ball.getVX(), ball.getVY()) < 0.05) {
                        ball.setVX(0);
                        ball.setVY(0);
                    }
                }

            }

            // hole sucking logic made by Ai
            case HOLE  -> {
                // Distance from ball to hole center
                double dx = getX() - ball.getX();
                double dy = getY() - ball.getY();
                double dist = Math.hypot(dx, dy);

                // If ball is near the hole, apply "pull"
                double attractionRadius = radius + 20; // how far the hole pulls
                if (dist < attractionRadius) {
                    // Scale factor: closer = stronger pull
                    double strength = (attractionRadius - dist) / attractionRadius;

                    // Apply a small velocity toward the center
                    ball.setVX(ball.getVX() + dx * 0.0075 * strength);
                    ball.setVY(ball.getVY() + dy * 0.0075 * strength);
                }

                // Slow down if very close (original stopping logic)
                if (dist + ball.getRadius() < radius && Math.abs(ball.getVX()) < 0.1 && Math.abs(ball.getVY()) < 0.1) {
                    ball.setVX(0);
                    ball.setVY(0);
                    ball.setInHole(true);
                    ball.setColor(Color.DARKGRAY.darker().darker());
                }
            }
        }
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(getColorAsColor());
        gc.fillOval(getX() - radius, getY() - radius,
                radius * 2, radius * 2);
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    boolean contains(double mx, double my) {
        double dist = Math.hypot(mx - getX(), my - getY());
        return dist < radius;
    }

}
