package fall2018.csc2017.GameCentre.slidingTiles;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fall2018.csc2017.GameCentre.sudoku.SudokuBoardManager;

import static org.junit.Assert.*;

public class SlidingTilesBoardManagerTest {

    private SlidingTilesBoardManager boardManager;
    private SlidingTilesBoard board;

    /**
     * This sets up necessary steps for the following tests.
     */
    public void setUp() {
        setUpCorrect();
        boardManager.makeMove(7);
        boardManager.setStepsTaken(1);
    }

    /**
     * Set up the board for testing.
     */
    private void setUpCorrect() {
        List<Integer> tiles = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        board = new SlidingTilesBoard(tiles, 3);
        boardManager = new SlidingTilesBoardManager(board);
    }

    /**
     * This tests the functionality of getBoard().
     */
    @Test
    public void getBoard() {
        setUp();
        assertEquals(board, boardManager.getBoard());
        setUpCorrect();
        assertEquals(board, boardManager.getBoard());
    }

    /**
     * This tests the functionality of setCapacity() and getCapacity().
     */
    @Test
    public void undoAndMoveMethodsTest() {
        setUpCorrect();
        assertEquals(3, boardManager.getCapacity());
        boardManager.setCapacity(2);
        assertEquals(2, boardManager.getCapacity());
        boardManager.move(7);
        assertFalse(boardManager.undoAvailable());
        boardManager.move(8);
        assertFalse(boardManager.undoAvailable());
        boardManager.makeMove(7);
        boardManager.makeMove(6);
        boardManager.makeMove(3);
        assertEquals(6, (int) boardManager.popUndo());
        assertEquals(7, (int) boardManager.popUndo());
        assertFalse(boardManager.undoAvailable());

        setUpCorrect();
        boardManager.setCapacity(3);
        boardManager.makeMove(7);
        boardManager.makeMove(6);
        boardManager.makeMove(3);
        assertEquals(6, (int) boardManager.popUndo());
        assertEquals(7, (int) boardManager.popUndo());
        assertTrue(boardManager.undoAvailable());
    }

    /**
     * This tests the steps tracking function of the board.
     */
    @Test
    public void stepSettingAndGettingTest() {
        setUpCorrect();
        boardManager.move(7);
        assertEquals(0, boardManager.getStepsTaken());
        boardManager.makeMove(6);
        assertEquals(0, boardManager.getStepsTaken());
        boardManager.setStepsTaken(10);
        assertEquals(10, boardManager.getStepsTaken());
    }

    /**
     * This tests the functionality of getTimeTaken() and setTimeTaken().
     */
    @Test
    public void getAndSetTimeAndSetpsTaken() {
        setUp();
        boardManager.setTimeTaken(5);
        assertEquals(5, boardManager.getTimeTaken());
    }

    /**
     * This tests the functionality of isSolvable().
     */
    @Test
    public void solvable() {
        setUpCorrect();
        assertEquals(1, boardManager.blankPosition());
        assertTrue(boardManager.isSolvable());
        board.swapTiles(0, 0, 0, 1);
        assertFalse(boardManager.isSolvable());
    }

    /**
     * This tests the functionality of isSolvable().
     */
    @Test
    public void newBoardSolvable() {
        boardManager = new SlidingTilesBoardManager(3);
        assertTrue(boardManager.isSolvable());
        boardManager = new SlidingTilesBoardManager(4);
        assertTrue(boardManager.isSolvable());
        boardManager = new SlidingTilesBoardManager(5);
        assertTrue(boardManager.isSolvable());
    }

    /**
     * This tests the functionality of getImageBackground().
     */
    @Test
    public void getImageBackground() {
        setUp ();
        assertNull(boardManager.getImageBackground());
        boardManager.setImageBackground(new byte[16]);
        assertEquals(0, boardManager.getImageBackground()[0]);
    }

    /**
     * This tests the functionality of gameFinished().
     */
    @Test
    public void boardSolved() {
        setUp();
        assertFalse(boardManager.boardSolved());
        setUpCorrect();
        assertTrue(boardManager.boardSolved());
    }

    /**
     * This tests the functionality of isValidTap().
     */
    @Test
    public void isValidTap() {
        setUpCorrect();
        assertTrue(boardManager.isValidTap(7));
        boardManager.makeMove(7);
        assertTrue(boardManager.isValidTap(4));
        boardManager.makeMove(4);
        assertTrue(boardManager.isValidTap(5));
        boardManager.makeMove(5);
        assertTrue(boardManager.isValidTap(8));
        boardManager.makeMove(8);
        assertFalse(boardManager.isValidTap(8));
    }
}