package com.example.golf;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static List<Game> games = new ArrayList<>();
    private static Game activeGame;

    // Replace single listener with a list
    private static List<GameSelectionListener> listeners = new ArrayList<>();

    public static void addGame(Game g) {
        games.add(g);
    }

    public static List<Game> getGames() {
        return games;
    }

    public static Game getActiveGame() {
        return activeGame;
    }

    public static void setActiveGame(Game g) {
        activeGame = g;
        // Notify all listeners
        for (GameSelectionListener l : listeners) {
            l.onGameSelected(activeGame);
        }
    }

    public static void addGameSelectionListener(GameSelectionListener l) {
        listeners.add(l);
        // Optional: immediately notify the new listener of current active game
        if (activeGame != null) {
            l.onGameSelected(activeGame);
        }
    }

    // Remove a listener if needed
    public static void removeGameSelectionListener(GameSelectionListener l) {
        listeners.remove(l);
    }

    public interface GameSelectionListener {
        void onGameSelected(Game game);
    }
}