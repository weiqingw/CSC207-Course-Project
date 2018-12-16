package fall2018.csc2017.GameCentre.sudoku;

import android.content.Context;

import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;

/**
 * The SudokuGameController class.
 */
public class SudokuGameController {
    /**
     * The database.
     */
    private SQLDatabase db;
    /**
     * The user.
     */
    private User user;
    /**
     * The gameStateFile.
     */
    private String gameStateFile;
    /**
     * The gameRunning.
     */
    private boolean gameRunning;
    /**
     * The tempGameStateFile.
     */
    private String tempGameStateFile;
    /**
     * The SudokuBoardManager.
     */
    private SudokuBoardManager boardManager;
    /**
     * The game name.
     */
    private static final String GAME_NAME = "Sudoku";


    /**
     * Constructor for SudokuGameController.
     *
     * @param context the context of the game activity
     * @param user the current user
     */
    SudokuGameController(Context context, User user){
        this.db = new SQLDatabase(context);
        this.user = user;
    }

    public void setDb(SQLDatabase db) {
        this.db = db;
    }

    /**
     * Return the user's file
     *
     * @return user's file
     */
    public String getUserFile(){
        return db.getUserFile(user.getUsername());
    }

    /**
     * Return whether game is still running.
     *
     * @return whether game is still running
     */
    boolean isGameRunning() {
        return gameRunning;
    }

    /**
     * Set gameRunning.
     *
     * @param gameRunning indicator of game running
     */
    void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    /**
     * Set boardManager.
     *
     * @param boardManager the board manager
     */
    public void setBoardManager(SudokuBoardManager boardManager) {
        this.boardManager = boardManager;
    }

    /**
     * Return gameStateFile.
     *
     * @return gameStateFile
     */
    String getGameStateFile() {
        return gameStateFile;
    }

    /**
     * Return tempGameStateFile.
     *
     * @return tempGameStateFile
     */
    String getTempGameStateFile() {
        return tempGameStateFile;
    }

    /**
     * Return boardManager.
     *
     * @return boardManager
     */
    public SudokuBoardManager getBoardManager() {
        return boardManager;
    }

    /**
     * Return SudokuBoard.
     *
     * @return SudokuBoard
     */
    public SudokuBoard getBoard(){
        return boardManager.getBoard();
    }

    /**
     * Return user.
     *
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * Set up file.
     */
    void setupFile(){
        if (!db.dataExists(user.getUsername(), GAME_NAME))
            db.addData(user.getUsername(), GAME_NAME);
        gameStateFile = db.getDataFile(user.getUsername(), GAME_NAME);
        tempGameStateFile = "temp_" + gameStateFile;
    }


    /**
     * Return time in String format.
     *
     * @param time time
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
     * Return current score.
     *
     * @param totalTimeTaken the total time taken
     * @return score the current score
     */
    Integer calculateScore(Long totalTimeTaken) {
        int timeInSec = totalTimeTaken.intValue() / 1000;
        return 10000 / (timeInSec) * SudokuBoardManager.getLevelOfDifficulty();
    }

    /**
     * Return whether update score succeed.
     *
     * @param score the current score
     * @return whether succeed
     */
    boolean updateScore(int score){
        boolean newRecord = user.updateScore(GAME_NAME, score);
        db.updateScore(user, GAME_NAME);
        return newRecord;
    }

    /**
     * Return whether board is solved.
     *
     * @return whether board is solved
     */
    public boolean boardSolved() {
        return boardManager.boardSolved();
    }

}
