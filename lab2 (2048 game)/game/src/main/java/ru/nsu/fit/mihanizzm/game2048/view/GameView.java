package ru.nsu.fit.mihanizzm.game2048.view;

import ru.nsu.fit.mihanizzm.game2048.presenter.GamePresenter;

public interface GameView {
    void start(int[][] field);
    void update(int[][] field, int score);
    void endNoMoves();
    void endWon();
    void attachPresenter(GamePresenter presenter);
}
