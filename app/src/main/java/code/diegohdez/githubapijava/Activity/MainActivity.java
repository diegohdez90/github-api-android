package code.diegohdez.githubapijava.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import code.diegohdez.githubapijava.R;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.USERS;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        account = findViewById(R.id.account);
    }

    public void getAccount(View view) {
        String username = account.getText().toString();
        ANRequest request = AndroidNetworking
                .get(BASE_URL + USERS + username)
                .setPriority(Priority.HIGH)
                .build();
        request.getAsJSONObject(new JSONObjectRequestListener() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, response.toString());
            }

            @Override
            public void onError(ANError anError) {
                Log.i(TAG, anError.getErrorBody());
                Log.i(TAG, anError.getErrorDetail());
                Log.i(TAG, anError.getMessage());
            }
        });
    }
}
