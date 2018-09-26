package code.diegohdez.githubapijava.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import code.diegohdez.githubapijava.Data.DataOfCommits;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.Utils.Constants.API;

public class CommitsAdapter extends RecyclerView.Adapter {

    private static final String TAG = CommitsAdapter.class.getSimpleName();

    private static int ITEM = 0;
    private static int LOADER = 1;

    private ArrayList<DataOfCommits> commits;
    private boolean isLoading;
    SimpleDateFormat dateFormat;

    public CommitsAdapter() {
        this.commits = new ArrayList<>();
        this.isLoading = false;
        dateFormat = new SimpleDateFormat(API.DATE_REPO_FORMAT);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM) {
            View item = inflater.inflate(R.layout.item_commit, parent, false);
            return new ViewHolderCommitItem(item);
        } else if (viewType == LOADER) {
            View loader = inflater.inflate(R.layout.commit_loader, parent, false);
            return new ViewHolderCommitLoader(loader);
        } throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderCommitItem) {
            DataOfCommits commit = commits.get(position);
            ((ViewHolderCommitItem) holder).title.setText(parseTitle(commit.getMessage()));
            ((ViewHolderCommitItem) holder).description.setText(setDescription(commit.getAuthor(), commit.getDate()));
        } else if (holder instanceof ViewHolderCommitLoader){
            // Nothing
        } else
            Log.d(TAG, "no instance of view holder found");

    }

    private String parseTitle(String title) {
        return (title.length() > 50)
                ? ((title.indexOf("\n") > -1) ? title.split("\n")[0] : title.substring(0, 50) + "...")
                : ((title.indexOf("\n") > -1) ? title.split("\n")[0] : title);
    }

    private String setDescription(String author, Date date) {
        return author + " commited at " + dateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return commits.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == this.commits.size() - 1 && isLoading ) return LOADER;
        else return ITEM;
    }

    public void addCommits(ArrayList<DataOfCommits> commits) {
        this.commits.addAll(commits);
        notifyDataSetChanged();
    }

    public void add(DataOfCommits commit) {
        this.commits.add(commit);
        notifyItemInserted(commits.size() - 1);
    }

    public void deleteLoading() {
        isLoading = false;
        int position = this.commits.size() - 1;
        this.commits.remove(position);
        notifyItemRemoved(position);
    }

    public void addLoading() {
        isLoading = true;
        add(new DataOfCommits());
    }

    public boolean isLoading() {
        return isLoading;
    }

    private class ViewHolderCommitItem extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;

        public ViewHolderCommitItem(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.commit_title);
            description = itemView.findViewById(R.id.commit_header);
        }
    }

    private class ViewHolderCommitLoader extends RecyclerView.ViewHolder {

        public ViewHolderCommitLoader(View itemView) {
            super(itemView);
        }
    }
}
