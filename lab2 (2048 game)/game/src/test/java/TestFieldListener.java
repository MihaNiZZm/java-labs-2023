import ru.nsu.fit.mihanizzm.game2048.model.FieldListener;

public class TestFieldListener implements FieldListener {
    public boolean got2048 = false;
    @Override
    public void update(boolean hasWon, boolean hasNoMoves) {
        if (hasWon) {
            got2048 = true;
        }
    }
}
