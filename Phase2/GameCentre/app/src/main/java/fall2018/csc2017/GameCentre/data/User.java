package fall2018.csc2017.GameCentre.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User implements Serializable {
    /**
     * the name of current user.
     */
    private String username;
    /**
     * The password of the current user.
     */
    private String password;
    /**
     * the nickname of the current user.
     */
    private String nickname;
    /**
     * A HashMap that store per game per score.
     */
    private HashMap<String, Integer> score;
    /**
     * the byte array that stores the picture of avatar.
     */
    private byte[] avatar;

    /**
     * Construct a User object when signed up
     * Initialized with a username, a password and an empty hashmap which records all game the
     * user has played and the highest score the user got in that game.
     * <p>
     * nickname is initialized to be the same as username in case nickname is not entered.
     *
     * @param username the name of the user.
     * @param password the password of the user.
     */
    public User(String username, String password) {
        this.nickname = username;
        this.username = username;
        this.password = password;
        this.score = new HashMap<>();
    }

    /**
     * Return avatar.
     *
     * @return avatar.
     */
    public byte[] getAvatar() {
        return avatar;
    }

    /**
     * Set avatar
     *
     * @param avatar the byte array of Bitmap for avatar.
     */
    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    /**
     * set nickname
     * In the sign up UI, if the input of nickname is not empty, nickname should be set to be
     * the input after user object is constructed using setNickname()
     *
     * @param name the name of the nickname.
     */
    public void setNickname(String name) {
        this.nickname = name;
    }

    /**
     * Returns nickname.
     *
     * @return nickname of user
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Returns username.
     *
     * @return username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set Username, but shouldn't be used, just in case.
     * username should not be changed
     *
     * @param username the name of the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * set password, when password needs to be changed.
     *
     * @param password the new password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * check if the entered password is correct and return either true or false
     *
     * @param password the password that user entered.
     * @return if the password is correct
     */
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Update score taking gameType and score as input.
     * update score only when the input score is higher than current highest score.
     *
     * @param gameType the type of the name.
     * @param score    the new score of the user.
     * @return true if update succeeds, false if not updated (input score < current score)
     */
    public boolean updateScore(String gameType, Integer score) {
        if (this.score.containsKey(gameType)) {
            if (this.score.get(gameType) < score) {
                this.score.put(gameType, score);
                return true;
            }
        } else {
            this.score.put(gameType, score);
            return true;
        }
        return false;
    }

    /**
     * Returns the file address for a game type.
     *
     * @param game the name of the game.
     * @return the file name where the game state of the user of a specific game is stored
     */
    public String getFile(String game) {
        if (this.score.containsKey(game)) {
            return this.username + "_" + game + "_data.ser";
        } else {
            return "DNE";
        }
    }

    /**
     * -1 is returned if the user has never played the game but the method is called
     * Theoretically this shouldn't happen because game is added to hashmap at the beginning
     * of each game if it's the first time playing
     *
     * @param game the name of the game.
     * @return score the user got in a specific game
     */
    public int getScore(String game) {
        if (this.score.containsKey(game)) {
            return this.score.get(game);
        } else {
            return -1;
        }
    }

    /**
     * Return information needed for scoreboard.
     *
     * @return 2D ArrayList containing necessary information
     */
    public List<List<String>> getScoreboardData() {
        List<List<String>> datalist = new ArrayList<>();
        for (String game : score.keySet()) {
            List<String> data = new ArrayList<>();
            data.add(game);
            data.add(username);
            data.add(score.get(game).toString());
            datalist.add(data);
        }
        return datalist;
    }


}
