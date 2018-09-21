package code.diegohdez.githubapijava.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;

import java.util.ArrayList;

import code.diegohdez.githubapijava.BuildConfig;
import code.diegohdez.githubapijava.Model.Branch;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.Utils.Constants.Fields;
import code.diegohdez.githubapijava.Utils.Request.API;
import code.diegohdez.navbottom.githubapijava.Adapter.BranchesFragment;
import io.realm.Realm;
import io.realm.RealmList;

public class BranchesRepo extends AsyncTask<String, Void, ANResponse> {

    private static final String TAG = BranchesRepo.class.getSimpleName();
    private Realm realm;
    private Context context;
    private BranchesFragment fragment;
    private long id;

    public BranchesRepo(FragmentActivity context, long id) {
        this.context = context;
        this.id = id;
        realm = Realm.getDefaultInstance();
    }

    public BranchesRepo(BranchesFragment fragment, long id) {
        this.fragment = fragment;
        this.id = id;
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected ANResponse doInBackground(String... urls) {
        ANRequest request = API.getBranches(urls[0]);
        return request.executeForObjectList(Branch.class);
    }

    @Override
    protected void onPostExecute(ANResponse response) {
        super.onPostExecute(response);
        if (response.isSuccess()) {
            final RealmList<Branch> list = toBrancheshList((ArrayList<Branch>) response.getResult());
            if (realm.isClosed()) realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Repo repo = realm.where(Repo.class).equalTo(Fields.ID, id).findFirst();
                    repo.getBranches().addAll(list);
                    realm.insertOrUpdate(repo);
                }
            });
            if (BuildConfig.FLAVOR.equals("navBottom")) fragment.addBranches(list);
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

    private RealmList<Branch> toBrancheshList(ArrayList<Branch> list) {
        RealmList<Branch> branches = new RealmList<>();
        for (Branch item : list) {
            Branch branch = new Branch();
            branch.setName(item.getName());
            branches.add(branch);
        }
        return branches;
    }
}
