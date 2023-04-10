package ru.nsu.fit.mihanizzm.game2048.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class GameFrame extends JFrame implements GameView {
    private KeyListener listener = null;
    private JPanel mainGamePanel;
    private JPanel nameSection;
    private JLayeredPane scoreSection;
    private JPanel fieldSection;
    private JLayeredPane timerSection;
    private JLabel scoreText;
    private JLabel timerText;
    private RoundedRectWithText[][] fieldMatrix;
    private Integer[][] fieldNumbers;
    private String time;
    private String score;
    private int axisSize;

    public void setField(Integer[][] fieldNumbers) {
        this.fieldNumbers = fieldNumbers;
    }

    public void setScore(int score) {
        this.score = Integer.toString(score);
    }

    public void setTime(String time) {
        this.time = time;
        this.repaint();
    }

    public void setListener(KeyListener listener) {
        this.listener = listener;
    }

    public GameFrame(int axisSize) {
        this.score = "0";
        this.time = "0:00";
        this.fieldNumbers = null;
        this.axisSize = axisSize;
        setDefaultFrameParameters();

        registerFont();
        setMenuBar();

        mainGamePanel = new JPanel();
        mainGamePanel.setLayout(null);
        mainGamePanel.setPreferredSize(new Dimension(768, 966));
        mainGamePanel.setBackground(new Color(238, 238, 238));

        initNameSection();
        initScoreSection();
        initFieldSection();
        initTimerSection();

        mainGamePanel.add(fieldSection);
        mainGamePanel.add(nameSection);
        mainGamePanel.add(scoreSection);
        mainGamePanel.add(timerSection);

        add(mainGamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initNameSection() {
        nameSection = new JPanel();
        nameSection.setBounds(0, 0, 768, 91);
        JLabel gameName = new JLabel();
        gameName.setFont(new Font("Clear Sans Bold", Font.PLAIN, 73));
        gameName.setHorizontalAlignment(SwingConstants.CENTER);
        gameName.setVerticalAlignment(SwingConstants.CENTER);
        gameName.setVerticalTextPosition(SwingConstants.CENTER);
        gameName.setHorizontalTextPosition(SwingConstants.CENTER);
        gameName.setText("2048 Pro");
        gameName.setForeground(new Color(65, 65, 65));
        nameSection.add(gameName);
    }

    private void initScoreSection() {
        scoreSection = new JLayeredPane();
        scoreSection.setOpaque(false);
        scoreSection.setBounds(0, 91, 768, 91);
        scoreSection.setLayout(null);
        RoundedRect scorePlane = new RoundedRect(new Color(255, 189, 90), 30, 12,708, 67, 32);
        scorePlane.setHorizontalAlignment(SwingConstants.CENTER);
        scorePlane.setVerticalAlignment(SwingConstants.CENTER);
        scoreText = new JLabel();
        scoreText.setBounds(0, -3, scoreSection.getWidth(), scoreSection.getHeight());
        scoreText.setText("Score: " + score);
        scoreText.setHorizontalAlignment(SwingConstants.CENTER);
        scoreText.setVerticalAlignment(SwingConstants.CENTER);
        scoreText.setFont(new Font("Clear Sans Bold", Font.BOLD, 55));
        scoreText.setForeground(new Color(85, 85, 85));
        scoreSection.add(scorePlane, 1);
        scoreSection.add(scoreText, 0);
    }

    private void initFieldSection() {
        fieldSection = new JPanel();
        fieldSection.setBounds(0, 182, 768, 708);
        fieldSection.setLayout(null);
        JLabel fieldGrid = new JLabel();
        fieldGrid.setHorizontalAlignment(SwingConstants.CENTER);
        fieldGrid.setVerticalAlignment(SwingConstants.CENTER);
        fieldGrid.setBounds(30, 0, 708, 708);
        fieldGrid.setLayout(new GridLayout(axisSize, axisSize, 60 / axisSize, 60 / axisSize));
        fieldMatrix = new RoundedRectWithText[axisSize][axisSize];
        if (fieldNumbers == null) {
            for (int i = 0; i < axisSize; ++i) {
                for (int j = 0; j < axisSize; ++j) {
                    fieldMatrix[i][j] = new RoundedRectWithText(getColor(0), "0", getOneBoxSize(60 / axisSize, fieldGrid.getWidth(), this.axisSize));
                    fieldGrid.add(fieldMatrix[i][j]);
                }
            }
        } else {
            for (int i = 0; i < axisSize; ++i) {
                for (int j = 0; j < axisSize; ++j) {
                    fieldMatrix[i][j] = new RoundedRectWithText(getColor(fieldNumbers[i][j]), fieldNumbers[i][j].toString(), getOneBoxSize(60 / axisSize, fieldGrid.getWidth(), this.axisSize));
                    fieldGrid.add(fieldMatrix[i][j]);
                }
            }
        }
        fieldSection.add(fieldGrid);
    }

    private void initTimerSection() {
        timerSection = new JLayeredPane();
        timerSection.setBounds(0, 91 + 91 + 708, 768, 76);
        timerSection.setLayout(null);
        RoundedRect timerPanel = new RoundedRect(new Color(255, 189, 90), 30, 10,708, 57, 32);
        timerPanel.setHorizontalAlignment(SwingConstants.CENTER);
        timerPanel.setVerticalAlignment(SwingConstants.CENTER);
        timerText = new JLabel();
        timerText.setBounds(0, -1, timerSection.getWidth(), timerSection.getHeight());
        timerText.setText("Time spent " + time);
        timerText.setHorizontalAlignment(SwingConstants.CENTER);
        timerText.setVerticalAlignment(SwingConstants.CENTER);
        timerText.setFont(new Font("Clear Sans Bold", Font.PLAIN, 35));
        timerText.setForeground(new Color(85, 85, 85));
        timerSection.add(timerPanel, 1);
        timerSection.add(timerText, 0);
    }

    private void setDefaultFrameParameters() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("2048 Pro");
        ImageIcon image = new ImageIcon("D:\\NSU\\course_2\\object_oriented_programming\\java-labs-2023\\lab2 (2048 game)\\game\\src\\main\\resources\\game_logo.png");
        setIconImage(image.getImage());
        setResizable(false);
    }

    private Dimension getOneBoxSize(int gapSize, int fieldSize, int axisSize) {
        int size = (fieldSize - gapSize * (axisSize - 1)) / axisSize;
        return new Dimension(size, size);
    }

    private Color getColor(int number) {
        return switch(number) {
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

    private void setMenuBar() {
        JMenuBar newMenu = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");

        JMenuItem restart = new JMenuItem("Restart");
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem exit = new JMenuItem("Exit");

        gameMenu.add(restart);
        gameMenu.add(undo);
        gameMenu.add(exit);

        JMenu settingsMenu = new JMenu("Settings");
        JMenu highScoresMenu = new JMenu("Scores");
        JMenu aboutMenu = new JMenu("About");
        JMenu helpMenu = new JMenu("Help");

        newMenu.add(gameMenu);
        newMenu.add(settingsMenu);
        newMenu.add(highScoresMenu);
        newMenu.add(aboutMenu);
        newMenu.add(helpMenu);

        this.setJMenuBar(newMenu);
    }

    private void registerFont() {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("D:\\NSU\\course_2\\object_oriented_programming\\java-labs-2023\\lab2 (2048 game)\\game\\src\\main\\resources\\fonts\\ClearSans\\ClearSans-Bold.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("D:\\NSU\\course_2\\object_oriented_programming\\java-labs-2023\\lab2 (2048 game)\\game\\src\\main\\resources\\fonts\\ClearSans\\ClearSans-Bold.ttf")));
        }
        catch (IOException | FontFormatException error) {

        }
    }

    private void setFieldToTest() {
        if (fieldNumbers == null) {
            fieldNumbers = new Integer[axisSize][axisSize];
        }
        for (int i = 0; i < axisSize; ++i) {
            for (int j = 0; j < axisSize; ++j) {
                fieldNumbers[i][j] = (int) (2 * Math.pow(2, i * fieldNumbers.length + j));
            }
        }
    }

    @Override
    public void start(Integer[][] field) {
        this.setField(field);
        setFieldValues(field);
        repaint();
        mainGamePanel.repaint();
        fieldSection.repaint();
        for (int i = 0; i < axisSize; ++i) {
            for (int j = 0; j < axisSize; ++j) {
                fieldMatrix[i][j].repaint();
            }
        }

    }

    @Override
    public void update(Integer[][] field, int score, String time) {
        this.setField(field);
        this.setFieldValues(field);
        this.setScore(score);
        this.setTime(time);
        this.repaint();
    }

    @Override
    public void endNoMoves() {

    }

    @Override
    public void endWon() {

    }

    private void setFieldValues(Integer[][] values) {
        for (int i = 0; i < axisSize; ++i) {
            for (int j = 0; j < axisSize; ++j) {
                fieldMatrix[i][j].setColor(getColor(values[i][j]));
                fieldMatrix[i][j].setText(values[i][j].toString());
            }
        }
    }
}
