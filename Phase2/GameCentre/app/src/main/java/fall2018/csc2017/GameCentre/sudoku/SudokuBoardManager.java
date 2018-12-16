package fall2018.csc2017.GameCentre.sudoku;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fall2018.csc2017.GameCentre.data.StateStack;
import fall2018.csc2017.GameCentre.util.BoardManagerForBoardGames;

import static java.lang.Integer.max;

/**
 * The SudokuBoardManager class.
 */
public class SudokuBoardManager extends BoardManagerForBoardGames implements Serializable {

    /**
     * The board begin managed.
     */
    private SudokuBoard board;

    /**
     * The number of hints the user has.
     */
    private int hintAvailable;

    /**
     * The cell currently selected
     */
    private Cell currentCell;

    /**
     * The position which the user has selected.
     */
    private int currentPos;

    /**
     * The time has taken so far.
     */
    private long timeTaken;

    /**
     * The undoStack storing steps has taken.(limited capacity)
     */
    private StateStack<Integer[]> undoStack;

    /**
     * The default number of performUndo time.
     */
    private static final int MAX_UNDO_LIMIT = 20;

    /**
     * The level of difficulty.
     */
    private static Integer levelOfDifficulty = 2;


    /**
     * Manage a new shuffled board.
     */
    public SudokuBoardManager() {
        List<Cell> cells = new ArrayList<>();
        Integer[][] newBoard = new BoardGenerator().getBoard();
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                cells.add(new Cell(row, column, newBoard[row][column]));
            }
        }
        Integer editable;
        switch (levelOfDifficulty) {
            case 1: editable = 18;
                    setHintAvailable(1);
                    break;
            case 2: editable = 36;
                    setHintAvailable(5);
                    break;
            case 3: editable = 54;
                    setHintAvailable(3);
                    break;
            default:editable = 0;
                    setHintAvailable(5);
                    break;
        }
        Integer changed = 0;
        while (!changed.equals(editable)) {
            Random r = new Random();
            int index = r.nextInt(SudokuBoard.NUM_COL * SudokuBoard.NUM_ROW);
            if (!cells.get(index).isEditable()) {
                cells.get(index).makeEditable();
                cells.get(index).setFaceValue(0);
                changed++;
            }
        }
        this.board = new SudokuBoard(cells);
        this.timeTaken = 0L;
        this.undoStack = new StateStack<>(MAX_UNDO_LIMIT);
    }

    /**
     * Getter function for number of hints available.
     */
    int getHintAvailable() {
        return hintAvailable;
    }

    /**
     * Setter function for number of hints available.
     */
    void setHintAvailable(int hint) {
        this.hintAvailable = hint;
    }

    /**
     * Reduce hintAvailable.
     */
    void reduceHint() {
        hintAvailable = max(0, hintAvailable - 1);
    }

    /**
     * Return the current board.
     */
    public SudokuBoard getBoard() {
        return board;
    }

    /**
     * Get the time which the user has already used.
     */
    public long getTimeTaken() {
        return timeTaken;
    }

    /**
     * Setter function for time taken.
     */
    public void setTimeTaken(long timeTakenSoFar) {
        this.timeTaken = timeTakenSoFar;
    }

    /**
     * Add a move to the performUndo stack.
     */
    private void addUndo(Integer[] move) {
        undoStack.put(move);
    }

    /**
     * Setter function for the cell which the user selected.
     */
    void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    /**
     * Getter function for the cell which the user selected.
     */
    Cell getCurrentCell() {
        return currentCell;
    }

    /**
     * Setter for level of difficulty.
     */
    static void setLevelOfDifficulty(int levelOfDifficulty) {
        SudokuBoardManager.levelOfDifficulty = levelOfDifficulty;
    }

    /**
     * Getter for levelOfDifficulty
     * @return a integer that represents the level of difficulty
     */
    static Integer getLevelOfDifficulty() {
        return levelOfDifficulty;
    }

    /**
     * Returns if performUndo is available.
     */
    boolean undoAvailable() {
        return !undoStack.isEmpty();
    }

    /**
     * Get the performUndo step.
     */
    private Integer[] popUndo() {
        return undoStack.pop();
    }

    /**
     * Returns whether the sudoku puzzle has been solved.
     */
    public boolean boardSolved() {
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if (!board.checkCell(row, column)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Return whether the tap is valid.
     */
    public boolean isValidTap(int position) {
        return board.checkEditable(position / 9, position % 9);
    }

    /**
     * When a cell is taped, set it to be currentCell and highlight it.
     *
     * @param position The position of the cell taped.
     */
    public void makeMove(int position) {
        currentPos = position;
        if (getCurrentCell() != null) {
            getCurrentCell().setHighlighted(false);
            getCurrentCell().setFaceValue(getCurrentCell().getFaceValue());
        }
        setCurrentCell(board.getCell(position / SudokuBoard.NUM_COL,
                position % SudokuBoard.NUM_ROW));
        getCurrentCell().setHighlighted(true);
        getCurrentCell().setFaceValue(getCurrentCell().getFaceValue());
        setChanged();
        notifyObservers();
    }

    /**
     * Update the face value of the board.
     */
    void updateValue(int value, boolean undo) {
        if (currentCell != null) {
            if (!undo)
                addUndo(new Integer[]{currentPos, currentCell.getFaceValue()});
            currentCell.setFaceValue(value);
            setChanged();
            notifyObservers();
        }

    }

    /**
     * Do all steps of an performUndo
     */
    void undo() {
        dehighlightCell();
        Integer[] move = popUndo();
        int position = move[0];
        int value = move[1];
        currentCell = board.getCell(position / SudokuBoard.NUM_COL,
                position % SudokuBoard.NUM_ROW);
        if (!undoStack.isEmpty())
            currentCell.setHighlighted(false);
        updateValue(value, true);
        currentCell.setFaceValue(currentCell.getFaceValue());
        highlightNextCell();
        setChanged();
        notifyObservers();
    }

    /**
     * de-highlight the cell.
     */
    private void dehighlightCell() {
        if (currentCell != null && undoStack.size() > 1) {
            // De-highlight cell
            currentCell.setHighlighted(false);
            currentCell.setFaceValue(0);
        }
    }

    /**
     * highlight the next cell, the helper of undo method.
     */
    private void highlightNextCell() {
        Integer[] move;
        int position;
        if (!undoStack.isEmpty()) {
            move = this.undoStack.get();
            position = move[0];
            setCurrentCell(board.getCell(position / SudokuBoard.NUM_COL,
                    position % SudokuBoard.NUM_ROW));
            currentCell.setHighlighted(true);
            currentPos = position;
            currentCell.setFaceValue(currentCell.getFaceValue());
        }
    }
}
