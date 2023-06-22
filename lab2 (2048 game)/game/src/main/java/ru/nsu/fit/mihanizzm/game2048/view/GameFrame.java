package ru.nsu.fit.mihanizzm.game2048.view;

import ru.nsu.fit.mihanizzm.game2048.presenter.Presenter;


import javax.swing.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;

public class GameFrame extends JFrame implements GameView, KeyListener, ActionListener {
    private final ScoreBoardFrame highScoresFrame;
    private GameMenu menuBar;
    private final ViewPanel gamePanel;
    private Presenter gamePresenter;

    public GameFrame(int axisSize) {
        addKeyListener(this);
        setDefaultFrameParameters();
        setMenuBar();

        this.highScoresFrame = new ScoreBoardFrame();

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
        this.menuBar = new GameMenu(this);
        this.setJMenuBar(this.menuBar);
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
        String[] responses = { "Restart" };
        if (needToAddScore) {
            String name = JOptionPane.showInputDialog("Congratulations! You got a high score! Type your name here:");
            gamePresenter.registerNewScore(name, score);
        }
        String messageString = "You have no more moves! You have played for: " + timeElapsed + " and got " + score + " score points.";
        int input = JOptionPane.showOptionDialog(
                null,
                messageString,
                "GAME OVER!",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                responses,
                0);
        if (input == 0 || input == -1) {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menuBar.restart) {
            gamePresenter.restartGame();
        }
        else if (e.getSource() == menuBar.exit) {
            System.exit(0);
        }
        else if (e.getSource() == menuBar.showScores) {
            highScoresFrame.updateScores();
            this.highScoresFrame.setVisible(true);
        }
    }
}
