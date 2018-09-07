package code.diegohdez.githubapijava.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import code.diegohdez.githubapijava.R;
import io.realm.Realm;

public class ReposActivity extends AppCompatActivity {

    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        realm = Realm.getDefaultInstance();
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
}
