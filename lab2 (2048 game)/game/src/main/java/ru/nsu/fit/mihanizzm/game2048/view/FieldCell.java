package ru.nsu.fit.mihanizzm.game2048.view;

import javax.swing.*;
import java.awt.*;

public class FieldCell extends JLayeredPane {
    private Color color;
    private String text;
    private JLabel number;
    private RoundedRect box;


    public void updateCell(Color color, String text) {
        if (text.equals("0")) {
            this.number.setText("");
        }
        else {
            this.number.setText(text);
        }

        this.box.setColor(color);

        number.repaint();
        box.repaint();
    }

    public void setColor(Color color) {
        box.setColor(color);
        box.repaint();
    }

    public FieldCell(Color colorOfThePanel, String textOfThePanel, Dimension size) {
        super();
        this.text = textOfThePanel;
        this.color = colorOfThePanel;
        setSize(size);

        number = new JLabel();
        number.setBounds(0, 0, getWidth(), getHeight());
        number.setFont(new Font("Clear Sans Bold", Font.PLAIN, calcFontSize(textOfThePanel)));
        number.setText("");
        number.setVerticalAlignment(SwingConstants.CENTER);
        number.setHorizontalAlignment(SwingConstants.CENTER);
        number.setHorizontalTextPosition(SwingConstants.CENTER);
        number.setVerticalTextPosition(SwingConstants.CENTER);

        box = new RoundedRect(this.color, this.getWidth(), this.getHeight(), getWidth() / 3);

        add(box, 1);
        add(number, 0);
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
