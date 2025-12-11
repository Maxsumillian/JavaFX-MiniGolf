package com.example.golf;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static List<Game> games = new ArrayList<>();
    private static Game activeGame;

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
        if (listener != null) listener.onGameSelected(activeGame);
    }

    // Listener pattern so the menu updates automatically
    public interface GameSelectionListener {
        void onGameSelected(Game game);
    }

    private static GameSelectionListener listener;

    public static void setGameSelectionListener(GameSelectionListener l) {
        listener = l;
    }

}