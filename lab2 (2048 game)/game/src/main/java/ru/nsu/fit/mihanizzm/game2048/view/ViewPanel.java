package ru.nsu.fit.mihanizzm.game2048.view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ViewPanel extends JPanel {

    private static final int TILE_SIZE = 165;  // Размер плиточки
    private static final int FIELD_STARTING_X_COORDINATE = 30;
    private static final int FIELD_STARTING_Y_COORDINATE = 182;
    private static final int DEFAULT_ARC_RADIUS = 55;
    private static final int BOARD_SIZE_FACTOR = 4;
    private static final int FIELD_PIXEL_SIZE = 708;

    private final int boardSize;
    private final int gapSize;
    private final int boxSize;

    private int[][] board;
    private int score;

    public ViewPanel(int boardSize) {
        this.boardSize = boardSize;
        this.gapSize = 60 / boardSize;
        this.boxSize = getOneBoxSize(gapSize, boardSize);
        setPreferredSize(new Dimension(768, 966));
        setBackground(new Color(238, 238, 238));

        board = new int[boardSize][boardSize];

        score = 0;
    }

    private void setTestField() {
        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j) {
                board[i][j] = (int) Math.pow(2, i) * (int) Math.pow(2, j) * 2;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Отрисовка игрового поля
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                int value = board[row][col];
                drawTile(g, value, FIELD_STARTING_X_COORDINATE + col * (boxSize + gapSize), FIELD_STARTING_Y_COORDINATE + row * (boxSize + gapSize));
            }
        }

        // Отрисовка плашки с количеством очков
        drawScorePanel(g);

        // Отрисовка названия игры
        drawTitle(g);
    }

    private void drawTile(Graphics g, int value, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int arcRadius = boxSize / 3;

        // Задание цвета и отрисовка плиточки
        Color tileColor = getColorForValue(value);
        g2d.setColor(tileColor);
        g2d.fill(new RoundRectangle2D.Double(x, y, boxSize, boxSize, arcRadius, arcRadius));

        // Отрисовка текста внутри плиточки
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, calcFontSize(Integer.toString(value))));
        String text = value > 0 ? String.valueOf(value) : "";
        int textX = x + boxSize / 2 - g2d.getFontMetrics().stringWidth(text) / 2;
        int textY = y + boxSize / 2 + g2d.getFontMetrics().getAscent() / 2 - (10 / Integer.toString(value).length() * BOARD_SIZE_FACTOR / boardSize);
        g2d.drawString(text, textX, textY);
    }

    private void drawScorePanel(Graphics g) {
        // Определение координат и размеров плашки с закругленными краями
        int arcRadius = DEFAULT_ARC_RADIUS;
        int panelWidth = 708;
        int panelHeight = 67;
        int panelX = 30;
        int panelY = 103;

        // Задание цвета и отрисовка плашки
        Color panelColor = new Color(255, 189, 90);  // Цвет плашки
        g.setColor(panelColor);
        g.fillRoundRect(panelX, panelY, panelWidth, panelHeight, arcRadius, arcRadius);

        // Отрисовка текста с количеством очков
        g.setColor(new Color(85, 85, 85));
        g.setFont(new Font("Arial", Font.BOLD, 52));
        String scoreText = "Score: " + score;
        int textX = panelX + panelWidth / 2 - g.getFontMetrics().stringWidth(scoreText) / 2;
        int textY = panelY + panelHeight / 2 + g.getFontMetrics().getAscent() / 2 - 5;
        g.drawString(scoreText, textX, textY);
    }

    private void drawTitle(Graphics g) {
        // Отрисовка названия игры
        g.setColor(new Color(85, 85, 85));
        g.setFont(new Font("Arial", Font.BOLD, 88));
        String titleText = "2048 Pro";
        g.drawString(titleText, 195, 86);
    }

    private Color getColorForValue(int value) {
        // Цвета плиточек в зависимости от значения
        return switch (value) {
            case 0 -> new Color(168, 168, 168);
            case 2 -> new Color(255, 209, 120);
            case 4 -> new Color(253, 171, 96);
            case 8 -> new Color(252, 136, 102);
            case 16 -> new Color(253, 109, 96);
            case 32 -> new Color(253, 95, 119);
            case 64 -> new Color(253, 96, 161);
            case 128 -> new Color(253, 96, 241);
            case 256 -> new Color(182, 95, 254);
            case 512 -> new Color(141, 96, 254);
            case 1024 -> new Color(116, 95, 254);
            case 2048 -> new Color(94, 117, 254);
            case 4096 -> new Color(95, 164, 254);
            case 8192 -> new Color(95, 217, 254);
            case 16384 -> new Color(94, 240, 253);
            case 32768 -> new Color(93, 252, 228);
            case 65536 -> new Color(92, 251, 166);
            case 131072 -> new Color(91, 250, 117);
            case 262144 -> new Color(130, 249, 90);
            case 524288 -> new Color(172, 248, 90);
            case 1048576 -> new Color(213, 247, 90);
            case 2097152 -> new Color(246, 238, 90);
            default -> new Color(254, 204, 95);
        };
    }

    private int calcFontSize(String text) {
        return switch (text.length()) {
            case 1 -> (int) ( TILE_SIZE * 0.73 );
            case 2 -> (int) ( TILE_SIZE * 0.6 );
            case 3 -> (int) ( TILE_SIZE * 0.46 );
            case 4 -> (int) ( TILE_SIZE * 0.34 );
            case 5 -> (int) ( TILE_SIZE * 0.3 );
            default -> (int) ( TILE_SIZE * 0.24 );
        };
    }

    private int getOneBoxSize(int gapSize, int boardSize) {
        return (ViewPanel.FIELD_PIXEL_SIZE - gapSize * (boardSize - 1)) / boardSize;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

