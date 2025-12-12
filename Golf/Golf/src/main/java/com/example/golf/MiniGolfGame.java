package com.example.golf;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MiniGolfGame extends Application {


    private static boolean darkMode = false;
    private static Scene scene1;
    private static Scene scene2;
    private static Stage editStage = null;
    private static final ModeManager modeManager = new ModeManager();
    private static final ScoreManager scoreManager = new ScoreManager();

    public static Stage getEditStage() {
        return editStage;
    }

    @Override
    public void start(@NotNull Stage mainMenu) throws IOException {

        //====================== Multiple Windows =================/
        // Main Menu
        FXMLLoader MainMenu = new  FXMLLoader(getClass().getResource("/com.example.golf/MainMenu.fxml"));

        Parent MainMenuRoot = MainMenu.load();
        MainMenuController MainMenuController = MainMenu.getController();
        MainMenuController.setModeManager(modeManager);

        scene1 = new Scene(MainMenuRoot, 350, 200);

        scene1.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/light.css")).toExternalForm());
        mainMenu.setTitle(" \uD83D\uDEE0 Main Menu");
        mainMenu.setScene(scene1);
        mainMenu.setResizable(false);
        mainMenu.setX(Screen.getPrimary().getBounds().getMaxX()-700);
        mainMenu.setY(100);
        mainMenu.show();
        mainMenu.setOnCloseRequest(event -> {
            Platform.exit(); // Stops the entire JavaFX application
            System.exit(0); // Optional, ensures JVM exits
        });

        OpenEditor();
        OpenTutorial();

    }

    public static void main(String[] args) {
        makeFolder();
        launch();
    }
    //Creates A Folder for levels if one is not Present
    public static void makeFolder() {
        File folder = new File("levels");
        if (!folder.exists()) folder.mkdirs();
    }

    //opens course levels for score tracking
    public static void OpenLevel(String FilePath, @NotNull String FileName) throws IOException {

        Stage play = new Stage();

        FXMLLoader level = new  FXMLLoader(MiniGolfGame.class.getResource(FilePath));
        Parent root = level.load(); // must call load() on the loader instance
        LevelController controller = level.getController();


        controller.setModeManager(modeManager);
//
        GraphicsContext gc = controller.getCanvas().getGraphicsContext2D();
        Scene scene = new Scene(root, controller.getCanvasWidth(), controller.getCanvasHeight() + 40);
        ArrayList<GameObject> test = controller.getGameObjects();
        Game game = new Game(gc,test);
        game.setModeManager(modeManager);

        //attaches a game reference to the controller
        controller.setGame(game);


        // gets the current hole
        controller.setCourseLevel(FileName.charAt(6)-48);

        //sets the game to have a current level for "course levels"
        game.setCurrentLvl(controller.getCourseLevel());

        game.setScoreManager(scoreManager);

 //==============================

        play.setScene(scene);
        play.setTitle( FileName + " ⛳\uD83C\uDFCC️");
        play.setX(200);
        play.setResizable(false);
        play.show();

        // ================ Mouse Controls to shoot ball
        // because I have a double clock function on my edit window it takes the focus off the play screen this refocus it to see keyboard events
        // sets active focus on this window on clicked
        root.setOnMouseClicked(e -> {
            root.requestFocus();           // ensure key events go to this scene
            GameManager.setActiveGame(game); // your existing logic
        });

        scene.setOnMousePressed(e -> game.startDrag(e.getX(), e.getY()));
        scene.setOnMouseDragged(e -> game.updateDrag(e.getX(), e.getY()));
        scene.setOnMouseReleased(e -> game.endDrag());
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                game.reset();
                e.consume();
            }
            if (e.getCode() == KeyCode.SHIFT) {
                game.setShiftDown(true);
                e.consume();
            }
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SHIFT)
            {
                game.setShiftDown(false);
                e.consume();
            }
        });

        if (darkMode) {
      scene.getStylesheets().add(Objects.requireNonNull(MiniGolfGame.class.getResource("/css/dark.css")).toExternalForm());
        }else{
            scene.getStylesheets().add(Objects.requireNonNull(MiniGolfGame.class.getResource("/css/light.css")).toExternalForm());
        }

        game.start();

        play.focusedProperty().addListener((obs, oldV, newV) -> {
            if (newV) {
                GameManager.setActiveGame(game);
            }
        });

    }

    //opens blank/practice maps useful
    public static void OpenTutorial() throws IOException {

        FXMLLoader level = new  FXMLLoader(MiniGolfGame.class.getResource("/com.example.golf/BaseLvl.fxml"));
        Parent root = level.load(); // must call load() on the loader instance
        LevelController controller = level.getController();

        controller.setModeManager(modeManager);



        GraphicsContext gc = controller.getCanvas().getGraphicsContext2D();
        Scene scene = new Scene(root, controller.getCanvasWidth(), controller.getCanvasHeight() + 40);

        Stage tutorial  = new Stage();
        tutorial.setScene(scene);
        tutorial.setTitle("Mini Golf - Tutorial - Controls");
        tutorial.setX(200);
        tutorial.setResizable(false);
        tutorial.show();


        ArrayList<GameObject> test = new ArrayList<>();
//        ArrayList<GameObject> test = new ArrayList<>(LevelManager.loadLevel("Test"));
        test.add(new BallObject(200,300,Color.WHITE,ObjectType.BALL,10));
        test.add(new CircularObject(600,300,Color.BLACK,ObjectType.HOLE,20));
        Game game = new Game(gc, test);
        game.initialize();

        controller.setGame(game);

        // ================ Mouse Controls to shoot ball
        // because I have a double clock function on my edit window it takes the focus off the play screen this refocus it to see keyboard events
        root.setOnMouseClicked(e -> {
            root.requestFocus();           // ensure key events go to this scene
            GameManager.setActiveGame(game); // your existing logic
        });

        scene.setOnMousePressed(e -> game.startDrag(e.getX(), e.getY()));
        scene.setOnMouseDragged(e -> game.updateDrag(e.getX(), e.getY()));
        scene.setOnMouseReleased(e -> game.endDrag());
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                game.reset();
                e.consume();
            }
            if (e.getCode() == KeyCode.SHIFT) {
                game.setShiftDown(true);
                e.consume();
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SHIFT) {
                game.setShiftDown(false);
                e.consume();
            }
        });



        if (darkMode) {
            scene.getStylesheets().add(Objects.requireNonNull(MiniGolfGame.class.getResource("/css/dark.css")).toExternalForm());
        }else{
            scene.getStylesheets().add(Objects.requireNonNull(MiniGolfGame.class.getResource("/css/light.css")).toExternalForm());
        }
        // Has to be after the canvas is made but before the game starts

        game.setModeManager(modeManager);
        game.start();

        tutorial.focusedProperty().addListener((obs, oldV, newV) -> {
            if (newV) {
                GameManager.setActiveGame(game);
            }
        });

    }

    //opens custom levels no score tracking
    public static void OpenJSON(ArrayList<GameObject> gameObjects , String FileName) throws IOException {


//        ====================================================

        FXMLLoader level = new  FXMLLoader(MiniGolfGame.class.getResource("/com.example.golf/BaseLvl.fxml"));
        Parent root = level.load(); // must call load() on the loader instance
        LevelController controller = level.getController();

        controller.setModeManager(modeManager);



        GraphicsContext gc = controller.getCanvas().getGraphicsContext2D();

        Scene scene = new Scene(root, controller.getCanvasWidth(), controller.getCanvasHeight() + 40);

        Stage blank  = new Stage();
        blank.setScene(scene);
        blank.setTitle("⛳ Custom :: " + FileName + " \uD83C\uDFCC️ ");
        blank.setX(200);
        blank.setResizable(false);
        blank.show();

//        ======================================================
        ArrayList<GameObject> jsonData = new ArrayList<>(LevelManager.loadLevel(FileName));
        Game game = new Game(gc, jsonData);
        game.initialize();

        controller.setGame(game);

        if (darkMode) {
            scene.getStylesheets().add(Objects.requireNonNull(MiniGolfGame.class.getResource("/css/dark.css")).toExternalForm());
        }else{
            scene.getStylesheets().add(Objects.requireNonNull(MiniGolfGame.class.getResource("/css/light.css")).toExternalForm());
        }

        // ================ Mouse Controls to shoot ball

        // because I have a double clock function on my edit window it takes the focus off the play screen this refocus it to see keyboard events
        //I can also use this double click function to help me get the game objects to modify the size of objects
        root.setOnMouseClicked(e -> {
            root.requestFocus();           // ensure key events go to this scene
            GameManager.setActiveGame(game); // your existing logic
        });
        scene.setOnMousePressed(e -> game.startDrag(e.getX(), e.getY()));
        scene.setOnMouseDragged(e -> game.updateDrag(e.getX(), e.getY()));
        scene.setOnMouseReleased(e -> game.endDrag());
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                game.reset();
                e.consume();
            }
            if (e.getCode() == KeyCode.SHIFT) {
                game.setShiftDown(true);
                e.consume();
            }
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.SHIFT) {
                game.setShiftDown(false);
                e.consume();
            }
        });

        game.setModeManager(modeManager);

        game.start();


        blank.focusedProperty().addListener((obs, oldV, newV) -> {
            if (newV) {
                GameManager.setActiveGame(game);
            }
        });

    }

    public static void OpenEditor() throws IOException {

        // Play and Edit Window
        FXMLLoader editScreen = new  FXMLLoader(MiniGolfGame.class.getResource("/com.example.golf/EditScreen.fxml"));

        Parent editScreenRoot = editScreen.load();
        EditScreenController editScreenController = editScreen.getController();
        editScreenController.setModeManager(modeManager);
        editScreenController.setScoreManager(scoreManager);

        editStage = new Stage();
        StackPane root2 = new StackPane(new Label("Second Window"));
        scene2 = new Scene(editScreenRoot, 750, 500);
        scene2.getStylesheets().add(Objects.requireNonNull(MiniGolfGame.class.getResource("/css/light.css")).toExternalForm());

        editStage.setTitle(" ⯈ Play Mode");
        editStage.setScene(scene2);
        editStage.setX(Screen.getPrimary().getBounds().getMaxX() - 900);
        editStage.setY(400);
        editStage.show();
    }

    public static void SwitchTheme() throws IOException {
        darkMode = !darkMode;
        String theme = darkMode ? "/css/dark.css" : "/css/light.css";

        scene1.getStylesheets().clear();
        scene1.getStylesheets().add(Objects.requireNonNull(MiniGolfGame.class.getResource(theme)).toExternalForm());

        scene2.getStylesheets().clear();
        scene2.getStylesheets().add(Objects.requireNonNull(MiniGolfGame.class.getResource(theme)).toExternalForm());
    }
}
