package ru.nsu.fit.mihanizzm.game2048.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameField implements FieldManager {
    private final int[][] gameField;
    private Integer score;
    private FieldListener listener;
    private boolean has2048 = false;

    public int[][] getGameField() {
        return gameField;
    }

    public GameField(int axisSize) {
        this.gameField = new int[axisSize][axisSize];
        generateField(gameField);
    }

    public void clear() {
        clearField(gameField);
        this.score = 0;
    }

    public void setListener(FieldListener listener) {
        this.listener = listener;
    }

    private void merge(int srcRowIndex, int srcColIndex, int dstRowIndex, int dstColIndex) {
        gameField[dstRowIndex][dstColIndex] *= 2;
        gameField[srcRowIndex][srcColIndex] = 0;
        score += gameField[dstRowIndex][dstColIndex];
    }

    private boolean moveValue(int srcRowIndex, int srcColIndex, int dstRowIndex, int dstColIndex) {
        if (srcRowIndex == dstRowIndex && srcColIndex == dstColIndex) {
            return false;
        }
        gameField[dstRowIndex][dstColIndex] = gameField[srcRowIndex][srcColIndex];
        gameField[srcRowIndex][srcColIndex] = 0;
        return true;
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

    private boolean hasNoMoreMoves() {
        for (int rows = 0; rows < gameField.length; ++rows) {
            for (int cols = 0; cols < gameField.length; ++cols) {
                if (checkNeighbors(rows, cols)) {
                    return false;
                }
            }
        }

        return true;
    }

    public void move(Direction dir) {
        boolean had2048 = has2048;
        boolean fieldIsChanged;

        fieldIsChanged = proceedMove(dir);

        if (fieldIsChanged) {
            spawnNewNumber(gameField);
        }

        listener.update(had2048 != has2048, hasNoMoreMoves());
    }

    private boolean proceedMove(Direction dir) {
        boolean fieldIsChanged = false;
        Point firstNumSlot;
        Point currentNumSlot;

        switch (dir) {
            case LEFT -> {
                for (int rowIndex = 0; rowIndex < gameField.length; ++rowIndex) {
                    firstNumSlot = new Point();
                    currentNumSlot = new Point();

                    for (int colIndex = 0; colIndex < gameField.length; ++colIndex) {
                        if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value == 0) {
                            firstNumSlot.setValues(rowIndex, colIndex, gameField[rowIndex][colIndex]);
                            if (moveValue(firstNumSlot.rowIndex, firstNumSlot.colIndex, rowIndex, 0)) {
                                fieldIsChanged = true;
                            }
                            currentNumSlot.setValues(rowIndex, 0, firstNumSlot.value);
                        }
                        else if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value != 0) {
                            if (gameField[rowIndex][colIndex] == currentNumSlot.value && !currentNumSlot.isMerged) {
                                merge(rowIndex, colIndex, currentNumSlot.rowIndex, currentNumSlot.colIndex);
                                currentNumSlot.value = gameField[currentNumSlot.rowIndex][currentNumSlot.colIndex];
                                currentNumSlot.isMerged = true;
                                fieldIsChanged = true;
                                if (!has2048 && currentNumSlot.value == 2048) has2048 = true;
                            }
                            else {
                                if (moveValue(rowIndex, colIndex, currentNumSlot.rowIndex, currentNumSlot.colIndex + 1)) {
                                    fieldIsChanged = true;
                                }
                                currentNumSlot.setValues(currentNumSlot.rowIndex, currentNumSlot.colIndex + 1, gameField[currentNumSlot.rowIndex][currentNumSlot.colIndex + 1]);
                                currentNumSlot.isMerged = false;
                            }

                        }
                    }
                }
                return fieldIsChanged;
            }
            case RIGHT -> {
                for (int rowIndex = 0; rowIndex < gameField.length; ++rowIndex) {
                    firstNumSlot = new Point();
                    currentNumSlot = new Point();

                    for (int colIndex = gameField.length - 1; colIndex >= 0; --colIndex) {
                        if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value == 0) {
                            firstNumSlot.setValues(rowIndex, colIndex, gameField[rowIndex][colIndex]);
                            if (moveValue(firstNumSlot.rowIndex, firstNumSlot.colIndex, rowIndex, gameField.length - 1)) {
                                fieldIsChanged = true;
                            }
                            currentNumSlot.setValues(rowIndex, gameField.length - 1, firstNumSlot.value);
                        } else if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value != 0) {
                            if (gameField[rowIndex][colIndex] == currentNumSlot.value && !currentNumSlot.isMerged) {
                                merge(rowIndex, colIndex, currentNumSlot.rowIndex, currentNumSlot.colIndex);
                                currentNumSlot.value = gameField[currentNumSlot.rowIndex][currentNumSlot.colIndex];
                                currentNumSlot.isMerged = true;
                                fieldIsChanged = true;
                                if (!has2048 && currentNumSlot.value == 2048) has2048 = true;
                            } else {
                                if (moveValue(rowIndex, colIndex, currentNumSlot.rowIndex, currentNumSlot.colIndex - 1)) {
                                    fieldIsChanged = true;
                                }
                                currentNumSlot.setValues(currentNumSlot.rowIndex, currentNumSlot.colIndex - 1, gameField[currentNumSlot.rowIndex][currentNumSlot.colIndex - 1]);
                                currentNumSlot.isMerged = false;
                            }
                        }
                    }
                }
                return fieldIsChanged;
            }
            case UP -> {
                for (int colIndex = 0; colIndex < gameField.length; ++colIndex) {
                    firstNumSlot = new Point();
                    currentNumSlot = new Point();

                    for (int rowIndex = 0; rowIndex < gameField.length; ++rowIndex) {
                        if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value == 0) {
                            firstNumSlot.setValues(rowIndex, colIndex, gameField[rowIndex][colIndex]);
                            if (moveValue(firstNumSlot.rowIndex, firstNumSlot.colIndex, 0, colIndex)) {
                                fieldIsChanged = true;
                            }
                            currentNumSlot.setValues(0, colIndex, firstNumSlot.value);
                        } else if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value != 0) {
                            if (gameField[rowIndex][colIndex] == currentNumSlot.value && !currentNumSlot.isMerged) {
                                merge(rowIndex, colIndex, currentNumSlot.rowIndex, currentNumSlot.colIndex);
                                currentNumSlot.value = gameField[currentNumSlot.rowIndex][currentNumSlot.colIndex];
                                currentNumSlot.isMerged = true;
                                fieldIsChanged = true;
                                if (!has2048 && currentNumSlot.value == 2048) has2048 = true;
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
            case DOWN -> {
                for (int colIndex = 0; colIndex < gameField.length; ++colIndex) {
                    firstNumSlot = new Point();
                    currentNumSlot = new Point();

                    for (int rowIndex = gameField.length - 1; rowIndex >= 0; --rowIndex) {
                        if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value == 0) {
                            firstNumSlot.setValues(rowIndex, colIndex, gameField[rowIndex][colIndex]);
                            if (moveValue(firstNumSlot.rowIndex, firstNumSlot.colIndex, gameField.length - 1, colIndex)) {
                                fieldIsChanged = true;
                            }
                            currentNumSlot.setValues(gameField.length - 1, colIndex, firstNumSlot.value);
                        } else if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value != 0) {
                            if (gameField[rowIndex][colIndex] == currentNumSlot.value && !currentNumSlot.isMerged) {
                                merge(rowIndex, colIndex, currentNumSlot.rowIndex, currentNumSlot.colIndex);
                                currentNumSlot.value = gameField[currentNumSlot.rowIndex][currentNumSlot.colIndex];
                                currentNumSlot.isMerged = true;
                                fieldIsChanged = true;
                                if (!has2048 && currentNumSlot.value == 2048) has2048 = true;
                            } else {
                                if (moveValue(rowIndex, colIndex, currentNumSlot.rowIndex - 1, currentNumSlot.colIndex)) {
                                    fieldIsChanged = true;
                                }
                                currentNumSlot.setValues(currentNumSlot.rowIndex - 1, currentNumSlot.colIndex, gameField[currentNumSlot.rowIndex - 1][currentNumSlot.colIndex]);
                                currentNumSlot.isMerged = false;
                            }
                        }
                    }
                }
                return fieldIsChanged;
            }
        }
        return false;
    }

    private boolean isEmptySlot(int rowIndex, int colIndex) { return gameField[rowIndex][colIndex] == 0; }

    public int getScore() {
        return this.score;
    }

    @Override
    public void generateField(int[][] field) {
        spawnNewNumber(field);
        spawnNewNumber(field);
    }

    @Override
    public void spawnNewNumber(int[][] field) {
        Random pointChooser = new Random();
        Random numberChooser = new Random();

        List<Point> availablePoints = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < field.length; ++rowIndex) {
            for (int colIndex = 0; colIndex < field.length; ++colIndex) {
                if (isEmptySlot(rowIndex, colIndex)) {
                    availablePoints.add(new Point(rowIndex, colIndex));
                }
            }
        }

        int index = pointChooser.nextInt(availablePoints.size());
        Point chosenPoint = availablePoints.get(index);
        if (numberChooser.nextDouble() < 0.9) {
            field[chosenPoint.rowIndex][chosenPoint.colIndex] = 2;
        } else {
            field[chosenPoint.rowIndex][chosenPoint.colIndex] = 4;
        }
    }

    @Override
    public void clearField(int[][] field) {
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field.length; ++j) {
                field[i][j] = 0;
            }
        }
        spawnNewNumber(field);
        spawnNewNumber(field);
    }

    public enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }
}
