package com.example.golf;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class EditScreenController  implements GameManager.GameSelectionListener{

    @FXML
    public Button button1;
    public Button button2;
    public Button button3;
    public Button button4;
    public Button button5;
    public Button button6;
    public Button button7;
    public Button button8;
    public Button button9;
    public Button refresh;
    public Button delete;

    public Label score1;
    public Label score2;
    public Label score3;
    public Label score4;
    public Label score5;
    public Label score6;
    public Label score7;
    public Label score8;
    public Label score9;
    public Label scoreTotal;

    private ScoreManager scoreManager;


    private Game currentSelectedGame;

    @Override
    public void onGameSelected(Game game) {
        updateForGame(game);
    }

    private void updateForGame(Game game) {
//        System.out.println("EDIT  " + game);
//            currentScore.setText("Edit Mode");
        currentSelectedGame = game;//sets the selected game to this game reference, so I can modify objects from that game in the edit menu

    }



    //gets set the same score manager that then gets its keys binded to the scores based on the level
    public void setScoreManager(ScoreManager scoreManager){
        this.scoreManager = scoreManager;

        score1.textProperty().bind(scoreManager.getLevelScoreString(1));
        score2.textProperty().bind(scoreManager.getLevelScoreString(2));
        score3.textProperty().bind(scoreManager.getLevelScoreString(3));
        score4.textProperty().bind(scoreManager.getLevelScoreString(4));
        score5.textProperty().bind(scoreManager.getLevelScoreString(5));
        score6.textProperty().bind(scoreManager.getLevelScoreString(6));
        score7.textProperty().bind(scoreManager.getLevelScoreString(7));
        score8.textProperty().bind(scoreManager.getLevelScoreString(8));
        score9.textProperty().bind(scoreManager.getLevelScoreString(9));


        scoreTotal.textProperty().bind(scoreManager.getLevelScoreString(0));

    }


    // i need a way to access the (Focus)game class to add and objects to its game array

    @FXML
    private ListView<String> listView;
    private ObservableList<String> observableList;

    private ModeManager modeManager;

    List<String> fileNames = new ArrayList<>();

    public void listFiles() {
        fileNames.clear();// NOTE: if i don't clear here next time I run it only adds more and creates dupes

//ChatGPT CODE FOR GRABBING FILE NAMES
        Path dirPath = Paths.get("levels");
        try (Stream<Path> stream = Files.list(dirPath)) {
            stream.filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString().replaceFirst("\\.json$", "")) // Filters out subdirectories
                    .forEach(fileNames::add); // Process each file path
        } catch (IOException e) {
            System.err.println("An I/O error occurred: " + e.getMessage());
        }
    }


    //====================== List viewer ====================
    @FXML
    public void initialize() {

        //i need this to initialize the game selector listiner
        GameManager.setGameSelectionListener(this);

//        updateScoreTotal();

        // Create observableList and attach to ListView
        observableList = FXCollections.observableArrayList();
        listView.setItems(observableList);

        listFiles();

        // Example: Add some items
//        observableList.add("Apple");
//        observableList.add("Banana");
//        observableList.add("Cherry");

        for (String fileName : fileNames) {
            //.replaceFirst("\\.json$", "") // removes the json from the file
            observableList.add(fileName.replaceFirst("\\.json$", ""));
        }

        listView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // double-click
                String selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    System.out.println("Double-clicked: " + selected);

                    // do whatever you want here
                    try {
                        openLevel(selected);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }

//    Method to dynamically add an item
    public void addItem(String newItem) {
        observableList.add(newItem);
    }

    @FXML
    private void openLevel(String selected) throws IOException {
        MiniGolfGame.OpenJSON((ArrayList<GameObject>) LevelManager.loadLevel(selected),selected);
    }

    @FXML
    private void updateList() {
        listFiles();
        observableList.setAll(fileNames);
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

            MiniGolfGame.getEditStage().setTitle(" ✎ Edit Mode");

            button1.setText("add Wall⬜");
            button2.setText("add Wall⚪");
            button3.setText("add Sand⬜");
            button4.setText("add Sand⚪");
            button5.setText("add Water⬜");
            button6.setText("add Water⚪");
            button7.setText("Delete Object");
            button8.setDisable(true);
            button9.setDisable(true);

//            button2.setDisable(false);
        } else {

            //NOTE: Since the default is to start in Play mode, without this check it returns null and crashes
            if (MiniGolfGame.getEditStage() != null)  {
                MiniGolfGame.getEditStage().setTitle(" ⯈ Play Mode");
            }
            button1.setText("Hole #1");
            button2.setText("Hole #2");
            button3.setText("Hole #3");
            button4.setText("Hole #4");
            button5.setText("Hole #5");
            button6.setText("Hole #6");
            button7.setText("Hole #7");
            button8.setDisable(false);
            button9.setDisable(false);
        }
    }
