package com.example.golf;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

import java.io.IOException;

import static com.example.golf.SoundPlayer.amp;

public class MainMenuController implements GameManager.GameSelectionListener{


    @FXML
    public Button PlayEdit;
    public Button Save;
    public TextField NameField;
    public Button Quit;
    public Button Practice;
    public Label currentScore;
    public Slider soundControls;
    private ModeManager modeManager;

    public MainMenuController() throws IOException {
    }

    public void setModeManager(ModeManager mm) {
        this.modeManager = mm;
        updateText();
    }

    @FXML
    private void quitGame() {
        javafx.application.Platform.exit(); // closes all windows and stops the app
    }

    //this button switches between play mode and edit mode
    @FXML
    private void playEdit() throws IOException {
        if (modeManager.getMode() == ModeManager.Mode.PLAY) {
            modeManager.setMode(ModeManager.Mode.EDIT);
        } else {
            modeManager.setMode(ModeManager.Mode.PLAY);
        }

        MiniGolfGame.SwitchTheme();

        if(!MiniGolfGame.getEditStage().isShowing()){
            MiniGolfGame.getEditStage().show();
        } else if (MiniGolfGame.getEditStage() == null){
            MiniGolfGame.OpenEditor();
        } else {
            // Window is already open, optionally bring it to front
            MiniGolfGame.getEditStage().toFront();
        }

        updateText();
    }

    private void updateText() {
        if (modeManager.getMode() == ModeManager.Mode.PLAY) {
            Practice.setText("Practice");
        } else {
            Practice.setText("BlankMap");
        }
    }

    @FXML
    private void openPractice() throws IOException {
        MiniGolfGame.OpenTutorial();
    }


    public void setCurrentScore(int score) {
        currentScore.setText(String.valueOf(score));
    }


/// ChatGPT assistance, use for my listening and active score counter
/// Because I had multiple windows I needed a way to communicate witch one was teh focus window
/// I have a Game manager that displays

    @FXML
    public void initialize() {
        GameManager.setGameSelectionListener(this);

        soundControls.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSound(newValue);
        });

        // If a game is already active when the menu opens:
        if (GameManager.getActiveGame() != null) {
            updateForGame(GameManager.getActiveGame());
        }
    }

    @Override
    public void onGameSelected(Game game) {
        updateForGame(game);
    }

    private void updateForGame(Game game) {
        if (game.getStrokeCount() < 999999999){
            currentScore.setText(String.valueOf(game.getStrokeCount()));
        System.out.println(game);
        }
        else{
            currentScore.setText("Edit Mode");
        }
    }

    private void updateSound(Number newValue){
            amp = newValue.doubleValue();
            SoundPlayer.playHitWall((Double) newValue);

    }
}

