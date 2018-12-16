package fall2018.csc2017.GameCentre.slidingTiles;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;

import static android.graphics.Bitmap.createBitmap;

public class SlidingTilesNewGamePop extends AppCompatActivity {

    /**
     * User file of the game.
     */
    private User user;

    /**
     * The name of the user's file.
     */
    private String userFile;

    /**
     * The database for user data.
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
    public static final String GAME_NAME = "SlidingTiles";

    /**
     * The boardManager for the game.
     */
    private SlidingTilesBoardManager boardManager;

    /**
     * The image code.
     */
    private static final int SELECT_IMAGE_CODE = 1801;

    /**
     * Import button on the user interface.
     */
    private ImageButton importButton;

    /**
     *
     */
    private Bitmap bitmapCut;

    /**
     * The level of difficulty selected.
     */
    private int selected_difficulty;

    /**
     * The level of difficulty available.
     */
    private String[] list_diff = new String[]{"Easy(3x3)", "Normal(4x4)", "Hard(5x5)"};

    /**
     * The maximum undo limit that can be set by the user.
     */
    private static final int MAX_UNDO_LIMIT = 20;

    /**
     * Dispatch onCreate() to fragments.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slidingtiles_pop);

        db = new SQLDatabase(this);
        setupUser();
        setupFile();

        addImportButtonListener();
        addRadioButtonListener();
        addDiffSpinnerListener();
        addNewGameButtonListener();
    }

    /**
     * setup user object according to username and define the value of userFile (where user
     * object is saved)
     */
    private void setupUser() {
        user = (User) getIntent().getSerializableExtra("user");
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
     * Activate the import ImageButton.
     */
    private void addImportButtonListener() {
        importButton = findViewById(R.id.select_image);
        importButton.setVisibility(View.INVISIBLE);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    /**
     * open gallery for image selection
     */
    private void openGallery() {
        Intent get_photo = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(get_photo, SELECT_IMAGE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_CODE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            try {
                Bitmap bitmapUncut = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                bitmapCut = cutToTileRatio(bitmapUncut);
                importButton.setImageBitmap(bitmapCut);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Cut the input bitmap to the correct size that fits into a tile.
     */
    private Bitmap cutToTileRatio(Bitmap bitmapUncut) {
        int width = bitmapUncut.getWidth();
        int height = bitmapUncut.getHeight();
        if (width * 320 > height * 250) {
            int x = (width - height * 250 / 320) / 2;
            return createBitmap(bitmapUncut, x, 0,
                    width - 2 * x, height, null, false);
        } else if (width * 320 < height * 250) {
            int y = (height - width * 320 / 250) / 2;
            return createBitmap(bitmapUncut, 0, y,
                    width, height - 2 * y, null, false);
        } else {
            return bitmapUncut;
        }
    }

    /**
     * Set up the radio button.
     */
    private void addRadioButtonListener() {
        RadioButton withImageButton = findViewById(R.id.withImageButton);
        RadioButton withNumberButton = findViewById(R.id.withNumberButton);
        withNumberButton.setChecked(true);
        withImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SlidingTilesNewGamePop.this, "With Image Selected",
                        Toast.LENGTH_SHORT).show();
                importButton.setVisibility(View.VISIBLE);
            }
        });
        withNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SlidingTilesNewGamePop.this, "With Number Selected",
                        Toast.LENGTH_SHORT).show();
                importButton.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * Set up the spinner for level of difficulty selection.
     */
    private void addDiffSpinnerListener() {
        Spinner select_diff = findViewById(R.id.list_diff_sele);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, list_diff);
        select_diff.setAdapter(arrayAdapter);
        select_diff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int tmp = 0; tmp < list_diff.length; tmp++) {
                    if (parent.getItemAtPosition(position) == list_diff[tmp]) {
                        selected_difficulty = tmp + 3;
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
     * Activate the new game button.
     */
    private void addNewGameButtonListener() {
        Button startButton = findViewById(R.id.NewGameButton);
        final EditText undoLimit = findViewById(R.id.undoLimitInput);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputStr = undoLimit.getText().toString();
                int input;
                if (inputStr.matches("")) {
                    input = 3;
                } else {
                    input = Integer.parseInt(inputStr);
                }
                if (input > MAX_UNDO_LIMIT) {
                    Toast.makeText(SlidingTilesNewGamePop.this, "Exceeds Undo Limit: "
                            + MAX_UNDO_LIMIT, Toast.LENGTH_SHORT).show();
                } else if (importButton.getVisibility() == View.INVISIBLE) {
                    boardManager = new SlidingTilesBoardManager(selected_difficulty);
                    boardManager.setCapacity(input);
                    switchToGame();
                } else if (bitmapCut == null) {
                    Toast.makeText(SlidingTilesNewGamePop.this,
                            "You need to import image!", Toast.LENGTH_SHORT).show();
                } else {
                    boardManager = new SlidingTilesBoardManager(selected_difficulty);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmapCut.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    stream.toByteArray();
                    boardManager.setImageBackground(stream.toByteArray());
                    boardManager.setCapacity(input);
                    switchToGame();
                }
            }
        });
    }

    /**
     * Switch to the SlidingTilesGameActivity view to play the game.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, SlidingTilesGameActivity.class);
        saveToFile(tempGameStateFile);
        tmp.putExtra("user", user);
        startActivity(tmp);
        finish();
    }

    /**
     * Load the saved boardManager or user.
     */
    private void loadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                if (fileName.equals(userFile)) {
                    user = (User) input.readObject();
                } else if (fileName.equals(gameStateFile) || fileName.equals(tempGameStateFile)) {
                    boardManager = (SlidingTilesBoardManager) input.readObject();
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