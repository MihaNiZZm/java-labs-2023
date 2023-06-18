package ru.nsu.fit.mihanizzm.game2048.view;

import ru.nsu.fit.mihanizzm.game2048.presenter.Presenter;

public interface GameView {
    void start(int[][] field);
    void update(int[][] field, int score);
    void endNoMoves(String timeElapsed, int score, boolean needToAddScore);
    void endWon(String timeElapsed);
    void attachPresenter(Presenter presenter);
}
