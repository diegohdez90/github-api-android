package code.diegohdez.githubapijava.Adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import code.diegohdez.githubapijava.AsyncTask.PullsRepo;
import code.diegohdez.githubapijava.Data.DataOfPulls;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.ScrollListener.PullsPaginationScrollListener;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.PAGE_SIZE;
import static code.diegohdez.githubapijava.Utils.Constants.API.STATE_ALL;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_PULLS;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_REPOS;
import static code.diegohdez.githubapijava.Utils.Constants.Numbers.PAGE_ONE;

public class PullsFragment extends Fragment implements UpdateablePullsFragment{

    public static final String ARG_ID = "ID";
    public static final String ARG_REPO_NAME = "REPO_NAME";

    PullsAdapter adapter;

    int page = PAGE_ONE;
    boolean isLoading = false;
    boolean isLastPage = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pulls_fragment, container, false);
        final Bundle args = getArguments();
        RecyclerView recyclerView = root.findViewById(R.id.pulls_list);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        adapter = new PullsAdapter();
        recyclerView.setScrollbarFadingEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new PullsPaginationScrollListener(linearLayout) {
            @Override
            protected void loadPulls() {
                isLoading = true;
                page++;
                Toast.makeText(
                        getContext(),
                        "Display page " + page,
                        Toast.LENGTH_SHORT).show();
                PullsRepo pulls = new PullsRepo(getActivity(), args.getLong(ARG_ID));
                pulls.execute(BASE_URL + USER_REPOS + AppManager.getOurInstance().getAccount() +
                        "/" + args.getString(ARG_REPO_NAME) + USER_PULLS + STATE_ALL + "&page=" + page);
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        return  root;
    }

    @Override
    public void update(ArrayList<DataOfPulls> pulls) {
        if (page > 1) {
            adapter.deleteLoading();
            isLoading = false;
        }
        if (!adapter.isLoading()) {
            adapter.addPulls(pulls);
            if (pulls.size() < PAGE_SIZE) isLastPage = true;
            else adapter.addLoading();
        }
    }
}
