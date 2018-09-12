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

    public ReposAdapter (List<DataOfRepos> repos, String account, ReposActivity context) {
        this.repos = repos;
        this.account = account;
        this.context = context;
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
            final DataOfRepos repo = repos.get(position - 1);
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

    public class ViewHolderHeaderRepo extends RecyclerView.ViewHolder {

        TextView account;
        public ViewHolderHeaderRepo(View itemView) {
            super(itemView);
            account = itemView.findViewById(R.id.account_repos_header);
        }
    }
}
