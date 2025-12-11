package com.example.golf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    private static final String LEVELS_FOLDER = "levels/";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void makeFolder() {
        File folder = new File(LEVELS_FOLDER);
        if (!folder.exists()) folder.mkdirs();
    }



    // Save level
    public static void saveLevel(List<GameObject> objects, String fileName) {
        makeFolder();

        //separates my GameObject array into separate classes
        LevelData data = new LevelData();

        for (GameObject obj : objects) {

        //Ball is a child of circle if not seperated, Ball saves twice as circle Object and Ball object
            if (obj instanceof BallObject) {
                ((BallObject) obj).setInitialX(obj.getX());
                ((BallObject) obj).setInitialY(obj.getY());
                data.ballObjects.add((BallObject) obj);
            } else if (obj instanceof CircularObject) {
                data.circularObjects.add((CircularObject) obj);
            }
            if (obj instanceof RectangularObject) {
                data.rectangularObjects.add((RectangularObject) obj);
            }
        }

        try (FileWriter writer = new FileWriter(LEVELS_FOLDER + fileName + ".json")) {
            gson.toJson(data, writer);
            System.out.println("Level saved: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load level
    public static List<GameObject> loadLevel(String fileName) {

        makeFolder();

        List<GameObject> loadedObjects = new ArrayList<>();

        File file = new File(LEVELS_FOLDER + fileName + ".json");
        if (!file.exists()) return loadedObjects;// returns empty if file doesnt exist

        LevelData level;
        try (FileReader reader = new FileReader(file)) {
            level = gson.fromJson(reader, LevelData.class);
//            GameObject[] rawObjects = gson.fromJson(reader, GameObject[].class);

            loadedObjects.addAll(level.ballObjects);
            loadedObjects.addAll(level.circularObjects);
            loadedObjects.addAll(level.rectangularObjects);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return loadedObjects;
    }
}
