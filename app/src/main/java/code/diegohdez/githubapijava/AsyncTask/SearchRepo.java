package code.diegohdez.githubapijava.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import code.diegohdez.githubapijava.Activity.ReposActivity;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.Utils.Request.API;
import io.realm.Realm;
import io.realm.RealmList;

import static code.diegohdez.githubapijava.Utils.Constants.Numbers.PAGE_ONE;

public class SearchRepo extends AsyncTask<String, Void, ANResponse> {

    private static final String TAG = SearchRepo.class.getSimpleName();

    private Context context;
    private Realm realm;
    private int page;

    public SearchRepo(ReposActivity context) {
        this.context = context;
        this.page = PAGE_ONE;
        this.realm = Realm.getDefaultInstance();
    }

    public SearchRepo(ReposActivity context, int page) {
        this.context = context;
        this.page = page;
        this.realm = Realm.getDefaultInstance();
    }

    @Override
    protected ANResponse doInBackground(String... urls) {
        ANRequest request = API.searchInRepo(urls[0] + "&page=" + page);
        return request.executeForJSONObject();
    }

    @Override
    protected void onPostExecute(ANResponse response) {
        super.onPostExecute(response);
        if (response.isSuccess()) {
            Log.i(TAG, response
                    .getOkHttpResponse()
                    .body().toString());
            JsonParser parser = new JsonParser();
            JsonObject result = parser
                    .parse(response
                            .getResult().toString())
                    .getAsJsonObject();
            RealmList<Repo> repos = toReposList(result.getAsJsonArray("items"));
            long total_repos = result.get("total_count").getAsLong();
            if (realm.isClosed()) realm = Realm.getDefaultInstance();
            final RealmList<Repo> finalRepos = repos;
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    realm.insertOrUpdate(finalRepos);
                }
            });
            realm.close();
            if (page > PAGE_ONE) ((ReposActivity) context).addSearch(repos);
            else ((ReposActivity) context).initSearch(repos, total_repos);
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

    private RealmList<Repo> toReposList(JsonArray items) {
        RealmList<Repo> repos = new RealmList<>();
        Gson gson = new Gson();
        for (int i = 0; i < items.size(); i++){
            Repo repo = gson.fromJson(items.get(i), Repo.class);
            repos.add(repo);
        }
        return repos;
    }
}