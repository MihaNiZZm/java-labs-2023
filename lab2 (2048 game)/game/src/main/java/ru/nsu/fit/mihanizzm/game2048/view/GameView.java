package ru.nsu.fit.mihanizzm.game2048.view;

import ru.nsu.fit.mihanizzm.game2048.presenter.GamePresenter;

import java.awt.event.KeyListener;

public interface GameView {
    void start(Integer[][] field);
    void update(Integer[][] field, int score);
    void endNoMoves();
    void endWon();
    void attachPresenter(GamePresenter presenter);
}
