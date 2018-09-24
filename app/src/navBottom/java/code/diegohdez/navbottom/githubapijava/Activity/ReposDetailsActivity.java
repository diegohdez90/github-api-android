package code.diegohdez.navbottom.githubapijava.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import code.diegohdez.githubapijava.AsyncTask.DetailsRepo;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.Utils.Constants.Fields;
import code.diegohdez.githubapijava.Utils.Constants.Intents;
import code.diegohdez.navbottom.githubapijava.Adapter.BranchesFragment;
import code.diegohdez.navbottom.githubapijava.Adapter.IssuesFragment;
import code.diegohdez.navbottom.githubapijava.Adapter.PullsFragment;
import io.realm.Realm;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.BRANCHES;
import static code.diegohdez.githubapijava.Utils.Constants.API.STATE_ALL;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_ISSUES;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_PULLS;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_REPOS;

public class ReposDetailsActivity extends AppCompatActivity {

    public static final String ARG_ID = "ID";
    private long repoId;
    public static final String ARG_REPO_NAME = "REPO_NAME";
    private String repoName;

    private AppManager appManager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos_details);

        Toolbar toolbar = findViewById(R.id.repo_details_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appManager = AppManager.getOurInstance();
        Bundle bundle = getIntent().getExtras();

        repoName = bundle.getString(Intents.REPO_NAME);
        repoId = bundle.getLong(Intents.REPO_ID);

        DetailsRepo details = new DetailsRepo(this, repoId);
        details.execute(BASE_URL + USER_REPOS + appManager.getAccount() + "/" + repoName + USER_PULLS + STATE_ALL,
                BASE_URL + USER_REPOS + appManager.getAccount() + "/" +repoName + USER_ISSUES + STATE_ALL,
                BASE_URL + USER_REPOS + appManager.getAccount() + "/" + repoName + BRANCHES);

        bottomNavigationView = findViewById(R.id.repo_details_nav_bottom);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle args = new Bundle();
                args.putLong(ARG_ID, repoId);
                args.putString(ARG_REPO_NAME, repoName);
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.issues_menu:
                        fragment = code.diegohdez.navbottom.githubapijava.Adapter.IssuesFragment.newInstance();
                        break;
                    case R.id.pulls_menu:
                        fragment = PullsFragment.newInstance();
                        break;
                    case R.id.branches_menu:
                        fragment = BranchesFragment.newInstance();
                        break;
                }
                if (fragment != null) {
                    fragment.setArguments(args);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_repo_details, fragment);
                    fragmentTransaction.commit();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void createFragment() {
        Bundle args = new Bundle();
        args.putLong(ARG_ID, repoId);
        args.putString(ARG_REPO_NAME, repoName);

        IssuesFragment initFragment = IssuesFragment.newInstance();
        initFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_repo_details, initFragment);
        fragmentTransaction.commit();
        ProgressBar initLoader = findViewById(R.id.issues_init_loader);
        initLoader.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Realm realm = Realm.getDefaultInstance();
        final Repo repo = realm.where(Repo.class).equalTo(Fields.ID, repoId).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                repo.getIssues().deleteAllFromRealm();
                repo.getPulls().deleteAllFromRealm();
                repo.getBranches().deleteAllFromRealm();
            }
        });
        realm.close();
        AppManager.getOurInstance().resetRepoDetailsPage();
    }
}
