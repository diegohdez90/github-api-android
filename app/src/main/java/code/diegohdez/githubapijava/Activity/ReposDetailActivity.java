package code.diegohdez.githubapijava.Activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Adapter.PageRepoAdapter;
import code.diegohdez.githubapijava.AsyncTask.IssuesRepo;
import code.diegohdez.githubapijava.Data.DataOfIssues;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.Utils.Constants.Fields;
import code.diegohdez.githubapijava.Utils.Constants.Intents;
import io.realm.Realm;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_ISSUES;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_REPOS;

public class ReposDetailActivity extends FragmentActivity {

    private static final String TAG = ReposDetailActivity.class.getSimpleName();

    private Realm realm;
    private AppManager appManager;

    PageRepoAdapter pageRepoAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getActionBar();
        setContentView(R.layout.activity_repos_detail);
        realm = Realm.getDefaultInstance();
        appManager = AppManager.getOurInstance();

        Bundle bundle = getIntent().getExtras();
        String repoName = bundle.getString(Intents.REPO_NAME);
        long repoId = bundle.getLong(Intents.REPO_ID);
        IssuesRepo issues = new IssuesRepo(this, repoId);
        issues.execute(BASE_URL + USER_REPOS + appManager.getAccount() + "/" +repoName + USER_ISSUES);
        pageRepoAdapter = new PageRepoAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.repo_pager_details);
        viewPager.setAdapter(pageRepoAdapter);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }
        };

        actionBar.addTab(actionBar.newTab()
        .setText("Issues")
        .setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab()
        .setText("Pull Request"));
    }

    public void createIssuesAdapter(long id) {
        Repo repo = realm.where(Repo.class).equalTo(Fields.ID, id).findFirst();
        ArrayList<DataOfIssues> list = DataOfIssues.createList(repo.getIssues());
        pageRepoAdapter.setList(list);
    }
}
