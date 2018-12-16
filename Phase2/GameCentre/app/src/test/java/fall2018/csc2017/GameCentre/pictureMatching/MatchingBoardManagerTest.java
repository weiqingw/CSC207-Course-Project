package fall2018.csc2017.GameCentre.pictureMatching;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * JUnit tests for MatchingBoard and MatchingBoardManager.
 *
 * @author Weiqing Wang
 */
public class MatchingBoardManagerTest {

    /**
     * The boardManager for testing.
     */
    private MatchingBoardManager boardManager;

    /**
     * Set up a board for testing.
     */
    private void setUpCorrect(int difficulty) {
        boardManager = new MatchingBoardManager(difficulty, "number");
        List<PictureTile> tiles = new ArrayList<>();
        for (int tileNum = 0;
             tileNum < boardManager.getDifficulty() * boardManager.getDifficulty(); tileNum++) {
            boardManager.addPictureTileToList(tiles,
                    boardManager.getDifficulty() * boardManager.getDifficulty(), tileNum);
        }
        boardManager.setBoard(new MatchingBoard(tiles, boardManager.getDifficulty()));
    }

    /**
     * Solve the board.
     */
    private void solveBoard() {
        int typeOfTiles = boardManager.getDifficulty() * boardManager.getDifficulty() / 4;
        for (int time = 0; time < 2; time++) {
            for (int index = 0; index < typeOfTiles; index++) {
                boardManager.makeMove(time * 2 * typeOfTiles + index);
                boardManager.makeMove((time * 2 + 1) * typeOfTiles + index);
                boardManager.solveTile();
            }
        }
    }

