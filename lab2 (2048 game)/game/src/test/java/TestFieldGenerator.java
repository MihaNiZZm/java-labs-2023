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
