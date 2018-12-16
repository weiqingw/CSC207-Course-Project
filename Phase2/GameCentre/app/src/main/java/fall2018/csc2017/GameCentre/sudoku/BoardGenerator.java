package fall2018.csc2017.GameCentre.sudoku;

import java.util.Random;

/**
 * The BoardGenerator class.
 */
class BoardGenerator {

    /**
     * The board (to be) generated.
     */
    private Integer[][] board = new Integer[9][9];


    /**
     * Constructor for the BoardGenerator Class.
     */
    BoardGenerator() {
        boardInitializer();
        for (int n = 0; n < 100; n++) {
            juniorBoardShuffler(false);
            seniorBoardShuffler(false);
        }
        for (int n = 0; n < 100; n++) {
            juniorBoardShuffler(true);
            seniorBoardShuffler(true);
        }
    }

    /**
     * Getter function for the board.
     *
     * @return 2d list of integers
     */
    Integer[][] getBoard() {
        return board;
    }

    /**
     * Initialize a board that satisfies the Sudoku requirements.
     */
    private void boardInitializer() {
        int boxValue;
        int firstBoxValue = 1;
        for (int row = 0; row < 9; row++) {
            boxValue = firstBoxValue;
            for (int column = 0; column < 9; column++) {
                if (boxValue <= 9) {
                    board[column][row] = boxValue;
                    boxValue++;
                } else {
                    boxValue = 1;
                    board[column][row] = boxValue;
                    boxValue++;
                }
            }
            firstBoxValue = boxValue + 3;
            if (boxValue == 10)
                firstBoxValue = 4;
            if (firstBoxValue > 9)
                firstBoxValue = (firstBoxValue % 9) + 1;
        }
    }

    /**
     * Shuffle the game board.
     *
     * @param switchRow whether switchRow or Column
     */
    private void juniorBoardShuffler(boolean switchRow) {
        int k1 = 0, k2 = 0;
        int startingIndex = 0;
        Random r = new Random();
        for (int i = 0; i < 3; i++) {
            while (k1 == k2) {
                k1 = r.nextInt(3) + startingIndex;
                k2 = r.nextInt(3) + startingIndex;
            }
            if (switchRow)
                switchRows(k1, k2);
            else
                switchColumns(k1, k2);
            startingIndex += 3;
        }
    }

    /**
     * Further Shuffle the GameBoard.
     *
     * @param switchHorizontalGroup determines whether vertical groups are switched or vertical
     *                              are switched
     */
    private void seniorBoardShuffler(boolean switchHorizontalGroup) {
        Random r = new Random();
        int k1;
        int k2;
        if (switchHorizontalGroup) {
            k1 = r.nextInt(3) + 1;
            k2 = r.nextInt(3) + 1;
            while (k1 == k2) {
                k1 = r.nextInt(3) + 1;
                k2 = r.nextInt(3) + 1;
            }
            switchVerticalGroups(k1, k2);
        } else {
            k1 = r.nextInt(3) + 1;
            k2 = r.nextInt(3) + 1;
            while (k1 == k2) {
                k1 = r.nextInt(3) + 1;
                k2 = r.nextInt(3) + 1;
            }
            switchHorizontalGroups(k1, k2);
        }
    }

    /**
     * Switch two rows on the game board.
     *
     * @param row1 the first row
     * @param row2 the second row
     */
    private void switchRows(int row1, int row2) {
        int cache;
        for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
            cache = board[row1][columnIndex];
            board[row1][columnIndex] = board[row2][columnIndex];
            board[row2][columnIndex] = cache;
        }
    }

    /**
     * Switch two columns on the game board.
     *
     * @param col1 the first column.
     * @param col2 the second column.
     */
    private void switchColumns(int col1, int col2) {
        int cache;
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            cache = board[rowIndex][col1];
            board[rowIndex][col1] = board[rowIndex][col2];
            board[rowIndex][col2] = cache;
        }
    }

    /**
     * Switch two groups of rows in the game board.
     *
     * @param group1 the first group.
     * @param group2 the second group.
     */
    private void switchHorizontalGroups(int group1, int group2) {
        int row1 = 3 * group1 - 3;
        int row2 = 3 * group2 - 3;
        int cache;
        for (int n = 1; n <= 3; n++) {
            for (int columnIndex = 0; columnIndex < 9; columnIndex++) {
                cache = board[row1][columnIndex];
                board[row1][columnIndex] = board[row2][columnIndex];
                board[row2][columnIndex] = cache;
            }
            row1++;
            row2++;
        }
    }

    /**
     * Switch two groups of columns in the game board.
     *
     * @param group1 the first group.
     * @param group2 the second group.
     */
    private void switchVerticalGroups(int group1, int group2) {
        int row1 = 3 * group1 - 3;
        int row2 = 3 * group2 - 3;
        int cache;
        for (int n = 1; n <= 3; n++) {
            for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
                cache = board[rowIndex][row1];
                board[rowIndex][row1] = board[rowIndex][row2];
                board[rowIndex][row2] = cache;
            }
            row1++;
            row2++;
        }
    }
}
