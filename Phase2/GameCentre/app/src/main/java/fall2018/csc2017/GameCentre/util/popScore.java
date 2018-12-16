package fall2018.csc2017.GameCentre.util;
// Source: https://www.youtube.com/watch?v=fn5OlqQuOCk

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.data.User;
import fall2018.csc2017.GameCentre.gameCentre.GameCentreInterfaceActivity;
import fall2018.csc2017.GameCentre.gameCentre.ScoreBoardActivity;

public class popScore extends Activity {
    /**
     * the name of the game.
     */
    private String game_name;
    /**
     * the user object that store the user information.
     */
    private User user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_score);
        setupGetIntentExtra();
        setupPopUpWindow();
        setupScoreboardButton();
    }

    /**
     * get the intent from previous activity.
     */
    private void setupGetIntentExtra() {
        game_name = getIntent().getStringExtra("gameType");
        user = (User) getIntent().getSerializableExtra("user");
    }



    /**
     * setup the score board button.
     */
    private void setupScoreboardButton() {
        Button scoreboardButton = findViewById(R.id.popScoreToScoreboard);
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ScoreBoardActivity.class);
                intent.putExtra("scoreBoardType", "byGame");
                intent.putExtra("user", user);
                intent.putExtra("gameType", game_name);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * setup the pop up window.
     */
    private void setupPopUpWindow() {
        int score = getIntent().getIntExtra("score", 0);
        boolean newRecord = getIntent().getBooleanExtra("newRecord", false);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout(((int) (width * .8)), (int) (height * .3));
        TextView scoreDisplay = findViewById(R.id.scoreDisplay);
        scoreDisplay.setText(String.valueOf(score));

        TextView newRecordDisplay = findViewById(R.id.newRecordDisplay);
        String toDisplay;
        if (newRecord)
            toDisplay = "New Record: " + String.valueOf(score);
        else
            toDisplay = "Your Highest Score Was: " + user.getScore(game_name);
        newRecordDisplay.setText(toDisplay);
    }
}
