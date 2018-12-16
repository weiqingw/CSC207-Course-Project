package fall2018.csc2017.GameCentre.pictureMatching;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;

public class MatchingNewGamePop extends AppCompatActivity {
    /**
     * the User object that store the user information.
     */
    private User user;
    /**
     * the name of the user file.
     */
    private String userFile;
    /**
     * the database that stores the user and game information.
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
     * the name of the current game.
     */
    public static final String GAME_NAME = "PictureMatch";
    /**
     * The board manager.
     */
    private MatchingBoardManager boardManager;
    /**
     * the difficulty that we selected.
     */
    private int selected_difficulty;
    /**
     * The list of String for different difficulty.
     */
    private String[] list_diff = new String[]{"Easy(4x4)", "Normal(6x6)", "Hard(8x8)" };
    /**
     * the selected theme.
     */
    private String selected_theme;
    /**
     * the list of the themes.
     */
    private String[] list_theme = new String[]{"number", "animal", "emoji" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_new_game_pop);

        db = new SQLDatabase(this);
        setupUser();
        setupFile();

        addDiffSpinnerListener();
        addThemeSpinnerListener();
        addNewGameButtonListener();
    }

    /**
     * add spinner listener.
     */
    private void addDiffSpinnerListener() {
        Spinner select_diff = findViewById(R.id.match_diff_select);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list_diff);
        select_diff.setAdapter(arrayAdapter);

        select_diff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int tmp = 0; tmp < list_diff.length; tmp++) {
                    if (parent.getItemAtPosition(position) == list_diff[tmp]) {
                        selected_difficulty = tmp * 2 + 4;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_difficulty = 4;
            }
        });
    }

    /**
     * add theme spinner.
     */
    private void addThemeSpinnerListener() {
        final Spinner select_theme = findViewById(R.id.match_theme_select);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list_theme);
        select_theme.setAdapter(arrayAdapter);

        select_theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int tmp = 0; tmp < list_theme.length; tmp++) {
                    if (parent.getItemAtPosition(position) == list_theme[tmp]) {
                        selected_theme = list_theme[tmp];
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected_theme = list_theme[0];
            }
        });
    }

    /**
     * add new game button.
     */
    private void addNewGameButtonListener() {
        Button startButton = findViewById(R.id.match_new_game);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardManager = new MatchingBoardManager(selected_difficulty, selected_theme);
                Intent tmp = new Intent(getApplication(), PictureMatchingGameActivity.class);
                saveToFile(tempGameStateFile);
                tmp.putExtra("user", user);
                startActivity(tmp);
                finish();
            }
        });
    }

    /**
     * setup user object according to username and define the value of userFile (where user
     * object is saved)
     */
    private void setupUser() {
        user = (User) getIntent().getSerializableExtra("user");
//        username = getIntent().getStringExtra("user");
        userFile = db.getUserFile(user.getUsername());
        loadFromFile(userFile);
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
     * load from fileName file.
     *
     * @param fileName the name of the file.
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
            if (fileName.equals(user.getFile(GAME_NAME))) {
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
