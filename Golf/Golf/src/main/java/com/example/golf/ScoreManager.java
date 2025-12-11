package com.example.golf;


//CHAT GPT How to bind Observables from my controller class to my manager class
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
//these imports can be more data types as well

public class ScoreManager {

    private IntegerProperty[] levelScore = new IntegerProperty[10];

    public ScoreManager() {
        for (int i = 0; i < 10; i++) {
            levelScore[i] = new SimpleIntegerProperty(999999999);
        }
    }

    // Return an observable string
    public ObservableValue<String> getLevelScoreString(int level) {
//        if (levelScore[level].get() != 999999999) {
//            return new SimpleStringProperty("N/a");
//        }
        return levelScore[level].asString();
    }

    // Get/set
    public int getLevelScore(int level) {
        return levelScore[level].get();
    }

    public void setLevelScore(int level, int score) {
        levelScore[level].set(score);
        levelScore[0].set(  levelScore[1].get() +
                            levelScore[2].get() +
                            levelScore[3].get() +
                            levelScore[4].get() +
                            levelScore[5].get() +
                            levelScore[6].get() +
                            levelScore[7].get() +
                            levelScore[8].get() +
                            levelScore[9].get()
        );
    }

}
