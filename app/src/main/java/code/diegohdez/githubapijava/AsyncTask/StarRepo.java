package code.diegohdez.githubapijava.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;

import code.diegohdez.githubapijava.Activity.ReposActivity;
import code.diegohdez.githubapijava.Utils.Request.API;

public class StarRepo extends AsyncTask<String, Void, ANResponse> {

    private static final String TAG = StarRepo.class.getSimpleName();
    private Context context;
    private boolean isStarred;
    private String name;

    public StarRepo (boolean isStarred, ReposActivity context, String name) {
        this.isStarred = isStarred;
        this.context = context;
        this.name = name;
    }

    @Override
    protected ANResponse doInBackground(String... urls) {
        if (isStarred) return API.unStarRepo(urls[0]).executeForOkHttpResponse();
        else return API.starRepo(urls[0]).executeForOkHttpResponse();
    }

    @Override
    protected void onPostExecute(ANResponse response) {
        super.onPostExecute(response);
        if (response.isSuccess()) {
            String message = "";
            if(isStarred) {
                message = "Unstar repo successfully";
            } else message = "Star repo successfully";
            ((ReposActivity) context).updateRepoAfterStarred(!isStarred, name, message);
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
