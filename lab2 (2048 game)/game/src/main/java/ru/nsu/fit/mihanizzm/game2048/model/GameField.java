package ru.nsu.fit.mihanizzm.game2048.model;

public class GameField {

    private final FieldManager fieldManager;
    private final int[][] gameField;
    private int score = 0;
    private FieldListener listener;
    private boolean has2048 = false;

    public int[][] getGameField() {
        return gameField;
    }
    public boolean getHas2048() { return has2048; }

    public GameField(int axisSize, FieldManager fieldManager) {
        this.fieldManager = fieldManager;
        this.gameField = new int[axisSize][axisSize];
        fieldManager.generateField(gameField, null);
    }

    public void generateField(int[][] reference) {
        fieldManager.generateField(this.getGameField(), reference);
    }

    public void clear() {
        fieldManager.clearField(gameField);
        this.score = 0;
    }

    public void setListener(FieldListener listener) {
        this.listener = listener;
    }

    private void merge(int srcRowIndex, int srcColIndex, int dstRowIndex, int dstColIndex) {
        if (gameField[dstRowIndex][dstColIndex] != Integer.MAX_VALUE / 2 + 1) {
            gameField[dstRowIndex][dstColIndex] *= 2;
        }
        else {
            score = Integer.MAX_VALUE;
            gameField[srcRowIndex][srcColIndex] = 0;
            return;
        }
        gameField[srcRowIndex][srcColIndex] = 0;

        long checkedScore = score + (long) gameField[dstRowIndex][dstColIndex];
        if (checkedScore > Integer.MAX_VALUE) {
            score = Integer.MAX_VALUE;
        }
        else {
            score = (int) checkedScore;
        }
    }

    private boolean moveValue(int srcRowIndex, int srcColIndex, int dstRowIndex, int dstColIndex) {
        if (srcRowIndex == dstRowIndex && srcColIndex == dstColIndex) {
            return false;
        }
        gameField[dstRowIndex][dstColIndex] = gameField[srcRowIndex][srcColIndex];
        gameField[srcRowIndex][srcColIndex] = 0;
        return true;
    }

    private void rotateField90DegreesClockWise() {
        int[][] reference = new int[this.gameField.length][this.gameField.length];
        for (int i = 0; i < gameField.length; ++i) {
            System.arraycopy(gameField[i], 0, reference[i], 0, gameField.length);
        }

        for (int i = 0; i < gameField.length; ++i) {
            for (int j = 0; j < gameField.length; ++j) {
                gameField[j][gameField.length - 1 - i] = reference[i][j];
            }
        }

    }

    private void doRotation(Direction dir) {
        switch (dir) {
            case UP -> {}
            case LEFT -> rotateField90DegreesClockWise();
            case DOWN -> {
                rotateField90DegreesClockWise();
                rotateField90DegreesClockWise();
            }
            case RIGHT -> {
                rotateField90DegreesClockWise();
                rotateField90DegreesClockWise();
                rotateField90DegreesClockWise();
            }
        }
    }

    private void undoRotation(Direction dir) {
        switch (dir) {
            case UP -> {}
            case LEFT -> {
                rotateField90DegreesClockWise();
                rotateField90DegreesClockWise();
                rotateField90DegreesClockWise();
            }
            case DOWN -> {
                rotateField90DegreesClockWise();
                rotateField90DegreesClockWise();
            }
            case RIGHT -> rotateField90DegreesClockWise();
        }
    }

    private boolean checkNeighbors(int x, int y) {
       int currentValue = gameField[x][y];

       // High
       if (x != 0) {
           int valueToCheck = gameField[x - 1][y];
           if (valueToCheck == 0 || valueToCheck == currentValue) {
                return true;
           }
       }

       // Low
       if (x != gameField.length - 1) {
           int valueToCheck = gameField[x + 1][y];
           if (valueToCheck == 0 || valueToCheck == currentValue) {
                return true;
           }
       }

        // Right
        if (y != gameField.length - 1) {
            int valueToCheck = gameField[x][y + 1];
            if (valueToCheck == 0 || valueToCheck == currentValue) {
                return true;
            }
        }

        // Left
        if (y != 0) {
            int valueToCheck = gameField[x][y - 1];
            if (valueToCheck == 0 || valueToCheck == currentValue) {
                return true;
            }
        }

       return false;
    }

    public boolean hasNoMoreMoves() {
        for (int rows = 0; rows < gameField.length; ++rows) {
            for (int cols = 0; cols < gameField.length; ++cols) {
                if (checkNeighbors(rows, cols)) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean move(Direction dir) {
        boolean had2048 = has2048;
        boolean fieldIsChanged;

        doRotation(dir);
        fieldIsChanged = pushTiles();
        undoRotation(dir);

        if (fieldIsChanged) {
            fieldManager.spawnNewNumber(gameField);
        }

        if (listener != null) {
            listener.update(had2048 != has2048, hasNoMoreMoves());
        }
        return fieldIsChanged;
    }

    private boolean pushTiles() {
        boolean fieldIsChanged = false;
        Point firstNumSlot;
        Point currentNumSlot;

        for (int colIndex = 0; colIndex < gameField.length; ++colIndex) {
            firstNumSlot = new Point();
            currentNumSlot = new Point();

            for (int rowIndex = 0; rowIndex < gameField.length; ++rowIndex) {
                if (isNotEmptySlot(rowIndex, colIndex) && firstNumSlot.value == 0) {
                    firstNumSlot.setValues(rowIndex, colIndex, gameField[rowIndex][colIndex]);
                    if (moveValue(firstNumSlot.rowIndex, firstNumSlot.colIndex, 0, colIndex)) {
                        fieldIsChanged = true;
                    }
                    currentNumSlot.setValues(0, colIndex, firstNumSlot.value);
                } else if (isNotEmptySlot(rowIndex, colIndex) && firstNumSlot.value != 0) {
                    if (gameField[rowIndex][colIndex] == currentNumSlot.value && !currentNumSlot.isMerged) {
                        merge(rowIndex, colIndex, currentNumSlot.rowIndex, currentNumSlot.colIndex);
                        currentNumSlot.value = gameField[currentNumSlot.rowIndex][currentNumSlot.colIndex];
                        currentNumSlot.isMerged = true;
                        fieldIsChanged = true;
                        if (!has2048 && currentNumSlot.value == 2048) {
                            has2048 = true;
                        }
                    } else {
                        if (moveValue(rowIndex, colIndex, currentNumSlot.rowIndex + 1, currentNumSlot.colIndex)) {
                            fieldIsChanged = true;
                        }
                        currentNumSlot.setValues(currentNumSlot.rowIndex + 1, currentNumSlot.colIndex, gameField[currentNumSlot.rowIndex + 1][currentNumSlot.colIndex]);
                        currentNumSlot.isMerged = false;
                    }
                }
            }
        }
        return fieldIsChanged;
    }

    private boolean isNotEmptySlot(int rowIndex, int colIndex) { return gameField[rowIndex][colIndex] != 0; }

    public int getScore() {
        return this.score;
    }

    public enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}
