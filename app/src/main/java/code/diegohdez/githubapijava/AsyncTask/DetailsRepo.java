package code.diegohdez.githubapijava.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Activity.ReposDetailActivity;
import code.diegohdez.githubapijava.BuildConfig;
import code.diegohdez.githubapijava.Model.Branch;
import code.diegohdez.githubapijava.Model.Issue;
import code.diegohdez.githubapijava.Model.Pull;
import code.diegohdez.githubapijava.Model.PullInfo;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.Utils.Constants.Fields;
import code.diegohdez.githubapijava.Utils.Request.API;
import code.diegohdez.navbottom.githubapijava.Activity.ReposDetailsActivity;
import io.realm.Realm;
import io.realm.RealmList;

import static code.diegohdez.githubapijava.Utils.Constants.API.CLOSED;

public class DetailsRepo extends AsyncTask<String, Void, ANResponse[]> {

    private static final String TAG = DetailsRepo.class.getSimpleName();

    private Realm realm;
    private Context context;
    private long id;

    public DetailsRepo(ReposDetailActivity context, long id) {
        this.context = context;
        this.id = id;
        realm = Realm.getDefaultInstance();
    }

    public DetailsRepo(ReposDetailsActivity context, long id) {
        this.context = context;
        this.id = id;
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected ANResponse[] doInBackground(String... urls) {
        ANRequest[] requests = new ANRequest[3];
        ANResponse[] responses = new ANResponse[3];
        requests[0] = API.getPulls(urls[0]);
        requests[1] = API.getIssues(urls[1]);
        requests[2] = API.getBranches(urls[2]);
        responses[0] = requests[0].executeForObjectList(Pull.class);
        responses[1] = requests[1].executeForObjectList(Issue.class);
        responses[2] = requests[2].executeForObjectList(Branch.class);
        return responses;
    }

    @Override
    protected void onPostExecute(ANResponse[] responses) {
        super.onPostExecute(responses);
        if (responses[0].isSuccess()) {
            final RealmList<Pull> pulls = toPullsList((ArrayList<Pull>) responses[0].getResult());
            if (realm.isClosed()) realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Repo repo = realm.where(Repo.class).equalTo(Fields.ID, id).findFirst();
                    for (Pull pull : pulls) {
                        if (pull.getMergedAt() != null) pull.setState("merged");
                    }
                    repo.getPulls().addAll(pulls);
                    realm.insertOrUpdate(repo);
                }
            });
        } else {
            ANError anError = responses[0].getError();
            String message = "Delete: " + "\n" +
                    "Error: " + anError.getErrorDetail() + "\n" +
                    "Body: " + anError.getErrorBody() + "\n" +
                    "Message: " + anError.getMessage() + "\n" +
                    "Code: " + anError.getErrorCode();
            Log.e(TAG, message);
        }
        if (responses[1].isSuccess()) {
            if (realm.isClosed()) realm = Realm.getDefaultInstance();
            final RealmList<Issue> list = toIssuesList((ArrayList<Issue>) responses[1].getResult());
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
        } else {
            ANError anError = responses[1].getError();
            String message = "Delete: " + "\n" +
                    "Error: " + anError.getErrorDetail() + "\n" +
                    "Body: " + anError.getErrorBody() + "\n" +
                    "Message: " + anError.getMessage() + "\n" +
                    "Code: " + anError.getErrorCode();
            Log.e(TAG, message);
        }
        if (responses[2].isSuccess()) {
            final RealmList<Branch> list = toBrancheshList((ArrayList<Branch>) responses[2].getResult());
            if (realm.isClosed()) realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Repo repo = realm.where(Repo.class).equalTo(Fields.ID, id).findFirst();
                    repo.getBranches().addAll(list);
                    realm.insertOrUpdate(repo);
                }
            });
        } else {
            ANError anError = responses[2].getError();
            String message = "Delete: " + "\n" +
                    "Error: " + anError.getErrorDetail() + "\n" +
                    "Body: " + anError.getErrorBody() + "\n" +
                    "Message: " + anError.getMessage() + "\n" +
                    "Code: " + anError.getErrorCode();
            Log.e(TAG, message);
        }
        realm.close();
        if (BuildConfig.FLAVOR.equals("navBottom")) ((ReposDetailsActivity) context).createFragment();
        else ((ReposDetailActivity) context).createAdapter(id);
    }

    public RealmList<Issue> toIssuesList(ArrayList<Issue> list) {
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

    public RealmList<Pull> toPullsList(ArrayList<Pull> list) {
        RealmList<Pull> pulls = new RealmList();
        for (Pull item : list) {
            Pull pull = new Pull();
            pull.setId(item.getId());
            pull.setTitle(item.getTitle());
            pull.setDescription(item.getDescription());
            pull.setNumber(item.getNumber());
            pull.setState(item.getState());
            pull.setUser(item.getUser());
            if (item.getAssignee() != null) pull.setAssignee(item.getAssignee());
            pull.setCreatedAt(item.getCreatedAt());
            pull.setUpdatedAt(item.getUpdatedAt());
            if (item.getState().equals(CLOSED)) pull.setClosedAt(item.getClosedAt());
            if (item.getMergedAt() != null) pull.setMergedAt(item.getMergedAt());
            pulls.add(pull);
        }
        return pulls;
    }

    public RealmList<Branch> toBrancheshList(ArrayList<Branch> list) {
        RealmList<Branch> branches = new RealmList<>();
        for (Branch item : list) {
            Branch branch = new Branch();
            branch.setName(item.getName());
            branches.add(branch);
        }
        return branches;
    }

}
