package fall2018.csc2017.GameCentre.sudoku;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.util.CustomAdapter;
import fall2018.csc2017.GameCentre.util.GestureDetectGridView;
import fall2018.csc2017.GameCentre.util.popScore;

/**
 * The SudokuGameActivity class.
 */
public class SudokuGameActivity extends AppCompatActivity implements Observer {

    /**
     * Controller object for this activity
     */
    private SudokuGameController logicalController;
    /**
     * TextView for displaying time
     */
    private TextView timeDisplay;
    /**
     * GridView for displaying cells
     */
    private GestureDetectGridView gridView;
    /**
     * column width and height of each row and column of gridView
     */
    private static int columnWidth, columnHeight;
    /**
     * Game name of current game
     */
    private static final String GAME_NAME = "Sudoku";
    /**
     * Time when the game starts or loads
     */
    private LocalTime startingTime;
    /**
     * Time loaded from previous saved game
     */
    private Long preStartTime = 0L;
    /**
     * Total time taken before the board is solved
     */
    private Long totalTimeTaken;
    /**
     * Warning message TextView Display
     */
    private TextView warning;
    /**
     * Hint TextView Display
     */
    private TextView hintText;
    /**
     * List of Buttons (from 1-9) for number input
     */
    private Button[] buttons;
    /**
     * The list of cell buttons.
     */
    private List<Button> cellButtons;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startingTime = LocalTime.now();
        setupController();
        loadFromFile();
        createCellButton();
        setContentView(R.layout.activity_sudoku_game);
        setupTime();

