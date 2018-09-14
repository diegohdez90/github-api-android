package code.diegohdez.githubapijava.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;

import code.diegohdez.githubapijava.Activity.ReposActivity;
import code.diegohdez.githubapijava.Utils.Request.API;

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
            Log.d(TAG, "onResponse delete: " + response);
        } else {
            ANError anError = response.getError();
            String message = "Delete: " + "\n" +
                    "Error: " + anError.getErrorDetail() + "\n" +
                    "Body: " + anError.getErrorBody() + "\n" +
                    "Message: " + anError.getMessage() + "\n" +
                    "Code: " + anError.getErrorCode();
            Log.e(TAG, message);
        }
        ((ReposActivity) context).updateRepo(!isWatched, name);
    }
}