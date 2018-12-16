package fall2018.csc2017.GameCentre.pictureMatching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import fall2018.csc2017.GameCentre.util.BoardManagerForBoardGames;


/**
 * Manage a board for Picture Matching Game..
 */
public class MatchingBoardManager extends BoardManagerForBoardGames implements Serializable {

    /**
     * The board being managed.
     */
    private MatchingBoard board;

    /**
     * The time has taken so far.
     */
    private long timeTaken;

    /**
     * the theme of the board.
     */
    private String theme;


    /**
     * Constructor of MatchingBoardManager
     *
     * @param difficulty Level of difficulty determined by user
     * @param theme      theme of background (number, animal or emoji)
     */
    public MatchingBoardManager(int difficulty, String theme) {
        this.theme = theme;
        List<PictureTile> tiles = new ArrayList<>();
        final int numTiles = difficulty * difficulty;
        for (int tileNum = 0; tileNum < numTiles; tileNum++) {
            addPictureTileToList(tiles, numTiles, tileNum);
        }
        Collections.shuffle(tiles);
        this.board = new MatchingBoard(tiles, difficulty);
    }

    /**
     * Add pictureTiles to the list.
     *
     * @param tiles    the tiles List that contains pictureTile.
     * @param numTiles the id of the picture Tile.
     * @param tileNum  the Total number of tiles need to be added.
     */
    void addPictureTileToList(List<PictureTile> tiles, int numTiles, int tileNum) {
        if (tileNum < numTiles / 4) {
            tiles.add(new PictureTile(tileNum + 1));
        } else if (tileNum >= numTiles / 4 && tileNum < numTiles / 2) {
            tiles.add(new PictureTile(tileNum - numTiles / 4 + 1));
        } else if (tileNum >= numTiles / 2 && tileNum < (numTiles * 3) / 4) {
            tiles.add(new PictureTile(tileNum - numTiles / 2 + 1));
        } else {
            tiles.add(new PictureTile(tileNum - (numTiles * 3) / 4 + 1));
        }
    }

    /**
     * Board setter
     *
     * @param board the board to be set.
     */
    public void setBoard(MatchingBoard board) {
        this.board = board;
    }

    /**
     * Board getter
     *
     * @return current board
     */
    public MatchingBoard getBoard() {
        return board;
    }

    /**
     * getter of difficulty
     *
     * @return current board's difficulty
     */
    int getDifficulty() {
        return board.getDifficulty();
    }

    /**
     * theme getter
     *
     * @return the theme of the current board game.
     */
    public String getTheme() {
        return theme;
    }

    /**
     * timeTaken getter
     *
     * @return time
     */
    public long getTimeTaken() {
        return timeTaken;
    }

    /**
     * timeTaken setter
     *
     * @param timeTakenSoFar new time taken so far after a game is finished or paused
     */
    public void setTimeTaken(long timeTakenSoFar) {
        this.timeTaken = timeTakenSoFar;
    }

    /**
     * Return true if all pictureTiles are solved.
     *
     * @return whether the tiles are all solved.
     */
    public boolean boardSolved() {
        Iterator<PictureTile> itr = board.iterator();
        for (int i = 1; i <= (board.getDifficulty() * board.getDifficulty()); i++) {
            if (itr.hasNext() && !itr.next().getState().equals(PictureTile.SOLVED)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    public boolean isValidTap(int position) {
        int row = position / board.getDifficulty();
        int col = position % board.getDifficulty();
        PictureTile currentTile = this.board.getTile(row, col);
        return !currentTile.getState().equals(PictureTile.SOLVED)
                && !currentTile.getState().equals(PictureTile.FLIP);
    }

    /**
     * whether the board is solved.
     */
    void solveTile() {
        board.solveTile();
    }

    /**
     * Performs changes to the board.
     *
     * @param position the position that the user clicked on the grid view
     */
    public void makeMove(int position) {
        int row = position / board.getDifficulty();
        int col = position % board.getDifficulty();
        board.flipTile(row, col);
        if (check2tiles()) {
            notifyObservers();
        }
    }

    /**
     * check whether two tiles are flipped.
     *
     * @return true if two tiles are flipped else false.
     */
    boolean check2tiles() {
        int col1 = board.getCol1();
        int col2 = board.getCol2();
        return col1 != -1 && col2 != -1;
    }
}
