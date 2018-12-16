package fall2018.csc2017.GameCentre.pictureMatching;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.util.CustomAdapter;
import fall2018.csc2017.GameCentre.util.GestureDetectGridView;
import fall2018.csc2017.GameCentre.util.popScore;

public class PictureMatchingGameActivity extends AppCompatActivity implements Observer {
    /**
     * TextView for displaying time.
     */
    private TextView timeDisplay;
    /**
     * Grid View and calculated column height and width based on device size
     */
    private GestureDetectGridView gridView;
    /**
     * column width and height of each row and column of gridView
     */
    private static int columnWidth, columnHeight;
    /**
     * The name of the current game.
     */
    private static final String GAME_NAME = "PictureMatch";
    /**
     * Current User.
     */
    private LocalTime startingTime;
    /**
     * the total time
     */
    private Long totalTimeTaken;
    /**
     * Controller object for this activity
     */
    private PictureMatchingGameController logicalController;
    /**
     * A collection of buttons that is to be manipulated and displayed
     */
    private List<Button> tileButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startingTime = LocalTime.now();
        setupController();
        loadFromFile();
        createTileButtons();
        setContentView(R.layout.activity_picturematching_game);
        setupTime();
        addGridViewToActivity();
    }


    /**
     * Create and setup logicalController
     */
    private void setupController() {
        logicalController = new PictureMatchingGameController(this, (User) getIntent().getSerializableExtra("user"));
        logicalController.setupFile();
    }


    /**
     * Time counting, setup initial time based on the record in boardmanager
     */
    private void setupTime() {
        if (!logicalController.boardSolved())
            logicalController.setGameRunning(true);
        Timer timer = new Timer();
        final Long preStartTime = logicalController.getBoardManager().getTimeTaken();
        timeDisplay = findViewById(R.id.time_display_view_in_picturematching);
        totalTimeTaken = preStartTime;
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                long time = Duration.between(startingTime, LocalTime.now()).toMillis();
                if (logicalController.isGameRunning()) {
                    totalTimeTaken = time + preStartTime;
                    timeDisplay.setText(logicalController.convertTime(totalTimeTaken));
                    logicalController.getBoardManager().setTimeTaken(totalTimeTaken);
                }
            }
        };
        timer.schedule(task2, 0, 1000);
    }


    /**
     * Setup the gridview where the tiles are located
     */
    private void addGridViewToActivity() {
        gridView = findViewById(R.id.PictureMatchingGrid);
        gridView.setNumColumns(logicalController.getBoardManager().getDifficulty());
        gridView.setBoardManager(logicalController.getBoardManager());
        logicalController.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();
                        columnWidth = (displayWidth / logicalController.getBoardManager().getDifficulty());
                        columnHeight = (displayHeight / logicalController.getBoardManager().getDifficulty());
                        display();
                    }
                });
    }

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String text = "Time: " + logicalController.convertTime(logicalController.getBoardManager().getTimeTaken());
        timeDisplay.setText(text);
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(logicalController.getTempGameStateFile());
        saveToFile(logicalController.getGameStateFile());
    }


    @Override
    public void update(Observable o, Object arg) {
        setupTimeDelay();
        display();
        if (logicalController.boardSolved()) {
            Toast.makeText(PictureMatchingGameActivity.this, "YOU WIN!", Toast.LENGTH_SHORT).show();
            Integer score = logicalController.calculateScore(totalTimeTaken);
            boolean newRecord = logicalController.updateScore(score);
            saveToFile(logicalController.getUserFile());
            logicalController.setGameRunning(false);
            popScoreWindow(score, newRecord);
        }
    }

    /**
     * A time delay of 0.5 second for the pictures to turn over
     */
    private void setupTimeDelay() {
        if (logicalController.getBoardManager().check2tiles()) {
            final android.os.Handler handler = new android.os.Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (logicalController.getBoardManager().check2tiles())
                            logicalController.getBoardManager().solveTile();
                    } catch (Exception e) {
                        Toast.makeText(getApplication(), "slow down!", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 500);
        }
    }


    /**
     * Pop up window that shows user the score he/she gets
     *
     * @param score     Score that is to be displayed on popup window
     * @param newRecord Indicator that determines which text is to be displayed (New Record: or
     *                  Your Highest Score Was
     */
    private void popScoreWindow(Integer score, boolean newRecord) {
        Intent goToPopWindow = new Intent(getApplication(), popScore.class);
        goToPopWindow.putExtra("score", score);
        goToPopWindow.putExtra("user", logicalController.getUser());
        goToPopWindow.putExtra("gameType", GAME_NAME);
        goToPopWindow.putExtra("newRecord", newRecord);
        startActivity(goToPopWindow);
    }

    /**
     * create the tile buttons for displaying.
     */
    void createTileButtons() {
        tileButtons = new ArrayList<>();
        for (int row = 0; row != logicalController.getBoardManager().getBoard().getDifficulty(); row++) {
            for (int col = 0; col != logicalController.getBoardManager().getBoard().getDifficulty(); col++) {
                Button tmp = new Button(this);
                tmp.setBackgroundResource(R.drawable.picturematching_tile_back);
                tileButtons.add(tmp);
            }
        }
    }

    /**
     * update the tileButtons after make a move.
     */
    void updateTileButtons() {
        MatchingBoard board = logicalController.getBoardManager().getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / logicalController.getBoardManager().getDifficulty();
            int col = nextPos % logicalController.getBoardManager().getDifficulty();
            PictureTile currentTile = board.getTile(row, col);
            switch (currentTile.getState()) {
                case PictureTile.FLIP:
                    String name = "pm_" + logicalController.getBoardManager().getTheme() + "_" + Integer.toString(currentTile.getId());
                    int id = getResources().getIdentifier(name, "drawable", getPackageName());
                    b.setBackgroundResource(id);
                    break;
                case PictureTile.COVERED:
                    b.setBackgroundResource(R.drawable.picturematching_tile_back);
                    break;
                case PictureTile.SOLVED:
                    b.setBackgroundResource(R.drawable.picturematching_tile_done);
                    break;
            }
            nextPos++;
        }
    }

    /**
     * load the boardManager from the file.
     */
    public void loadFromFile() {
        try {
            InputStream inputStream = this.openFileInput(logicalController.getTempGameStateFile());
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                logicalController.setBoardManager((MatchingBoardManager) input.readObject());
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            if (fileName.equals(logicalController.getUserFile())) {
                outputStream.writeObject(logicalController.getUser());
            } else if (fileName.equals(logicalController.getGameStateFile()) || fileName.equals(logicalController.getTempGameStateFile())) {
                outputStream.writeObject(logicalController.getBoardManager());
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
