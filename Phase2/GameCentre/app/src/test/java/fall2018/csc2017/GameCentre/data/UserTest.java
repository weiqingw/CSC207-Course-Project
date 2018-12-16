package fall2018.csc2017.GameCentre.data;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserTest {
    private User user;
    @Before
    public void setUp() throws Exception {
        user = new User("username", "password");
        user.updateScore("game", 100);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void checkPassword() {
        boolean result = user.checkPassword("password");
        assertTrue(result);
        user.setPassword("newPassword");
        result = user.checkPassword("password");
        assertFalse(result);
        result = user.checkPassword("newPassword");
        assertTrue(result);

    }

    @Test
    public void updateScore() {
        assertEquals(user.getScore("game"), 100);
        assertEquals(user.getScore("game1"), -1);
    }

    @Test
    public void getFile() {
        assertEquals(user.getFile("game"), "username_game_data.ser");
        assertEquals(user.getFile("game1"), "DNE");
    }

    @Test
    public void getScore() {

    }

    @Test
    public void getScoreboardData() {
        List<List<String>> dataList = new ArrayList<>();
        List<String> data = new ArrayList<>();
        data.add("game");
        data.add("username");
        data.add("100");
        dataList.add(data);
        assertEquals(dataList, user.getScoreboardData());

    }
}