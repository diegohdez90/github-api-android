package code.diegohdez.githubapijava.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;

import java.util.ArrayList;
import java.util.List;

import code.diegohdez.githubapijava.Activity.MainActivity;
import code.diegohdez.githubapijava.Activity.ReposActivity;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Model.Owner;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.Utils.Request.API;
import io.realm.Realm;

import static code.diegohdez.githubapijava.Utils.Constants.Numbers.PAGE_ONE;

public class Repos extends AsyncTask<String, Void, ANResponse[]> {

    public static final String TAG = Repos.class.getSimpleName();
    private Realm realm;
    private Context context;
    private int page = 1;

    public Repos(MainActivity context) {
        realm = Realm.getDefaultInstance();
        this.context = context;
    }

    public Repos(ReposActivity context, int page) {
        realm = Realm.getDefaultInstance();
        this.context = context;
        this.page = page;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ANResponse[] doInBackground(String... urls) {
        ANRequest[] request;
        if (urls.length > 1 ) request = new ANRequest[2];
        else request = new ANRequest[1];
        request[0] = API.getRepos(urls[0], page);
        ANResponse user = null;
        ANResponse repos = (ANResponse<List<Repo>>) request[0].executeForObjectList(Repo.class);
        if (urls.length > 1) {
            request[1] = API.getAccount(urls[1]);
            user = (ANResponse<Owner>) request[1].executeForObject(Owner.class);
        }
        ANResponse[] results;
        if (urls.length > 1 ) results = new ANResponse[2];
        else  results = new ANResponse[1];
        results[0] = repos;
        if (urls.length > 1) results[1] = user;
        return results;
    }

    @Override
    protected void onPostExecute(ANResponse[] response) {

        super.onPostExecute(response);
        List<Repo> list = new ArrayList<>();
        String message = "";
        int status = 0;
        ANResponse repos = response[0];
        if (repos.isSuccess()) {
            list = (List<Repo>) repos.getResult();
            if (realm.isClosed()) {
                realm = Realm.getDefaultInstance();
            }
            final List<Repo> listOfRepos = list;
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(listOfRepos);
                }
            });
            message = AppManager.getOurInstance().getAccount() + " repositories successfully";
            status = repos.getOkHttpResponse().code();
        } else {
            AppManager.getOurInstance().resetAccount();
            ANError anError = repos.getError();
            message = "Error: " + anError.getErrorDetail() + "\n" +
                    "Body: " + anError.getErrorBody() + "\n" +
                    "Message: " + anError.getMessage() + "\n" +
                    "Code: " + anError.getErrorCode();
            status = anError.getErrorCode();
        }
        if (response.length > 1) {
            ANResponse user = response[1];
            if (user.isSuccess()) {
                final Owner owner = (Owner) user.getResult();
                if (realm.isClosed()) realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insertOrUpdate(owner);
                    }
                });
            } else {
                AppManager.getOurInstance().resetAccount();
                ANError anError = user.getError();
                message = "Error: " + anError.getErrorDetail() + "\n" +
                        "Body: " + anError.getErrorBody() + "\n" +
                        "Message: " + anError.getMessage() + "\n" +
                        "Code: " + anError.getErrorCode();
                Log.e(TAG, message);
            }
        }
        realm.close();
        switch (context.getClass().getSimpleName()) {
            case "MainActivity":
                MainActivity.successRepos(context, message, status);
                break;
            case "ReposActivity":
                ReposActivity activity = (ReposActivity) context;
                if (page > PAGE_ONE)activity.successLoader(list);
                else activity.initList(list);
                break;
        }
    }
}
