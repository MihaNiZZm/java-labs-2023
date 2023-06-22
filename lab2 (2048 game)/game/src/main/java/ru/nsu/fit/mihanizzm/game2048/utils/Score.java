package ru.nsu.fit.mihanizzm.game2048.utils;

public record Score(String name, int score) implements Comparable<Score> {
    @Override
    public int compareTo(Score other) {
        return Integer.compare(this.score, other.score);
    }
}
