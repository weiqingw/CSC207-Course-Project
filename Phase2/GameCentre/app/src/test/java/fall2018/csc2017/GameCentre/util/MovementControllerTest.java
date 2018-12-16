package fall2018.csc2017.GameCentre.util;

import android.content.Context;
import android.test.mock.MockContext;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import fall2018.csc2017.GameCentre.pictureMatching.MatchingBoardManager;

import static org.junit.Assert.*;

public class MovementControllerTest {
    private MovementController movementController;
    Context context;
    MatchingBoardManager boardManager;
    @Before
    public void setUp() {
        boardManager = new MatchingBoardManager(2, "emoji");
        movementController = new MovementController();
        movementController.setBoardManager(boardManager);
        context = mock(MockContext.class);
    }

    @Test
    public void processTapMovement() {
        movementController.processTapMovement(context, 0);
        assertEquals("flip", boardManager.getBoard().getTile(0, 0).getState());
    }
}