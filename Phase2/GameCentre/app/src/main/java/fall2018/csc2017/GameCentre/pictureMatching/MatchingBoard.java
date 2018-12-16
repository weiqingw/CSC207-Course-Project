package fall2018.csc2017.GameCentre.pictureMatching;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import fall2018.csc2017.GameCentre.util.BoardForBoardGames;

/**
 * The sliding tiles board.
 */
public class MatchingBoard extends BoardForBoardGames implements Serializable, Iterable<PictureTile> {

    /**
     * The tiles on the board in row-major order.
     */
    private PictureTile[][] tiles;

    /**
     * Level of difficulty of the game.
     */
    private int difficulty;

    /**
     * Index of Column of the first tile selected.
     */
    private int col1 = -1;

    /**
     * Index of Column of the second tile selected.
     */
    private int col2 = -1;

    /**
     * Index of Row of the first tile selected.
     */
    private int row1 = -1;

    /**
     * Index of Row of the second tile selected.
     */
    private int row2 = -1;

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the list of tiles for the board
     */
    MatchingBoard(List<PictureTile> tiles, int difficulty) {
        this.difficulty = difficulty;
        this.tiles = new PictureTile[difficulty][difficulty];
        Iterator<PictureTile> iter = tiles.iterator();
        for (int row = 0; row != difficulty; row++) {
            for (int col = 0; col != difficulty; col++) {
                this.tiles[row][col] = iter.next();
            }
        }
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    public PictureTile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Getter function of index of column of the first tile selected.
     *
     * @return the col number of the first tile.
     */
    int getCol1() {
        return col1;
    }

    /**
     * Getter function of index of column of the second tile selected.
     *
     * @return the column number of the second tile.
     */
    int getCol2() {
        return col2;
    }

    /**
     * Flip the tile at the ith row and jth column.
     *
     * @param row the row number of the tile
     * @param col the column number of the tile.
     */
    void flipTile(int row, int col) {
        if (col1 == -1 && row1 == -1) {
            tiles[row][col].setState(PictureTile.FLIP);
            col1 = col;
            row1 = row;
        } else if (col2 == -1 && row2 == -1) {
            tiles[row][col].setState(PictureTile.FLIP);
            col2 = col;
            row2 = row;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Permanently flip the Tile if it has been solved.
     */
    void solveTile() {
        PictureTile tile1 = tiles[row1][col1];
        PictureTile tile2 = tiles[row2][col2];
        if (tile1.getId() == tile2.getId()) {
            tile1.setState(PictureTile.SOLVED);
            tile2.setState(PictureTile.SOLVED);
        } else {
            tile1.setState(PictureTile.COVERED);
            tile2.setState(PictureTile.COVERED);
        }
        this.col1 = this.row1 = this.col2 = this.row2 = -1;
        setChanged();
        notifyObservers();
    }

    /**
     * get the difficulty of the current board game.
     *
     * @return the difficulty of the game.
     */
    public int getDifficulty() {
        return difficulty;
    }

    @Override
    @NonNull
    public Iterator<PictureTile> iterator() {
        return new MatchingBoard.MatchingBoardIterator();
    }

    /**
     * The iterator class for the pictureTile.
     */
    public class MatchingBoardIterator implements Iterator<PictureTile> {
        int nextIndex = 0;

        @Override
        public boolean hasNext() {
            return nextIndex != difficulty * difficulty;
        }

        @Override
        public PictureTile next() {
            int row = nextIndex / difficulty;
            int col = nextIndex % difficulty;
            PictureTile tile = tiles[row][col];
            nextIndex++;
            return tile;
        }
    }
}
