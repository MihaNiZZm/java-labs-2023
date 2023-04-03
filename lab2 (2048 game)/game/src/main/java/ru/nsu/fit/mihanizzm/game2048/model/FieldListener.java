package ru.nsu.fit.mihanizzm.game2048.model;

public interface FieldListener {
    void update(boolean hasWon, boolean hasNoMoves);
}
