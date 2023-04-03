package ru.nsu.fit.mihanizzm.game2048.view;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FieldPanel  extends JPanel {
    private final int fieldSize;
    private final Integer[][] field;
    private KeyListener listener = null;

    public FieldPanel(int fieldSize) {
        this.fieldSize = fieldSize;
        this.field = new Integer[fieldSize][fieldSize];
        for (int i = 0; i < fieldSize; ++i) {
            for (int j = 0; j < fieldSize; ++j) {
                field[i][j] = 0;
            }
        }
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }
}
