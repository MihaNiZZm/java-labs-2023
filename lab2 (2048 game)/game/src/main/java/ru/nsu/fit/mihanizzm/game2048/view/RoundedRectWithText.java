package ru.nsu.fit.mihanizzm.game2048.view;

import javax.swing.*;
import java.awt.*;

public class RoundedRectWithText extends JLayeredPane {

    private Color color;
    private String text;

    public void setText(String text) {
        this.text = text;
        this.repaint();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public RoundedRectWithText(Color colorOfThePanel, String textOfThePanel, Dimension size) {
        super();
        this.text = textOfThePanel;
        this.color = colorOfThePanel;
        setSize(size);

        JLabel textLabel = new JLabel();
        textLabel.setBounds(0, 0, getWidth(), getHeight());
        textLabel.setFont(new Font("Clear Sans Bold", Font.PLAIN, calcFontSize(textOfThePanel)));
        textLabel.setForeground(Color.white);
        if (!this.text.equals("0")) {
            textLabel.setText(this.text);
        } else {
            textLabel.setText("");
        }
        textLabel.setVerticalAlignment(SwingConstants.CENTER);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        textLabel.setVerticalTextPosition(SwingConstants.CENTER);

        RoundedRect box = new RoundedRect(this.color, this.getWidth(), this.getHeight(), getWidth() / 3);

        add(box, 1);
        add(textLabel, 0);
    }

    private int calcFontSize(String text) {
        return switch (text.length()) {
            case 1 -> (int) ( Math.min(getWidth(), getHeight()) * 0.73 );
            case 2 -> (int) ( Math.min(getWidth(), getHeight()) * 0.6 );
            case 3 -> (int) ( Math.min(getWidth(), getHeight()) * 0.46 );
            case 4 -> (int) ( Math.min(getWidth(), getHeight()) * 0.34 );
            case 5 -> (int) ( Math.min(getWidth(), getHeight()) * 0.3 );
            default -> (int) ( Math.min(getWidth(), getHeight()) * 0.24 );
        };
    }
}
