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

    public CommitsBranch(CommitsActivity context, long id, String branch) {
        this.context = context;
        this.id = id;
        this.branch = branch;
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected ANResponse doInBackground(String... urls) {
        ANRequest request = API.getCommits(urls[0]);
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
                    realm.beginTransaction();
                    realm.where(Repo.class)
                            .equalTo(Fields.ID, id)
                            .findFirst()
                            .getBranches()
                            .where()
                            .equalTo(Fields.BRANCH_NAME, branch)
                            .findFirst()
                            .getCommits()
                            .addAll(list);
                    realm.commitTransaction();
                }
            });
            ((CommitsActivity) context).addCommits(list);
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
