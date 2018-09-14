package code.diegohdez.githubapijava.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import code.diegohdez.githubapijava.Activity.ReposActivity;
import code.diegohdez.githubapijava.Data.DataOfRepos;
import code.diegohdez.githubapijava.R;

public class ReposAdapter extends RecyclerView.Adapter {
    private static final String TAG = ReposAdapter.class.getSimpleName();
    private static int HEADER = 0;
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
            ((ViewHolderItemRepo) holder).name.setText(repo.getName());
            ((ViewHolderItemRepo) holder).description.setText(repo.getDescription());
            ((ViewHolderItemRepo) holder).repoModal.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = context.getLayoutInflater();
                    View view = inflater.inflate(R.layout.repo_details_modal, null);
                    TextView watches = view.findViewById(R.id.watches);
                    watches.setText(Long.toString(repo.getWatchers()));
                    TextView stars = view.findViewById(R.id.stars);
                    stars.setText(Long.toString(repo.getStars()));
                    TextView forks = view.findViewById(R.id.forks);
                    forks.setText(Long.toString(repo.getForks()));
                    builder.setView(view)
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
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
        public ViewHolderItemRepo(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.repo_name);
            description = itemView.findViewById(R.id.repo_description);
            repoModal = itemView.findViewById(R.id.repo_details_modal);
            root = itemView;
        }
    }

    private class ViewHolderHeaderRepo extends RecyclerView.ViewHolder {

        TextView account;
        public ViewHolderHeaderRepo(View itemView) {
            super(itemView);
            account = itemView.findViewById(R.id.account_repos_header);
        }
    }

    private class ViewHolderLoaderRepo extends RecyclerView.ViewHolder {

        public ViewHolderLoaderRepo(View itemView) {
            super(itemView);
        }
    }

}
