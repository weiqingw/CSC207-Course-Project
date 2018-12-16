package fall2018.csc2017.GameCentre.slidingTiles;

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

public class SlidingTilesGameControllerTest {
    private Context context;
    private SlidingTilesGameController controller;
    private SQLDatabase db;

    @Before
    public void setup() {
        context = mock(MockContext.class);
        User user = new User("admin", "admin");
        db = mock(SQLDatabase.class);
        controller = new SlidingTilesGameController(context, user);
        controller.setDb(db);
        controller.setGameRunning(true);
        db.addData("admin", "SlidingTiles");
        SlidingTilesBoardManager boardManager = new SlidingTilesBoardManager(3);
        controller.setBoardManager(boardManager);
        controller.setSteps(2);
    }

    @Test
    public void getStepTest(){
        assertEquals(2, controller.getSteps());
    }

    @Test
    public void setupFileTest() {
        when(db.dataExists(controller.getUser().getUsername(), "SlidingTiles")).thenReturn(false);
        when(db.getDataFile(controller.getUser().getUsername(), "SlidingTiles")).thenReturn("admin_SlidingTiles_data.ser");
        controller.setupFile();
        assertEquals("admin_SlidingTiles_data.ser",controller.getGameStateFile());
        assertEquals("temp_admin_SlidingTiles_data.ser",controller.getTempGameStateFile());
    }

    @Test
    public void convertTimeTest() {
        assertEquals("00:00:36", controller.convertTime(36000L));
    }

    @Test
    public void calculateScoreTest() {
        Integer time = controller.calculateScore(1000L);
        assertEquals(3333, (Object)time);
    }

    @Test
    public void updateScoreTest() {
        assertTrue(controller.updateScore(1000));
    }

    @Test
    public void performUndoTest() {
        assertFalse(controller.performUndo());
    }
}