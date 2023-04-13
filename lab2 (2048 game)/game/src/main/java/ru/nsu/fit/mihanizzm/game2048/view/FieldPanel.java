package ru.nsu.fit.mihanizzm.game2048.view;

import javax.swing.*;
import java.awt.*;

public class FieldPanel extends JPanel {
    private int gapSize;
    private FieldCell[][] cells;
    private int axisSize;

    public void updateCells(Integer[][] numbers) {
        for (int i = 0; i < axisSize; ++i) {
            for (int j = 0; j < axisSize; ++j) {
                cells[i][j].updateCell(getColor(numbers[i][j]), numbers[i][j].toString());
                cells[i][j].repaint();
            }
        }
    }

    public FieldPanel(int axisSize, Dimension size) {
        super();
        this.setPreferredSize(size);
        this.axisSize = axisSize;
        this.gapSize = 60 / axisSize;
        this.setLayout(new GridLayout(axisSize, axisSize, gapSize, gapSize));

        cells = new FieldCell[axisSize][axisSize];
        for (int i = 0; i < axisSize; ++i) {
            for (int j = 0; j < axisSize; ++j) {
                cells[i][j] = new FieldCell(getColor(0), "0", getOneBoxSize(gapSize, this.getWidth(), axisSize));
            }
        }
    }

    private Dimension getOneBoxSize(int gapSize, int fieldSize, int axisSize) {
        int size = (fieldSize - gapSize * (axisSize - 1)) / axisSize;
        return new Dimension(size, size);
    }

    private Color getColor(int number) {
        return switch(number) {
            case 0 -> new Color(168, 168, 168);
            case 2 -> new Color(255, 209, 120);
            case 4 -> new Color(253, 171, 96);
            case 8 -> new Color(252, 136, 102);
            case 16 -> new Color(253, 109, 96);
            case 32 -> new Color(253, 95, 119);
            case 64 -> new Color(253, 96, 161);
            case 128 -> new Color(253, 96, 241);
            case 256 -> new Color(182, 95, 254);
            case 512 -> new Color(141, 96, 254);
            case 1024 -> new Color(116, 95, 254);
            case 2048 -> new Color(94, 117, 254);
            case 4096 -> new Color(95, 164, 254);
            case 8192 -> new Color(95, 217, 254);
            case 16384 -> new Color(94, 240, 253);
            case 32768 -> new Color(93, 252, 228);
            case 65536 -> new Color(92, 251, 166);
            case 131072 -> new Color(91, 250, 117);
            case 262144 -> new Color(130, 249, 90);
            case 524288 -> new Color(172, 248, 90);
            case 1048576 -> new Color(213, 247, 90);
            case 2097152 -> new Color(246, 238, 90);
            default -> new Color(254, 204, 95);
        };
    }
}
