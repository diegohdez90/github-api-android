package code.diegohdez.githubapijava;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.jacksonandroidnetworking.JacksonParserFactory;

import code.diegohdez.githubapijava.Manager.AppManager;

public class GithubApi extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AppManager.init(getApplicationContext());
    }
}
