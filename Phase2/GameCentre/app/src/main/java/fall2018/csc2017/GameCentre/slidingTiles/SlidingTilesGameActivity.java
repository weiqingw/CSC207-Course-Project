package fall2018.csc2017.GameCentre.slidingTiles;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import static android.graphics.Bitmap.createBitmap;

/**
 * The game activity.
 */
public class SlidingTilesGameActivity extends AppCompatActivity implements Observer {

    /**
     * Controller of the game.
     */
    private SlidingTilesGameController logicalController;

    /**
     * The time and steps user has taken to be displayed on the user interface.
     */
    private TextView timeDisplay, displayStep;

    /**
     * Grid View for the game.
     */
    private GestureDetectGridView gridView;

    /**
     * Calculated column height and width based on device size.
     */
    private static int columnWidth, columnHeight;

    /**
     * The name of the game.
     */
    private static final String GAME_NAME = "SlidingTiles";

    /**
     * The time user started to play the game.
     */
    private LocalTime startingTime;

    /**
     * The time user took to play the game in total.
     */
    private Long totalTimeTaken;

    /**
     * Warning message
     */
    private TextView warning;
    /**
     * The list of buttons on the gridView.
     */
    private List<Button> tileButtons;
    /**
     * The formatted picture that will be used as the buttons' backgrounds.
     */
    private Bitmap[] tileImages;
    /**
     * The background image of the game.
     */
    private Bitmap backgroundImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startingTime = LocalTime.now();
        setupController();
        loadFromFile();
        createTileButtons();
        setContentView(R.layout.activity_main);
        setupTime();
        setUpStep();
        addGridViewToActivity();
        addUndoButtonListener();
        addWarningTextViewListener();
        addStepDisplayListener();
        setupTileImagesAndBackground();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayStep.setText(String.format("%s", "Steps: " +
                Integer.toString(logicalController.getSteps())));
        timeDisplay.setText(logicalController.
                convertTime(logicalController.getBoardManager().getTimeTaken()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(logicalController.getTempGameStateFile());
        saveToFile(logicalController.getGameStateFile());
    }

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    /**
     * Set up the logicalController for the game.
     */
    private void setupController() {
        logicalController = new SlidingTilesGameController(this,
                (User) getIntent().getSerializableExtra("user"));
        logicalController.setupFile();
    }

    /**
     * Setup the initial step base on the record in the Board's manager.
     */
    private void setUpStep() {
        displayStep = findViewById(R.id.stepDisplayTextView);
        logicalController.setupSteps();
        displayStep.setText(String.format("%s", "Steps: " +
                Integer.toString(logicalController.getSteps())));
    }

    /**
     * Setup the initial time based on the record in the board's manager.
     */
    private void setupTime() {
        if (!logicalController.gameFinished())
            logicalController.setGameRunning(true);
        Timer timer = new Timer();
        final long preStartTime = logicalController.getBoardManager().getTimeTaken();
        totalTimeTaken = preStartTime;
        timeDisplay = findViewById(R.id.time_display_view);
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
     * Set up the warning message displayed on the UI.
     */
    private void addWarningTextViewListener() {
        warning = findViewById(R.id.warningTextView);
        warning.setVisibility(View.INVISIBLE);
    }

    /**
     * Set up the step display textView
     */
    @SuppressLint("SetTextI18n")
    private void addStepDisplayListener() {
        displayStep = findViewById(R.id.stepDisplayTextView);
        displayStep.setText("Step: 0");
    }

    /**
     * Setup the GridView where the tiles are located
     */
    private void addGridViewToActivity() {
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(logicalController.getBoard().getDifficulty());
        gridView.setBoardManager(logicalController.getBoardManager());
        logicalController.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        columnWidth = (gridView.getMeasuredWidth() /
                                logicalController.getBoard().getDifficulty());
                        columnHeight = (gridView.getMeasuredHeight() /
                                logicalController.getBoard().getDifficulty());
                        display();
                    }
                });
    }

    /**
     * Set up the performUndo button.
     */
    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.undo_button);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (!logicalController.performUndo()) {
                    warning.setText("Exceeds Undo-Limit!");
                    warning.setVisibility(View.VISIBLE);
                    warning.setError("Exceeds Undo-Limit! ");
                    displayStep.setVisibility(View.INVISIBLE);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            warning.setVisibility(View.INVISIBLE);
                            displayStep.setVisibility(View.VISIBLE);
                        }
                    }, 1000);
                }
            }
        });
    }

    /**
     * Set up the tile images and background.
     */
    void setupTileImagesAndBackground() {
        tileImages = new Bitmap[logicalController.getBoardManager().getDifficulty() *
                logicalController.getBoardManager().getDifficulty()];
        try {
            byte[] tmpImage = logicalController.getBoardManager().getImageBackground();
            backgroundImage = BitmapFactory.decodeByteArray(tmpImage, 0, tmpImage.length);
            imageConverter();
        } catch (Exception e) {
            integerConverter();
        }
    }

    /**
     * Convert image to multiple button-size images which could
     * be used and tile button backgrounds.
     */
    private void imageConverter() {
        int width = backgroundImage.getWidth();
        int height = backgroundImage.getHeight();
        int count = 0;
        for (int i = 0; i < logicalController.getBoardManager().getDifficulty(); i++) {
            for (int j = 0; j < logicalController.getBoardManager().getDifficulty(); j++) {
                tileImages[count++] = createBitmap(backgroundImage,
                        i * (width / logicalController.getBoardManager().getDifficulty()),
                        j * (height / logicalController.getBoardManager().getDifficulty()),
                        width / logicalController.getBoardManager().getDifficulty(),
                        height / logicalController.getBoardManager().getDifficulty(),
                        null,
                        false);
            }
        }
        tileImages[logicalController.getBoardManager().getDifficulty() *
                logicalController.getBoardManager().getDifficulty() - 1]
                = BitmapFactory.decodeResource(getResources(), R.drawable.tile_empty);
    }

    /**
     * Converts integer numbers to tile images.
     */
    private void integerConverter() {
        for (int i = 0; i < logicalController.getBoardManager().getDifficulty() *
                logicalController.getBoardManager().getDifficulty(); i++) {
            String name = "tile_" + Integer.toString(i + 1);
            int numImage = getResources().getIdentifier(name, "drawable", getPackageName());
            tileImages[i] = BitmapFactory.decodeResource(getResources(), numImage);
        }
        tileImages[logicalController.getBoardManager().getDifficulty() *
                logicalController.getBoardManager().getDifficulty() - 1]
                = BitmapFactory.decodeResource(getResources(), R.drawable.tile_empty);
    }

    /**
     * Set up the tile buttons of the game.
     */
    void createTileButtons() {
        tileButtons = new ArrayList<>();
        for (int row = 0; row != logicalController.getBoard().getDifficulty(); row++) {
            for (int col = 0; col != logicalController.getBoard().getDifficulty(); col++) {
                Button tmp = new Button(this);
                tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the tile buttons on the board.
     */
    void updateTileButtons() {
        SlidingTilesBoard board = logicalController.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / logicalController.getBoard().getDifficulty();
            int col = nextPos % logicalController.getBoard().getDifficulty();
            int tile_id = board.getTile(row, col);
            b.setBackground(new BitmapDrawable(this.getResources(), tileImages[tile_id - 1]));
            nextPos++;
        }
    }

    /**
     * Activate the pop window.
     *
     * @param score     score get
     * @param newRecord new record
     */
    private void popScoreWindow(Integer score, boolean newRecord) {
        Intent goToPopWindow = new Intent(getApplication(), popScore.class);
        goToPopWindow.putExtra("score", score);
        goToPopWindow.putExtra("user", logicalController.getUser());
        goToPopWindow.putExtra("gameType", GAME_NAME);
        goToPopWindow.putExtra("newRecord", newRecord);
        startActivity(goToPopWindow);
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
        logicalController.setSteps(logicalController.getSteps() + 1);
        displayStep.setText(String.format("%s", "Steps: " +
                Integer.toString(logicalController.getSteps())));
        if (logicalController.gameFinished()) {
            Toast.makeText(this, "YOU WIN!", Toast.LENGTH_SHORT).show();
            Integer score = logicalController.calculateScore(totalTimeTaken);
            boolean newRecord = logicalController.updateScore(score);
            saveToFile(logicalController.getUserFile());
            logicalController.setGameRunning(false);
            popScoreWindow(score, newRecord);
        }
    }

    /**
     * Load the saved Board Manager from fire base.
     */
    public void loadFromFile() {
        try {
            InputStream inputStream = this.openFileInput(logicalController.getTempGameStateFile());
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                logicalController.setBoardManager((SlidingTilesBoardManager) input.readObject());
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
            } else if (fileName.equals(logicalController.getGameStateFile()) ||
                    fileName.equals(logicalController.getTempGameStateFile())) {
                outputStream.writeObject(logicalController.getBoardManager());
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
