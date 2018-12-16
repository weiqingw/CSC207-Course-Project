package fall2018.csc2017.GameCentre.gameCentre;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;

public class ScoreBoardActivity extends AppCompatActivity {

    /**
     * the list of the list where the inner list contains username, game type, score and its rank.
     */
    private List<List<String>> dataList;
    /**
     * the tableLayout of the scoreboard.
     */
    private TableLayout scoreboard;
    /**
     * the database that store user and game information.
     */
    private SQLDatabase db;
    /**
     * the type of scoreboard either by user or by name
     */
    private String type;
    /**
     * the name of the game.
     */
    private String game_type;
    /**
     * the user object that store the user information.
     */
    private User user;
    /**
     * the String to be displayed in scoreboard activity.
     */
    private final String[] byUserTitle = new String[]{"Game", "Username", "Highest Score" };
    /**
     * the string to be displayed in scoreboard activity.
     */
    private final String[] byGameTitle = new String[]{"Rank", "Game", "User", "Score" };
    /**
     * the string byUser
     */
    private final String byUser = "byUser";
    /**
     * the string byGame.
     */
    private final String byGame = "byGame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_board);
        db = new SQLDatabase(this);
        scoreboard = findViewById(R.id.tableView);
        setupUser();
        setupData();
        addTable();
    }

    /**
     * set up the user information.
     */
    private void setupUser() {
        Intent intent = getIntent();
        type = intent.getStringExtra("scoreBoardType");

        user = (User) intent.getSerializableExtra("user");
    }

    /**
     * set up the data information.
     */
    private void setupData() {
        if (type.equals(byGame))
            game_type = getIntent().getStringExtra("gameType");
        if (type.equals(byUser))
            dataList = user.getScoreboardData();
        else
            dataList = db.getScoreByGame(game_type);
    }

    /**
     * add the table to scoreboard activity.
     */
    private void addTable() {
        setupTitle();
        TableRow row;
        TextView text;
        for (int rowNum = 0; rowNum < dataList.size(); rowNum++) {
            row = new TableRow(this);
            for (int colNum = 0; colNum < dataList.get(rowNum).size(); colNum++) {
                text = new TextView(this);
                text.setText(dataList.get(rowNum).get(colNum));
                text.setTextColor(Color.parseColor("#FFFFFF"));
                text.setGravity(Gravity.CENTER);
                row.addView(text);
            }
            scoreboard.addView(row);
        }
    }

    /**
     * set up the title of the scoreboard.
     */
    private void setupTitle() {
        initializeColumnsWidth();
        String[] titles = setupTitleContent();
        addConfigTitles(titles);
        addLine();
    }

    /**
     * set up the title content.
     *
     * @return title content string array.
     */
    private String[] setupTitleContent() {
        String[] titles;
        if (type.equals(byUser))
            titles = byUserTitle;
        else
            titles = byGameTitle;
        return titles;
    }

    /**
     * add lines to table.
     */
    private void addLine() {
        TableRow row = new TableRow(this);
        row.setMinimumHeight(5);
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 10, 0, 10);
        row.setLayoutParams(params);
        row.setBackgroundColor(ContextCompat.getColor(this, R.color.scoreBoardTileLine));
        scoreboard.addView(row);
    }

    /**
     * configure the titles.
     *
     * @param titles the titles of string.
     */
    private void addConfigTitles(String[] titles) {
        TextView text;
        TableRow row = new TableRow(this);
        for (String title : titles) {
            text = new TextView(this);
            text.setText(title);
            text.setTextColor(Color.parseColor("#FFFFFF"));
            text.setGravity(Gravity.CENTER);
            text.setTextSize(18);
            row.addView(text);
        }
        scoreboard.addView(row);
    }

    /**
     * initialize columns width.
     */
    private void initializeColumnsWidth() {
        TableRow newRow = new TableRow(this);
        int numCol = type.equals(byGame) ? 4 : 3;
        int colWidth = type.equals(byUser) ? 300 : 250;
        for (int i = 0; i < numCol; i++) {
            TextView tmp = new TextView(this);
            tmp.setWidth(colWidth);
            newRow.addView(tmp);
        }
        scoreboard.addView(newRow);
    }
}

