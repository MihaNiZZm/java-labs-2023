package ru.nsu.fit.mihanizzm.game2048.view;

import javax.swing.*;
import java.awt.event.ActionListener;

public class GameMenu extends JMenuBar {
    public JMenu gameMenu;
    public JMenuItem restart;
    public JMenuItem exit;
    public JMenuItem showScores;


    public GameMenu(ActionListener listener) {
        gameMenu = new JMenu("Game");

        restart = new JMenuItem("Restart");
        showScores = new JMenuItem("Show high scores");
        exit = new JMenuItem("Exit");

        gameMenu.add(restart);
        gameMenu.add(showScores);
        gameMenu.add(exit);

        showScores.addActionListener(listener);
        restart.addActionListener(listener);
        exit.addActionListener(listener);

        this.add(gameMenu);
    }
}
