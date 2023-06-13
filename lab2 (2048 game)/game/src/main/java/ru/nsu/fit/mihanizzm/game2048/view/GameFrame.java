package ru.nsu.fit.mihanizzm.game2048.view;

import ru.nsu.fit.mihanizzm.game2048.presenter.GamePresenter;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameFrame extends JFrame implements GameView, KeyListener {
    private final ViewPanel gamePanel;
    private GamePresenter gamePresenter;

    public GameFrame(int axisSize) {
        setDefaultFrameParameters();
        setMenuBar();
        addKeyListener(this);

        gamePanel = new ViewPanel(axisSize);
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setDefaultFrameParameters() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("2048 Pro");
        // CR: GameFrame.class.getResourceAsStream()
        ImageIcon image = new ImageIcon("D:/NSU/course_2/object_oriented_programming/java-labs-2023/lab2 (2048 game)/game/src/main/resources/game_logo.png");
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

    @Override
    public void start(int[][] field) {
        gamePanel.setBoard(field);
        gamePanel.setScore(0);
        this.repaint();
    }

    @Override
    public void update(int[][] field, int score) {
        gamePanel.setBoard(field);
        gamePanel.setScore(score);
        this.repaint();
    }

    @Override
    public void endNoMoves() {

    }

    @Override
    public void endWon() {

    }

    @Override
    public void attachPresenter(GamePresenter presenter) {
        gamePresenter = presenter;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        gamePresenter.handleKeyPressed(keyCode);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
