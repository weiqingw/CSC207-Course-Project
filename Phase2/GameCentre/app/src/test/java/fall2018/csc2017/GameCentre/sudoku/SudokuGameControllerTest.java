package fall2018.csc2017.GameCentre.sudoku;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.Test;

import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SudokuGameControllerTest {
    private Context context;
    private SudokuGameController controller;
    private SQLDatabase db;

    @Before
    public void setup(){
        context = mock(MockContext.class);
        User user = new User("admin", "admin");
        db = mock(SQLDatabase.class);
        controller = new SudokuGameController(context, user);
        controller.setDb(db);
        controller.setGameRunning(true);
        db.addData("admin", "PictureMatch");
        SudokuBoardManager boardManager = new SudokuBoardManager();
        controller.setBoardManager(boardManager);
    }

    @Test
    public void setupFileTest() {
        when(db.dataExists(controller.getUser().getUsername(), "Sudoku")).thenReturn(false);
        when(db.getDataFile(controller.getUser().getUsername(), "Sudoku")).thenReturn("admin_Sudoku_data.ser");
        controller.setupFile();
        assertEquals("admin_Sudoku_data.ser",controller.getGameStateFile());
        assertEquals("temp_admin_Sudoku_data.ser",controller.getTempGameStateFile());
    }

    @Test
    public void convertTime() {
        assertEquals("00:00:36", controller.convertTime(36000L));
    }

    @Test
    public void calculateScore() {
        Integer time = controller.calculateScore(1000L);
        assertEquals(20000, (Object)time);
    }

    @Test
    public void updateScore() {
        assertTrue(controller.updateScore(1000));
    }

    @Test
    public void boardSolved() {
        assertFalse(controller.boardSolved());
    }
}