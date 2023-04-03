package ru.nsu.fit.mihanizzm.game2048.presenter;

import ru.nsu.fit.mihanizzm.game2048.model.FieldListener;
import ru.nsu.fit.mihanizzm.game2048.model.GameField;
import ru.nsu.fit.mihanizzm.game2048.view.GameView;

public class GamePresenter implements Runnable, FieldListener {
    private GameView view;
    private final GameField field;

    public GamePresenter(GameField field) {
        this.field = field;
    }

    public void setView(GameView view) {
        this.view = view;
    }

    public void startNewGame() {
        field.clear();
        view.start(field.getGameField());
    }

    @Override
    public void run() {
        field.setListener(this);
        view.attachPresenter(this);
        startNewGame();
    }

    @Override
    public void update(boolean hasWon, boolean hasNoMoves) {
        view.update(field.getGameField());
        if (hasWon) {
            view.endWin();
        } else if (hasNoMoves) {
            view.endNoMoves();
        }
    }
}
