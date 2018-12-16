package fall2018.csc2017.GameCentre.gameCentre;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectOutputStream;

import fall2018.csc2017.GameCentre.R;
import fall2018.csc2017.GameCentre.data.SQLDatabase;
import fall2018.csc2017.GameCentre.data.User;

public class SignUpActivity extends AppCompatActivity {
    /**
     * the nickname that user entered.
     */
    private EditText nicknameInput;
    /**
     * the username that user entered.
     */
    private EditText usernameInput;
    /**
     * the password that user entered.
     */
    private EditText passwordInput;
    /**
     * the password that user re-entered.
     */
    private EditText password_repeat_Input;
    /**
     * the databases that stores the user and game information.
     */
    private SQLDatabase db;
    /**
     * the user object that stores the user information.
     */
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // setup text inputs and register button and database
        db = new SQLDatabase(this);
        nicknameInput = findViewById(R.id.nicknameInput);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        password_repeat_Input = findViewById(R.id.password_repeat_Input);
        addRegisterListener();
    }

    /**
     * add register button.
     */
    private void addRegisterListener() {
        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = nicknameInput.getText().toString();
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                String password_repeat = password_repeat_Input.getText().toString();
                Object[] message = checkInput(username, password, password_repeat);
                if ((boolean) message[0]) {
                    user = new User(username, password);
                    if (!nickname.equals(""))
                        user.setNickname(nickname);
                    db.addUser(user);
                    saveToFile(db.getUserFile(username));
                }
                Toast.makeText(getApplication(), (String) message[1], Toast.LENGTH_SHORT).show();
                if (message[0].equals(true)) {
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                }
            }
        });
    }

    /**
     * save user to file
     *
     * @param fileName the name of the file.
     */
    private void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(user);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * check whether the input is proper.
     *
     * @param username        the name of the user.
     * @param password        the password of the current user.
     * @param password_repeat the password that re-entered.
     * @return an array of object with length 2, the first one is boolean type and the second one is corresponding message.
     */
    private Object[] checkInput(String username, String password, String password_repeat) {
        Object[] result = new Object[2];
        if (username.equals("")) {
            result[0] = false;
            result[1] = "Username Cannot Be Empty";
        } else if (username.contains(" ")) {
            result[0] = false;
            result[1] = "Username Should Not Contain Spaces";
        } else if (db.userExists(username)) {
            result[0] = false;
            result[1] = "Username Exists!";
        } else if (!password.equals(password_repeat)) {
            result[0] = false;
            result[1] = "Password Do Not Match!";
        } else {
            result[0] = true;
            result[1] = "Successfully Registered!";
        }
        return result;
    }
}
