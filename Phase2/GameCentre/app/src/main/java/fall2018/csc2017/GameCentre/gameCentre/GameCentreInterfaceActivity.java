package fall2018.csc2017.GameCentre.gameCentre;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.pictureMatching.PictureMatchingStartingActivity;
import fall2018.csc2017.GameCentre.slidingTiles.SlidingTilesStartingActivity;
import fall2018.csc2017.GameCentre.sudoku.SudokuStartingActivity;

public class GameCentreInterfaceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * the user object that stores the user information.
     */
    private User user;
    /**
     * the name of the user.
     */
    private String username;
    /**
     * the database that stores the user and game information.
     */
    private SQLDatabase db;

    /**
     * A TextView to display.
     */
    private TextView userNickName;
    /**
     * A image Button to display.
     */
    private ImageButton icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_centre_interface);
        // instantiate user object
        db = new SQLDatabase(this);
        setupUser();

        addSlidingTilesButton();
        addSudokuButton();
        addPictureMatchingButton();
        addNavigationView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile(db.getUserFile(username));
        userNickName.setText(user.getNickname());
        if (user.getAvatar() != null) {
            byte[] byteArray = user.getAvatar();
            Bitmap avatar = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            icon.setImageBitmap(avatar);
        }
    }

    /**
     * add Navigation View to GameCenterInterface.
     */
    private void addNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        LinearLayout header = headerView.findViewById(R.id.nav_header);
        addIconButton(header);
        setTextView(headerView);
    }

    /**
     * Add textView to headerView
     *
     * @param headerView A view where we should put textView.
     */
    private void setTextView(View headerView) {
        TextView userAccountName = headerView.findViewById(R.id.userAccountName);
        userAccountName.setText(username);
        userNickName = headerView.findViewById(R.id.userNickName);
        userNickName.setText(user.getNickname());
    }

    /**
     * Add Icon button to linearLayout header.
     *
     * @param header A Linear Layout where we put Icon button.
     */
    private void addIconButton(LinearLayout header) {
        icon = header.findViewById(R.id.userIcon);
        if (user.getAvatar() != null) {
            byte[] byteArray = user.getAvatar();
            Bitmap avatar = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            icon.setImageBitmap(avatar);
        } else {
            icon.setImageResource(R.mipmap.cool_jason);
        }
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSetting = new Intent(GameCentreInterfaceActivity.this, NavSetting.class);
                toSetting.putExtra("user", user.getUsername());
                startActivity(toSetting);
            }
        });
    }

    /**
     * Set up user.
     */
    private void setupUser() {
        username = getIntent().getStringExtra("user");
        loadFromFile(db.getUserFile(username));
        Toast.makeText(this, "Welcome " + user.getUsername(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Add SlidingTilesButton in GameCentreInterface.
     */
    private void addSlidingTilesButton() {
        Button game = findViewById(R.id.SlidingTiles);
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), SlidingTilesStartingActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    /**
     * Add SudokuButton in GameCentreInterface.
     */
    private void addSudokuButton() {
        Button game = findViewById(R.id.Sudoku);
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), SudokuStartingActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    /**
     * Add PictureMatchingButton in GameCentreInterface.
     */
    private void addPictureMatchingButton() {
        Button game = findViewById(R.id.PictureMatching);
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), PictureMatchingStartingActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });
    }

    /**
     * Load data from filename.
     *
     * @param fileName the name of the file.
     */
    private void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                user = (User) input.readObject();
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
     * When Click Items in Navigation layout, intent to another Activity
     *
     * @param item menu items in Navigation layout
     * @return boolean
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.change_password) {
            goToChangePassword();
        } else if (id == R.id.score_board) {
            goToScoreBoard();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Steps need to be taken to scoreboard when clicking Scoreboard.
     */
    private void goToScoreBoard() {
        Intent toScoreBoard = new Intent(this, ScoreBoardActivity.class);
        toScoreBoard.putExtra("scoreBoardType", "byUser");
        toScoreBoard.putExtra("user", user);
        startActivity(toScoreBoard);
    }

    /**
     * Steps need to be taken to change password when click setting.
     */
    private void goToChangePassword() {
        if (username.equals("admin")) {
            Toast.makeText(GameCentreInterfaceActivity.this,
                    "admin cannot change password!", Toast.LENGTH_SHORT).show();
        } else {
            Intent toChangePassword = new Intent(this, NavChangePassword.class);
            toChangePassword.putExtra("user", user);
            startActivity(toChangePassword);
        }
    }
}
