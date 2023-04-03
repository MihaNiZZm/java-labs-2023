package ru.nsu.fit.mihanizzm.game2048.view;

import ru.nsu.fit.mihanizzm.game2048.presenter.GamePresenter;

public interface GameView {
    void start(Integer[][] field);
    void update(Integer[][] field);
    void attachPresenter(GamePresenter presenter);
    void endWin();
    void endNoMoves();
}
