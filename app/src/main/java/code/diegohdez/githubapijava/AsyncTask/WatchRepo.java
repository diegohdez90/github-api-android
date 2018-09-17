package code.diegohdez.githubapijava.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;

import code.diegohdez.githubapijava.Activity.ReposActivity;
import code.diegohdez.githubapijava.Utils.Request.API;

import static code.diegohdez.githubapijava.Utils.Constants.API.UNWATCH_REPO_SUCCESS;
import static code.diegohdez.githubapijava.Utils.Constants.API.WATCH_REPO_SUCCESS;

public class WatchRepo extends AsyncTask<String, Void, ANResponse> {

    private static final String TAG = WatchRepo.class.getSimpleName();
    private Context context;
    private boolean isWatched;
    private String name;

    public WatchRepo (boolean isWatched, ReposActivity context, String name){
        this.isWatched = isWatched;
        this.context = context;
        this.name = name;
    }

    @Override
    protected ANResponse doInBackground(String... urls) {
        if (isWatched) {
            ANRequest request = API.unWatchRepo(urls[0]);
            return request.executeForOkHttpResponse();
        }
        ANRequest request = API.watchRepo(urls[0]);
        return request.executeForOkHttpResponse();
    }

    @Override
    protected void onPostExecute(ANResponse response) {
        super.onPostExecute(response);
        if (response.isSuccess()) {
            if(response.getOkHttpResponse().code() == WATCH_REPO_SUCCESS) {
                ((ReposActivity) context).updateRepoAfterWatched(true, name,  "Watch repo successfully");
            } else if (response.getOkHttpResponse().code() == UNWATCH_REPO_SUCCESS)
                ((ReposActivity) context).updateRepoAfterWatched(false, name, "Unwatch repo successfully");
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
