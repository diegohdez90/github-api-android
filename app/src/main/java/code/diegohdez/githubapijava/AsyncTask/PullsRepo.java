package code.diegohdez.githubapijava.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Activity.ReposDetailActivity;
import code.diegohdez.githubapijava.BuildConfig;
import code.diegohdez.githubapijava.Model.Pull;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.Utils.Constants.Fields;
import code.diegohdez.githubapijava.Utils.Request.API;
import code.diegohdez.navbottom.githubapijava.Adapter.PullsFragment;
import io.realm.Realm;
import io.realm.RealmList;

import static code.diegohdez.githubapijava.Utils.Constants.API.CLOSED;

public class PullsRepo extends AsyncTask<String, Void, ANResponse> {

    private static final String TAG = PullsRepo.class.getSimpleName();

    private Realm realm;
    private Context context;
    private PullsFragment fragment;
    private long id;

    public PullsRepo (FragmentActivity context, long id) {
        this.context = context;
        this.id = id;
        this.realm = Realm.getDefaultInstance();
    }

    public PullsRepo (PullsFragment context, long id) {
        this.fragment = context;
        this.id = id;
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    protected ANResponse doInBackground(String... urls) {
        ANRequest request = API.getPulls(urls[0]);
        return request.executeForObjectList(Pull.class);
    }

    @Override
    protected void onPostExecute(ANResponse response) {
        super.onPostExecute(response);

        if (response.isSuccess()) {
            final RealmList<Pull> pulls = toPullsList((ArrayList<Pull>) response.getResult());
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
            if (BuildConfig.FLAVOR.equals("navBottom")) fragment.addPulls(pulls);
            else ((ReposDetailActivity) context).addPulls(pulls);
        }
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
}
