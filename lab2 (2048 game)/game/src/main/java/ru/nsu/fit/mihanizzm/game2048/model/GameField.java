package ru.nsu.fit.mihanizzm.game2048.model;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameField {
    private final Integer[][] gameField;
    private final Integer axisSize;
    private Integer score;
    private FieldListener listener;
    private boolean has2048 = false;

    public Integer[][] getGameField() {
        return gameField;
    }


    public GameField(int axisSize) {
        this.axisSize = axisSize;
        this.gameField = new Integer[axisSize][axisSize];
        for (int i = 0; i < gameField.length; ++i) {
            for (int j = 0; j < gameField.length; ++j) {
                gameField[i][j] = 0;
            }
        }
        this.score = 0;
        spawnNewNumber();
        spawnNewNumber();
    }

    public void clear() {
        for (int i = 0; i < gameField.length; ++i) {
            for (int j = 0; j < gameField.length; ++j) {
                gameField[i][j] = 0;
            }
        }
        this.score = 0;
        spawnNewNumber();
        spawnNewNumber();
    }

    public void setListener(FieldListener listener) {
        this.listener = listener;
    }

    public boolean hasEnded() {
        return hasNoMoreMoves();
    }

    public void printField() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < gameField.length; ++i) {
            for (int j = 0; j < gameField.length; ++j) {
                str.append(gameField[i][j]).append("\t");
            }
            str.append("\n");
        }
        System.out.println(str);
    }

    private void spawnNewNumber() {
        Random pointChooser = new Random();
        Random numberChooser = new Random();

        List<Point> availablePoints = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < gameField.length; ++rowIndex) {
            for (int colIndex = 0; colIndex < gameField.length; ++colIndex) {
                if (isEmptySlot(rowIndex, colIndex)) {
                    availablePoints.add(new Point(rowIndex, colIndex));
                }
            }
        }

        int index = pointChooser.nextInt(availablePoints.size());
        Point chosenPoint = availablePoints.get(index);
        if (numberChooser.nextDouble() < 0.9) {
            gameField[chosenPoint.rowIndex][chosenPoint.colIndex] = 2;
        } else {
            gameField[chosenPoint.rowIndex][chosenPoint.colIndex] = 4;
        }
    }

    private void merge(int srcRowIndex, int srcColIndex, int dstRowIndex, int dstColIndex) {
        gameField[dstRowIndex][dstColIndex] *= 2;
        gameField[srcRowIndex][srcColIndex] = 0;
        score += gameField[dstRowIndex][dstColIndex];
    }

    private void moveValue(int srcRowIndex, int srcColIndex, int dstRowIndex, int dstColIndex) {
        if (srcRowIndex == dstRowIndex && srcColIndex == dstColIndex) {
            return;
        }
        gameField[dstRowIndex][dstColIndex] = gameField[srcRowIndex][srcColIndex];
        gameField[srcRowIndex][srcColIndex] = 0;
    }

    private boolean hasNoMoreMoves() {
        return cantMoveInDirection(Direction.UP) &&
               cantMoveInDirection(Direction.DOWN) &&
               cantMoveInDirection(Direction.LEFT) &&
               cantMoveInDirection(Direction.RIGHT);
    }

    private boolean reached2048() {
        int maxFieldNumber = 0;
        for (int i = 0; i < axisSize; ++i) {
            for (int j = 0; j < axisSize; ++j) {
                if (maxFieldNumber < gameField[i][j]) {
                    maxFieldNumber = gameField[i][j];
                }
            }
        }
        if (!has2048 && maxFieldNumber == 2048) {
            has2048 = true;

        }
        return false;
    }

    public void move(Direction dir) {
        boolean result;
        result = switch (dir) {
            case LEFT -> moveLeft();
            case RIGHT -> moveRight();
            case UP -> moveUp();
            case DOWN -> moveDown();
        };
        if (result) {
            spawnNewNumber();
        }
        listener.update(reached2048(), hasNoMoreMoves());
    }

    private boolean moveLeft() {
        if (cantMoveInDirection(Direction.LEFT)) {
            return false;
        }

        for (int rowIndex = 0; rowIndex < gameField.length; ++rowIndex) {
            Point firstNumSlot = new Point();
            Point numSlot = new Point();

            for (int colIndex = 0; colIndex < gameField.length; ++colIndex) {
                if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value == 0) {
                    firstNumSlot.setValues(rowIndex, colIndex, gameField[rowIndex][colIndex]);
                    moveValue(firstNumSlot.rowIndex, firstNumSlot.colIndex, rowIndex, 0);
                    numSlot.setValues(rowIndex, 0, firstNumSlot.value);
                } else if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value != 0) {
                    if (gameField[rowIndex][colIndex] == numSlot.value && !numSlot.isMerged) {
                        merge(rowIndex, colIndex, numSlot.rowIndex, numSlot.colIndex);
                        numSlot.value = gameField[numSlot.rowIndex][numSlot.colIndex];
                        numSlot.isMerged = true;
                    } else {
                        moveValue(rowIndex, colIndex, numSlot.rowIndex, numSlot.colIndex + 1);
                        numSlot.setValues(numSlot.rowIndex, numSlot.colIndex + 1, gameField[numSlot.rowIndex][numSlot.colIndex + 1]);
                        numSlot.isMerged = false;
                    }
                }
            }
        }
        return true;
    }

    private boolean moveRight() {
        if (cantMoveInDirection(Direction.RIGHT)) {
            return false;
        }

        for (int rowIndex = 0; rowIndex < gameField.length; ++rowIndex) {
            Point firstNumSlot = new Point();
            Point numSlot = new Point();

            for (int colIndex = gameField.length - 1; colIndex >= 0; --colIndex) {
                if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value == 0) {
                    firstNumSlot.setValues(rowIndex, colIndex, gameField[rowIndex][colIndex]);
                    moveValue(firstNumSlot.rowIndex, firstNumSlot.colIndex, rowIndex, gameField.length - 1);
                    numSlot.setValues(rowIndex, gameField.length - 1, firstNumSlot.value);
                } else if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value != 0) {
                    if (gameField[rowIndex][colIndex] == numSlot.value && !numSlot.isMerged) {
                        merge(rowIndex, colIndex, numSlot.rowIndex, numSlot.colIndex);
                        numSlot.value = gameField[numSlot.rowIndex][numSlot.colIndex];
                        numSlot.isMerged = true;
                    } else {
                        moveValue(rowIndex, colIndex, numSlot.rowIndex, numSlot.colIndex - 1);
                        numSlot.setValues(numSlot.rowIndex, numSlot.colIndex - 1, gameField[numSlot.rowIndex][numSlot.colIndex - 1]);
                        numSlot.isMerged = false;
                    }
                }
            }
        }
        return true;
    }

    private boolean moveUp() {
        if (cantMoveInDirection(Direction.UP)) {
            return false;
        }

        for (int colIndex = 0; colIndex < gameField.length; ++colIndex) {
            Point firstNumSlot = new Point();
            Point numSlot = new Point();

            for (int rowIndex = 0; rowIndex < gameField.length; ++rowIndex) {
                if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value == 0) {
                    firstNumSlot.setValues(rowIndex, colIndex, gameField[rowIndex][colIndex]);
                    moveValue(firstNumSlot.rowIndex, firstNumSlot.colIndex, 0, colIndex);
                    numSlot.setValues(0, colIndex, firstNumSlot.value);
                } else if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value != 0) {
                    if (gameField[rowIndex][colIndex] == numSlot.value && !numSlot.isMerged) {
                        merge(rowIndex, colIndex, numSlot.rowIndex, numSlot.colIndex);
                        numSlot.value = gameField[numSlot.rowIndex][numSlot.colIndex];
                        numSlot.isMerged = true;
                    } else {
                        moveValue(rowIndex, colIndex, numSlot.rowIndex + 1, numSlot.colIndex);
                        numSlot.setValues(numSlot.rowIndex + 1, numSlot.colIndex, gameField[numSlot.rowIndex + 1][numSlot.colIndex]);
                        numSlot.isMerged = false;
                    }
                }
            }
        }
        return true;
    }

    private boolean moveDown() {
        if (cantMoveInDirection(Direction.DOWN)) {
            return false;
        }

        for (int colIndex = 0; colIndex < gameField.length; ++colIndex) {
            Point firstNumSlot = new Point();
            Point numSlot = new Point();

            for (int rowIndex = gameField.length - 1; rowIndex >= 0; --rowIndex) {
                if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value == 0) {
                    firstNumSlot.setValues(rowIndex, colIndex, gameField[rowIndex][colIndex]);
                    moveValue(firstNumSlot.rowIndex, firstNumSlot.colIndex, gameField.length - 1, colIndex);
                    numSlot.setValues(gameField.length - 1, colIndex, firstNumSlot.value);
                } else if (!isEmptySlot(rowIndex, colIndex) && firstNumSlot.value != 0) {
                    if (gameField[rowIndex][colIndex] == numSlot.value && !numSlot.isMerged) {
                        merge(rowIndex, colIndex, numSlot.rowIndex, numSlot.colIndex);
                        numSlot.value = gameField[numSlot.rowIndex][numSlot.colIndex];
                        numSlot.isMerged = true;
                    } else {
                        moveValue(rowIndex, colIndex, numSlot.rowIndex - 1, numSlot.colIndex);
                        numSlot.setValues(numSlot.rowIndex - 1, numSlot.colIndex, gameField[numSlot.rowIndex - 1][numSlot.colIndex]);
                        numSlot.isMerged = false;
                    }
                }
            }
        }
        return true;
    }

    private boolean cantMoveInDirection(Direction dir) {
        return !switch (dir) {
            case LEFT -> checkMoveLeft();
            case RIGHT -> checkMoveRight();
            case UP -> checkMoveUp();
            case DOWN -> checkMoveDown();
        };
    }

    private boolean checkMoveLeft() {
        for (int rowIndex = 0; rowIndex < gameField.length; ++rowIndex) {
            boolean hasEmptySlot = false;
            int theClosestNumberSlot = -1;

            for (int colIndex = 0; colIndex < gameField.length; ++colIndex) {
                if (!hasEmptySlot && isEmptySlot(rowIndex, colIndex)) {
                    hasEmptySlot = true;
                } else if (hasEmptySlot && !isEmptySlot(rowIndex, colIndex)) {
                    return true;
                } else if (!hasEmptySlot && !isEmptySlot(rowIndex, colIndex)) {
                    if (theClosestNumberSlot == gameField[rowIndex][colIndex]) {
                        return true;
                    }
                    theClosestNumberSlot = gameField[rowIndex][colIndex];
                }
            }
        }
        return false;
    }

    private boolean checkMoveRight() {
        for (int rowIndex = 0; rowIndex < gameField.length; ++rowIndex) {
            boolean hasEmptySlot = false;
            int theClosestNumberSlot = -1;

            for (int colIndex = gameField.length - 1; colIndex >= 0; --colIndex) {
                if (!hasEmptySlot && isEmptySlot(rowIndex, colIndex)) {
                    hasEmptySlot = true;
                } else if (hasEmptySlot && !isEmptySlot(rowIndex, colIndex)) {
                    return true;
                } else if (!hasEmptySlot && !isEmptySlot(rowIndex, colIndex)) {
                    if (theClosestNumberSlot == gameField[rowIndex][colIndex]) {
                        return true;
                    }
                    theClosestNumberSlot = gameField[rowIndex][colIndex];
                }
            }
        }
        return false;
    }

    private boolean checkMoveUp() {
        for (int colIndex = 0; colIndex < gameField.length; ++colIndex) {
            boolean hasEmptySlot = false;
            int theClosestNumberSlot = -1;

            for (int rowIndex = 0; rowIndex < gameField.length; ++rowIndex) {
                if (!hasEmptySlot && isEmptySlot(rowIndex, colIndex)) {
                    hasEmptySlot = true;
                } else if (hasEmptySlot && !isEmptySlot(rowIndex, colIndex)) {
                    return true;
                } else if (!hasEmptySlot && !isEmptySlot(rowIndex, colIndex)) {
                    if (theClosestNumberSlot == gameField[rowIndex][colIndex]) {
                        return true;
                    }
                    theClosestNumberSlot = gameField[rowIndex][colIndex];
                }
            }
        }
        return false;
    }

    private boolean checkMoveDown() {
        for (int colIndex = 0; colIndex < gameField.length; ++colIndex) {
            boolean hasEmptySlot = false;
            int theClosestNumberSlot = -1;

            for (int rowIndex = gameField.length - 1; rowIndex >= 0; --rowIndex) {
                if (!hasEmptySlot && isEmptySlot(rowIndex, colIndex)) {
                    hasEmptySlot = true;
                } else if (hasEmptySlot && !isEmptySlot(rowIndex, colIndex)) {
                    return true;
                } else if (!hasEmptySlot && !isEmptySlot(rowIndex, colIndex)) {
                    if (theClosestNumberSlot == gameField[rowIndex][colIndex]) {
                        return true;
                    }
                    theClosestNumberSlot = gameField[rowIndex][colIndex];
                }
            }
        }
        return false;
    }

    private boolean isEmptySlot(int rowIndex, int colIndex) { return gameField[rowIndex][colIndex] == 0; }

    public int getScore() {
        return this.score;
    }

    public enum Direction {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    private static class Point {
        int rowIndex;
        int colIndex;
        int value;
        boolean isMerged;

        public Point(int row, int col, int val, boolean merged) {
            this.rowIndex = row;
            this.colIndex = col;
            this.value = val;
            this.isMerged = merged;
        }

        Point(int row, int col) {
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
}
