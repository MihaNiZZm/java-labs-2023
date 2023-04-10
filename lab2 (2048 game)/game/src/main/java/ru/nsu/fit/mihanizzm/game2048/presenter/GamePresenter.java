package ru.nsu.fit.mihanizzm.game2048.presenter;

import ru.nsu.fit.mihanizzm.game2048.model.FieldListener;
import ru.nsu.fit.mihanizzm.game2048.model.GameField;
import ru.nsu.fit.mihanizzm.game2048.view.GameView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePresenter implements KeyListener, FieldListener, Runnable, ActionListener {
    GameView view;
    GameField field;

    public GamePresenter(GameField field, GameView view) {
        this.field = field;
        this.view = view;
    }

    public void setView(GameView view) {
        this.view = view;
    }

    public void setField(GameField field) {
        this.field = field;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> field.move(GameField.Direction.UP);
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> field.move(GameField.Direction.DOWN);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> field.move(GameField.Direction.RIGHT);
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> field.move(GameField.Direction.LEFT);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void update(boolean hasWon, boolean hasNoMoves) {
        view.update(field.getGameField(), field.getScore(), field.getTime());
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
        view.setListener(this);
        newGame();
    }

    public void newGame() {
        field.clear();
        field.initTimer(this);
        view.start(field.getGameField());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.update(field.getGameField(), field.getScore(), field.getTime());
    }
}
