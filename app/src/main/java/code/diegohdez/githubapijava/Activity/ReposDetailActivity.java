package code.diegohdez.githubapijava.Activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Adapter.PageRepoAdapter;
import code.diegohdez.githubapijava.AsyncTask.DetailsRepo;
import code.diegohdez.githubapijava.Data.DataOfBranches;
import code.diegohdez.githubapijava.Data.DataOfIssues;
import code.diegohdez.githubapijava.Data.DataOfPulls;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Model.Issue;
import code.diegohdez.githubapijava.Model.Pull;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.Utils.Constants.Fields;
import code.diegohdez.githubapijava.Utils.Constants.Intents;
import io.realm.Realm;
import io.realm.RealmList;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.BRANCHES;
import static code.diegohdez.githubapijava.Utils.Constants.API.STATE_ALL;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_ISSUES;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_PULLS;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_REPOS;

public class ReposDetailActivity extends AppCompatActivity {

    private static final String TAG = ReposDetailActivity.class.getSimpleName();

    private Realm realm;
    private AppManager appManager;

    private long repoId;

    PageRepoAdapter pageRepoAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        setContentView(R.layout.activity_repos_detail);
        realm = Realm.getDefaultInstance();
        appManager = AppManager.getOurInstance();

        Bundle bundle = getIntent().getExtras();
        String repoName = bundle.getString(Intents.REPO_NAME);
        repoId = bundle.getLong(Intents.REPO_ID);
        DetailsRepo details = new DetailsRepo(this, repoId);
        details.execute(BASE_URL + USER_REPOS + appManager.getAccount() + "/" + repoName + USER_PULLS + STATE_ALL,
                BASE_URL + USER_REPOS + appManager.getAccount() + "/" +repoName + USER_ISSUES + STATE_ALL,
                BASE_URL + USER_REPOS + appManager.getAccount() + "/" + repoName + BRANCHES);
        pageRepoAdapter = new PageRepoAdapter(getSupportFragmentManager());
        pageRepoAdapter.setId(repoId);
        pageRepoAdapter.setRepoName(repoName);
        viewPager = findViewById(R.id.repo_pager_details);
        viewPager.setAdapter(pageRepoAdapter);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        android.support.v7.app.ActionBar.TabListener tabListener = new android.support.v7.app.ActionBar.TabListener() {
            @Override
            public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {

            }
        };

        actionBar.addTab(actionBar.newTab()
        .setText("Issues")
        .setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab()
        .setText("Pull Request")
        .setTabListener(tabListener));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    }

    public void createAdapter(long id) {
        Repo repo = realm.where(Repo.class).equalTo(Fields.ID, id).findFirst();
        ArrayList<DataOfIssues> issues = DataOfIssues.createList(repo.getIssues());
        ArrayList<DataOfPulls> pulls = DataOfPulls.createList(repo.getPulls());
        ArrayList<DataOfBranches> branches = DataOfBranches.createList(repo.getBranches());
        pageRepoAdapter.setData(issues, pulls, branches);
    }

    public void addIssues(RealmList<Issue> issues) {
        ArrayList<DataOfIssues> list = DataOfIssues.createList(issues);
        pageRepoAdapter.setIssues(list);
    }

    public void addPulls(RealmList<Pull> pulls) {
        ArrayList<DataOfPulls> list= DataOfPulls.createList(pulls);
        pageRepoAdapter.setPulls(list);
    }
}
