package code.diegohdez.githubapijava.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import code.diegohdez.githubapijava.Adapter.ReposAdapter;
import code.diegohdez.githubapijava.AsyncTask.ForkRepo;
import code.diegohdez.githubapijava.AsyncTask.RepoInfo;
import code.diegohdez.githubapijava.AsyncTask.Repos;
import code.diegohdez.githubapijava.AsyncTask.SearchRepo;
import code.diegohdez.githubapijava.AsyncTask.StarRepo;
import code.diegohdez.githubapijava.AsyncTask.WatchRepo;
import code.diegohdez.githubapijava.Data.DataOfRepos;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Model.Owner;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.ScrollListener.ReposPaginationScrollListener;
import code.diegohdez.githubapijava.Utils.Constants.API;
import code.diegohdez.githubapijava.Utils.Constants.Dialog;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.COLON;
import static code.diegohdez.githubapijava.Utils.Constants.API.FORK_REPO;
import static code.diegohdez.githubapijava.Utils.Constants.API.PAGE_SIZE;
import static code.diegohdez.githubapijava.Utils.Constants.API.PLUS;
import static code.diegohdez.githubapijava.Utils.Constants.API.QUERY;
import static code.diegohdez.githubapijava.Utils.Constants.API.REPOSITORIES;
import static code.diegohdez.githubapijava.Utils.Constants.API.SEARCH;
import static code.diegohdez.githubapijava.Utils.Constants.API.STAR;
import static code.diegohdez.githubapijava.Utils.Constants.API.STAR_REPO;
import static code.diegohdez.githubapijava.Utils.Constants.API.UN_STAR;
import static code.diegohdez.githubapijava.Utils.Constants.API.UN_WATCH;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_REPOS;
import static code.diegohdez.githubapijava.Utils.Constants.API.WATCH;
import static code.diegohdez.githubapijava.Utils.Constants.API.WATCH_REPO;
import static code.diegohdez.githubapijava.Utils.Constants.Fields.LOGIN;
import static code.diegohdez.githubapijava.Utils.Constants.Fields.OWNER_LOGIN;
import static code.diegohdez.githubapijava.Utils.Constants.Fields.REPO_NAME;
import static code.diegohdez.githubapijava.Utils.Constants.Numbers.PAGE_ONE;
import static code.diegohdez.githubapijava.Utils.Constants.Result.RESULT_MAIN_GET_TOKEN;
import static code.diegohdez.githubapijava.Utils.Constants.Result.RESULT_OK_GET_TOKEN;

public class ReposActivity extends AppCompatActivity {

    private static final String TAG = ReposActivity.class.getSimpleName();
    private Realm realm;
    private String account;
    ReposAdapter adapter;
    private Menu menu;

    AlertDialog dialog;
    AlertDialog.Builder builder;
    LayoutInflater inflaterRepoDetails;
    View viewRepoDetailsModal;

    private boolean isWatched = false;
    private boolean isStarred = false;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int page = PAGE_ONE;
    private int TOTAL_PAGES = PAGE_ONE;

    private long total_repos = 0;
    private ProgressBar loadReposBar;
    private boolean searchRepos = false;
    private boolean searchInAccount = false;
    private String queryTextEdit = "";

