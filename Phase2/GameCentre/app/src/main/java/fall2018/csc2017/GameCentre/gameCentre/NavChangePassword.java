package fall2018.csc2017.GameCentre.gameCentre;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class NavChangePassword extends AppCompatActivity {

    /**
     * Database used to save user, game and score info
     */
    private SQLDatabase db;
    /**
     * User object of current user
     */
    private User user;
    /**
     * Input text box of original password
     */
    private EditText original_password;
    /**
     * Input text box of new password
     */
    private EditText new_password;
    /**
     * Input text box of new password repeat
     */
    private EditText reenter_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_change_password);
        db = new SQLDatabase(this);
        setupUser();
        setupEditTexts();
        addConfirmButton();
    }

    /**
     * Link text box variables with real text boxes in xml layout
     */
    private void setupEditTexts() {
        original_password = findViewById(R.id.original_password);
        new_password = findViewById(R.id.new_password);
        reenter_password = findViewById(R.id.reenter_password);
    }

    /**
     * Add confirm button listener, execute changes of setting when clicked
     */
    private void addConfirmButton() {
        Button confirmButton = findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkOriginalPassword(original_password)) {
                    Toast.makeText(NavChangePassword.this,
                            "Wrong Original Password", Toast.LENGTH_SHORT).show();
                } else if (!compareTwoPassword(new_password, reenter_password)) {
                    Toast.makeText(NavChangePassword.this,
                            "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                } else {
                    user.setPassword(new_password.getText().toString());
                    saveToFile(db.getUserFile(user.getUsername()));
                    Toast.makeText(NavChangePassword.this,
                            "Password change successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Check original password to make sure it's the right user changing password
     *
     * @param originalPassword input original password
     * @return true if input password matches user's password, false otherwise
     */
    private boolean checkOriginalPassword(EditText originalPassword) {
        String password = originalPassword.getText().toString();
        return user.checkPassword(password);
    }

    /**
     * Make sure new password is entered correctly
     *
     * @param newPassword     input new password
     * @param ReenterPassword input of repeat new password
     * @return true if two password matches, false otherwise
     */
    private boolean compareTwoPassword(EditText newPassword, EditText ReenterPassword) {
        return newPassword.getText().toString().equals(ReenterPassword.getText().toString());
    }

    /**
     * Instantiate user object using username
     */
    private void setupUser() {
        user = (User) getIntent().getSerializableExtra("user");
    }


    /**
     * Save user object to file specified by fileName.
     *
     * @param fileName the name of the file where user object is stored
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(user);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
