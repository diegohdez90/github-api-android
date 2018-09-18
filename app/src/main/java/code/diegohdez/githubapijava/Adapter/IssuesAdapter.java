package code.diegohdez.githubapijava.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import static code.diegohdez.githubapijava.Utils.Constants.API.OPEN;

public class IssuesAdapter extends RecyclerView.Adapter {

    private static int ITEM = 0;

    SimpleDateFormat dateFormat;
    private ArrayList<DataOfIssues> issues;

    public IssuesAdapter() {
        issues = new ArrayList<>();
        dateFormat = new SimpleDateFormat(DATE_REPO_FORMAT);
    }


    public IssuesAdapter (ArrayList<DataOfIssues> issues) {
        this.issues = issues;
        dateFormat = new SimpleDateFormat(DATE_REPO_FORMAT);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM) {
            View item = inflater.inflate(R.layout.item_issue, parent, false);
            return new ViewHolderItemIssue(item);
        } else
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderItemIssue) {
            DataOfIssues issue = issues.get(position);
            ((ViewHolderItemIssue) holder).title.setText(issue.getTitle());
            ((ViewHolderItemIssue) holder).header.setText(getHeader(
                    issue.getNumber(),
                    issue.getState(),
                    issue.getUser(),
                    issue.getCreatedAt(),
                    issue.getClosedAt()
            ));
            ((ViewHolderItemIssue) holder).icon.setImageResource(getDrawable(issue.getState()));
        }
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ITEM;
    }

    public String getHeader(long number, String state, String user, Date createdAt, Date closedAt) {
        switch (state) {
            case OPEN:
                return "#" + number + " opened by " + user + " in " + dateFormat.format(createdAt);
            case CLOSED:
                return "#" + number + " by " + user + " closed in " + dateFormat.format(closedAt);
                default:
                    return "";
        }
    }

    public int getDrawable(String state) {
        switch (state) {
            case OPEN:
                return R.drawable.issue_open;
            case CLOSED:
                return R.drawable.issue_closed;
        }
        return -1;
    }

    public void addIssues(ArrayList<DataOfIssues> issues) {
        this.issues.addAll(issues);
        notifyDataSetChanged();
    }

    private class ViewHolderItemIssue extends RecyclerView.ViewHolder {

        TextView title;
        TextView header;
        ImageView icon;
        public ViewHolderItemIssue(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.issue_title);
            header = itemView.findViewById(R.id.issue_header);
            icon = itemView.findViewById(R.id.image_issue);
        }
    }
}