    /**
     * Test makeMove and solveTile methods in a 4 X 4 board.
     */
    @Test
    public void flipAndSolveCorrectEasy() {
        setUpCorrect(4);
        boardManager.makeMove(0);
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 0).getState());
        boardManager.makeMove(4);
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 0).getState());
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(1, 0).getState());
        boardManager.solveTile();
        assertEquals(PictureTile.SOLVED, boardManager.getBoard().getTile(0, 0).getState());
        assertEquals(PictureTile.SOLVED, boardManager.getBoard().getTile(1, 0).getState());
    }

    /**
     * Test makeMove and solveTile methods in a 6 X 6 board.
     */
    @Test
    public void flipAndSolveCorrectMedium() {
        setUpCorrect(6);
        boardManager.makeMove(0);
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 0).getState());
        boardManager.makeMove(9);
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 0).getState());
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(1, 3).getState());
        boardManager.solveTile();
        assertEquals(PictureTile.SOLVED, boardManager.getBoard().getTile(0, 0).getState());
        assertEquals(PictureTile.SOLVED, boardManager.getBoard().getTile(1, 3).getState());
    }

    /**
     * Test makeMove and solveTile methods in a 8 X 8 board.
     */
    @Test
    public void flipAndSolveCorrectHard() {
        setUpCorrect(8);
        boardManager.makeMove(0);
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 0).getState());
        boardManager.makeMove(16);
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 0).getState());
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(2, 0).getState());
        boardManager.solveTile();
        assertEquals(PictureTile.SOLVED, boardManager.getBoard().getTile(0, 0).getState());
        assertEquals(PictureTile.SOLVED, boardManager.getBoard().getTile(2, 0).getState());
    }

    /**
     * Test makeMove and solveTile methods in a 4 X 4 board.
     */
    @Test
    public void flipAndSolveIncorrectEasy() {
        setUpCorrect(6);
        boardManager.getBoard().flipTile(0, 0);
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 0).getState());
        boardManager.getBoard().flipTile(0, 1);
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 0).getState());
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 1).getState());
        boardManager.solveTile();
        assertEquals(PictureTile.COVERED, boardManager.getBoard().getTile(0, 0).getState());
        assertEquals(PictureTile.COVERED, boardManager.getBoard().getTile(0, 1).getState());
    }

    /**
     * Test makeMove and solveTile methods in a 6 X 6 board.
     */
    @Test
    public void flipAndSolveIncorrectMedium() {
        setUpCorrect(6);
        boardManager.getBoard().flipTile(0, 0);
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 0).getState());
        boardManager.getBoard().flipTile(0, 1);
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 0).getState());
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 1).getState());
        boardManager.solveTile();
        assertEquals(PictureTile.COVERED, boardManager.getBoard().getTile(0, 0).getState());
        assertEquals(PictureTile.COVERED, boardManager.getBoard().getTile(0, 1).getState());
    }

    /**
     * Test makeMove and solveTile methods in a 8 X 8 board.
     */
    @Test
    public void flipAndSolveIncorrectHard() {
        setUpCorrect(8);
        boardManager.getBoard().flipTile(0, 0);
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 0).getState());
        boardManager.getBoard().flipTile(0, 1);
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 0).getState());
        assertEquals(PictureTile.FLIP, boardManager.getBoard().getTile(0, 1).getState());
        boardManager.solveTile();
        assertEquals(PictureTile.COVERED, boardManager.getBoard().getTile(0, 0).getState());
        assertEquals(PictureTile.COVERED, boardManager.getBoard().getTile(0, 1).getState());
    }

    /**
     * Test whether the theme of the game could be correctly returned when multiple
     * boardManager exists.
     */
    @Test
    public void multipleBoardManagerThemeCorrectness() {
        MatchingBoardManager boardManager1 =
                new MatchingBoardManager(4, "number");
        MatchingBoardManager boardManager2 =
                new MatchingBoardManager(4, "emoji");
        MatchingBoardManager boardManager3 =
                new MatchingBoardManager(4, "animal");
        MatchingBoardManager boardManager4 =
                new MatchingBoardManager(6, "number");
        MatchingBoardManager boardManager5 =
                new MatchingBoardManager(6, "emoji");
        MatchingBoardManager boardManager6 =
                new MatchingBoardManager(6, "animal");
        assertEquals("number", boardManager1.getTheme());
        assertEquals("emoji", boardManager2.getTheme());
        assertEquals("animal", boardManager3.getTheme());
        assertEquals("number", boardManager4.getTheme());
        assertEquals("emoji", boardManager5.getTheme());
        assertEquals("animal", boardManager6.getTheme());
    }

    /**
     * Test the timeTaken getter and setter function and initialization of
     * this variable.
     */
    @Test
    public void timeSetUp() {
        setUpCorrect(4);
        assertEquals(0L, boardManager.getTimeTaken());
        boardManager.setTimeTaken(10);
        assertEquals(10, boardManager.getTimeTaken());
    }

    /**
     * Test whether a valid tap and an invalid tap can be identified.
     */
    @Test
    public void isValidTap() {
        setUpCorrect(4);
        boardManager.makeMove(0);
        boardManager.makeMove(4);
        boardManager.solveTile();
        assertFalse(boardManager.isValidTap(0));
        assertTrue(boardManager.isValidTap(1));
        setUpCorrect(6);
        boardManager.makeMove(0);
        boardManager.makeMove(9);
        boardManager.solveTile();
        assertFalse(boardManager.isValidTap(0));
        assertTrue(boardManager.isValidTap(1));
        setUpCorrect(8);
        boardManager.makeMove(0);
        boardManager.makeMove(16);
        boardManager.solveTile();
        assertFalse(boardManager.isValidTap(0));
        assertTrue(boardManager.isValidTap(1));
    }

    /**
     * Tests whether isSolved method works properly.
     */
    @Test
    public void isSolved() {
        setUpCorrect(4);
        solveBoard();
        assertTrue(boardManager.boardSolved());
        setUpCorrect(6);
        solveBoard();
        assertTrue(boardManager.boardSolved());
        setUpCorrect(8);
        solveBoard();
        assertTrue(boardManager.boardSolved());
    }
}