package code.diegohdez.githubapijava.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import code.diegohdez.githubapijava.Adapter.ReposAdapter;
import code.diegohdez.githubapijava.AsyncTask.RepoInfo;
import code.diegohdez.githubapijava.AsyncTask.Repos;
import code.diegohdez.githubapijava.Data.DataOfRepos;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Model.Owner;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.ScrollListener.ReposPaginationScrollListener;
import code.diegohdez.githubapijava.Utils.Constants.API;
import io.realm.Realm;
import io.realm.RealmResults;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.PAGE_SIZE;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_REPOS;
import static code.diegohdez.githubapijava.Utils.Constants.API.WATCH_REPO;
import static code.diegohdez.githubapijava.Utils.Constants.Result.RESULT_MAIN_GET_TOKEN;
import static code.diegohdez.githubapijava.Utils.Constants.Result.RESULT_OK_GET_TOKEN;

public class ReposActivity extends AppCompatActivity {

    private static final String TAG = ReposActivity.class.getSimpleName();
    private Realm realm;
    private AppManager appManager;
    private String account;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    ReposAdapter adapter;
    private Menu menu;

    AlertDialog.Builder builder;
    LayoutInflater inflaterRepoDetails;
    View viewRepoDetailsModal;

    private boolean isWatched = false;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int page = 1;
    private int TOTAL_PAGES = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        Toolbar toolbar = findViewById(R.id.repos_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        builder = new AlertDialog.Builder(this);
        inflaterRepoDetails = this.getLayoutInflater();
        viewRepoDetailsModal = inflaterRepoDetails.inflate(R.layout.repo_details_modal, null);
        realm = Realm.getDefaultInstance();
        appManager = AppManager.getOurInstance();
        account = appManager.getAccount();
        Owner owner = realm.where(Owner.class).equalTo("login", account).findFirst();
        TOTAL_PAGES = (int) Math.ceil((double) owner.getRepos() / PAGE_SIZE);
        recyclerView = findViewById(R.id.reposList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RealmResults<Repo> repos = realm.where(Repo.class).equalTo("owner.login", account).findAll();
        ArrayList<DataOfRepos> list = DataOfRepos.createRepoList(repos);
        adapter = new ReposAdapter(list, account, this);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new ReposPaginationScrollListener(layoutManager) {
            @Override
            protected void loadRepos() {
                isLoading = true;
                page++;
                AppManager.getOurInstance().setCurrentPage(page);
                Repos repos = new Repos(ReposActivity.this, page);
                repos.execute(API.getRepos(account));
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getOurInstance().initPager();
        final RealmResults<Repo> rows = realm.where(Repo.class).equalTo("owner.login", account).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                rows.deleteAllFromRealm();
            }
        });
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
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getOurInstance().initPager();
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

    public void successLoader(String message, int status, List<Repo> list) {
        Realm realm = Realm.getDefaultInstance();
        adapter.deleteLoading();
        isLoading = false;
        List<DataOfRepos> repos = DataOfRepos.createRepoList(list);
        adapter.addAll(repos);
        if (page <= TOTAL_PAGES) adapter.addLoading();
        else isLastPage = true;
    }

    public void setRepoStatus(String name, long watchers, long stars, long forks) {
        final String repoName = name;
        TextView watchTextView = viewRepoDetailsModal.findViewById(R.id.watches);
        watchTextView.setText(Long.toString(watchers));
        ImageView watchRepo = viewRepoDetailsModal.findViewById(R.id.watch);
        watchRepo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if (isWatched) {
                    Log.i(TAG, "To delete: " + BASE_URL + USER_REPOS + account + "/" + repoName + WATCH_REPO);
                    Toast.makeText(getApplicationContext(),
                            "To delete: " + BASE_URL + USER_REPOS + account + "/" + repoName + WATCH_REPO,
                            Toast.LENGTH_SHORT).show();
                    ANRequest.DeleteRequestBuilder builder = AndroidNetworking
                            .delete(BASE_URL + USER_REPOS + account + "/" + repoName + "/" + WATCH_REPO);
                    String token = AppManager.getOurInstance().getToken();
                    if (token.length() > 0)
                        builder.addHeaders("Authorization", token);
                    builder.build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // /repos/:owner/:repo
                                    Log.d(TAG, "onResponse delete: " + response);
                                    RepoInfo updateInfo = new RepoInfo(ReposActivity.this);
                                    updateInfo.execute(BASE_URL + USER_REPOS + account + "/" + repoName);
                                    isSubscribed(false);
                                }

                                @Override
                                public void onError(ANError anError) {
                                    String message = "Delete: " + "\n" +
                                            "Error: " + anError.getErrorDetail() + "\n" +
                                            "Body: " + anError.getErrorBody() + "\n" +
                                            "Message: " + anError.getMessage() + "\n" +
                                            "Code: " + anError.getErrorCode();
                                    Log.e(TAG, message);
                                }
                            });
                } else {
                    Log.i(TAG, "To watch: " + BASE_URL + USER_REPOS + account + "/" + repoName + WATCH_REPO);
                    Toast.makeText(getApplicationContext(),
                            "To watch: " + BASE_URL + USER_REPOS + account + "/" + repoName + WATCH_REPO,
                            Toast.LENGTH_SHORT).show();
                    ANRequest.PutRequestBuilder builder = AndroidNetworking
                            .put(BASE_URL + USER_REPOS + account + "/" + repoName + WATCH_REPO);
                    String token = AppManager.getOurInstance().getToken();
                    if (token.length() > 0)
                        builder.addHeaders("Authorization", token);
                    builder.build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // /repos/:owner/:repo
                                    Log.d(TAG, "onResponse put: " + response);
                                    RepoInfo updateInfo = new RepoInfo(ReposActivity.this);
                                    updateInfo.execute(BASE_URL + USER_REPOS + account + "/" +repoName);
                                    isSubscribed(true);
                                }

                                @Override
                                public void onError(ANError anError) {
                                    String message = "Put:" + "\n" +
                                            "Error: " + anError.getErrorDetail() + "\n" +
                                            "Body: " + anError.getErrorBody() + "\n" +
                                            "Message: " + anError.getMessage() + "\n" +
                                            "Code: " + anError.getErrorCode();
                                    Log.e(TAG, message);
                                }
                            });
                }
            }
        });
        TextView starsTextView = viewRepoDetailsModal.findViewById(R.id.stars);
        starsTextView.setText(Long.toString(stars));
        TextView forksTextView = viewRepoDetailsModal.findViewById(R.id.forks);
        forksTextView.setText(Long.toString(forks));
        builder.setView(viewRepoDetailsModal)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        isWatched = false;
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void isSubscribed(boolean subscribed) {
        ImageView watchRepo = viewRepoDetailsModal.findViewById(R.id.watch);
        TextView watchTextView = viewRepoDetailsModal.findViewById(R.id.watched_text);
        if (subscribed) {
            watchRepo.setImageResource(R.mipmap.baseline_visibility_off_black_48);
            watchTextView.setText("Unwatch");
            isWatched = true;
        } else {
            watchRepo.setImageResource(R.mipmap.baseline_visibility_black_48);
            watchTextView.setText("Watch");
            isWatched = false;
        }
    }

    public void updateCounter(String name) {
        Repo repo = realm.where(Repo.class).equalTo("name", name).findFirst();
        TextView watchTextView = viewRepoDetailsModal.findViewById(R.id.watches);
        watchTextView.setText(Long.toString(repo.getWatchers()));
    }
}
