package ru.nsu.fit.mihanizzm.game2048.model;

public interface FieldManager {
    void generateField(int[][] field, int[][] reference);
    void spawnNewNumber(int[][] field);
    void clearField(int[][] field);
}

