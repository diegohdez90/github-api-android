package code.diegohdez.githubapijava.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.androidnetworking.common.ANResponse;
import com.androidnetworking.error.ANError;

import code.diegohdez.githubapijava.Activity.ReposActivity;
import code.diegohdez.githubapijava.Utils.Request.API;

public class ForkRepo extends AsyncTask<String, Void, ANResponse> {

    private static final String TAG = ForkRepo.class.getSimpleName();

    private Context context;
    private String name;

    public ForkRepo(ReposActivity context, String name) {
        this.context = context;
        this.name = name;
    }

    @Override
    protected ANResponse doInBackground(String... urls) {
        return API.forkRepo(urls[0]).executeForOkHttpResponse();
    }

    @Override
    protected void onPostExecute(ANResponse response) {
        super.onPostExecute(response);
        if (response.isSuccess()) {
            Log.i(TAG, "fork repo: " + response.getOkHttpResponse().code());
            if (response.getOkHttpResponse().code() == 202) ((ReposActivity) context).displayMessage("Fork success", name);
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