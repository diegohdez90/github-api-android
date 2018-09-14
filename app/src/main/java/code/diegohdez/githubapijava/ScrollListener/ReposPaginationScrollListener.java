package code.diegohdez.githubapijava.ScrollListener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class ReposPaginationScrollListener extends RecyclerView.OnScrollListener {
    LinearLayoutManager layoutManager;

    public ReposPaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= getTotalPageCount()) {
                loadRepos();
            }
        }
    }

    protected abstract void loadRepos ();

    public abstract int getTotalPageCount ();
    public abstract boolean isLastPage ();
    public abstract boolean isLoading ();
}
