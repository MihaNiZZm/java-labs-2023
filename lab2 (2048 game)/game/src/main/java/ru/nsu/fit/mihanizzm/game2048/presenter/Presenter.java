package ru.nsu.fit.mihanizzm.game2048.presenter;

public interface Presenter {
    void handleKeyPressed(int keyCode);
    void updateScores();
    void registerNewScore(String name, int scoreVal);
    void restartGame();
}
