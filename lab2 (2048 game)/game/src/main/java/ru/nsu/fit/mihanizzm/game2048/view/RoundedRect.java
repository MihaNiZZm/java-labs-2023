package ru.nsu.fit.mihanizzm.game2048.view;

import javax.swing.*;
import java.awt.*;

public class RoundedRect extends JLabel {
    private Color color;
    int arcRadius = Math.min(getWidth() / 3, getHeight() / 3);

    public RoundedRect(Color color) {
        this.color = color;
        setSize(100, 100);
    }
    public RoundedRect(Color color, int width, int height) {
        this.color = color;
        setSize(width, height);
    }

    public RoundedRect(Color color, int width, int height, int arcRadius) {
        this(color, width, height);
        this.arcRadius = arcRadius;
    }

    public RoundedRect(Color color, int x, int y, int width, int height, int arcRadius) {
        this(color, x, y, arcRadius);
        setBounds(x, y, width, height);
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), arcRadius, arcRadius);
    }
}
