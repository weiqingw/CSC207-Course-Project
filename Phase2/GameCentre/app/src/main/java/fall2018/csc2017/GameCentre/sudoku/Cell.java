package fall2018.csc2017.GameCentre.sudoku;

import java.io.Serializable;

import fall2018.csc2017.GameCentre.R;

/**
 * The Cell class.
 */
public class Cell implements Serializable {

    /**
     * The correct value of the box
     */
    private Integer solutionValue;

    /**
     * Whether the value of the box is displayed.
     */
    private Integer faceValue;

    /**
     * Whether the face value of the box can be edited.
     */
    private boolean editable;

    /**
     * The background ID to find the box image
     */
    private int background;

    /**
     * Whether the cell is highlighted.
     */
    private boolean highlighted;

    /**
     * The row which the cell is in.
     */
    private int row;

    /**
     * The column which the cell is in.
     */
    private int col;


    /**
     * The constructor for Cell.
     *
     * @param row
     * @param col
     * @param value
     */
    Cell(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.solutionValue = value;
        this.editable = false;
        setFaceValue(value);
    }

    /**
     * Get the solution of the box.
     *
     * @return solutionValue
     */
    Integer getSolutionValue() {
        return solutionValue;
    }

    /**
     * Get the face value of the box.
     *
     * @return faceValue
     */
    Integer getFaceValue() {
        return faceValue;
    }

    /**
     * Set the face value of the box.
     *
     * @param faceValue the faceValue.
     */
    void setFaceValue(Integer faceValue) {
        this.faceValue = faceValue;

        if (this.highlighted) {
            background = R.drawable.sudoku_cell_red;
        } else
            // In order to distinguish different 3 X 3 grids
            // on the board, adjacent grids will have different
            // background colours.
            if (row / 3 == 0 || row / 3 == 2) {
                if (col / 3 == 1) {
                    background = R.drawable.sudoku_cell_grey;
                } else {
                    background = R.drawable.sudoku_cell_white;
                }
            } else {
                if (col / 3 == 0 || col / 3 == 2) {
                    background = R.drawable.sudoku_cell_grey;
                } else {
                    background = R.drawable.sudoku_cell_white;
                }
            }
    }

    /**
     * Returns whether the user successfully figured
     * out the value of the box.
     *
     * @return whether user user successfully figured
     * out the value of the box.
     */
    boolean checkValue() {
        return faceValue.equals(solutionValue);
    }

    /**
     * Returns whether the box is editable.
     *
     * @return whether the cell is editable
     */
    boolean isEditable() {
        return editable;
    }

    /**
     * Make the box editable.
     */
    void makeEditable() {
        this.editable = true;
    }

    /**
     * Returns the background of the cell.
     *
     * @return background value
     */
    int getBackground() {
        return background;
    }

    /**
     * Set the cell to be highlighted / regular.
     */
    void setHighlighted(boolean value) {
        highlighted = value;
    }

}
