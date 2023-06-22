import junit.framework.TestCase;
import org.junit.jupiter.api.Test;
import ru.nsu.fit.mihanizzm.game2048.model.FieldManager;
import ru.nsu.fit.mihanizzm.game2048.model.GameField;

import java.util.Arrays;

public class ModelTests {
    @Test
    public void checkNumberOfSpawnedTiles() {
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        int[][] actualField = testField.getGameField();

        int expected = 2;
        int actual = 0;

        for (int i = 0; i < actualField.length; ++i) {
            for (int j = 0; j < actualField.length; ++j) {
                if (actualField[i][j] != 0) {
                    ++actual;
                }
            }
        }

        TestCase.assertEquals(expected, actual);
    }

    @Test
    public void fourSameNumbersInARowUp() {
        FieldManager testManager = new TestFieldGenerator();
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        testField.setFieldManager(testManager);

        int[][] referenceField = new int[gameFieldSize][gameFieldSize];
        for (int j = 0; j < gameFieldSize; ++j) {
            referenceField[j][0] = 2;
        }
        testField.generateField(testField.getGameField(), referenceField);
        testField.move(GameField.Direction.UP);

        int[][] actualField = testField.getGameField();
        int[][] expectedField = new int[gameFieldSize][gameFieldSize];
        for (int j = 0; j < gameFieldSize / 2; ++j) {
            expectedField[j][0] = 4;
        }

        TestCase.assertTrue(Arrays.deepEquals(expectedField, actualField));
    }

    @Test
    public void fourSameNumbersInARowDown() {
        FieldManager testManager = new TestFieldGenerator();
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        testField.setFieldManager(testManager);

        int[][] referenceField = new int[gameFieldSize][gameFieldSize];
        for (int j = 0; j < gameFieldSize; ++j) {
            referenceField[j][0] = 2;
        }
        testField.generateField(testField.getGameField(), referenceField);
        testField.move(GameField.Direction.DOWN);

        int[][] actualField = testField.getGameField();
        int[][] expectedField = new int[gameFieldSize][gameFieldSize];
        for (int j = gameFieldSize - 1; j > 1; --j) {
            expectedField[j][0] = 4;
        }

        TestCase.assertTrue(Arrays.deepEquals(expectedField, actualField));
    }

    @Test
    public void fourSameNumbersInARowLeft() {
        FieldManager testManager = new TestFieldGenerator();
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        testField.setFieldManager(testManager);

        int[][] referenceField = new int[gameFieldSize][gameFieldSize];
        for (int j = 0; j < gameFieldSize; ++j) {
            referenceField[0][j] = 2;
        }
        testField.generateField(testField.getGameField(), referenceField);
        testField.move(GameField.Direction.LEFT);

        int[][] actualField = testField.getGameField();
        int[][] expectedField = new int[gameFieldSize][gameFieldSize];
        for (int j = 0; j < gameFieldSize / 2; ++j) {
            expectedField[0][j] = 4;
        }

        TestCase.assertTrue(Arrays.deepEquals(expectedField, actualField));
    }

    @Test
    public void fourSameNumbersInARowRight() {
        FieldManager testManager = new TestFieldGenerator();
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        testField.setFieldManager(testManager);

        int[][] referenceField = new int[gameFieldSize][gameFieldSize];
        for (int j = 0; j < gameFieldSize; ++j) {
            referenceField[0][j] = 2;
        }
        testField.generateField(testField.getGameField(), referenceField);
        testField.move(GameField.Direction.RIGHT);

        int[][] actualField = testField.getGameField();
        int[][] expectedField = new int[gameFieldSize][gameFieldSize];
        for (int j = gameFieldSize - 1; j > 1; --j) {
            expectedField[0][j] = 4;
        }

        TestCase.assertTrue(Arrays.deepEquals(expectedField, actualField));
    }

    @Test
    public void checkForNothingChangedUp() {
        FieldManager testManager = new TestFieldGenerator();
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        testField.setFieldManager(testManager);

        int[][] referenceField = new int[gameFieldSize][gameFieldSize];
        referenceField[0][0] = 4;
        referenceField[1][0] = 2;
        testField.generateField(testField.getGameField(), referenceField);

        TestCase.assertFalse(testField.move(GameField.Direction.UP));
    }

    @Test
    public void checkForNothingChangedDown() {
        FieldManager testManager = new TestFieldGenerator();
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        testField.setFieldManager(testManager);

        int[][] referenceField = new int[gameFieldSize][gameFieldSize];
        referenceField[3][0] = 4;
        referenceField[2][0] = 2;
        testField.generateField(testField.getGameField(), referenceField);

        TestCase.assertFalse(testField.move(GameField.Direction.DOWN));
    }

