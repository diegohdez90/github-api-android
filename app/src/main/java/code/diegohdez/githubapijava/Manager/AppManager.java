package code.diegohdez.githubapijava.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static code.diegohdez.githubapijava.Utils.Constants.Numbers.PAGE_ONE;

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
    private final String CURRENT_PAGE = "CURRENT_PAGE";
    private int currentPage;
    private final String ISSUES_PAGE = "ISSUES_PAGE";
    private int issuesPage;
    private final String PULLS_PAGE = "PULLS_PAGE";
    private int pullsPage;
    private final String BRANCHES_PAGE = "BRANCHES_PAGE";
    private int branchesPage;

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

    public void setIssuesPage(int issuesPage) {
        this.issuesPage = issuesPage;
        editor.putInt(ISSUES_PAGE, issuesPage);
        editor.commit();
    }

    public void setPullsPage(int pullsPage) {
        this.pullsPage = pullsPage;
        editor.putInt(PULLS_PAGE, pullsPage);
        editor.commit();
    }

    public void setBranchesPage(int branchesPage) {
        this.branchesPage = branchesPage;
        editor.putInt(BRANCHES_PAGE, branchesPage);
        editor.commit();
    }

    public String getToken() {
        return token;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getCurrentIssuesPage() { return issuesPage; }

    public int getCurrentPullsPage() { return pullsPage; }

    public int getCurrentBranchPage() { return  branchesPage; }

    public static void init (Context context) {
        ourInstance = new AppManager(context.getApplicationContext());
    }

    private AppManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        editor = prefs.edit();
        account = prefs.getString(ACCOUNT, "");
        token = prefs.getString(TOKEN, "");
        currentPage = prefs.getInt(CURRENT_PAGE, PAGE_ONE);

        /*
          For NavBottom
         */

        issuesPage = prefs.getInt(ISSUES_PAGE, PAGE_ONE);
        pullsPage = prefs.getInt(PULLS_PAGE, PAGE_ONE);
        branchesPage = prefs.getInt(BRANCHES_PAGE, PAGE_ONE);
    }

    public void resetAccount () {
        setAccount("");
    }

    public void resetRepoDetailsPage() {
        setIssuesPage(1);
        setPullsPage(1);
        setBranchesPage(1);
    }

    public void initPager () { setCurrentPage(1); }

    public void logout () {
        setAccount("");
        setToken("");
        setCurrentPage(1);
    }
}
