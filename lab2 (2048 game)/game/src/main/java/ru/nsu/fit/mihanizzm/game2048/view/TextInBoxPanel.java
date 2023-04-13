package ru.nsu.fit.mihanizzm.game2048.view;

import javax.swing.*;
import java.awt.*;

public class TextInBoxPanel extends JLayeredPane {
    private RoundedRect panel;
    private JLabel textLabel;

    public void setText(String text) {
        textLabel.setText(text);
        textLabel.repaint();
    }

    public void setPanelColor(Color color) {
        panel.setColor(color);
        panel.repaint();
    }

    public void setTextColor(Color color) {
        textLabel.setForeground(color);
        textLabel.repaint();
    }

    public void setFontToText(Font font) {
        textLabel.setFont(font);
        textLabel.repaint();
    }

    TextInBoxPanel(Dimension size) {
        super();
        this.setPreferredSize(size);
        textLabel = new JLabel();
        panel = new RoundedRect(Color.black, this.getWidth(), this.getHeight());

        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setVerticalAlignment(SwingConstants.CENTER);
        panel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.setVerticalAlignment(SwingConstants.CENTER);

        this.add(panel, 1);
        this.add(textLabel, 0);
    }
}
