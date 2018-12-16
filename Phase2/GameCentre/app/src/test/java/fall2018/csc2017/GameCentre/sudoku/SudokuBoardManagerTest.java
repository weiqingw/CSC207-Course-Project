package fall2018.csc2017.GameCentre.sudoku;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SudokuBoardManagerTest {

    private SudokuBoardManager boardManager;
    private int moveTaken;

    /**
     * Set up necessary steps for following test cases.
     */
    public void setUp() {
        SudokuBoardManager.setLevelOfDifficulty(2);
        boardManager = new SudokuBoardManager();
        moveTaken = findEditablePosition(boardManager.getBoard());
        boardManager.makeMove(moveTaken);
        int move = findEditablePosition(boardManager.getBoard());
        boardManager.updateValue(move, false);
        boardManager.undo();
        boardManager.setHintAvailable(5);
    }

    /**
     * Make a solved SudokuBoard.
     */
    private void setUpCorrect() {
        this.boardManager = new SudokuBoardManager();
    }

    /**
     * This is a helper function for setUpCorrect. It helps to find a position in the sudoku board
     * that is editable.
     * @param board the board
     * @return a editable position
     */
    private int findEditablePosition(SudokuBoard board){
        int position = -1;
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(board.getCell(i, j).isEditable()){
                    position = i * 9 + j;
                    break;
                }
            }
        }
        return position;
    }

    /**
     * This test the functionality getHintAvailable() and setHintAvailable().
     */
    @Test
    public void hintFunction() {
        setUp();
        assertEquals(5, boardManager.getHintAvailable());
        boardManager.reduceHint();
        assertEquals(4, boardManager.getHintAvailable());
        boardManager.reduceHint();
        boardManager.reduceHint();
        boardManager.reduceHint();
        boardManager.reduceHint();
        boardManager.reduceHint();
        assertEquals(0, boardManager.getHintAvailable());
        boardManager.setHintAvailable(1);
        assertEquals(1, boardManager.getHintAvailable());
        boardManager.reduceHint();
        assertEquals(0, boardManager.getHintAvailable());
    }

    /**
     * This test the functionality getCurrentCell() and setCurrentCell().
     */
    @Test
    public void setAndGetCurrentCell() {
        setUp();
        boardManager.makeMove(80);
        assertEquals(boardManager.getBoard().getCell(8, 8),
                boardManager.getCurrentCell());
        boardManager.makeMove(2);
        assertEquals(boardManager.getBoard().getCell(0, 2),
                boardManager.getCurrentCell());

    }

    /**
     * This test the functionality undoAvailable().
     */
    @Test
    public void makeMoveAndUndoFunction() {
        setUp();
        assertFalse(boardManager.undoAvailable());
        int oldValue = boardManager.getBoard().getCell(0,
                2).getFaceValue();
        boardManager.makeMove(2);
        boardManager.updateValue(9, false);
        boardManager.makeMove(3);
        boardManager.updateValue(1, false);
        assertEquals(9,
                (int) boardManager.getBoard().getCell(0,
                        2).getFaceValue());
        assertTrue(boardManager.undoAvailable());
        boardManager.undo();
        assertTrue(boardManager.undoAvailable());
        boardManager.undo();
        assertFalse(boardManager.undoAvailable());
        assertEquals(oldValue, (int) boardManager.getBoard().getCell(0,
                2).getFaceValue());
        boardManager.updateValue(2, true);
        assertEquals(2,
                (int) boardManager.getBoard().getCell(0,
                        2).getFaceValue());
        assertFalse(boardManager.undoAvailable());
    }

    /**
     * This test the functionality gameFinished().
     */
    @Test
    public void boardSolved() {
        setUp();
        assertFalse(boardManager.boardSolved());
        SudokuBoardManager.setLevelOfDifficulty(10);
        setUpCorrect();
        assertTrue(boardManager.boardSolved());
    }

    /**
     * This test the functionality isValidTap().
     */
    @Test
    public void isValidTap() {
        setUp();
        assertTrue(boardManager.isValidTap(moveTaken));
    }

    /**
     * Checks whether each row has numbers 1 to 9.
     */
    @Test
    public void horizontallySetUp() {
        setUpCorrect();
        SudokuBoard board = boardManager.getBoard();
        ArrayList<ArrayList<Integer>> horizontal =
                new ArrayList<ArrayList<Integer>>();
        for (int row = 0; row < 9; row++) {
            ArrayList<Integer> rows = new ArrayList<Integer>();
            for (int column = 0; column < 9; column++) {
                rows.add(board.getCell(row, column).getSolutionValue());
            }
            horizontal.add(rows);
        }
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                assertTrue(horizontal.get(row).contains(column + 1));
            }
        }
    }

    /**
     * This tests setter and getter function for time taken.
     */
    @Test
    public void timeTaken() {
        setUpCorrect();
        assertEquals(0L, boardManager.getTimeTaken());
        boardManager.setTimeTaken(10);
        assertEquals(10, boardManager.getTimeTaken());
    }

    /**
     * Checks whether each column has numbers 1 to 9.
     */
    @Test
    public void verticallySetUp() {
        setUpCorrect();
        SudokuBoard board = boardManager.getBoard();
        ArrayList<ArrayList<Integer>> horizontal =
                new ArrayList<ArrayList<Integer>>();
        for (int column = 0; column < 9; column++) {
            ArrayList<Integer> rows = new ArrayList<Integer>();
            for (int row = 0; row < 9; row++) {
                rows.add(board.getCell(row, column).getSolutionValue());
            }
            horizontal.add(rows);
        }
        boolean result = true;
        for (int column = 0; column < 9; column++) {
            for (int row = 0; row < 9; row++) {
                if (!horizontal.get(column).contains(row + 1)) {
                    result = false;
                    break;
                };
            }
        }
        assertTrue(result);
    }

    /**
     * Checks whether each column has numbers 1 to 9.
     */
    @Test
    public void boxSetUp() {
        setUpCorrect();
        SudokuBoard board = boardManager.getBoard();
        ArrayList<ArrayList<Integer>> boxes =
                new ArrayList<ArrayList<Integer>>();
        int columnStarting = 0;
        int rowStarting = 0;
        while (columnStarting != 9) {
            ArrayList<Integer> box = new ArrayList<Integer>();
            for (int column = columnStarting;
                 column < columnStarting + 3;
                 column++) {
                if (column == columnStarting) {
                    box = new ArrayList<Integer>();
                }
                for (int row = rowStarting; row < rowStarting + 3; row++) {
                    box.add(board.getCell(row, column).getSolutionValue());
                }
            }
            boxes.add(box);
            if (rowStarting == 6) {
                rowStarting = 0;
                columnStarting += 3;
            } else {
                rowStarting += 3;
            }
        }
        boolean result = true;
        for (int boxNumber = 0; boxNumber < 9; boxNumber++) {
            for (int boxIndex = 0; boxIndex < 9; boxIndex++) {
                if (!boxes.get(boxNumber).contains(boxIndex + 1)) {
                    result = false;
                    break;
                };
            }
        }
        assertTrue(result);
    }

    /**
     * Test whether the level of difficulty works properly.
     */
    @Test
    public void levelOfDifficultyEasy() {
        SudokuBoardManager.setLevelOfDifficulty(1);
        setUpCorrect();
        assertEquals(1, (int) boardManager.getLevelOfDifficulty());
        int count = 0;
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (boardManager.getBoard().getCell(row, column)
                        .getFaceValue().equals(0)) {
                    count += 1;
                }
            }
        }
        assertEquals(count, 18);
    }

    /**
     * Test whether the level of difficulty works properly.
     */
    @Test
    public void levelOfDifficultyMedium() {
        SudokuBoardManager.setLevelOfDifficulty(2);
        setUpCorrect();
        assertEquals(2, (int) boardManager.getLevelOfDifficulty());
        int count = 0;
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (boardManager.getBoard().getCell(row, column)
                        .getFaceValue().equals(0)) {
                    count += 1;
                }
            }
        }
        assertEquals(count, 36);
    }

    /**
     * Test whether the level of difficulty works properly.
     */
    @Test
    public void levelOfDifficultyHard() {
        SudokuBoardManager.setLevelOfDifficulty(3);
        setUpCorrect();
        assertEquals(3, (int) boardManager.getLevelOfDifficulty());
        int count = 0;
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (boardManager.getBoard().getCell(row, column)
                        .getFaceValue().equals(0)) {
                    count += 1;
                }
            }
        }
        assertEquals(count, 54);
    }

}