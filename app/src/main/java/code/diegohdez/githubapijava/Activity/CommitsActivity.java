package code.diegohdez.githubapijava.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import code.diegohdez.githubapijava.AsyncTask.CommitsBranch;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Model.Commit;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.Utils.Constants.Intents;
import io.realm.RealmList;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.COMMITS;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_REPOS;

public class CommitsActivity extends AppCompatActivity {

    private Long id;
    private String repoName;
    private String branchName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commits);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong(Intents.REPO_ID);
        repoName = bundle.getString(Intents.REPO_NAME);
        branchName = bundle.getString(Intents.BRANCH_NAME);
        CommitsBranch commitsBranch = new CommitsBranch(this, id, branchName);
        commitsBranch.execute(BASE_URL + USER_REPOS + AppManager.getOurInstance().getAccount() + "/" + repoName + COMMITS + "?sha=" + branchName);
    }

    public void addCommits(RealmList<Commit> list) {
        Toast.makeText(
                getApplicationContext(),
                "Get " + list.size() + " commits",
                Toast.LENGTH_SHORT).show();
    }
}
