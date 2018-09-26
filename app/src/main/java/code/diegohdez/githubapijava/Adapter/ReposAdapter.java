package code.diegohdez.githubapijava.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.OkHttpResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import code.diegohdez.githubapijava.Activity.ReposActivity;
import code.diegohdez.githubapijava.Activity.ReposDetailActivity;
import code.diegohdez.githubapijava.BuildConfig;
import code.diegohdez.githubapijava.Data.DataOfRepos;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.Utils.Constants.Intents;
import code.diegohdez.navbottom.githubapijava.Activity.ReposDetailsActivity;
import okhttp3.Response;

import static code.diegohdez.githubapijava.Utils.Constants.API.ACCEPT;
import static code.diegohdez.githubapijava.Utils.Constants.API.APPLICATION_VND_GITHUB;
import static code.diegohdez.githubapijava.Utils.Constants.API.AUTHORIZATION;
import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.STARRED_REPO_ERROR;
import static code.diegohdez.githubapijava.Utils.Constants.API.STARRED_REPO_SUCCESS;
import static code.diegohdez.githubapijava.Utils.Constants.API.STAR_REPO;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_REPOS;
import static code.diegohdez.githubapijava.Utils.Constants.API.WATCHED_REPO_ERROR;
import static code.diegohdez.githubapijava.Utils.Constants.API.WATCH_REPO;

public class ReposAdapter extends RecyclerView.Adapter {
    private static final String TAG = ReposAdapter.class.getSimpleName();
    private static int ITEM = 1;
    private static int LOADER = 2;

    private List<DataOfRepos> repos;
    private String account;
    private ReposActivity context;
    private boolean isLoading;

    public ReposAdapter (List<DataOfRepos> repos, String account, ReposActivity context) {
        this.repos = repos;
        this.account = account;
        this.context = context;
        this.isLoading = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM) {
            View repo = inflater.inflate(R.layout.item_repos, parent, false);
            return new ViewHolderItemRepo(repo);
        } else if (viewType == LOADER) {
            View loader = inflater.inflate(R.layout.repo_loader, parent, false);
            return new ViewHolderLoaderRepo(loader);
        } else
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderItemRepo) {
            final DataOfRepos repo = repos.get(position);
            ((ViewHolderItemRepo) holder).root.setTag(holder);
            ((ViewHolderItemRepo) holder).root.setOnClickListener(new View.OnClickListener(){
                Intent intent = null;
                @Override
                public void onClick(View v) {
                    if (BuildConfig.FLAVOR == "navBottom") {
                        intent = new Intent(context, ReposDetailsActivity.class);
                    } else {
                        intent = new Intent(context, ReposDetailActivity.class);
                    }
                    intent.putExtra(Intents.REPO_ID, repo.getId())
                            .putExtra(Intents.REPO_NAME, repo.getName());
                    context.startActivity(intent);
                }
            });
            ((ViewHolderItemRepo) holder).name.setText(repo.getName());
            ((ViewHolderItemRepo) holder).description.setText(repo.getDescription());
            ((ViewHolderItemRepo) holder).repoModal.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    context.setRepoStatus(
                            repo.getName(),
                            repo.getWatchers(),
                            repo.getStars(),
                            repo.getForks());
                    ANRequest.GetRequestBuilder getWatcher = AndroidNetworking
                            .get(BASE_URL + USER_REPOS + account + "/" + repo.getName() + WATCH_REPO);
                    ANRequest.GetRequestBuilder getStar = AndroidNetworking
                            .get(BASE_URL + USER + STAR_REPO + "/" + account + "/" + repo.getName());
                    String token = AppManager.getOurInstance().getToken();
                    if (token.length() > 0) {
                        getWatcher.addHeaders(AUTHORIZATION, token);
                        getStar.addHeaders(AUTHORIZATION, token);

                        getWatcher.build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        boolean subscribed = false;
                                        try {
                                            subscribed = response.getBoolean("subscribed");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        context.isSubscribed(subscribed);
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        if (anError.getErrorCode() == WATCHED_REPO_ERROR) context.isSubscribed(false);
                                        String message = "Error: " + anError.getErrorDetail() + "\n" +
                                                "Body: " + anError.getErrorBody() + "\n" +
                                                "Message: " + anError.getMessage() + "\n" +
                                                "Code: " + anError.getErrorCode();
                                        Log.e(TAG, message);
                                    }
                                });

                        getStar.addHeaders(ACCEPT, APPLICATION_VND_GITHUB)
                                .build()
                                .getAsOkHttpResponse(new OkHttpResponseListener() {
                                    @Override
                                    public void onResponse(Response response) {
                                        if (response.code() == STARRED_REPO_SUCCESS) context.isStarred(true);
                                        else if (response.code() == STARRED_REPO_ERROR) context.isStarred(false);
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        if (anError.getErrorCode() == STARRED_REPO_ERROR) context.isStarred(false);
                                        String message = "Error: " + anError.getErrorDetail() + "\n" +
                                                "Body: " + anError.getErrorBody() + "\n" +
                                                "Message: " + anError.getMessage() + "\n" +
                                                "Code: " + anError.getErrorCode();
                                        Log.e(TAG, message);
                                    }
                                });
                    }
                }
            });
        } else if (holder instanceof ViewHolderLoaderRepo) {
            /*
            Nothing
             */
        } else {
            Log.d(TAG, "no instance of view holder found");
        }
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void add(DataOfRepos repo) {
        repos.add(repo);
        notifyItemInserted(repos.size() - 1);
    }

    public void addAll(List<DataOfRepos> repos) {
        for (DataOfRepos repo : repos) add(repo);
    }

    public void clear () {
        isLoading = false;
        repos = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addLoading () {
        isLoading = true;
        add(new DataOfRepos());
    }

    public void deleteLoading () {
        isLoading = false;
        int position = repos.size() - 1;
        repos.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == repos.size() - 1 && isLoading) return LOADER;
        else return ITEM;
    }

    private class ViewHolderItemRepo extends RecyclerView.ViewHolder{

        TextView name;
        TextView description;
        ImageView repoModal;
        View root;
        ViewHolderItemRepo(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.repo_name);
            description = itemView.findViewById(R.id.repo_description);
            repoModal = itemView.findViewById(R.id.repo_details_modal);
            root = itemView;
        }
    }

    private class ViewHolderLoaderRepo extends RecyclerView.ViewHolder {

        ViewHolderLoaderRepo(View itemView) {
            super(itemView);
        }
    }

}
