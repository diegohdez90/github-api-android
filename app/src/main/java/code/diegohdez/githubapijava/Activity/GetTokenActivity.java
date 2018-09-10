package code.diegohdez.githubapijava.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import code.diegohdez.githubapijava.AsyncTask.GetToken;
import code.diegohdez.githubapijava.R;
import okhttp3.Credentials;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER;

public class GetTokenActivity extends AppCompatActivity {

    private static final String TAG = GetTokenActivity.class.getSimpleName();

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
            String credentials = Credentials.basic(username,password);
            GetToken asyncGetToken = new GetToken(GetTokenActivity.this, credentials);
            asyncGetToken.execute(BASE_URL + USER);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Please provide username a password",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public static void responseMessage(GetTokenActivity context, String message) {
        Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT)
                .show();
    }
}
