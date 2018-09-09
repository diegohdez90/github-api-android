package code.diegohdez.githubapijava.AsyncTask;

import android.os.AsyncTask;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;

import java.util.List;

import code.diegohdez.githubapijava.Activity.MainActivity;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.Utils.Request.API;
import io.realm.Realm;

public class Repos extends AsyncTask<String, Void, ANResponse<List<Repo>>> {

    private Realm realm;
    private MainActivity context;
    API api;
    private int page = 1;

    public Repos(MainActivity context) {
        realm = Realm.getDefaultInstance();
        this.context = context;
        api = new API();
    }

    public Repos(MainActivity context, int page) {
        realm = Realm.getDefaultInstance();
        this.context = context;
        this.page = page;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ANResponse<List<Repo>> doInBackground(String... urls) {
        ANRequest request = API.getRepos(urls[0]);
        return (ANResponse<List<Repo>>) request.executeForObjectList(Repo.class);
    }

    @Override
    protected void onPostExecute(ANResponse<List<Repo>> response) {
        String message = "";
        super.onPostExecute(response);
        if (response.isSuccess()) {
            final List<Repo> repos = (List<Repo>) response.getResult();
            if (realm.isClosed()) {
                realm = Realm.getDefaultInstance();
            }
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insert(repos);
                }
            });
            message = AppManager.getOurInstance().getAccount() + " repositories successfully";
        } else {
            AppManager.getOurInstance().resetAccount();
            ANError anError = response.getError();
            message = "Error: " + anError.getErrorDetail() + "\n" +
                    "Body: " + anError.getErrorBody() + "\n" +
                    "Message: " + anError.getMessage() + "\n" +
                    "Code: " + anError.getErrorCode();
        }
        realm.close();
        MainActivity.successRepos(context, message);
    }
}
