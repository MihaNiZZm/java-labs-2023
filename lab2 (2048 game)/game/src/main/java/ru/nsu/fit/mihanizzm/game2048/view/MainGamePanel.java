package ru.nsu.fit.mihanizzm.game2048.view;

import javax.swing.*;
import java.awt.*;

public class MainGamePanel extends JPanel {
    private final String GAME_TITLE = "2048 Pro";
    private final Color TITLE_COLOR = new Color(65, 65, 65);
    private final Color BG_COLOR = new Color(238, 238, 238);
    private final Color SCORE_AND_TIMER_TEXT_COLOR = new Color(85, 85, 85);
    private final Color SCORE_AND_TIMER_PANEL_COLOR = new Color(255, 189, 90);
    private final Dimension DEFAULT_SIZE = new Dimension(768, 966);
    private final FieldPanel field;
    private JLabel gameTitle;
    private final TextInBoxPanel scorePanel;
    private final TextInBoxPanel timerPanel;
    private int axisSize;

    public void updateGame(Integer[][] field, String score, String time) {
        this.field.updateCells(field);
        this.scorePanel.setText("Score: " + score);
        this.timerPanel.setText("Time spent: " + time);
        this.repaint();
    }

    public MainGamePanel(int axisSize) {
        this.axisSize = axisSize;
        this.setBackground(BG_COLOR);

        gameTitle = new JLabel(GAME_TITLE);
        gameTitle.setBounds(0, 0, 768, 91);
        gameTitle.setFont(new Font("Clear Sand Bold", Font.PLAIN, 73));
        gameTitle.setForeground(TITLE_COLOR);
        gameTitle.setVerticalAlignment(SwingConstants.CENTER);
        gameTitle.setHorizontalAlignment(SwingConstants.CENTER);

        scorePanel = new TextInBoxPanel(new Dimension(708, 67));
        scorePanel.setBounds(30, 103, 708, 67);
        scorePanel.setText("Score: 0");
        scorePanel.setFontToText(new Font("Clear Sans Bold", Font.PLAIN, 55));
        scorePanel.setTextColor(SCORE_AND_TIMER_TEXT_COLOR);
        scorePanel.setPanelColor(SCORE_AND_TIMER_PANEL_COLOR);

        field = new FieldPanel(this.axisSize, new Dimension(708, 708));
        field.setBounds(30, 182, 708, 708);

        timerPanel = new TextInBoxPanel(new Dimension(708, 57));
        timerPanel.setBounds(30, 900, 708, 57);
        timerPanel.setText("Time spent: 0:00");
        timerPanel.setFontToText(new Font("Clear Sans Bold", Font.PLAIN, 35));
        timerPanel.setTextColor(SCORE_AND_TIMER_TEXT_COLOR);
        timerPanel.setPanelColor(SCORE_AND_TIMER_PANEL_COLOR);

        this.add(gameTitle);
        this.add(scorePanel);
        this.add(field);
        this.add(timerPanel);
    }
}
