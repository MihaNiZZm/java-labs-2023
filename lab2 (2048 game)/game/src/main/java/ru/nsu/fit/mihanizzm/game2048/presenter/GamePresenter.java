package ru.nsu.fit.mihanizzm.game2048.presenter;

import ru.nsu.fit.mihanizzm.game2048.model.FieldListener;
import ru.nsu.fit.mihanizzm.game2048.model.GameField;
import ru.nsu.fit.mihanizzm.game2048.view.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePresenter implements FieldListener, Runnable {
    GameView view;
    GameField field;

    public void handleKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> field.move(GameField.Direction.LEFT);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> field.move(GameField.Direction.RIGHT);
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> field.move(GameField.Direction.DOWN);
            case KeyEvent.VK_UP, KeyEvent.VK_W -> field.move(GameField.Direction.UP);
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
            view.endNoMoves();
        }
    }

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
