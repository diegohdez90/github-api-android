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
import code.diegohdez.githubapijava.Data.DataOfBranches;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.Utils.Constants.Intents;

public class BranchesAdapter extends RecyclerView.Adapter {

    private static final String TAG = BranchesAdapter.class.getSimpleName();

    private static final int ITEM = 0;

    private ArrayList<DataOfBranches> branches;
    private long id;
    private String repoName;
    private Context context;

    public BranchesAdapter() {
        this.branches = new ArrayList<>();
    }

    public BranchesAdapter(long id, String repoName, Context context) {
        this.branches = new ArrayList<>();
        this.id = id;
        this.repoName = repoName;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM) {
            View item = inflater.inflate(R.layout.item_branch, parent, false);
            return new ViewHolderBranchItem(item);
        } else throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderBranchItem) {
            final DataOfBranches item = this.branches.get(position);
            ((ViewHolderBranchItem) holder).item.setText(item.getName());
            ((ViewHolderBranchItem) holder).root.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommitsActivity.class)
                            .putExtra(Intents.REPO_ID, id)
                            .putExtra(Intents.REPO_NAME, repoName)
                            .putExtra(Intents.BRANCH_NAME, item.getName());
                    context.startActivity(intent);
                }
            });
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
        return ITEM;
    }

    public void addBranches(ArrayList<DataOfBranches> branches) {
        this.branches.addAll(branches);
        notifyDataSetChanged();
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
}
