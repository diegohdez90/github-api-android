package code.diegohdez.githubapijava.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;

import java.util.ArrayList;

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
        return (ANResponse) request.executeForObjectList(Issue.class);
    }

    @Override
    protected void onPostExecute(ANResponse response) {
        super.onPostExecute(response);
        if (response.isSuccess()) {
            if (realm.isClosed()) realm = Realm.getDefaultInstance();
            final RealmList<Issue> list = toIssueList(realm, (ArrayList<Issue>) response.getResult());
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Repo repo = realm.where(Repo.class).equalTo(Fields.ID, id).findFirst();
                    repo.getIssues().addAll(list);
                    realm.insertOrUpdate(repo);
                }
            });
            realm.close();
            Log.i(TAG, "create issues : " + id);
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

    private RealmList<Issue> toIssueList(Realm realm, ArrayList<Issue> list) {
        RealmList<Issue> issues = new RealmList();
        for (Issue item: list) {
            Issue issue = new Issue();
            issue.setId(item.getId());
            issue.setTitle(item.getTitle());
            issue.setDescription(item.getDescription());
            issue.setNumber(item.getNumber());
            issue.setState(item.getState());
            issue.setUser(item.getUser());
            if (item.getAssignee() != null) issue.setAssignee(item.getAssignee());
            issue.setCreatedAt(item.getCreatedAt());
            issue.setUpdatedAt(item.getUpdatedAt());
            if (issue.getState() != "closed") issue.setClosedAt(item.getClosedAt());
            issues.add(issue);
        }
        return issues;
    }
}
