package fall2018.csc2017.GameCentre.sudoku;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.gameCentre.ScoreBoardActivity;

/**
 * The SudokuStartingActivity class.
 */
public class SudokuStartingActivity extends AppCompatActivity {

    /**
     * The Resource.
     */
    public static Resources RESOURCES;
    /**
     * the name of the package.
     */
    public static String PACKAGE_NAME;
    /**
     * The user.
     */
    private User user;
    /**
     * The userFile.
     */
    private String userFile;
    /**
     * The database.
     */
    private SQLDatabase db;
    /**
     * The main save file.
     */
    private String gameStateFile;
    /**
     * A temporary save file.
     */
    private String tempGameStateFile;
    /**
     * The board manager.
     */
    public static final String GAME_NAME = "Sudoku";
    /**
     * The SudokuBoardManager.
     */
    private SudokuBoardManager boardManager;
    /**
     * Levels of difficulty.
     */
    private String[] list_diff = new String[]{"Easy", "Normal", "Hard" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_starting);
        db = new SQLDatabase(this);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        RESOURCES = getResources();
        setupUser();
        setupFile();

        boardManager = new SudokuBoardManager();
        saveToFile(tempGameStateFile);

        addStartButtonListener();
        addLoadButtonListener();
        addScoreboardButtonListener();
    }

    /**
     * setup user object according to username and define the value of userFile (where user
     * object is saved)
     */
    private void setupUser() {
        user = (User) getIntent().getSerializableExtra("user");
        userFile = db.getUserFile(user.getUsername());
    }

    /**
     * setup file of the game
     * get the filename of where the game state should be saved
     */
    private void setupFile() {
        if (!db.dataExists(user.getUsername(), GAME_NAME)) {
            db.addData(user.getUsername(), GAME_NAME);
        }
        gameStateFile = db.getDataFile(user.getUsername(), GAME_NAME);
        tempGameStateFile = "temp_" + gameStateFile;
    }

    /**
     * Read the temporary board from disk.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile(tempGameStateFile);
    }

    /**
     * Activate the Start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.SudokuNewGameButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SudokuStartingActivity.this);
                builder.setTitle("Choose a difficulty:");
                builder.setItems(list_diff, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int diff) {
                        SudokuBoardManager.setLevelOfDifficulty(diff + 1);
                        boardManager = new SudokuBoardManager();
                        switchToGame();
                        Toast.makeText(SudokuStartingActivity.this, list_diff[diff] + " selected", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    /**
     * Activate the Scoreboard button.
     */
    private void addScoreboardButtonListener() {
        ImageButton scoreboardButton = findViewById(R.id.scoreboardButton_sudoku);
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ScoreBoardActivity.class);
                intent.putExtra("user", user);
                intent.putExtra("gameType", GAME_NAME);
                intent.putExtra("scoreBoardType", "byGame");
                startActivity(intent);

            }
        });
    }

    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.SudokuLoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFromFile(gameStateFile);
                saveToFile(tempGameStateFile);
                makeToast("Loaded Game");
                switchToGame();

            }
        });
    }

    /**
     * Make a toast.
     *
     * @param message the input message.
     */
    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Switch to the SlidingTilesGameActivity view to play the game.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, SudokuGameActivity.class);
        saveToFile(tempGameStateFile);
        tmp.putExtra("user", user);
        startActivity(tmp);
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                if (fileName.equals(userFile)) {
                    user = (User) input.readObject();
                } else if (fileName.equals(gameStateFile) || fileName.equals(tempGameStateFile)) {
                    boardManager = (SudokuBoardManager) input.readObject();
                }
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
            if (fileName.equals(userFile)) {
                outputStream.writeObject(user);
            } else if (fileName.equals(gameStateFile) || fileName.equals(tempGameStateFile)) {
                outputStream.writeObject(boardManager);
            }
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

}
