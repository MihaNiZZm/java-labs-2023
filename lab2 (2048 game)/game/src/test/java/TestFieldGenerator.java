import ru.nsu.fit.mihanizzm.game2048.model.FieldManager;

public class TestFieldGenerator implements FieldManager {

    @Override
    public void generateField(int[][] field, int[][] reference) {
        if (reference == null) {
            for (int i = 0; i < field.length; ++i) {
                for (int j = 0; j < field.length; ++j) {
                    field[i][j] = 0;
                }
            }
        }
        else {
            for (int i = 0; i < field.length; ++i) {
                for (int j = 0; j < field.length; ++j) {
                    field[i][j] = reference[i][j];
                }
            }
        }
    }

    @Override
    public void spawnNewNumber(int[][] field) {
//        Random pointChooser = new Random();
//        Random numberChooser = new Random();
//
//        List<Point> availablePoints = new ArrayList<>();
//        for (int rowIndex = 0; rowIndex < field.length; ++rowIndex) {
//            for (int colIndex = 0; colIndex < field.length; ++colIndex) {
//                if (field[rowIndex][colIndex] == 0) {
//                    availablePoints.add(new Point(rowIndex, colIndex));
//                }
//            }
//        }
//
//        int index = pointChooser.nextInt(availablePoints.size());
//        Point chosenPoint = availablePoints.get(index);
//        if (numberChooser.nextDouble() < 0.9) {
//            field[chosenPoint.rowIndex][chosenPoint.colIndex] = 2;
//        } else {
//            field[chosenPoint.rowIndex][chosenPoint.colIndex] = 4;
//        }
    }

    @Override
    public void clearField(int[][] field) {
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field.length; ++j) {
                field[i][j] = 0;
            }
        }
        generateField(field, null);
    }
}
