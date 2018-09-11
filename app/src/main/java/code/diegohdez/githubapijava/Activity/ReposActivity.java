package code.diegohdez.githubapijava.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Adapter.ReposAdapter;
import code.diegohdez.githubapijava.Data.DataOfRepos;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.R;
import io.realm.Realm;
import io.realm.RealmResults;

import static code.diegohdez.githubapijava.Utils.Constants.Result.RESULT_MAIN_GET_TOKEN;
import static code.diegohdez.githubapijava.Utils.Constants.Result.RESULT_OK_GET_TOKEN;

public class ReposActivity extends AppCompatActivity {

    private static final String TAG = ReposActivity.class.getSimpleName();
    private Realm realm;
    private AppManager appManager;
    private String account;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        Toolbar toolbar = findViewById(R.id.repos_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        realm = Realm.getDefaultInstance();
        appManager = AppManager.getOurInstance();
        account = appManager.getAccount();
        recyclerView = findViewById(R.id.reposList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        RealmResults<Repo> repos = realm.where(Repo.class).equalTo("owner.login", account).findAll();
        ArrayList<DataOfRepos> list = DataOfRepos.createRepoList(repos);
        adapter = new ReposAdapter(list, account, ReposActivity.this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (realm.isClosed()) realm = Realm.getDefaultInstance();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu != null) this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.repos_menu, menu);

        MenuItem menuItemSearch = menu.findItem(R.id.search_repos);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItemSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "change: " + newText);
                return true;
            }
        });

        if (AppManager.getOurInstance().getToken().length() > 0) menu.findItem(R.id.login_from_repos).setVisible(false);
        else menu.findItem(R.id.logout_from_repos).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout_from_repos:
                AppManager.getOurInstance().logout();
                realm.close();
                Realm.deleteRealm(Realm.getDefaultConfiguration());
                Intent intentToLogout = new Intent(this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intentToLogout);
                finish();
                return true;
            case R.id.login_from_repos:
                Intent intentToLogin = new Intent(getApplicationContext(), GetTokenActivity.class);
                startActivityForResult(intentToLogin, RESULT_MAIN_GET_TOKEN);
                return true;
            default:
                return false;
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
                menu.clear();
                onCreateOptionsMenu(menu);
                break;
        }
    }
}
