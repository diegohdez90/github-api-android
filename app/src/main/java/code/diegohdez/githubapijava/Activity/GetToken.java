package code.diegohdez.githubapijava.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;

import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.R;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER;
import static code.diegohdez.githubapijava.Utils.Constants.Result.RESULT_OK_GET_TOKEN;

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
                encode = android.util.Base64.encodeToString((username + ":" + password).getBytes("UTF-8"), Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ANRequest request =  AndroidNetworking
                    .get(BASE_URL + USER)
                    .addHeaders("Authorization", "Basic " +  encode)
                    .setPriority(Priority.HIGH)
                    .build();
            ANResponse response = request.executeForJSONObject();
            if (response.isSuccess()) {
                AppManager appManager = AppManager.getOurInstance();
                appManager.setToken(encode.toString());
                Intent intent = new Intent();
                setResult(RESULT_OK_GET_TOKEN, intent);
                finish();
            } else {
                ANError error = response.getError();
                Toast.makeText(getApplicationContext(),
                        "Error: " + error.getErrorDetail() + "\n" +
                                "Body: " + error.getErrorBody() + "\n" +
                                "Message: " + error.getMessage() +  "\n" +
                                "Code: " + error.getErrorCode(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Please provide username a password",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

}
