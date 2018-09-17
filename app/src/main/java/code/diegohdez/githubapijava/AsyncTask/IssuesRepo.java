package code.diegohdez.githubapijava.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;

import code.diegohdez.githubapijava.Activity.ReposDetailActivity;
import code.diegohdez.githubapijava.Model.Issue;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.Utils.Constants.Fields;
import code.diegohdez.githubapijava.Utils.Request.API;
import io.realm.Realm;
import io.realm.RealmList;

public class IssuesRepo extends AsyncTask<String, Void, ANResponse>{

    private static final String TAG = IssuesRepo.class.getSimpleName();

    private Realm realm;
    private Context context;
    private long id;

    public IssuesRepo(ReposDetailActivity context, long id) {
        this.context = context;
        this.id = id;
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected ANResponse doInBackground(String... urls) {
        ANRequest request = API.getIssues(urls[0]);
        return (ANResponse<IssuesRepo>) request.executeForObjectList(IssuesRepo.class);
    }

    @Override
    protected void onPostExecute(ANResponse response) {
        super.onPostExecute(response);
        if (response.isSuccess()) {
            final RealmList<Issue> list = (RealmList<Issue>) response.getResult();
            if (realm.isClosed()) realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Repo repo = realm.where(Repo.class).equalTo(Fields.ID, id).findFirst();
                    repo.setIssues(list);
                    realm.insertOrUpdate(repo);
                }
            });
            realm.close();
            ((ReposDetailActivity) context).createIssuesAdapter(id);
        } else {
            ANError anError = response.getError();
            String message = "Delete: " + "\n" +
                    "Error: " + anError.getErrorDetail() + "\n" +
                    "Body: " + anError.getErrorBody() + "\n" +
                    "Message: " + anError.getMessage() + "\n" +
                    "Code: " + anError.getErrorCode();
            Log.e(TAG, message);
        }
    }
}
