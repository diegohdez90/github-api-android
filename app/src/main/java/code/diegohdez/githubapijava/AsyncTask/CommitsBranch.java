package code.diegohdez.githubapijava.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Activity.CommitsActivity;
import code.diegohdez.githubapijava.Model.Commit;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.Utils.Constants.Fields;
import code.diegohdez.githubapijava.Utils.Request.API;
import io.realm.Realm;
import io.realm.RealmList;

public class CommitsBranch extends AsyncTask<String, Void, ANResponse> {

    private static final String TAG = CommitsBranch.class.getSimpleName();

    private long id;
    private String branch;
    private Realm realm;
    private Context context;
    private int page;

    public CommitsBranch(CommitsActivity context, long id, String branch) {
        this.context = context;
        this.id = id;
        this.branch = branch;
        realm = Realm.getDefaultInstance();
        this.page = 1;
    }

    public CommitsBranch(CommitsActivity context, long id, String branch, int page) {
        this.context = context;
        this.id = id;
        this.branch = branch;
        realm = Realm.getDefaultInstance();
        this.page = page;
    }

    @Override
    protected ANResponse doInBackground(String... urls) {
        ANRequest request =  (page > 1) ? API.getCommits(urls[0] + "&page=" + page) :  API.getCommits(urls[0]);
        return request.executeForObjectList(Commit.class);
    }

    @Override
    protected void onPostExecute(ANResponse response) {
        super.onPostExecute(response);
        if (response.isSuccess()) {
            final RealmList<Commit> list = toCommitList((ArrayList<Commit>) response.getResult());
            if (realm.isClosed()) realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Repo repo = realm.where(Repo.class)
                            .equalTo(Fields.ID, id)
                            .findFirst();
                    repo.getBranches()
                            .where()
                            .equalTo(Fields.BRANCH_NAME, branch)
                            .findFirst()
                            .getCommits()
                            .addAll(list);
                    realm.insertOrUpdate(repo);
                }
            });
            if (page > 1) ((CommitsActivity) context).addCommits(list);
            else ((CommitsActivity) context).setCommits(list);
        }
    }

    private RealmList<Commit> toCommitList (ArrayList<Commit> commits) {
        RealmList<Commit> list = new RealmList<>();
        for (Commit commit : commits) {
            Commit item = new Commit();
            item.setSha(commit.getSha());
            item.setAuthor(commit.getAuthor());
            item.setCommitInfo(commit.getCommitInfo());
            list.add(item);
        }
        return list;
    }
}