//============================= Button Controls ================================
    @FXML
    private void Button1Clicked() throws IOException {
        if (modeManager.getMode() == ModeManager.Mode.EDIT) {
            if(currentSelectedGame != null){
                currentSelectedGame.getGameObjects().add(new RectangularObject(0,0, Color.WHITE,ObjectType.WALL,100,100));
            }
        }else{
            OpenLevel("/com.example.golf/lvl1.fxml",button1.getText());
        }
    }

    @FXML
    private void Button2Clicked() throws IOException {
        if (modeManager.getMode() == ModeManager.Mode.EDIT) {
            if(currentSelectedGame != null){
                currentSelectedGame.getGameObjects().add(new CircularObject(0,0, Color.WHITE,ObjectType.WALL,100));
            }
        }else {
            OpenLevel("/com.example.golf/lvl2.fxml", button2.getText());
        }
    }

    @FXML
    private void Button3Clicked() throws IOException {
        if (modeManager.getMode() == ModeManager.Mode.EDIT) {
            if(currentSelectedGame != null){
                currentSelectedGame.getGameObjects().add(new RectangularObject(0,0, Color.SANDYBROWN,ObjectType.TRAP,100,100));
            }
        }else {
            OpenLevel("/com.example.golf/lvl3.fxml", button3.getText());
        }
    }

    @FXML
    private void Button4Clicked() throws IOException {
        if (modeManager.getMode() == ModeManager.Mode.EDIT) {
            if(currentSelectedGame != null){
                currentSelectedGame.getGameObjects().add(new CircularObject(0,0, Color.SANDYBROWN,ObjectType.TRAP,100));
            }
        }else {
            OpenLevel("/com.example.golf/lvl4.fxml", button4.getText());
        }
    }

    @FXML
    private void Button5Clicked() throws IOException {
            if (modeManager.getMode() == ModeManager.Mode.EDIT) {
                if(currentSelectedGame != null){
                    currentSelectedGame.getGameObjects().add(new RectangularObject(0,0, Color.BLUE,ObjectType.RESET,100,100));
                }
            }else {
                OpenLevel("/com.example.golf/lvl5.fxml", button5.getText());
            }
    }

    @FXML
    private void Button6Clicked() throws IOException {
                if (modeManager.getMode() == ModeManager.Mode.EDIT) {
                    if(currentSelectedGame != null){
                        currentSelectedGame.getGameObjects().add(new CircularObject(0,0, Color.BLUE,ObjectType.RESET,100));
                    }
                }else {
                    OpenLevel("/com.example.golf/lvl6.fxml", button6.getText());
                }
    }

    @FXML
    private void Button7Clicked() throws IOException {
        if (modeManager.getMode() == ModeManager.Mode.EDIT) {
            if(currentSelectedGame != null){
                if(currentSelectedGame.lastSelected  != null){
                    currentSelectedGame.getGameObjects().remove(currentSelectedGame.lastSelected );
                }
            }
        }else {
            OpenLevel("/com.example.golf/lvl7.fxml", button7.getText());
        }
    }

    @FXML
    private void Button8Clicked() throws IOException {
        OpenLevel("/com.example.golf/lvl8.fxml",button8.getText());
    }

    @FXML
    private void Button9Clicked() throws IOException {
        OpenLevel("/com.example.golf/lvl9.fxml",button9.getText());
    }

    @FXML
    private void OpenLevel(String FilePath, String LevelName) throws IOException {
        MiniGolfGame.OpenLevel(FilePath,LevelName);
    }

    @FXML
    public void deleteSelected() throws IOException {
        Path file = Paths.get("levels/" + listView.getSelectionModel().getSelectedItem() + ".json");
        Files.deleteIfExists(file);
        updateList();
    }
}
