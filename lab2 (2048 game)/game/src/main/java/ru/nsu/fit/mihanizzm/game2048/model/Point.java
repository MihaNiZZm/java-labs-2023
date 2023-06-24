package ru.nsu.fit.mihanizzm.game2048.model;

class Point {
    public int rowIndex;
    public int colIndex;
    public int value;
    public boolean isMerged;

    public Point(int row, int col, int val, boolean merged) {
        this.rowIndex = row;
        this.colIndex = col;
        this.value = val;
        this.isMerged = merged;
    }

    public Point(int row, int col) {
        this(row, col, 0, false);
    }

    public Point() {
        this(0, 0);
    }

    public void setValues(int row, int col, int val) {
        this.rowIndex = row;
        this.colIndex = col;
        this.value = val;
    }
}
