package code.diegohdez.githubapijava.Activity;

import android.content.Intent;
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
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;

import java.util.List;

import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.R;
import io.realm.Realm;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.REPOS;
import static code.diegohdez.githubapijava.Utils.Constants.API.USERS;
import static code.diegohdez.githubapijava.Utils.Constants.Result.RESULT_MAIN_GET_TOKEN;
import static code.diegohdez.githubapijava.Utils.Constants.Result.RESULT_OK_GET_TOKEN;


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
        final String username = account.getText().toString();
        ANRequest request = AndroidNetworking
                .get(BASE_URL + USERS + username + REPOS)
                .setPriority(Priority.HIGH)
                .build();
        Toast.makeText(
                getApplicationContext(),
                BASE_URL + USERS + username + REPOS,
                Toast.LENGTH_SHORT
        ).show();
        Log.i(TAG, BASE_URL + USERS + username + REPOS);
        ANResponse response = (ANResponse<Repo>) request.executeForObjectList(Repo.class);
        if (response.isSuccess()) {
            AppManager appManager = AppManager.getOurInstance();
            appManager.setAccount(username);
            final List<Repo> repos = (List<Repo>) response.getResult();
            Toast.makeText(
                    getApplicationContext(),
                    username + " has " + repos.size() + " repositories",
                    Toast.LENGTH_SHORT
            ).show();
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insert(repos);
                }
            });
            realm.close();
            Intent intent = new Intent(getApplicationContext(), ReposActivity.class);
            startActivity(intent);
        } else {
            ANError anError = response.getError();
            Toast.makeText(
                    getApplicationContext(),
                    "Error: " + anError.getErrorDetail() + "\n" +
                            "Body: " + anError.getErrorBody() + "\n" +
                            "Message: " + anError.getMessage() +  "\n" +
                            "Code: " + anError.getErrorCode(),
                    Toast.LENGTH_SHORT)
            .show();
        }
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
                Intent intent = new Intent(getApplicationContext(), GetToken.class);
                startActivityForResult(intent, RESULT_MAIN_GET_TOKEN);
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK_GET_TOKEN:
                Toast.makeText(getApplicationContext(),
                        "Get a token",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
