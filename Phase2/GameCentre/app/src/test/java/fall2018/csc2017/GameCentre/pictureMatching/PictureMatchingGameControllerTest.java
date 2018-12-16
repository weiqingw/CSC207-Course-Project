package fall2018.csc2017.GameCentre.pictureMatching;

import android.content.Context;
import android.test.mock.MockContext;
import org.junit.Before;
import org.junit.Test;
import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PictureMatchingGameControllerTest {

    private Context context;
    private PictureMatchingGameController controller;
    private SQLDatabase db;


    @Before
    public void setup(){
        context = mock(MockContext.class);
        User user = new User("admin", "admin");
        db = mock(SQLDatabase.class);
        controller = new PictureMatchingGameController(context, user);
        controller.setDb(db);
        controller.setGameRunning(true);
        db.addData("admin", "PictureMatch");
        MatchingBoardManager boardManager = new MatchingBoardManager(2, "emoji");
        controller.setBoardManager(boardManager);
    }

    @Test
    public void setupFileTest() {
        when(db.dataExists(controller.getUser().getUsername(), "PictureMatch")).thenReturn(false);
        when(db.getDataFile(controller.getUser().getUsername(), "PictureMatch")).thenReturn("admin_PictureMatch_data.ser");
        controller.setupFile();
        assertEquals("admin_PictureMatch_data.ser",controller.getGameStateFile());
        assertEquals("temp_admin_PictureMatch_data.ser",controller.getTempGameStateFile());
    }

    @Test
    public void convertTimeTest() {
        assertEquals("00:00:36", controller.convertTime(36000L));
    }

    @Test
    public void calculateScoreTest() {
        Integer time = controller.calculateScore(1000L);
        assertEquals(10000, (Object)time);
    }

    @Test
    public void updateScoreTest() {
        assertTrue(controller.updateScore(1000));
    }

    @Test
    public void boardSolvedTest() {
        assertFalse(controller.boardSolved());
    }

    @Test
    public void isGameRunningTest(){
        assertTrue(controller.isGameRunning());
    }

    @Test
    public void setGameRunningTest(){
        controller.setGameRunning(false);
        assertFalse(controller.isGameRunning());
    }
}
