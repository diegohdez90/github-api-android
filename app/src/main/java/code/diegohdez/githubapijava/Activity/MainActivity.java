package code.diegohdez.githubapijava.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import code.diegohdez.githubapijava.Manager.AppManager;
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
                AppManager appManager = AppManager.getOurInstance();
                try {
                    appManager.setAccount(response.getString("login"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ANError anError) {
                Log.i(TAG, anError.getErrorBody());
                Log.i(TAG, anError.getErrorDetail());
                Log.i(TAG, anError.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.openGetToken:
                Toast.makeText(getApplicationContext(), "Get Token", Toast.LENGTH_SHORT).show();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
