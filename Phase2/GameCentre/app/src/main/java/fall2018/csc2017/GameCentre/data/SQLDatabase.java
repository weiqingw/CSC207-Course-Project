package fall2018.csc2017.GameCentre.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLDatabase extends SQLiteOpenHelper {

    /**
     * the name of the database.
     */
    private static final String DB_NAME = "gameCentreDatabase";
    /**
     * the name of the user table.
     */
    private static final String USER_TABLE_NAME = "userTable";
    /**
     * the name of the data table.
     */
    private static final String DATA_TABLE_NAME = "dataTable";
    /**
     * the name of user.
     */
    private static final String KEY_USERNAME = "username";
    /**
     * the type of the game
     */
    private static final String KEY_GAME = "gameType";
    /**
     * the score for per user per game.
     */
    private static final String KEY_SCORE = "score";
    /**
     * the name of the file.
     */
    private static final String KEY_FILE = "fileAddress";


    /**
     * Constructor of database handler, this object manipulates database, and contains two tables
     *
     * @param context the context of the activity.
     */
    public SQLDatabase(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DATA_TABLE = "CREATE TABLE " + DATA_TABLE_NAME + "("
                + KEY_USERNAME + " TEXT," + KEY_GAME + " TEXT," + KEY_SCORE + " INTEGER," + KEY_FILE
                + " TEXT, PRIMARY KEY(" + KEY_USERNAME + ", " + KEY_GAME + "))";
        String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE_NAME + "("
                + KEY_USERNAME + " TEXT PRIMARY KEY," + KEY_FILE + " TEXT)";

        db.execSQL(CREATE_DATA_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DATA_TABLE_NAME);

        onCreate(db);
    }

    /**
     * Return true if User exists in the UserTable
     *
     * @param username the name of the user
     * @return whether the user exists.
     */
    public boolean userExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE " + KEY_USERNAME +
                " =?", new String[]{username});
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            Log.d("userExists", "userExists: True");
            return true;
        } else {
            cursor.close();
            db.close();
            Log.d("userExists", "userExists: False");
            return false;
        }
    }

    /**
     * return true if data exists in dataTable. By data, we mean username with gameType exists in
     * data table, since they are the keys. Score and file address don't matter.
     *
     * @param username the name of the user
     * @param game     the type of the game
     * @return true if data exists.
     */
    public boolean dataExists(String username, String game) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATA_TABLE_NAME + " WHERE " + KEY_USERNAME +
                " =? AND " + KEY_GAME + " =?", new String[]{username, game});
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            Log.d("dataExists", "dataExists: True");
            return true;
        } else {
            cursor.close();
            db.close();
            Log.d("dataExists", "dataExists: False");
            return false;
        }
    }


    /**
     * For sign up. Add user to user Table when signing up.
     * If user exists, there will be error because username is the primary key,
     * but the program won't stop, error message only shown in log.d
     *
     * @param user the user object that stores user information.
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_FILE, user.getUsername() + "_user.ser");

        db.insert(USER_TABLE_NAME, null, values);

        Log.d("Add user ", user.getUsername() + "File: " + user.getUsername() + "_user.ser");
    }


    /**
     * Add data file address as well as the whole data tuple into the data table.
     * Called when first entering a game which the user has never played.
     * dataExists() should be called first, if data doesn't exist, then call this method.
     * score is initialized to be 0 when data is added
     */
    public void addData(String username, String game) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_GAME, game);
        values.put(KEY_SCORE, 0);
        values.put(KEY_FILE, username + "_" + game + "_data.ser");

        db.insert(DATA_TABLE_NAME, null, values);
        db.close();

        Log.d("Add data ", username + " and " + game + "'s file is added to database");
    }


    /**
     * return user file address, the file contains the serialized user object
     * Call userExists() before calling this method, user must exist in the table in order to get
     * the file address.
     *
     * @param username the name of the user.
     * @return the file name of the current user.
     */
    public String getUserFile(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE "
                + KEY_USERNAME + " =?", new String[]{username});
        if (cursor.moveToFirst()) {
            String result = cursor.getString(cursor.getColumnIndex(KEY_FILE));
            cursor.close();
            db.close();
            Log.d("GetUser", result);
            return result;
        } else {
            cursor.close();
            db.close();
            Log.d("Get User", "Username Does Not Exist, Please Sign Up!");
            return "Username Does Not Exist, Please Sign Up!";
        }
    }


    /**
     * return highest score of a user in a game.
     * call dataExists() first to check if data exists.
     *
     * @param user the name of the current user.
     * @param game the type of the game.
     * @return the score of the user with specific game.
     */
    int getScore(String user, String game) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATA_TABLE_NAME + " WHERE " + KEY_USERNAME
                + " =? AND " + KEY_GAME + " =? ", new String[]{user, game});
        if (cursor.moveToFirst()) {
            int score = cursor.getInt(cursor.getColumnIndex(KEY_SCORE));
            Log.d("TestDataBase", "getScore: " + score);
            return score;

        } else {
            Log.d("TestDataBase", "getScore: not found");
            return -1;
        }

    }


    /**
     * return dataFile address, which is a serialized object containing current game state
     * get the file address and deserialize the object, then data could be accessed.
     *
     * @param username the name of the user.
     * @param game     the type of the game.
     * @return the name of the data file.
     */
    public String getDataFile(String username, String game) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATA_TABLE_NAME + " WHERE " + KEY_USERNAME
                + " =? AND " + KEY_GAME + " =?", new String[]{username, game});
        if (cursor.moveToFirst()) {

            String result = cursor.getString(cursor.getColumnIndex(KEY_FILE));
            cursor.close();
            db.close();
            Log.d("getDataFile", "getDataFile: fileName= " + result);
            return result;
        } else {
            cursor.close();
            db.close();
            Log.d("getDataFile", "getDataFile: filename= DNE");
            return "File Does Not Exist!";
        }
    }


    /**
     * Update score with User object first, if updateScore returns true, then call this method
     * else do not call this method.
     * <p>
     * Before calling this method, the score in User object must be updated first, since the we use
     * the score from user to update our data base.
     * <p>
     * Every time Game is over, update user object's score first then update the score in database.
     *
     * @param user the user object that store the user information.
     * @param game the type of the game.
     * @return the update score.
     */
    public int updateScore(User user, String game) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_GAME, game);
        values.put(KEY_SCORE, user.getScore(game));
        values.put(KEY_FILE, user.getFile(game));

        int result = db.update(DATA_TABLE_NAME, values, KEY_USERNAME + "=? AND " +
                KEY_GAME + "=?", new String[]{user.getUsername(), game});
        db.close();
        Log.d("updateScore", "updateScore: success?");
        return result;
    }


    /**
     * get all the scores by each game.
     *
     * @param game_type the type of the game.
     * @return the list of list, the inner list contains the game type, name of the user, score and its rank.
     */
    public List<List<String>> getScoreByGame(String game_type) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<List<String>> dataList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DATA_TABLE_NAME + " WHERE " + KEY_GAME +
                        " =? AND " + KEY_SCORE + " <> 0 ORDER BY " + KEY_SCORE + " DESC",
                new String[]{game_type});
        if (cursor.moveToFirst()) {
            int rank = 1;
            do {
                String user = cursor.getString(cursor.getColumnIndex(KEY_USERNAME));
                String score = Integer.toString(cursor.getInt(cursor.getColumnIndex(KEY_SCORE)));
                List<String> data = new ArrayList<>();
                data.add(String.valueOf(rank));
                data.add(game_type);
                data.add(user);
                data.add(score);
                dataList.add(data);
                rank++;
            } while (cursor.moveToNext());
        }

        db.close();
        return dataList;
    }
}




