    @Test
    public void checkForNothingChangedLeft() {
        FieldManager testManager = new TestFieldGenerator();
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        testField.setFieldManager(testManager);

        int[][] referenceField = new int[gameFieldSize][gameFieldSize];
        referenceField[0][0] = 4;
        referenceField[0][1] = 2;
        testField.generateField(testField.getGameField(), referenceField);

        TestCase.assertFalse(testField.move(GameField.Direction.LEFT));
    }

    @Test
    public void checkForNothingChangedRight() {
        FieldManager testManager = new TestFieldGenerator();
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        testField.setFieldManager(testManager);

        int[][] referenceField = new int[gameFieldSize][gameFieldSize];
        referenceField[0][3] = 4;
        referenceField[0][2] = 2;
        testField.generateField(testField.getGameField(), referenceField);

        TestCase.assertFalse(testField.move(GameField.Direction.RIGHT));
    }

    @Test
    public void checkForNoMovesLeft() {
        FieldManager testManager = new TestFieldGenerator();
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        testField.setFieldManager(testManager);

        int[][] referenceField = new int[gameFieldSize][gameFieldSize];
        for (int i = 0; i < gameFieldSize; ++i) {
            for (int j = 0; j < gameFieldSize; ++j) {
                referenceField[i][j] = 2 * (int) Math.pow(2, i) * (int) Math.pow(2, j);
            }
        }
        testField.generateField(testField.getGameField(), referenceField);

        TestCase.assertTrue(testField.hasNoMoreMoves());
    }

    @Test
    public void hasReached2048() {
        FieldManager testManager = new TestFieldGenerator();
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        testField.setFieldManager(testManager);

        int[][] referenceField = new int[gameFieldSize][gameFieldSize];
        for (int i = 0; i < gameFieldSize; ++i) {
            referenceField[0][i] = 512;
        }
        testField.generateField(testField.getGameField(), referenceField);
        TestCase.assertFalse(testField.alreadyWon());
        testField.move(GameField.Direction.RIGHT);
        TestCase.assertFalse(testField.alreadyWon());
        testField.move(GameField.Direction.RIGHT);
        TestCase.assertTrue(testField.alreadyWon());
    }

    @Test
    public void fullyFilledFieldHasMoves() {
        FieldManager testManager = new TestFieldGenerator();
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        testField.setFieldManager(testManager);

        int[][] referenceField = new int[gameFieldSize][gameFieldSize];
        for (int i = 0; i < gameFieldSize; ++i) {
            for (int j = 0; j < gameFieldSize; ++j) {
                referenceField[i][j] = 2 * (int) Math.pow(2, j);
            }
        }
        testField.generateField(testField.getGameField(), referenceField);

        TestCase.assertFalse(testField.hasNoMoreMoves());
    }

    @Test
    public void overflowTileCheck() {
        FieldManager testManager = new TestFieldGenerator();
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        testField.setFieldManager(testManager);

        int[][] referenceField = new int[gameFieldSize][gameFieldSize];
        referenceField[0][0] = Integer.MAX_VALUE / 2 + 1;
        referenceField[0][1] = Integer.MAX_VALUE / 2 + 1;
        testField.generateField(testField.getGameField(), referenceField);
        testField.move(GameField.Direction.LEFT);

        TestCase.assertEquals(referenceField[0][0], Integer.MAX_VALUE / 2 + 1);
    }

    @Test
    public void checkCorrectScoreCalculations() {
        FieldManager testManager = new TestFieldGenerator();
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        testField.setFieldManager(testManager);

        int[][] referenceField = new int[gameFieldSize][gameFieldSize];
        referenceField[0][0] = 128;
        referenceField[0][1] = 128;
        testField.generateField(testField.getGameField(), referenceField);
        testField.move(GameField.Direction.LEFT);

        TestCase.assertEquals(testField.getScore(), 256);
    }

    @Test
    public void overflowScoreCheck() {
        FieldManager testManager = new TestFieldGenerator();
        int gameFieldSize = 4;
        GameField testField = new GameField(gameFieldSize);
        testField.setFieldManager(testManager);

        int[][] referenceField = new int[gameFieldSize][gameFieldSize];
        referenceField[0][0] = Integer.MAX_VALUE / 2 + 1;
        referenceField[0][1] = Integer.MAX_VALUE / 2 + 1;
        referenceField[0][2] = Integer.MAX_VALUE / 2 + 1;
        referenceField[0][3] = Integer.MAX_VALUE / 2 + 1;
        testField.generateField(testField.getGameField(), referenceField);
        testField.move(GameField.Direction.LEFT);
        testField.move(GameField.Direction.LEFT);

        TestCase.assertEquals(testField.getScore(), Integer.MAX_VALUE);
    }
}
