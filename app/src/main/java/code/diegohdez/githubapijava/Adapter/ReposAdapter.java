package code.diegohdez.githubapijava.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import code.diegohdez.githubapijava.Activity.ReposActivity;
import code.diegohdez.githubapijava.Data.DataOfRepos;
import code.diegohdez.githubapijava.R;

public class ReposAdapter extends RecyclerView.Adapter {
    private static final String TAG = ReposAdapter.class.getSimpleName();
    private static int HEADER = 0;
    private static int ITEM = 1;

    private List<DataOfRepos> repos;
    private String account;
    private ReposActivity context;

    public ReposAdapter (List<DataOfRepos> repos, String account) {
        this.repos = repos;
        this.account = account;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == HEADER) {
            View header = inflater.inflate(R.layout.repos_header, parent, false);
            return new ViewHolderHeaderRepo(header);
        } else if (viewType == ITEM) {
            View repo = inflater.inflate(R.layout.item_repos, parent, false);
            return new ViewHolderItemRepo(repo);
        } else {
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderItemRepo) {
            DataOfRepos repo = repos.get(position - 1);
            ((ViewHolderItemRepo) holder).root.setTag(holder);
            ((ViewHolderItemRepo) holder).name.setText(repo.getName());
            ((ViewHolderItemRepo) holder).description.setText(repo.getDescription());
        } else if(holder instanceof ViewHolderHeaderRepo) {
            ((ViewHolderHeaderRepo) holder).account.setText(account + " repos");
        } else {
            Log.d(TAG, "no instance of view holder found");
        }
    }

    @Override
    public int getItemCount() {
        return repos.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == HEADER)
            return HEADER;
        else return ITEM;
    }

    public class ViewHolderItemRepo extends RecyclerView.ViewHolder{

        TextView name;
        TextView description;
        View root;
        public ViewHolderItemRepo(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.repo_name);
            description = itemView.findViewById(R.id.repo_description);
            root = itemView;
        }
    }

    public class ViewHolderHeaderRepo extends RecyclerView.ViewHolder {

        TextView account;
        public ViewHolderHeaderRepo(View itemView) {
            super(itemView);
            account = itemView.findViewById(R.id.account_repos_header);
        }
    }
}