    @SuppressLint("InflateParams")
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
        builder.setView(viewRepoDetailsModal)
            .setPositiveButton(Dialog.CLOSE_REPO_DETAIL_DIALOG, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    isWatched = false;
                    isStarred = false;
                }
            });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isWatched = false;
                isStarred = false;
            }
        });
        dialog = builder.create();
        realm = Realm.getDefaultInstance();
        AppManager appManager = AppManager.getOurInstance();
        account = appManager.getAccount();
        Owner owner = realm.where(Owner.class).equalTo(LOGIN, account).findFirst();
        if (owner != null) {
            TOTAL_PAGES = (owner.getRepos() > 0) ? (int) Math.ceil((double) owner.getRepos() / PAGE_SIZE) : 0;
            total_repos = owner.getRepos();
        }
        loadReposBar = findViewById(R.id.loading_repos);
        RecyclerView recyclerView = findViewById(R.id.reposList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RealmResults<Repo> repos = realm.where(Repo.class).equalTo(OWNER_LOGIN, account).findAll();
        ArrayList<DataOfRepos> list = DataOfRepos.createRepoList(repos);
        adapter = new ReposAdapter(list, account, this);
        if (list.size() == PAGE_SIZE) adapter.addLoading();
        recyclerView.setAdapter(adapter);
        loadReposBar.setVisibility(View.GONE);
        recyclerView.addOnScrollListener(new ReposPaginationScrollListener(layoutManager) {
            @Override
            protected void loadRepos() {
                isLoading = true;
                page++;
                AppManager.getOurInstance().setCurrentPage(page);
                Toast.makeText(
                        getApplicationContext(),
                        "Get repos for page: " + page,
                        Toast.LENGTH_SHORT).show();
                if (searchRepos) {
                    SearchRepo searchRepo = new SearchRepo(ReposActivity.this, page);
                    searchRepo.execute(BASE_URL + SEARCH + REPOSITORIES + QUERY + queryTextEdit + PLUS + USER + COLON + account);
                } else {
                    Repos repos = new Repos(ReposActivity.this, page);
                    repos.execute(API.getRepos(account));
                }
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
        if (realm.isClosed()) realm = Realm.getDefaultInstance();
        final RealmResults<Repo> result = realm.where(Repo.class).equalTo(OWNER_LOGIN, account).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                result.deleteAllFromRealm();
            }
        });
        AppManager.getOurInstance().resetAccount();
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
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItemSearch);
        searchView.setQueryHint("Search in " + account);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchRepos) {
                    loadReposBar.setVisibility(View.VISIBLE);
                    searchInAccount = true;
                    searchRepos = true;
                    queryTextEdit = query;
                    if (realm.isClosed()) realm = Realm.getDefaultInstance();
                    final RealmResults<Repo> result = realm.where(Repo.class).equalTo(OWNER_LOGIN, account).findAll();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            result.deleteAllFromRealm();
                        }
                    });
                    isLoading = false;
                    isLastPage = false;
                    page = 1;
                    AppManager.getOurInstance().setCurrentPage(page);
                    adapter.clear();
                    SearchRepo searchRepo = new SearchRepo(ReposActivity.this);
                    searchRepo.execute(BASE_URL + SEARCH + REPOSITORIES + QUERY + queryTextEdit + PLUS + USER + COLON + account);
                    realm.close();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                if (searchRepos && !text.equals(queryTextEdit)) {
                    searchRepos = false;
                }
                if (searchInAccount && text.length() == 0) {
                    loadReposBar.setVisibility(View.VISIBLE);
                    searchRepos = false;
                    if (realm.isClosed()) realm = Realm.getDefaultInstance();
                    final RealmResults<Repo> result = realm.where(Repo.class).equalTo(OWNER_LOGIN, account).findAll();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            result.deleteAllFromRealm();
                        }
                    });
                    isLoading = false;
                    isLastPage = false;
                    page = 1;
                    AppManager.getOurInstance().setCurrentPage(page);
                    adapter.clear();
                    Repos repos = new Repos(ReposActivity.this, page);
                    repos.execute(API.getRepos(account));
                    return false;
                }
                return false;
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

    public void successLoader(List<Repo> list) {
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
                WatchRepo watchRepo = new WatchRepo(isWatched, ReposActivity.this, repoName);
                watchRepo.execute(BASE_URL + USER_REPOS + account + "/" + repoName + WATCH_REPO);
            }
        });
        ImageView starRepo = viewRepoDetailsModal.findViewById(R.id.star);
        starRepo.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                StarRepo starRepo = new StarRepo(isStarred, ReposActivity.this, repoName);
                starRepo.execute(BASE_URL + USER + STAR_REPO + "/" + account + "/" + repoName);
            }
        });
        ImageView forkRepo = viewRepoDetailsModal.findViewById(R.id.fork);
        forkRepo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForkRepo forkRepo = new ForkRepo(ReposActivity.this, repoName);
                forkRepo.execute(BASE_URL + USER_REPOS + account + "/" + repoName + FORK_REPO);
            }
        });
        TextView starsTextView = viewRepoDetailsModal.findViewById(R.id.stars);
        starsTextView.setText(Long.toString(stars));
        TextView forksTextView = viewRepoDetailsModal.findViewById(R.id.forks);
        forksTextView.setText(Long.toString(forks));
        dialog.show();

    }

    public void isSubscribed(boolean subscribed) {
        ImageView watchRepo = viewRepoDetailsModal.findViewById(R.id.watch);
        TextView watchTextView = viewRepoDetailsModal.findViewById(R.id.watched_text);
        if (subscribed) {
            watchRepo.setImageResource(R.mipmap.baseline_visibility_off_black_48);
            watchTextView.setText(UN_WATCH);
            isWatched = true;
        } else {
            watchRepo.setImageResource(R.mipmap.baseline_visibility_black_48);
            watchTextView.setText(WATCH);
            isWatched = false;
        }
    }

    public void updateRepoAfterWatched(boolean isWatched, String name, String message) {
        Toast.makeText(
                getApplicationContext(),
                message,
                Toast.LENGTH_SHORT).show();
        RepoInfo updateInfo = new RepoInfo(ReposActivity.this);
        updateInfo.execute(BASE_URL + USER_REPOS + account + "/" + name);
        isSubscribed(isWatched);
    }

    public void updateRepoAfterStarred(boolean isStarred, String name, String message) {
        Toast.makeText(
                getApplicationContext(),
                message,
                Toast.LENGTH_SHORT).show();
        RepoInfo updateInfo = new RepoInfo(ReposActivity.this);
        updateInfo.execute(BASE_URL + USER_REPOS + account + "/" +  name);
        isStarred(isStarred);
    }

    public void isStarred(boolean isStarred) {
        ImageView starRepo = viewRepoDetailsModal.findViewById(R.id.star);
        TextView starred = viewRepoDetailsModal.findViewById(R.id.star_text);
        if (isStarred) {
            starRepo.setImageResource(R.mipmap.baseline_star_black_48);
            starred.setText(UN_STAR);
            this.isStarred = true;
        } else {
            starRepo.setImageResource(R.mipmap.baseline_star_border_black_48);
            starred.setText(STAR);
            this.isStarred = false;
        }
    }

    public void displayMessage(String message, String name) {
        Toast.makeText(
                getApplicationContext(),
                message,
                Toast.LENGTH_SHORT).show();
        RepoInfo updateInfo = new RepoInfo(ReposActivity.this);
        updateInfo.execute(BASE_URL + USER_REPOS + account + "/" +  name);
    }

    public void updateCounter(String name) {
        Repo repo = realm.where(Repo.class).equalTo(REPO_NAME, name).findFirst();
        TextView watchTextView = viewRepoDetailsModal.findViewById(R.id.watches);
        watchTextView.setText(Long.toString(repo.getWatchers()));
        TextView starTextView = viewRepoDetailsModal.findViewById(R.id.stars);
        starTextView.setText(Long.toString(repo.getStars()));
        TextView forkTextView = viewRepoDetailsModal.findViewById(R.id.forks);
        forkTextView.setText(Long.toString(repo.getForks()));
    }

    public void initSearch(RealmList<Repo> repos, long total_repos) {
        Toast.makeText(getApplicationContext(),
                "We found " + total_repos + " repos",
                Toast.LENGTH_SHORT).show();
        TOTAL_PAGES = (total_repos > 0) ? (int) Math.ceil((double) total_repos / PAGE_SIZE) : 0;
        ArrayList<DataOfRepos> list = DataOfRepos.createRepoList(repos);
        adapter.addAll(list);
        loadReposBar.setVisibility(View.GONE);
        if (page <= TOTAL_PAGES) adapter.addLoading();
        else isLastPage = true;
    }

    public void addSearch(RealmList<Repo> repos) {
        adapter.deleteLoading();
        isLoading = false;
        List<DataOfRepos> list = DataOfRepos.createRepoList(repos);
        adapter.addAll(list);
        if (page <= TOTAL_PAGES) adapter.addLoading();
        else isLastPage = true;
    }

    public void initList(List<Repo> repos) {
        TOTAL_PAGES =  (int) Math.ceil((double) total_repos / PAGE_SIZE);
        ArrayList<DataOfRepos> list = DataOfRepos.createRepoList(repos);
        adapter.addAll(list);
        loadReposBar.setVisibility(View.GONE);
        if (page <= TOTAL_PAGES) adapter.addLoading();
        else isLastPage = true;
    }
}
