package fall2018.csc2017.GameCentre.slidingTiles;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import fall2018.csc2017.GameCentre.util.BoardForBoardGames;

/**
 * The sliding tiles board.
 */
public class SlidingTilesBoard extends BoardForBoardGames implements Serializable, Iterable<Integer> {

    /**
     * Level of difficulty of the game.
     */
    private int difficulty;

    /**
     * The tiles on the board in row-major order.
     */
    private Integer[][] tiles;

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the tiles for the board
     */
    SlidingTilesBoard(List<Integer> tiles, int difficulty) {
        this.difficulty = difficulty;
        this.tiles = new Integer[difficulty][difficulty];
        Iterator<Integer> iterator = tiles.iterator();
        for (int row = 0; row != difficulty; row++) {
            for (int col = 0; col != difficulty; col++) {
                this.tiles[row][col] = iterator.next();
            }
        }
    }

    /**
     * Return the number of tiles on the board.
     * @return the number of tiles
     */
    int numTiles() {
        return difficulty * difficulty;
    }

    /**
     * Return the tile at (row, col)
     * @return tile
     */
    Integer getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Swap the tiles at (row1, col1) and (row2, col2)
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    void swapTiles(int row1, int col1, int row2, int col2) {
        int t = this.tiles[row1][col1];
        this.tiles[row1][col1] = this.tiles[row2][col2];
        this.tiles[row2][col2] = t;
        setChanged();
        notifyObservers();
    }

    /**
     * The getter function of level of difficulty.
     * @return difficulty
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the iterator of tiles.
     * @return iterator of Integer
     */
    @NonNull
    @Override
    public Iterator<Integer> iterator() {
        return new BoardIterator();
    }

    /**
     * The iterator class for board.
     * @deprecated BoardIterator
     */
    public class BoardIterator implements Iterator<Integer> {

        /**
         * The following index to be returned.
         */
        int nextIndex = 0;

        /**
         * Determines whether the iterator has reached the end.
         *
         * @return whether the iterator has reached the end
         */
        @Override
        public boolean hasNext() {
            return nextIndex != numTiles();
        }

        /**
         * Returns the next element in the iterator if available.
         * @return next item
         */
        @Override
        public Integer next() {
            int row = nextIndex / difficulty;
            int col = nextIndex % difficulty;
            nextIndex++;
            return tiles[row][col];
        }
    }
}
