package code.diegohdez.githubapijava.ScrollListener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class BranchPaginationScrollListener extends RecyclerView.OnScrollListener{

    LinearLayoutManager linearLayoutManager;

    public BranchPaginationScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        int visibleItemCount = linearLayoutManager.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadBranches();
            }
        }
    }

    protected abstract void loadBranches ();
    public abstract boolean isLastPage ();
    public abstract boolean isLoading ();
}
