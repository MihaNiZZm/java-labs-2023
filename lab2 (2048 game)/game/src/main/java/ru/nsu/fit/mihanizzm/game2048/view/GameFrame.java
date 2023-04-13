package ru.nsu.fit.mihanizzm.game2048.view;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class GameFrame extends JFrame implements GameView {
    private final Color BG_COLOR = new Color(238, 238, 238);
    private final Dimension WINDOW_SIZE = new Dimension(768, 966);
    private KeyListener listener = null;
    private MainGamePanel mainGamePanel;
    private int axisSize;

    public void setListener(KeyListener listener) {
        this.listener = listener;
    }

    public GameFrame(int axisSize) {
        this.axisSize = axisSize;
        setDefaultFrameParameters();
        registerFont();
        setMenuBar();

        mainGamePanel = new MainGamePanel(this.axisSize);
        mainGamePanel.setLayout(null);
        mainGamePanel.setPreferredSize(new Dimension(768, 966));

        add(mainGamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setDefaultFrameParameters() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("2048 Pro");
        ImageIcon image = new ImageIcon("D:\\NSU\\course_2\\object_oriented_programming\\java-labs-2023\\lab2 (2048 game)\\game\\src\\main\\resources\\game_logo.png");
        setIconImage(image.getImage());
        setResizable(false);

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
        catch (IOException | FontFormatException ignored) {

        }
    }

    @Override
    public void start(Integer[][] field) {
        this.mainGamePanel.updateGame(field, "0", "0:00");
        this.repaint();
    }

    @Override
    public void update(Integer[][] field, int score, String time) {
        this.mainGamePanel.updateGame(field, Integer.toString(score), time);
        this.repaint();
    }

    @Override
    public void endNoMoves() {

    }

    @Override
    public void endWon() {

    }
}
