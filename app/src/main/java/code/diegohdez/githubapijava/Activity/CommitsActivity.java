package code.diegohdez.githubapijava.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Adapter.CommitsAdapter;
import code.diegohdez.githubapijava.AsyncTask.CommitsBranch;
import code.diegohdez.githubapijava.Data.DataOfCommits;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Model.Branch;
import code.diegohdez.githubapijava.Model.Commit;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.ScrollListener.BranchPaginationScrollListener;
import code.diegohdez.githubapijava.Utils.Constants.Fields;
import code.diegohdez.githubapijava.Utils.Constants.Intents;
import io.realm.Realm;
import io.realm.RealmList;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.COMMITS;
import static code.diegohdez.githubapijava.Utils.Constants.API.PAGE_SIZE;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_REPOS;
import static code.diegohdez.githubapijava.Utils.Constants.Numbers.PAGE_ONE;

public class CommitsActivity extends AppCompatActivity {

    private Long id;
    private String repoName;
    private String branchName;
    private CommitsAdapter adapter;
    private int page = PAGE_ONE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private CommitsActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commits);
        context = this;
        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong(Intents.REPO_ID);
        repoName = bundle.getString(Intents.REPO_NAME);
        branchName = bundle.getString(Intents.BRANCH_NAME);
        CommitsBranch commitsBranch = new CommitsBranch(this, id, branchName);
        commitsBranch.execute(BASE_URL + USER_REPOS + AppManager.getOurInstance().getAccount() + "/" + repoName + COMMITS + "?sha=" + branchName);
        adapter = new CommitsAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.commits_list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new BranchPaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadBranches() {
                isLoading = true;
                page++;
                CommitsBranch commitsBranch = new CommitsBranch(context, id, branchName, page);
                commitsBranch.execute(BASE_URL + USER_REPOS + AppManager.getOurInstance().getAccount() + "/" + repoName + COMMITS + "?sha=" + branchName);
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


    public void setCommits(RealmList<Commit> list) {
        ArrayList<DataOfCommits> commits = DataOfCommits.createList(list);
        adapter.addCommits(commits);
        adapter.addLoading();
    }

    public void addCommits(RealmList<Commit> list) {
        adapter.deleteLoading();
        isLoading = false;
        ArrayList<DataOfCommits> commits = DataOfCommits.createList(list);
        this.adapter.addCommits(commits);
        if (list.size() < PAGE_SIZE) isLastPage = true;
        else adapter.addLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Realm realm = Realm.getDefaultInstance();
        final Repo repo = realm.where(Repo.class).equalTo(Fields.ID, id).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Branch branch = repo.getBranches().where().equalTo(Fields.BRANCH_NAME, branchName).findFirst();
                branch.getCommits().deleteAllFromRealm();
            }
        });
    }
}
