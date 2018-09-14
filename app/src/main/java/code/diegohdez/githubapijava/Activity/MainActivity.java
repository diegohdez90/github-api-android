package code.diegohdez.githubapijava.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import code.diegohdez.githubapijava.AsyncTask.Repos;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.R;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.USERS;
import static code.diegohdez.githubapijava.Utils.Constants.API.getRepos;
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
        final String username = account.getText().toString().trim();
        if (username.length() > 0) {
            AppManager appManager = AppManager.getOurInstance();
            appManager.setAccount(username);
            Repos asyncRepos = new Repos(MainActivity.this);
            asyncRepos.execute(getRepos(username), BASE_URL + USERS + username);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Provide a github account",
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
                Intent intent = new Intent(getApplicationContext(), GetTokenActivity.class);
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

    public static void successRepos(Context context, String message, int status) {
        Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT)
                .show();
        if (status == 200) {
            Intent intent = new Intent(context, ReposActivity.class);
            context.startActivity(intent);
        }
    }
}
