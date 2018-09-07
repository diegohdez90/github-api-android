package code.diegohdez.githubapijava.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppManager {
    private static AppManager ourInstance;

    public static AppManager getOurInstance() {
        return ourInstance;
    }

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private final String ACCOUNT = "ACCOUNT";
    private String account;
    private final String TOKEN = "TOKEN";
    private String token;

    public void setAccount (String account) {
        this.account = account;
        editor.putString(ACCOUNT, account);
        editor.commit();
    }

    public String getAccount() {
        return account;

    }

    public void setToken (String token) {
        this.token = token;
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public String getToken() {
        return token;
    }

    public static void init (Context context) {
        ourInstance = new AppManager(context.getApplicationContext());
    }

    private AppManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        editor = prefs.edit();
        account = prefs.getString(ACCOUNT, "");
        token = prefs.getString(TOKEN, "");
    }

    public void resetAccount () {
        setAccount("");
    }

    public void logout () {
        setAccount("");
        setToken("");
    }
}
