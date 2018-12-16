package fall2018.csc2017.GameCentre.pictureMatching;

import android.content.Context;

import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;


public class PictureMatchingGameController {

    /**
     * Database that stores user file address and game state
     */
    private SQLDatabase db;
    /**
     * User object of current user
     */
    private User user;
    /**
     * File that saves serialized BoardManager Object
     */
    private String gameStateFile;
    /**
     * Game status, true if game is solved, false otherwise
     */
    private boolean gameRunning;
    /**
     * File that saves serialized BoardManager Object
     */
    private String tempGameStateFile;
    /**
     * current BoardManager
     */
    private MatchingBoardManager boardManager;
    /**
     * Name of current game
     */
    private static final String GAME_NAME = "PictureMatch";

    /**
     * Constructor of the controller class
     *
     * @param context MatchingPictureGameActivity
     * @param user    user object of current user
     */
    PictureMatchingGameController(Context context, User user) {
        this.db = new SQLDatabase(context);
        this.user = user;
    }

    /**
     * set the database
     *
     * @param db the new database.
     */
    public void setDb(SQLDatabase db) {
        this.db = db;
    }


    /**
     * Used to determine whether the timer should keep counting
     *
     * @return true of game is not ended (board is not solved), false otherwise
     */
    boolean isGameRunning() {
        return gameRunning;
    }

    /**
     * set whether the game is running.
     *
     * @param gameRunning state of game, whether game is still running
     */
    void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    /**
     * set boardManager to a new boardManager.
     *
     * @param boardManager the new boardManager that we want to assign to.
     */
    public void setBoardManager(MatchingBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    /**
     * get the game state file
     *
     * @return the string of game state file.
     */
    String getGameStateFile() {
        return gameStateFile;
    }

    /**
     * get the temporary game state file.
     *
     * @return the the temporary game state file.
     */
    String getTempGameStateFile() {
        return tempGameStateFile;
    }

    /**
     * get boardManager
     *
     * @return boardManager
     */
    public MatchingBoardManager getBoardManager() {
        return boardManager;
    }

    /**
     * get the board of the boardManager.
     *
     * @return the board of the boardManager.
     */
    public MatchingBoard getBoard() {
        return boardManager.getBoard();
    }

    /**
     * get the User object that store the information of the current user.
     *
     * @return the User object that represent the current user.
     */
    public User getUser() {
        return user;
    }

    /**
     * get the user file
     *
     * @return the user file.
     */
    String getUserFile() {
        return db.getUserFile(user.getUsername());
    }

    /**
     * set up the game state file.
     */
    void setupFile() {
        if (!db.dataExists(user.getUsername(), GAME_NAME))
            db.addData(user.getUsername(), GAME_NAME);
        gameStateFile = db.getDataFile(user.getUsername(), GAME_NAME);
        tempGameStateFile = "temp_" + gameStateFile;
    }


    /**
     * covert the time to proper format.
     *
     * @param time the time that we want to convert.
     * @return the converted time.
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
     * calculate the score according to the totalTimeTaken.
     *
     * @param totalTimeTaken the time for calculating score.
     * @return the calculated score.
     */
    Integer calculateScore(Long totalTimeTaken) {
        int timeInSec = totalTimeTaken.intValue() / 1000;
        return 10000 / (timeInSec) * (boardManager.getDifficulty() - 1);
    }

    /**
     * update the score in user object and database.
     *
     * @param score the score that we wanted to store.
     * @return whether the score is updated.
     */
    boolean updateScore(int score) {
        boolean newRecord = user.updateScore(GAME_NAME, score);
        db.updateScore(user, GAME_NAME);
        return newRecord;
    }

    /**
     * whether the board is solved.
     *
     * @return whether the board is solved,
     */
    public boolean boardSolved() {
        return boardManager.boardSolved();
    }
}
