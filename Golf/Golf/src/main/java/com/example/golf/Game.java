package com.example.golf;

//Note!!!!!! CHATGPT Helped me set up my Project Structure figuring out class structure
import com.almasb.fxgl.audio.Sound;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;


public class Game {


    private ModeManager modeManager;
    private Color grassColor = Color.LIMEGREEN;

    private int strokeCount = 0;
    private int currentLvl = -1;

    private ScoreManager scoreManager;
    private boolean shiftDown;


    public int getStrokeCount(){
        return strokeCount;
    }


    public void setScoreManager(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
    }

    //============================== Mode selection (dual purpose toggle)================================
    public void setModeManager(ModeManager mm) {
        this.modeManager = mm;

        // update UI immediately
        updateBehavior();

        // live updates if the mode changes
        modeManager.modeProperty().addListener((obs, oldMode, newMode) -> {
            updateBehavior();
        });
    }

    private void updateBehavior() {
        if (modeManager.getMode() == ModeManager.Mode.EDIT) {
            grassColor = Color.DARKGREEN;
        } else {
            grassColor = Color.LIMEGREEN;
        }
    }


//================================== Default Values ===========================

    ArrayList<GameObject> gameObjects;
    BallObject ball = new BallObject(1,1,Color.WHITE,ObjectType.BALL,1);
    CircularObject hole = new CircularObject(1,1,Color.BLACK,ObjectType.HOLE,1);
    GraphicsContext gc;


    //=================================================================
    public Game(GraphicsContext gc , ArrayList<GameObject> gameObjects) {
        this.gc = gc;
        this.gameObjects = gameObjects;
        this.ball = findBall(gameObjects);// checks for ball in object list and if found returns ball
        this.hole = findHole(gameObjects);// checks for Hole in object list and if found returns hole

    }

    // NOTE TO SELF: put some crash handling here and this also serves top see if the gmae even had a ball
    public BallObject findBall(ArrayList<GameObject> gameObjects){
        for(GameObject current : gameObjects){
            if (current instanceof BallObject){
                return (BallObject) current;
            }
        }
        return null;
    }

    public CircularObject findHole(ArrayList<GameObject> gameObjects){
        for(GameObject current : gameObjects){
            if (current instanceof CircularObject && current.getObjectType().equals(ObjectType.HOLE)){
                return (CircularObject) current;
            }
        }
        return null;
    }



    //==================== Dragging Controls =========================

    // Tracking Dragging used to track mouse movent for base controls
    public boolean dragging = false;
    public double dragStartX, dragStartY;
    public double dragEndX, dragEndY;

    GameObject selected = null, lastSelected = null;
    double offsetX, offsetY;

    public void startDrag(double x, double y) {
        dragging = true;
        dragStartX = x;
        dragStartY = y;



        if (modeManager.getMode() == ModeManager.Mode.EDIT) {

            for (GameObject obj : gameObjects) {         // ← Your list of objects
                if (obj.contains(dragStartX, dragStartY)) {
                    selected = obj;
                    offsetX = dragStartX - obj.getX();
                    offsetY = dragStartY - obj.getY();
                    break;
                }
            }

            strokeCount = 999999999;

        } else {
            strokeCount ++;
        }

    }

    // gets last updated drag position
    public void updateDrag(double x, double y) {
        dragEndX = x;
        dragEndY = y;

        if (modeManager.getMode() == ModeManager.Mode.EDIT && selected != null) {

            if (shiftDown) {
                if (selected instanceof CircularObject) {
                    selected.setRadius(Math.max(Math.sqrt(Math.pow(dragEndX - selected.getX(), 2) + Math.pow(dragEndY - selected.getY(), 2)),5));
                }
                if (selected instanceof RectangularObject) {
                    selected.setHeight(abs(dragEndY - selected.getY()) + 5);
                    selected.setLenght(abs(dragEndX - selected.getX()) + 5);
                }
            }else{
                selected.setX(dragEndX - offsetX);
                selected.setY(dragEndY - offsetY);
            }
        }

    }

    // Get Distance and apply to ball velocity
    public void endDrag() {


        if (modeManager.getMode() == ModeManager.Mode.EDIT) {
            if (selected != null) {
                if (selected instanceof BallObject || selected.getObjectType().equals(ObjectType.HOLE)) {
                    lastSelected  = null;
                }else{
                    lastSelected  = selected;
                }
            }

            selected = null;

        } else {
            dragging = false;

            double dx = dragStartX - dragEndX;
            double dy = dragStartY - dragEndY;

            ball.setVX(dx * .05);
            ball.setVY(dy * .05);
        }
    }

    //=============================== Initializing ===========================================

//  This runs later. It helps with setting up variables that need time after the canvas has been made
//  Currently not being used but may be useful
    public void initialize() {
        Platform.runLater(() -> {
//            This was for testing purposes
//            holeX = ThreadLocalRandom.current().nextDouble((2*holeRadius), gc.getCanvas().getWidth()-(2*holeRadius));
//            holeY = ThreadLocalRandom.current().nextDouble((2*holeRadius), gc.getCanvas().getHeight()-(2*holeRadius));
//            walls.add(testWall);
//            walls.add(new Wall(50, 50, 100, 100));
//            draw();
        });
    }

