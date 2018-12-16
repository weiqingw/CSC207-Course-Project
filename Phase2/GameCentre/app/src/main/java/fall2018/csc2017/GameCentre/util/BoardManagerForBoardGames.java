package fall2018.csc2017.GameCentre.util;

import java.util.Observable;

/**
 * The BoardManagerForBoardGames class.
 */
public abstract class BoardManagerForBoardGames extends Observable {


    /**
     * The default Constructor for the SlidingTilesBoard Manager.
     */
    protected BoardManagerForBoardGames() {
    }

    /**
     * Return the current board.
     */
    public abstract BoardForBoardGames getBoard();

    /**
     * Getter function for the time the user used.
     *
     * @return time taken
     */
    public abstract long getTimeTaken();

    /**
     * Setter function for the time the user used.
     */
    public abstract void setTimeTaken(long timeTakenSoFar);

    /**
     * Returns whether the sudoku puzzle has been solved.
     *
     * @return whether the sudoku puzzle has been solved
     */
    public abstract boolean boardSolved();

    /**
     * Return whether the tap is valid.
     *
     * @return whether the tap is valid
     */
    public abstract boolean isValidTap(int position);

    /**
     * Performs changes to the board.
     */
    public abstract void makeMove(int position);
}
