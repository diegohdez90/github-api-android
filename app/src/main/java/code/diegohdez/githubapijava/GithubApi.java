package code.diegohdez.githubapijava;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.jacksonandroidnetworking.JacksonParserFactory;

import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Migration.Migration;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class GithubApi extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .name("github-api")
                .schemaVersion(2)
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        AppManager.init(getApplicationContext());
    }
}
