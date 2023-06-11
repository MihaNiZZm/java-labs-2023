package ru.nsu.fit.mihanizzm.game2048;

import ru.nsu.fit.mihanizzm.game2048.model.*;
import ru.nsu.fit.mihanizzm.game2048.presenter.GamePresenter;
import ru.nsu.fit.mihanizzm.game2048.view.GameFrame;
import ru.nsu.fit.mihanizzm.game2048.view.GameView;

public class GameMain {
    public static void main(String[] args) {
        GameField field = new GameField(4);
        GameView view = new GameFrame(4);
        GamePresenter presenter = new GamePresenter(field, view);
        presenter.run();
    }
}