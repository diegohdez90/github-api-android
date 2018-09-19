package code.diegohdez.githubapijava.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.Utils.Constants.Intents;

public class CommitsActivity extends AppCompatActivity {

    private Long id;
    private String repoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commits);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong(Intents.REPO_ID);
        repoName = bundle.getString(Intents.REPO_NAME);

    }
}
