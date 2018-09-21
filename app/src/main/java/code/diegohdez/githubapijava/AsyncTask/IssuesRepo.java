package code.diegohdez.githubapijava.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Activity.ReposDetailActivity;
import code.diegohdez.githubapijava.BuildConfig;
import code.diegohdez.githubapijava.Model.Issue;
import code.diegohdez.githubapijava.Model.Pull;
import code.diegohdez.githubapijava.Model.PullInfo;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.Utils.Constants.Fields;
import code.diegohdez.githubapijava.Utils.Request.API;
import code.diegohdez.navbottom.githubapijava.Adapter.IssuesFragment;
import io.realm.Realm;
import io.realm.RealmList;

import static code.diegohdez.githubapijava.Utils.Constants.API.CLOSED;

public class IssuesRepo extends AsyncTask<String, Void, ANResponse> {

    private static String TAG = IssuesRepo.class.getSimpleName();

    private Realm realm;
    private Context context;
    private IssuesFragment fragment;
    private long id;
    private int page;

    public IssuesRepo(FragmentActivity context, long id) {
        this.context = context;
        this.id = id;
        realm = Realm.getDefaultInstance();
    }

    public IssuesRepo(IssuesFragment context, long id) {
        this.fragment = context;
        this.id = id;
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    protected ANResponse doInBackground(String... urls) {
        ANRequest request = API.getIssues(urls[0]);
        return request.executeForObjectList(Issue.class);
    }

    @Override
    protected void onPostExecute(ANResponse response) {
        super.onPostExecute(response);
        if (response.isSuccess()) {
            if (realm.isClosed()) realm = Realm.getDefaultInstance();
            final RealmList<Issue> list = toIssuesList((ArrayList<Issue>) response.getResult());
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Repo repo = realm.where(Repo.class).equalTo(Fields.ID, id).findFirst();
                    RealmList<Pull> pulls = repo.getPulls();
                    for (Issue item : list) {
                        Pull pull = pulls.where().equalTo(Fields.NUMBER, item.getNumber()).findFirst();
                        if (pull != null) {
                            PullInfo pullInfo = new PullInfo();
                            pullInfo.setState(pull.getState());
                            item.setPullInfo(pullInfo);
                        }
                    }
                    repo.getIssues().addAll(list);
                    realm.insertOrUpdate(repo);
                }
            });
            realm.close();
            if (BuildConfig.FLAVOR == "navBottom") ((IssuesFragment) fragment).addIssues(list);
            else ((ReposDetailActivity) context).addIssues(list);
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

    private RealmList<Issue> toIssuesList(ArrayList<Issue> list) {
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
            if (issue.getState().equals(CLOSED)) issue.setClosedAt(item.getClosedAt());
            issues.add(issue);
        }
        return issues;
    }

}
