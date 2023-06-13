package ru.nsu.fit.mihanizzm.game2048.presenter;

import ru.nsu.fit.mihanizzm.game2048.model.FieldListener;
import ru.nsu.fit.mihanizzm.game2048.model.GameField;
import ru.nsu.fit.mihanizzm.game2048.view.GameView;

import java.awt.event.KeyEvent;

public class GamePresenter implements FieldListener, Runnable {
    private final GameView view;
    private final GameField field;

    // CR: pass as part of interface to view
    public void handleKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A, KeyEvent.VK_L -> field.move(GameField.Direction.LEFT);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D, KeyEvent.VK_QUOTE -> field.move(GameField.Direction.RIGHT);
            case KeyEvent.VK_DOWN, KeyEvent.VK_S, KeyEvent.VK_SEMICOLON -> field.move(GameField.Direction.DOWN);
            case KeyEvent.VK_UP, KeyEvent.VK_W, KeyEvent.VK_P -> field.move(GameField.Direction.UP);
            case KeyEvent.VK_R -> newGame();
        }
    }

    public GamePresenter(GameField field, GameView view) {
        this.field = field;
        this.view = view;
        view.attachPresenter(this);
    }

    @Override
    public void update(boolean hasWon, boolean hasNoMoves) {
        view.update(field.getGameField(), field.getScore());
        if (hasWon) {
            view.endWon();
        }
        if (hasNoMoves) {
            // CR: add high scores
            view.endNoMoves();
        }
    }

//    package utils;
//
//    static class ScoreManager {
//
//        List<Score> scores = new ArrayList<>();
//
//        private ScoreManager() {
//            this.scores = loadScores();
//        }
//
//        record Score(String name, int score) {}
//
//        public boolean addHighScore(String name, int score) {
//
//        }
//
//        private void loadScores() {
//
//        }
//
//        public void updateScores() {
//
//        }
//
//
//    }

    @Override
    public void run() {
        field.setListener(this);
        view.attachPresenter(this);
        newGame();
    }

    public void newGame() {
        field.clear();
        view.start(field.getGameField());
    }
}
