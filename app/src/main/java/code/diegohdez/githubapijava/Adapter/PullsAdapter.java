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

import code.diegohdez.githubapijava.Data.DataOfPulls;
import code.diegohdez.githubapijava.R;

import static code.diegohdez.githubapijava.Utils.Constants.API.CLOSED;
import static code.diegohdez.githubapijava.Utils.Constants.API.DATE_REPO_FORMAT;
import static code.diegohdez.githubapijava.Utils.Constants.API.MERGED;
import static code.diegohdez.githubapijava.Utils.Constants.API.OPEN;

public class PullsAdapter extends RecyclerView.Adapter {

    private static final String TAG = PullsAdapter.class.getSimpleName();

    private static final int ITEM = 0;
    private static final int LOADER = 1;

    ArrayList<DataOfPulls> pulls;
    SimpleDateFormat dateFormat;
    private boolean isLoading;

    public PullsAdapter() {
        this.pulls = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat(DATE_REPO_FORMAT);
        this.isLoading = false;
    }


    public PullsAdapter(ArrayList<DataOfPulls> pulls) {
        this.pulls = pulls;
        this.dateFormat = new SimpleDateFormat(DATE_REPO_FORMAT);
        this.isLoading = false;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == ITEM) {
            View item = inflater.inflate(R.layout.item_pull, parent, false);
            return new ViewHolderPullItem(item);
        } else if(viewType == LOADER) {
            View loader = inflater.inflate(R.layout.pull_loader, parent, false);
            return new VIewHolderPullLoader(loader);
        } else throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderPullItem) {
            DataOfPulls pull = pulls.get(position);
            ((ViewHolderPullItem) holder).title.setText(pull.getTitle());
            ((ViewHolderPullItem) holder).header.setText(getHeader(
                    pull.getState(),
                    pull.getNumber(),
                    pull.getUser(),
                    pull.getCreatedAt(),
                    pull.getClosedAt(),
                    pull.getMergedAt()));
            ((ViewHolderPullItem) holder).icon.setImageResource(getDrawable(pull.getState()));
        } else if (holder instanceof VIewHolderPullLoader) {
            /*
            Nothing
             */
        } else {
            Log.d(TAG, "no instance of view holder found");
        }
    }

    @Override
    public int getItemCount() {
        return pulls.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == this.pulls.size() - 1 && isLoading) return LOADER;
        return ITEM;
    }

    private String getHeader(String state,
                             long number,
                             String user,
                             Date createdAt,
                             Date closedAt,
                             Date mergedAt) {
        switch (state) {
            case OPEN:
                return "#" + number + " opened by " + user + " in " + dateFormat.format(createdAt);
            case CLOSED:
                return "#" + number + " by " + user + " closed in " + dateFormat.format(closedAt);
            case MERGED:
                return "#" + number + " by " + user + " merged in " + dateFormat.format(mergedAt);
                default:
                    return "";
        }
    }

    private int getDrawable(String state) {
        switch (state) {
            case OPEN:
                return R.drawable.pull_open;
            case CLOSED:
                return R.drawable.pull_closed;
            case MERGED:
                return R.drawable.pull_merged;
                default:
                    return 1;
        }

    }

    public void clear() {
        this.pulls = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void addPulls(ArrayList<DataOfPulls> list) {
        pulls.addAll(list);
        notifyDataSetChanged();
    }

    public void add(DataOfPulls pull) {
        this.pulls.add(pull);
        notifyItemInserted(pulls.size() - 1);
    }

    public void deleteLoading() {
        isLoading = false;
        int position = this.pulls.size() - 1;
        this.pulls.remove(position);
        notifyItemRemoved(position);
    }

    public void addLoading() {
        isLoading = true;
        add(new DataOfPulls());
    }

    public boolean isLoading() {
        return isLoading;
    }

    private class ViewHolderPullItem extends RecyclerView.ViewHolder {

        TextView title;
        TextView header;
        ImageView icon;

        ViewHolderPullItem(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.pull_title);
            header = itemView.findViewById(R.id.pull_header);
            icon = itemView.findViewById(R.id.image_pull);
        }
    }

    private class VIewHolderPullLoader extends RecyclerView.ViewHolder {

        public VIewHolderPullLoader(View itemView) {
            super(itemView);
        }
    }
}
