package code.diegohdez.githubapijava.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;

import code.diegohdez.githubapijava.Activity.ReposActivity;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.Utils.Request.API;
import io.realm.Realm;

public class RepoInfo extends AsyncTask<String, Void, ANResponse> {

    private static final String TAG = Repo.class.getSimpleName();

    private Realm realm;
    private Context context;

    public RepoInfo(ReposActivity context) {
        realm = Realm.getDefaultInstance();
        this.context = context;
    }

    @Override
    protected ANResponse doInBackground(String... urls) {
        ANRequest request = API.onRepoEvenListener(urls[0]);
        return (ANResponse<Repo>) request.executeForObject(Repo.class);
    }

    @Override
    protected void onPostExecute(ANResponse response) {
        super.onPostExecute(response);
        if (response.isSuccess()) {
            final Repo repo = (Repo) response.getResult();
            if (realm.isClosed())
                realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(repo);
                }
            });
            realm.close();
            ((ReposActivity) context).updateCounter(repo.getName());

        }
    }
}