        addGridViewToActivity();
        setUpHintDisplay();
        setUpButtons();
        addWarningTextViewListener();
        addClearButtonListener();
        addUndoButtonListener();
        addEraseButtonListener();
        addHintButtonListener();
    }

    /**
     * Create and setup logicalController
     */
    private void setupController() {
        logicalController = new SudokuGameController(this, (User) getIntent().getSerializableExtra("user"));
        logicalController.setupFile();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String text = "Time: " + logicalController.convertTime(logicalController.getBoardManager().getTimeTaken());
        timeDisplay.setText(text);
    }

    /**
     * Set up hint display.
     */
    private void setUpHintDisplay() {
        hintText = findViewById(R.id.hintTextView);
        String hintDisplay = "Hint: " + String.valueOf(logicalController.getBoardManager().getHintAvailable());
        hintText.setText(hintDisplay);
    }

    /**
     * Set up all buttons.
     */
    private void setUpButtons() {
        LinearLayout numLayout = findViewById(R.id.numButtons);

        buttons = new Button[9];
        for (int tmp = 0; tmp < buttons.length; tmp++) {
            buttons[tmp] = new Button(this);
            buttons[tmp].setId(1800 + tmp);
            buttons[tmp].setText(String.format("%s", Integer.toString(tmp + 1)));

            RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams(100, 50);
            btParams.leftMargin = 3;
            btParams.topMargin = 5;
            btParams.width = 115;
            btParams.height = 115;
            numLayout.addView(buttons[tmp], btParams);

            buttons[tmp].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int tmp = 0; tmp < buttons.length; tmp++) {
                        if (v == buttons[tmp] && logicalController.isGameRunning())
                            logicalController.getBoardManager().updateValue(tmp + 1, false);
                    }
                }
            });
        }
    }

    /**
     * Activate Clear button
     */
    private void addClearButtonListener() {
        Button clearButton = findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logicalController.isGameRunning()) {
                    while (logicalController.getBoardManager().undoAvailable()) {
                        logicalController.getBoardManager().undo();
                    }
                    Cell currentCell = logicalController.getBoardManager().getCurrentCell();
                    if (currentCell != null) {
                        currentCell.setHighlighted(false);
                        currentCell.setFaceValue(currentCell.getFaceValue());
                        logicalController.getBoardManager().setCurrentCell(null);
                    }
                    display();
                }
            }
        });
    }

    /**
     * Activate Undo button
     */
    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.sudoku_undo_button);
        warning.setError("Exceeds Undo-Limit! ");
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logicalController.isGameRunning()) {
                    if (logicalController.getBoardManager().undoAvailable())
                        logicalController.getBoardManager().undo();
                    else
                        displayWarning("Exceeds Undo-Limit!");
                }
            }
        });
    }


    /**
     * Set up the erase button listener.
     */
    private void addEraseButtonListener() {
        Button eraseButton = findViewById(R.id.eraseButton);
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logicalController.isGameRunning()) {
                    if (logicalController.getBoardManager().getCurrentCell() != null &&
                            logicalController.getBoardManager().getCurrentCell().getFaceValue() != 0)
                        logicalController.getBoardManager().updateValue(0, false);
                    display();
                }
            }
        });
    }

    /**
     * When Hint button is taped, the solution will display on the selected cell.
     */
    private void addHintButtonListener() {
        final Button hintButton = findViewById(R.id.hintButton);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cell currentCell = logicalController.getBoardManager().getCurrentCell();
                if (logicalController.getBoardManager().getHintAvailable() > 0 &&
                        logicalController.isGameRunning()) {
                    if (currentCell != null &&
                            !currentCell.getFaceValue().equals(currentCell.getSolutionValue())) {
                        logicalController.getBoardManager().updateValue(currentCell.getSolutionValue(),
                                false);
                        logicalController.getBoardManager().reduceHint();
                        String hintDisplay = "Hint: " +
                                String.valueOf(logicalController.getBoardManager().getHintAvailable());
                        hintText.setText(hintDisplay);
                    }
                } else {
                    if(logicalController.isGameRunning())
                        displayWarning("No More Hint!");
                }
                display();
            }
        });
    }

    /**
     * Display the warning.
     *
     * @param msg the input message
     */
    private void displayWarning(String msg) {
        warning.setVisibility(View.VISIBLE);
        warning.setText(msg);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                warning.setVisibility(View.INVISIBLE);
            }
        }, 1000);
    }

    /**
     * Set up the warning message displayed on the UI.
     */
    private void addWarningTextViewListener() {
        warning = findViewById(R.id.sudokuWarningTextView);
        warning.setVisibility(View.INVISIBLE);
    }

    /**
     * Time counting, setup initial time based on the record in boardmanager
     */
    private void setupTime() {
        if (!logicalController.boardSolved())
            logicalController.setGameRunning(true);
        Timer timer = new Timer();
        preStartTime = logicalController.getBoardManager().getTimeTaken();
        timeDisplay = findViewById(R.id.sudoku_time_text);
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                long time = Duration.between(startingTime, LocalTime.now()).toMillis();
                if (logicalController.isGameRunning()) {
                    totalTimeTaken = time + preStartTime;
                    timeDisplay.setText(String.format("Time: %s",
                            logicalController.convertTime(totalTimeTaken)));

                    logicalController.getBoardManager().setTimeTaken(time + preStartTime);
                }
            }
        };
        timer.schedule(task2, 0, 1000);
    }

    /**
     * Setup the gridview where the tiles are located
     */
    private void addGridViewToActivity() {
        gridView = findViewById(R.id.SudokuGrid);
        gridView.setNumColumns(9);
        gridView.setBoardManager(logicalController.getBoardManager());
        logicalController.getBoardManager().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        columnWidth = gridView.getMeasuredWidth() / 9;
                        columnHeight = gridView.getMeasuredHeight() / 9;
                        initializeCellButtons();
                    }
                });
    }

    /**
     * Initialize the backgrounds on the buttons to match the tiles.
     */
    private void initializeCellButtons() {
        SudokuBoard board = logicalController.getBoard();
        int nextPos = 0;
        for (Button b : cellButtons) {
            Cell cell = board.getCell(nextPos / 9, nextPos % 9);
            b.setTextSize(20);
            if (cell.isEditable()) {
                b.setTextColor(Color.RED);
            } else {
                b.setTextColor(Color.BLACK);
            }
            if (cell.getFaceValue() == 0) {
                b.setText("");
            } else {
                b.setText(String.format("%s", cell.getFaceValue().toString()));
            }
            b.setBackgroundResource(cell.getBackground());
            nextPos++;
        }
        gridView.setAdapter(new CustomAdapter(cellButtons, columnWidth, columnHeight));
    }

    /**
     * Create cell buttons.
     */
    void createCellButton(){
        SudokuBoard board = logicalController.getBoardManager().getBoard();
        cellButtons = new ArrayList<>();
        for (int row = 0; row != 9; row++) {
            for (int col = 0; col != 9; col++) {
                Button tmp = new Button(this);
                tmp.setBackgroundResource(board.getCell(row, col).getBackground());
                this.cellButtons.add(tmp);
            }
        }
    }

    /**
     * Update cell buttons.
     */
    void updateCellButtons(){
        SudokuBoard board = logicalController.getBoardManager().getBoard();
        int nextPos = 0;
        for (Button b : cellButtons) {
            Cell cell = board.getCell(nextPos / 9, nextPos % 9);
            if (cell.getFaceValue() == 0) {
                b.setText("");
            } else {
                b.setText(String.format("%s", cell.getFaceValue().toString()));
            }
            b.setBackgroundResource(cell.getBackground());
            nextPos++;
        }
    }

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    public void display() {
        updateCellButtons();
        gridView.setAdapter(new CustomAdapter(cellButtons, columnWidth, columnHeight));
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (logicalController.getBoardManager().getCurrentCell() != null) {
            logicalController.getBoardManager().getCurrentCell().setHighlighted(false);
            logicalController.getBoardManager().getCurrentCell().setFaceValue(logicalController.getBoardManager().getCurrentCell().getFaceValue());
        }
        logicalController.getBoardManager().setCurrentCell(null);
        saveToFile(logicalController.getTempGameStateFile());
        saveToFile(logicalController.getGameStateFile());
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
        if (logicalController.boardSolved() && logicalController.isGameRunning()) {
            Toast.makeText(this, "YOU WIN!", Toast.LENGTH_SHORT).show();
            Integer score = logicalController.calculateScore(totalTimeTaken);
            boolean newRecord = logicalController.updateScore(score);
            saveToFile(logicalController.getUserFile());
            logicalController.setGameRunning(false);
            popScoreWindow(score, newRecord);
        }
    }

    /**
     * Pop up window that shows user the score he/she gets
     * @param score Score that is to be displayed on popup window
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
     * Load boardManager from file.
     */
    public void loadFromFile() {
        try {
            InputStream inputStream = this.openFileInput(logicalController.getTempGameStateFile());
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                logicalController.setBoardManager((SudokuBoardManager) input.readObject());
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