    //================================= Animation / Tick Event / start ============================

    // Run the game updates and draws animation
    public void start() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                //Win Condition
                if(ball.getInHole()){
                    stop();
//                    System.out.println(strokeCount);
                    SoundPlayer.playVictory();

                    if(scoreManager != null){
                            scoreManager.setLevelScore(currentLvl,strokeCount);
                        System.out.println("Current LVL: "+ currentLvl);
                        System.out.println("Current Score: "+ scoreManager.getLevelScore(currentLvl));



                    }


                }

                update();
                //sorting For drawing objects in order from bottom to top
                gameObjects.sort(Comparator.comparingInt(o -> o.getObjectType().Priority));

                draw();
            }
        };
        timer.start();
    }

    //Updates the Position of ball and Checks for collisions
    void update() {
        //updates ball position
        ball.update();

        //check for collision against the edges  of the canvas
        canvasCollision();

        //check object collision
        checkCollisions();
    }
    //Note: this is only working for canvas measurements but the logic of inverting its velocity is key
    //when other objects such as a wall overlaps with the ball it will flip its velocity
    public void checkCollisions() {
        for (GameObject current : gameObjects) {
            current.CollisionCheck(ball,current.getObjectType());
        }
    }

    //============================================ Methods used to Display Animation =======================

    void draw() {

        //clear screen
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        //paints background
        gc.setFill(grassColor);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        //loops to draw objects
        for (GameObject current : gameObjects) {
            // each object has its own draw method
            current.draw(gc);
        }

        //Draws Red Arrow on screen if on play mode
        if (dragging && modeManager.getMode() == ModeManager.Mode.PLAY) {
            drawArrow();
        }

    }

    // Stolen from Chat-GPT ;p
    private void drawArrow() {
        double startX = ball.getX();
        double startY = ball.getY();

        double endX = startX + (dragStartX - dragEndX);
        double endY = startY + (dragStartY - dragEndY);

        gc.setStroke(Color.RED);
        gc.setLineWidth(3);

        gc.strokeLine(startX, startY, endX, endY);

        double angle = Math.atan2(endY - startY, endX - startX);
        double arrowLen = 15;

        gc.strokeLine(endX, endY,
                endX + arrowLen * Math.cos(angle + Math.toRadians(150)),
                endY + arrowLen * Math.sin(angle + Math.toRadians(150)));

        gc.strokeLine(endX, endY,
                endX + arrowLen * Math.cos(angle - Math.toRadians(150)),
                endY + arrowLen * Math.sin(angle - Math.toRadians(150)));
    }


    //reset the ball position
    public void reset() {
        ball.reset();
    }

    // canvas collision seperated if I wanted to add floors or other mechanics for future planning
    private void canvasCollision(){
        if (ball.getX() - ball.getRadius() < 0) {
            ball.setX(ball.getRadius());
            ball.setVX(ball.getVX() * -1);
//            Toolkit.getDefaultToolkit().beep(); // testing sound
            HitCanvas();
        }

        // if the balls x pos + its radius is greater than the width of the canvas it has hit the edge of the canvas
        if (ball.getX() + ball.getRadius() > gc.getCanvas().getWidth()) {
            ball.setX(gc.getCanvas().getWidth() - ball.getRadius());
            ball.setVX(ball.getVX() * -1);
            HitCanvas();

        }
        if (ball.getY() - ball.getRadius() < 0) {
            ball.setY(ball.getRadius());
            ball.setVY(ball.getVY() * -1);
            HitCanvas();

        }
        if (ball.getY() + ball.getRadius() > gc.getCanvas().getHeight()) {
            ball.setY(gc.getCanvas().getHeight() - ball.getRadius());
            ball.setVY(ball.getVY() * -1);
            HitCanvas();

        }
    }

    //=========================================== Sound Methods =======================================

    //small toggle that switches sound on each canvas hit :P
    private boolean toggle = false;
    public void HitCanvas(){
//        SoundPlayer.playHitWall(sqrt(ball.getVX()* ball.getVX() + ball.getVY()* ball.getVY()));

        if (toggle) {
            SoundPlayer.playHitCanvas1(sqrt(ball.getVX()* ball.getVX() + ball.getVY()* ball.getVY()));
        }else{
            SoundPlayer.playHitCanvas2(sqrt(ball.getVX()* ball.getVX() + ball.getVY()* ball.getVY()));
        }
        toggle = !toggle;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public int getCurrentLvl() {
        return currentLvl;
    }

    public void setCurrentLvl(int currentLvl) {
        this.currentLvl = currentLvl;
    }

    public void setShiftDown(boolean shiftDown) {
        this.shiftDown = shiftDown;
        System.out.println(shiftDown);
    }
}

