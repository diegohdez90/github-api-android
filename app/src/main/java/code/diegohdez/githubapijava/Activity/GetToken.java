package code.diegohdez.githubapijava.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import code.diegohdez.githubapijava.R;

public class GetToken extends AppCompatActivity {

    private static final String TAG = GetToken.class.getSimpleName();

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_token);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
    }


    public void getToken(View view) {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (username.length() > 0 && password.length() > 0) {
            String encode = "";
            try {
                encode = android.util.Base64.encodeToString((username + ":" + username).getBytes(), Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.i(TAG, "token: "+ encode);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Please provide username a password",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

}
