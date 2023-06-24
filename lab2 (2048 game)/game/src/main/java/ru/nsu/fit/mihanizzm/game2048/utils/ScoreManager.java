package ru.nsu.fit.mihanizzm.game2048.utils;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class ScoreManager {
    private static ScoreManager instance;
    private final int NUMBER_OF_HIGHSCORES = 10;
    private final String HIGH_SCORES_FILE_PATH = "/high_scores.txt";
    private List<Score> scores;

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }
    private ScoreManager() {
        this.scores = new ArrayList<>();
        loadScores();
    }


    private Score parseFileStringToScore(String string) {
        StringBuilder temp = new StringBuilder();
        int index = 0;
        while (string.charAt(index) != ' ') {
            temp.append(string.charAt(index));
            ++index;
        }

        int score = parseInt(temp.toString());
        temp.setLength(0);

        while (index != string.length()) {
            temp.append(string.charAt(index));
            ++index;
        }

        String name = temp.toString();
        return new Score(name, score);
    }

    private void loadScores() {
        // CR: rewrite with Files.lines() and stream, now it's very complicated
        try (InputStream inputStream = ScoreManager.class.getResourceAsStream(HIGH_SCORES_FILE_PATH)) {
            assert inputStream != null;
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                this.scores.add(parseFileStringToScore(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buildDataToWrite() {
        StringBuilder data = new StringBuilder();
        for (Score score : scores) {
            data.append(score.score()).append(" ").append(score.name()).append("\n");
        }
        return data.toString();
    }

    public void updateScoresFile() {
        // CR: rewrite with nio and stream
        String filePath = null;
        try {
            filePath = Objects.requireNonNull(getClass().getResource(HIGH_SCORES_FILE_PATH)).toURI().getPath();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (filePath != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
                writer.write(buildDataToWrite());
            } catch (IOException e) {
                System.err.println("Failed to update high scores: " + e.getMessage());
            }
        } else {
            System.err.println("High scores file not found.");
        }
    }

    public void addScore(String name, int score) {
        Score newScore = new Score(name != null ? name : "NO_NAME", score);
        this.scores.add(newScore);

        if (this.scores.size() > NUMBER_OF_HIGHSCORES) {
            this.scores = this.scores.stream()
                    .sorted(Comparator.comparingInt(Score::score).reversed())
                    .limit(NUMBER_OF_HIGHSCORES)
                    .collect(Collectors.toList());
        }
    }

    public boolean checkScore(int scoreValue) {
        if (scores.size() < NUMBER_OF_HIGHSCORES) {
            return true;
        }

        for (Score score : scores) {
            if (score.score() < scoreValue) {
                return true;
            }
        }

        return false;
    }

    public List<Score> getScores() {
        return this.scores;
    }
}
