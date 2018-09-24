package code.diegohdez.githubapijava.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import code.diegohdez.githubapijava.Data.DataOfIssues;
import code.diegohdez.githubapijava.R;

import static code.diegohdez.githubapijava.Utils.Constants.API.CLOSED;
import static code.diegohdez.githubapijava.Utils.Constants.API.DATE_REPO_FORMAT;
import static code.diegohdez.githubapijava.Utils.Constants.API.MERGED;
import static code.diegohdez.githubapijava.Utils.Constants.API.OPEN;

public class IssuesAdapter extends RecyclerView.Adapter {

    private static final String TAG = IssuesAdapter.class.getSimpleName();

    private static int ITEM = 1;
    private static int LOADER = 2;

    SimpleDateFormat dateFormat;
    private ArrayList<DataOfIssues> issues;
    private boolean isLoading;

    public IssuesAdapter() {
        this.issues = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat(DATE_REPO_FORMAT);
        this.isLoading = false;
    }


    public IssuesAdapter (ArrayList<DataOfIssues> issues) {
        this.issues = issues;
        this.dateFormat = new SimpleDateFormat(DATE_REPO_FORMAT);
        this.isLoading = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM) {
            View item = inflater.inflate(R.layout.item_issue, parent, false);
            return new ViewHolderItemIssue(item);
        } else if (viewType == LOADER){
            View loader = inflater.inflate(R.layout.issue_loader, parent, false);
            return new ViewHolderLoaderIssue(loader);
        } else
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderItemIssue) {
            DataOfIssues issue = issues.get(position);
            ((ViewHolderItemIssue) holder).title.setText(issue.getTitle());
            ((ViewHolderItemIssue) holder).header.setText(getHeader(
                    issue.isPull(),
                    issue.getPullState(),
                    issue.getNumber(),
                    issue.getState(),
                    issue.getUser(),
                    issue.getCreatedAt(),
                    issue.getClosedAt()
            ));
            ((ViewHolderItemIssue) holder).icon.setImageResource(getDrawable(
                    issue.isPull(),
                    issue.getPullState(),
                    issue.getState()));
        } else if (holder instanceof ViewHolderLoaderIssue) {
            /*
            Nothing
             */
        } else {
            Log.d(TAG, "no instance of view holder found");
        }
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == this.issues.size() - 1 && isLoading) return LOADER;
        else return ITEM;
    }

    private String getHeader(boolean isPull,
                             String pullState,
                             long number,
                             String state,
                             String user,
                             Date createdAt,
                             Date closedAt) {
        if (isPull) {
         switch (pullState) {
             case OPEN:
                 return "#" + number + " opened by " + user + " in " + dateFormat.format(createdAt);
             case CLOSED:
                 return "#" + number + " by " + user + " closed in " + dateFormat.format(closedAt);
             case MERGED:
                 return "#" + number + " by " + user + " merged in " + dateFormat.format(closedAt);
         }
        }
        switch (state) {
            case OPEN:
                return "#" + number + " opened by " + user + " in " + dateFormat.format(createdAt);
            case CLOSED:
                return "#" + number + " by " + user + " closed in " + dateFormat.format(closedAt);
            default:
                return "";
        }
    }

    private int getDrawable(boolean isPull, String pullState, String state) {
        if (isPull) {
            switch (pullState) {
                case OPEN:
                    return R.drawable.pull_open;
                case CLOSED:
                    return R.drawable.pull_closed;
                case MERGED:
                    return R.drawable.pull_merged;
            }
        }
        switch (state) {
            case OPEN:
                return R.drawable.issue_open;
            case CLOSED:
                return R.drawable.issue_closed;
        }
        return -1;
    }

    public void clear () {
        this.issues = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addIssues(ArrayList<DataOfIssues> issues) {
        this.issues.addAll(issues);
        notifyDataSetChanged();
    }

    public void add(DataOfIssues issue) {
        this.issues.add(issue);
        notifyItemInserted(issues.size() - 1);
    }

    public void deleteLoading() {
        isLoading = false;
        int position = this.issues.size() - 1;
        this.issues.remove(position);
        notifyItemRemoved(position);
    }

    public void addLoading() {
        isLoading = true;
        add(new DataOfIssues());
    }

    public boolean isLoading() {
        return isLoading;
    }

    private class ViewHolderItemIssue extends RecyclerView.ViewHolder {

        TextView title;
        TextView header;
        ImageView icon;
        ViewHolderItemIssue(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.issue_title);
            header = itemView.findViewById(R.id.issue_header);
            icon = itemView.findViewById(R.id.image_issue);
        }
    }

    private class ViewHolderLoaderIssue extends RecyclerView.ViewHolder {

        ViewHolderLoaderIssue(View itemView) {
            super(itemView);
        }
    }
}
