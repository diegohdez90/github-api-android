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
import io.realm.Realm;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.USERS;
import static code.diegohdez.githubapijava.Utils.Constants.API.getRepos;
import static code.diegohdez.githubapijava.Utils.Constants.Result.RESULT_MAIN_GET_TOKEN;
import static code.diegohdez.githubapijava.Utils.Constants.Result.RESULT_OK_GET_TOKEN;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText account;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        account = findViewById(R.id.account);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((AppManager.getOurInstance().getToken().length() > 0)) {
            Toast.makeText(
                    getApplicationContext(),
                    "Session Initialized",
                    Toast.LENGTH_SHORT).show();
        }
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
        if (menu != null) this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        if (AppManager.getOurInstance().getToken().length() > 0) menu.findItem(R.id.login_main).setVisible(false);
        else menu.findItem(R.id.logout_main).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login_main:
                Intent intent = new Intent(getApplicationContext(), GetTokenActivity.class);
                startActivityForResult(intent, RESULT_MAIN_GET_TOKEN);
                return true;
            case R.id.logout_main:
                menu.clear();
                AppManager.getOurInstance().logout();
                Realm.deleteRealm(Realm.getDefaultConfiguration());
                Intent intentToLogout = new Intent(this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intentToLogout);
                finish();
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
                menu.clear();
                onCreateOptionsMenu(this.menu);
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
