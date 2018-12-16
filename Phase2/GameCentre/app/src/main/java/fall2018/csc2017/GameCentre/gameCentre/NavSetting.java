package fall2018.csc2017.GameCentre.gameCentre;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


/**
 * A Setting Activity.
 */
public class NavSetting extends AppCompatActivity {

    /**
     * User
     */
    private User user;
    /**
     * Profile image
     */
    private byte[] newAvatar;
    /**
     * SQLDatabse
     */
    private SQLDatabase db;
    /**
     * current user name.
     */
    private String username;
    /**
     * Code to identify which intent you came back.
     */
    private static final int SELECT_IMAGE = 42;
    /**
     * the image view in setting activity.
     */
    private ImageView image_selected;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_setting);
        db = new SQLDatabase(this);
        setupUser();

        final EditText nickName = findViewById(R.id.nick_name);
        nickName.setText(user.getNickname());
        addApplyButton(nickName);
        addCancelButton();
        addAvatarButton();
        setImageSelected();
    }

    /**
     * Set up user info.
     */
    private void setupUser() {
        username = getIntent().getStringExtra("user");
        loadFromFile(db.getUserFile(username));
    }

    /**
     * Set image from gallery to the imageView.
     */
    private void setImageSelected() {
        image_selected = findViewById(R.id.imageView);
        if (user.getAvatar() != null) {
            byte[] byteArray = user.getAvatar();
            Bitmap avatar = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            image_selected.setImageBitmap(avatar);
        }

    }

    /**
     * Add avatar button to setting activity.
     */
    private void addAvatarButton() {
        Button avatarButton = findViewById(R.id.import_button);
        avatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    /**
     * Add cancel button to setting activity.
     */
    private void addCancelButton() {
        Button cancelButton = findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NavSetting.this,
                        "Nothing happens, haha!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * Add apply button to setting activity.
     *
     * @param nickName new nick name that we need to change to.
     */
    private void addApplyButton(final EditText nickName) {
        Button applyButton = findViewById(R.id.apply_Button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newNickname = nickName.getText().toString();
                user.setNickname(newNickname);
                user.setAvatar(newAvatar);
                saveToFile(db.getUserFile(username));

                Toast.makeText(NavSetting.this,
                        "change nickname and avatar Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Open gallery
     */
    private void openGallery() {
        Intent get_phote = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(get_phote, SELECT_IMAGE);
    }

    /**
     * Adapted from https://stackoverflow.com/questions/7620401/how-to-convert-byte-array-to-bitmap
     * and https://stackoverflow.com/questions/4989182/converting-java-bitmap-to-byte-array.
     *
     * @param requestCode the request code
     * @param resultCode the result code
     * @param data the data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 150, 150, false);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                newBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                newAvatar = stream.toByteArray();
                user.setAvatar(newAvatar);
            } catch (IOException e) {
                Toast.makeText(this, "file not found", Toast.LENGTH_SHORT).show();
            }
            image_selected.setImageURI(imageUri);

        }
    }

    /**
     * Load user object from filename.
     *
     * @param fileName the name of the file
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
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
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
