package code.diegohdez.githubapijava.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Activity.CommitsActivity;
import code.diegohdez.githubapijava.BuildConfig;
import code.diegohdez.githubapijava.Data.DataOfBranches;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.Utils.Constants.Intents;
import code.diegohdez.navbottom.githubapijava.Adapter.BranchesFragment;

public class BranchesAdapter extends RecyclerView.Adapter {

    private static final String TAG = BranchesAdapter.class.getSimpleName();

    private static final int ITEM = 0;
    private static final int LOADER = 1;

    private boolean isLoading;
    private ArrayList<DataOfBranches> branches;
    private long id;
    private String repoName;
    private Context context;
    private BranchesFragment fragment;

    public BranchesAdapter() {
        this.branches = new ArrayList<>();
    }

    public BranchesAdapter(long id, String repoName, Context context) {
        this.branches = new ArrayList<>();
        this.id = id;
        this.repoName = repoName;
        this.context = context;
    }

    public BranchesAdapter(long id, String repoName, BranchesFragment fragment) {
        this.branches = new ArrayList<>();
        this.id = id;
        this.repoName = repoName;
        this.fragment = fragment;
        this.isLoading = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM) {
            View item = inflater.inflate(R.layout.item_branch, parent, false);
            return new ViewHolderBranchItem(item);
        } else if (viewType == LOADER) {
            View loader = inflater.inflate(R.layout.branch_loader, parent, false);
            return new ViewHolderBranchLoader(loader);
        } else throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderBranchItem) {
            final DataOfBranches item = this.branches.get(position);
            ((ViewHolderBranchItem) holder).item.setText(item.getName());
            ((ViewHolderBranchItem) holder).root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (BuildConfig.FLAVOR.equals("navBottom"))
                        intent = new Intent(fragment.getContext(), CommitsActivity.class);
                    else intent = new Intent(context, CommitsActivity.class);
                    intent.putExtra(Intents.REPO_ID, id)
                            .putExtra(Intents.REPO_NAME, repoName)
                            .putExtra(Intents.BRANCH_NAME, item.getName());
                    if (BuildConfig.FLAVOR.equals("navBottom")) {
                        fragment.startActivity(intent);
                    } else {
                        context.startActivity(intent);
                    }
                }
            });
        } else if (holder instanceof ViewHolderBranchLoader) {
            // Nothing to do
        } else {
            Log.d(TAG, "no instance of view holder found");
        }
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == this.branches.size() - 1 && isLoading) return LOADER;
        else return ITEM;
    }

    public void addBranches(ArrayList<DataOfBranches> branches) {
        this.branches.addAll(branches);
        notifyDataSetChanged();
    }

    public void add(DataOfBranches branch) {
        this.branches.add(branch);
        notifyItemInserted(branches.size() - 1);
    }

    public void deleteLoading() {
        isLoading = false;
        int position = this.branches.size() - 1;
        this.branches.remove(position);
        notifyItemRemoved(position);
    }

    public void addLoading() {
        isLoading = true;
        add(new DataOfBranches());
    }

    private class ViewHolderBranchItem extends RecyclerView.ViewHolder {

        TextView item;
        View root;

        public ViewHolderBranchItem(View itemView) {
            super(itemView);
            root = itemView;
            item = itemView.findViewById(R.id.branch_name);
        }
    }

    private class ViewHolderBranchLoader extends RecyclerView.ViewHolder {
        public ViewHolderBranchLoader(View itemView) {
            super(itemView);
        }
    }
}
