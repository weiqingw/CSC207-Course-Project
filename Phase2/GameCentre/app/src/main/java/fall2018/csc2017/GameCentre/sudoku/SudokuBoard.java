package fall2018.csc2017.GameCentre.sudoku;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import fall2018.csc2017.GameCentre.util.BoardForBoardGames;

/**
 * The SudokuBoard class.
 */
public class SudokuBoard extends BoardForBoardGames implements Serializable {

    /**
     * The cells on the board.
     */
    private Cell[][] cells = new Cell[9][9];
    /**
     * Number of rows.
     */
    final static int NUM_ROW = 9;
    /**
     * Number of columns.
     */
    final static int NUM_COL = 9;


    /**
     * A new board of cells in row-major order.
     * Precondition: len(cells) == 81.
     *
     * @param cells the cells for the board
     */
    SudokuBoard(List<Cell> cells) {
        Iterator<Cell> iterator = cells.iterator();

        for (int row = 0; row != 9; row++) {
            for (int col = 0; col != 9; col++) {
                this.cells[row][col] = iterator.next();
            }
        }
    }

    /**
     * Get the box at the ith row and jth column
     *
     * @param row the row of the cell
     * @param column the column of the cell.
     * @return cell
     */
    Cell getCell(int row, int column) {
        return this.cells[row][column];
    }

    /**
     * Check whether the box at the ith row and jth column
     * has been correctly solved.
     *
     * @param row the row of the cell
     * @param col teh column of the cell.
     * @return whether the cell is solved
     */
    boolean checkCell(int row, int col) {
        return this.cells[row][col].checkValue();
    }

    /**
     * Check whether the face value of box at the ith row and
     * jth column can be edited.
     *
     * @param row the row of the cell.
     * @param col the column of the cell.
     * @return whether the cell is editable
     */
    boolean checkEditable(int row, int col) {
        return this.cells[row][col].isEditable();
    }
}
