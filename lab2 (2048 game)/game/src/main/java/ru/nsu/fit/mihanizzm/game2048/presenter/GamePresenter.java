package ru.nsu.fit.mihanizzm.game2048.presenter;

import ru.nsu.fit.mihanizzm.game2048.model.FieldListener;
import ru.nsu.fit.mihanizzm.game2048.model.GameField;
import ru.nsu.fit.mihanizzm.game2048.utils.ScoreManager;
import ru.nsu.fit.mihanizzm.game2048.view.GameView;

import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

public class GamePresenter implements FieldListener, Runnable, Presenter {
    private final GameView view;
    private final GameField field;
    private long startTime, endTime;

    public void handleKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A, KeyEvent.VK_L -> field.move(GameField.Direction.LEFT);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D, KeyEvent.VK_QUOTE -> field.move(GameField.Direction.RIGHT);
            case KeyEvent.VK_DOWN, KeyEvent.VK_S, KeyEvent.VK_SEMICOLON -> field.move(GameField.Direction.DOWN);
            case KeyEvent.VK_UP, KeyEvent.VK_W, KeyEvent.VK_P -> field.move(GameField.Direction.UP);
            case KeyEvent.VK_R -> newGame();
        }
    }

    @Override
    public void updateScores() {
        ScoreManager manager = ScoreManager.getInstance();
        manager.updateScoresFile();
    }

    @Override
    public void registerNewScore(String name, int scoreVal) {
        ScoreManager manager = ScoreManager.getInstance();
        manager.addScore(name, scoreVal);
    }

    @Override
    public void restartGame() {
        newGame();
    }

    public GamePresenter(GameField field, GameView view) {
        this.field = field;
        this.view = view;
        view.attachPresenter(this);
    }

    private String getFormattedElapsedTime() {
        long timeElapsed = endTime - startTime;
        long hours = TimeUnit.NANOSECONDS.toHours(timeElapsed);
        long minutes = TimeUnit.NANOSECONDS.toMinutes(timeElapsed) % 60;
        long seconds = TimeUnit.NANOSECONDS.toSeconds(timeElapsed) % 60;
        long milliseconds = TimeUnit.NANOSECONDS.toMillis(timeElapsed) % 1000;

        StringBuilder stringBuilder = new StringBuilder();
        if (hours != 0) {
            stringBuilder.append(hours).append(":");
        }
        if (hours != 0 || minutes != 0) {
            stringBuilder.append(minutes != 0 ? minutes : "00").append(":");
        }
        if (hours != 0 || minutes != 0 || seconds != 0) {
            stringBuilder.append(seconds != 0 ? seconds : "00").append(".");
        }
        if (hours != 0 || minutes != 0 || seconds != 0 || milliseconds != 0) {
            stringBuilder.append(milliseconds != 0 ? milliseconds : "000");
        }

        return stringBuilder.toString();
    }

    @Override
    public void update(boolean hasWon, boolean hasNoMoves) {
        view.update(field.getGameField(), field.getScore());
        if (hasWon) {
            endTime = System.nanoTime();
            view.endWon(getFormattedElapsedTime());
        }
        if (hasNoMoves) {
            endTime = System.nanoTime();

            ScoreManager manager = ScoreManager.getInstance();
            int score = field.getScore();
            boolean needToAddScore = manager.checkScore(score);

            view.endNoMoves(getFormattedElapsedTime(), score, needToAddScore);
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
        startTime = System.nanoTime();
    }
}
