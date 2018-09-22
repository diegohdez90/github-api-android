package code.diegohdez.githubapijava;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.gsonparserfactory.GsonParserFactory;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Migration.Migration;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;

public class GithubApi extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient = new OkHttpClient() .newBuilder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        AndroidNetworking.initialize(getApplicationContext(), okHttpClient);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        AndroidNetworking.setParserFactory(new GsonParserFactory(gson));
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration
                .Builder()
                .name("github-api")
                .schemaVersion(11)
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        AppManager.init(getApplicationContext());
    }
}
