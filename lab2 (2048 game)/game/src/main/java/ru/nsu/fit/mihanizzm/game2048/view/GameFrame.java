package ru.nsu.fit.mihanizzm.game2048.view;

import ru.nsu.fit.mihanizzm.game2048.presenter.Presenter;


import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.imageio.ImageIO;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

public class GameFrame extends JFrame implements GameView, KeyListener {
    private final ViewPanel gamePanel;
    private Presenter gamePresenter;

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
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gamePresenter.updateScores();
            }
        });

        try {
            URL imageURL = GameFrame.class.getResource("/game_logo.png");
            if (imageURL != null) {
                ImageIcon image = new ImageIcon(ImageIO.read(imageURL));
                setIconImage(image.getImage());
            }
        } catch (IOException e) {
            System.err.println("Failed to load image: " + e.getMessage());
        }

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
    public void endNoMoves(String timeElapsed, int score, boolean needToAddScore) {
        String[] responses = { "Restart [R]", "Cancel" };
        if (needToAddScore) {
            String name = JOptionPane.showInputDialog("Congratulations! You got a high score! Type your name here:");
            gamePresenter.registerNewScore(name, score);
        }
        String messageString = "You have no more moves! You have played for: " + timeElapsed + " and got " + score + " score points.";
        if (JOptionPane.showOptionDialog(
                null,
                messageString,
                "GAME OVER!",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                responses,
                0) == 0) {
            gamePresenter.restartGame();
        }
    }

    @Override
    public void endWon(String timeElapsed) {
        String[] responses = { "Continue", "New Game [R]" };
        String messageString = "You won! It took you " + timeElapsed + " time to reach \"2048\" tile.";

        if (JOptionPane.showOptionDialog(
                null,
                messageString,
                "CONGRATULATIONS!",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                responses,
                0) == 1) {
            gamePresenter.restartGame();
        }
    }

    @Override
    public void attachPresenter(Presenter presenter) {
        gamePresenter = presenter;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        gamePresenter.handleKeyPressed(keyCode);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // not used
    }
}
