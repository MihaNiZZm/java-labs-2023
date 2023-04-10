package ru.nsu.fit.mihanizzm.game2048.view;

import java.awt.event.KeyListener;

public interface GameView {
    void start(Integer[][] field);
    void update(Integer[][] field, int score, String time);
    void endNoMoves();
    void endWon();
    void setListener(KeyListener listener);
}
