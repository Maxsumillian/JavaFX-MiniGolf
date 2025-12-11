package com.example.golf;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


//this level controller serves as a connection between the fxml file and the shapes and things in the file to retrieve location data
public class LevelController {


    private int CourseLevel = -1;

    public VBox Vbox;
    public TextField NameField;
    public Button Save;
    @FXML
    private Canvas levelCanvas;

    private Game game;

    private ModeManager modeManager;

    // these two specific position are needed because my game only has one ball and one hole. so I will not be creating more instances of balls and or holes,
    // although I can pass the ball and hole to the game class and set it there, and it will only take the newer placement?
    // only thing this does is I would have to make my hole a class of its own but i may end up doing that beacuse i can make the sand-pit either its parnet or child of it
//    double[][] StartingPositions = { {100, 100}, {100, 100} };// first one is ball start position, second one is hole position I only need this because My hole and ball


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

    public Scene getScene() {
        return root.getScene(); // safe once stage is shown
    }

    private void updateBehavior() {

        if (levelCanvas.getScene() != null) {
            if (modeManager.getMode() == ModeManager.Mode.EDIT) {
                NameField.setPromptText("Enter Level Name");
                Save.setText(" \uD83D\uDCBE Save Level");

                levelCanvas.getScene().getStylesheets().clear();
                levelCanvas.getScene().getStylesheets().add(MiniGolfGame.class.getResource("/css/dark.css").toExternalForm());

                // if you change to edit mode it stops tracking your score
                CourseLevel = -1;

                Save.setDisable(false);
                NameField.setDisable(false);

            } else {

                Save.setDisable(true);
                NameField.setDisable(true);

                NameField.setPromptText("Enter Player Name \"Currently In Development\"");
                Save.setText(" \uD83D\uDCBE Save Score");

                levelCanvas.getScene().getStylesheets().clear();
                levelCanvas.getScene().getStylesheets().add(MiniGolfGame.class.getResource("/css/light.css").toExternalForm());
            }
        }

    }

    public void Save() {

        if (!NameField.getText().equals("")) {
            if (levelCanvas.getScene() != null) {
                LevelManager.saveLevel(game.getGameObjects(), NameField.getText());
            }
        } else {
            System.out.println("No Level Name Entered");
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }


    public Canvas getCanvas() {
        return levelCanvas;
    }

    public double getCanvasHeight() {
        return levelCanvas.getHeight();
    }

    public double getCanvasWidth() {
        return levelCanvas.getWidth();
    }


    @FXML
    private Pane root;

    private ArrayList<GameObject> gameObjects = new ArrayList<>();


    @FXML
    public void initialize() {
        loadObjects(root);
    }



    //what this does is it finds all children in the parent and compares it to other classes if the classes match then
    // it will take properties from that shape and pass them to the game logic so it can draw and check for collision
    private void loadObjects(Parent parent) {
        for (Node n : parent.getChildrenUnmodifiable()) {

            Color color = Color.WHITE;
            ObjectType objectType = ObjectType.WALL;

            if(n instanceof Shape){
                if(((Shape) n).getFill().equals(Color.SANDYBROWN)){
                    color = Color.SANDYBROWN;
                    objectType = ObjectType.TRAP;
                }
                if(((Shape) n).getFill().equals(Color.BLUE)){
                    color = Color.BLUE;
                    objectType = ObjectType.RESET;
                }
            }

            // Skip the Canvas
            //Chatgpt taught me how to use instance of here
            if (n instanceof Canvas) continue;

            // uses a checkbox to set the location for start
            //this is kinda a hack to get a basic crude level editor, so I can make my levels quickly with drag and drop using the scene builder
            // as for the user level editor... they will have to deal with numbers...
            if (n instanceof CheckBox a) {
                gameObjects.add(new BallObject(
                        a.getLayoutX(),
                        a.getLayoutY(),
                        Color.WHITE, // this is a default color need to change later
                        ObjectType.BALL, // this is default Type need to change later
                        10
                ));
                a.setVisible(false);
            }

            // uses a ProgressIndicator to set the location for hole
            if (n instanceof ProgressIndicator b) {
                gameObjects.add(new CircularObject(
                        b.getLayoutX(),
                        b.getLayoutY(),
                        Color.BLACK, // this is a default color need to change later
                        ObjectType.HOLE, // this is defaul Type need to change later
                        20
                ));
                b.setVisible(false);
            }

            // rectangles for walls (only rectangular walls no angles)
            if (n instanceof Rectangle r) {
                gameObjects.add(new RectangularObject(
                                r.getLayoutX(),
                                r.getLayoutY(),
                                color, // this is a default color need to change later
                                objectType, // this is defaults Type need to change later
                                r.getWidth(),
                                r.getHeight()
                ));
                r.setVisible(false);
                // You can add checks here for circles, rectangles, etc.
            }
            if (n instanceof Circle c) {
                gameObjects.add(new CircularObject(
                        c.getLayoutX(),
                        c.getLayoutY(),
                        color, // this is a default color need to change later
                        objectType, // this is default Type need to change later
                        c.getRadius()
                ));
                c.setVisible(false);
            }
        }
    }

    public ArrayList<GameObject> getGameObjects() {

//        sorted for drawing objects
//        FLOOR(0),
//        TRAP(1),
//        WALL(2),
//        RESET(3),
//        HOLE(4),
//        BALL(5);
        gameObjects.sort(Comparator.comparingInt(o -> o.getObjectType().Priority));

        return gameObjects;
    }

    public int getCourseLevel() {
        return CourseLevel;
    }

    public void setCourseLevel(int courseLevel) {
        CourseLevel = courseLevel;
    }
}