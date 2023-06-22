package ru.nsu.fit.mihanizzm.game2048.view;

import ru.nsu.fit.mihanizzm.game2048.utils.Score;
import ru.nsu.fit.mihanizzm.game2048.utils.ScoreManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class ScoreBoardFrame extends JFrame {
    private final DefaultTableModel tableModel;

    public ScoreBoardFrame() {
        setTitle("High Scores");
        setSize(200, 300);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Player");
        tableModel.addColumn("Score");

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);

        setContentPane(panel);
    }

    public void updateScores() {
        tableModel.setRowCount(0);

        List<Score> scores = ScoreManager.getInstance().getScores();
        scores.sort(Score::compareTo);
        Collections.reverse(scores);

        for (Score score : scores) {
            tableModel.addRow(new Object[]{score.name(), score.score()});
        }
    }
}

