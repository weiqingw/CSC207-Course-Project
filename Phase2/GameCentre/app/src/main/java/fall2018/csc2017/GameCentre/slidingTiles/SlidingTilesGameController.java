package fall2018.csc2017.GameCentre.slidingTiles;

import android.content.Context;

import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;

public class SlidingTilesGameController {

    /**
     * The database for game info.
     */
    private SQLDatabase db;

    /**
     * The user of the game.
     */
    private User user;

    /**
     * The file name of the game state file to be output.
     */
    private String gameStateFile, tempGameStateFile;

    /**
     * The number of steps which the user has taken.
     */
    private int steps;

    /**
     * The status of the game.
     */
    private boolean gameRunning;

    /**
     * The board's manager.
     */
    private SlidingTilesBoardManager boardManager;

    /**
     * The name of the game.
     */
    private static final String GAME_NAME = "SlidingTiles";


    /**
     * Constructor of SlidingTilesGameController class.
     */
    SlidingTilesGameController(Context context, User user) {
        this.db = new SQLDatabase(context);
        this.user = user;
    }

    /**
     * Getter function for the file of the user in the database.
     *
     * @return user file
     */
    public String getUserFile() {
        return db.getUserFile(user.getUsername());
    }

    /**
     * Returns the user file of the current user of the game.
     *
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns whether the user has successfully finished the task of the game.
     *
     * @return whether the game solved
     */
    boolean gameFinished() {
        return boardManager.boardSolved();
    }

    /**
     * The getter function of the Board's manager.
     *
     * @return SlidingTilesBoardManager
     */
    public SlidingTilesBoardManager getBoardManager() {
        return boardManager;
    }

    /**
     * Returns the board of the game.
     *
     * @return SlidingTilesBoard
     */
    public SlidingTilesBoard getBoard() {
        return boardManager.getBoard();
    }

    /**
     * Returns the steps that has been taken by the user.
     *
     * @return steps the steps taken
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Setter function of the steps taken by the user.
     * Recorded data in the board's manager is also updated.
     *
     * @param steps the steps taken
     */
    public void setSteps(int steps) {
        this.steps = steps;
        boardManager.setStepsTaken(steps);
    }

    /**
     * Setter function of the boardManager.
     *
     * @param boardManager the board manager
     */
    public void setBoardManager(SlidingTilesBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    /**
     * Set the database of the controller.
     *
     * @param db the database
     */
    public void setDb(SQLDatabase db) {
        this.db = db;
    }

    /**
     * Getter function of the name of the file of game state.
     *
     * @return gameStateFile
     */
    String getGameStateFile() {
        return gameStateFile;
    }

    /**
     * Getter function of the name of the temporary file of game state.
     *
     * @return tempGameStateFile
     */
    String getTempGameStateFile() {
        return tempGameStateFile;
    }

    /**
     * The setter function for gameRunning instance.
     *
     * @param gameRunning if the game is still running
     */
    void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    /**
     * Return whether the game is running.
     */
    boolean isGameRunning() {
        return gameRunning;
    }

    /**
     * Set up the file name for storing the game state.
     */
    void setupFile() {
        if (!db.dataExists(user.getUsername(), GAME_NAME))
            db.addData(user.getUsername(), GAME_NAME);
        gameStateFile = db.getDataFile(user.getUsername(), GAME_NAME);
        tempGameStateFile = "temp_" + gameStateFile;
    }

    /**
     * Set the steps taken recorded in the activity to the number
     * recorded by the board's manager.
     */
    void setupSteps() {
        this.steps = boardManager.getStepsTaken();
    }

    /**
     * Convert the time into the format of HH:MM:SS
     *
     * @param time time taken
     * @return string format of time
     */
    String convertTime(long time) {
        Integer hour = (int) (time / 3600000);
        Integer min = (int) ((time % 3600000) / 60000);
        Integer sec = (int) ((time % 3600000 % 60000) / 1000);
        String hourStr = hour.toString();
        String minStr = min.toString();
        String secStr = sec.toString();
        if (hour < 10) {
            hourStr = "0" + hourStr;
        }
        if (min < 10) {
            minStr = "0" + minStr;
        }
        if (sec < 10) {
            secStr = "0" + secStr;
        }
        return hourStr + ":" + minStr + ":" + secStr;
    }

    /**
     * Calculate and return the score of the user.
     *
     * @param totalTimeTaken
     * @return score
     */
    Integer calculateScore(Long totalTimeTaken) {
        int timeInSec = totalTimeTaken.intValue() / 1000;
        return 10000 / (steps + timeInSec) * (boardManager.getDifficulty() - 2);
    }

    /**
     * Update the user's score to the database.
     *
     * @param score the score
     * @return score
     */
    boolean updateScore(int score) {
        boolean newRecord = user.updateScore(GAME_NAME, score);
        db.updateScore(user, GAME_NAME);
        return newRecord;
    }

    /**
     * Performs undo action.
     *
     * @return Whether undo action is successful.
     */
    boolean performUndo() {
        if (boardManager.undoAvailable()) {
            boardManager.move(boardManager.popUndo());
            return true;
        } else {
            return false;
        }
    }
}