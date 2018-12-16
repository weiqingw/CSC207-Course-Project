package fall2018.csc2017.GameCentre.pictureMatching;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.gameCentre.ScoreBoardActivity;

public class PictureMatchingStartingActivity extends AppCompatActivity {
    /**
     * the user object.
     */
    private User user;
    /**
     * the name of the user file.
     */
    private String userFile;
    /**
     * the database
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
    public static final String GAME_NAME = "PictureMatch";
    /**
     * the matchingBoardManager.
     */
    private MatchingBoardManager boardManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picturematching_starting);
        db = new SQLDatabase(this);

        setupUser();
        setupFile();

        boardManager = new MatchingBoardManager(4, "number");
        saveToFile(tempGameStateFile);

        addStartButtonListener();
        addLoadButtonListener();
        addScoreboardButtonListener();
    }

    /**
     * Make message into toast
     *
     * @param message toast format of message
     */
    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Set up the user.
     */
    private void setupUser() {
        user = (User) getIntent().getSerializableExtra("user");
        userFile = db.getUserFile(user.getUsername());
        loadFromFile(userFile);
    }

    /**
     * Set up the file.
     */
    private void setupFile() {
        if (!db.dataExists(user.getUsername(), GAME_NAME)) {
            db.addData(user.getUsername(), GAME_NAME);
        }
        gameStateFile = db.getDataFile(user.getUsername(), GAME_NAME);
        tempGameStateFile = "temp_" + gameStateFile;
    }

    /**
     * Switch to the SlidingTilesGameActivity view to play the game.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, PictureMatchingGameActivity.class);
        saveToFile(tempGameStateFile);
        tmp.putExtra("user", user);
        startActivity(tmp);
    }

    /**
     * Add scoreBoard button.
     */
    private void addScoreboardButtonListener() {
        Button scoreboardButton = findViewById(R.id.scoreboardButton_picturematching);
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
     * Add load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.PictureMatchingLoadButton);
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
     * Add start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.PictureMatchingNewGameButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tmp = new Intent(getApplication(), MatchingNewGamePop.class);
                saveToFile(tempGameStateFile);
                tmp.putExtra("user", user);
                startActivity(tmp);
            }
        });
    }

    /**
     * Load from fileName
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
                    boardManager = (MatchingBoardManager) input.readObject();
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
