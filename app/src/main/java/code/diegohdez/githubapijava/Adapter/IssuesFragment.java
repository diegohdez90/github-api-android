package code.diegohdez.githubapijava.Adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import code.diegohdez.githubapijava.AsyncTask.IssuesRepo;
import code.diegohdez.githubapijava.Data.DataOfIssues;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.ScrollListener.IssuesPaginationScrollListener;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.PAGE_SIZE;
import static code.diegohdez.githubapijava.Utils.Constants.API.STATE_ALL;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_ISSUES;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_REPOS;
import static code.diegohdez.githubapijava.Utils.Constants.Numbers.PAGE_ONE;

@SuppressLint("ValidFragment")
public class IssuesFragment extends Fragment implements UpdateableIssuesFragment {

    public static final String ARG_ID = "ID";
    public static final String ARG_REPO_NAME = "REPO_NAME";

    RecyclerView recyclerView;
    IssuesAdapter adapter;
    LinearLayout layout_checkboxes;

    int page = PAGE_ONE;
    boolean isLoading = false;
    boolean isLastPage = false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.issues_fragment, container, false);
        layout_checkboxes.setVisibility(View.GONE);
        final Bundle args = getArguments();
        recyclerView = root.findViewById(R.id.issues_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new IssuesAdapter();
        recyclerView.setScrollbarFadingEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener( new IssuesPaginationScrollListener(layoutManager) {

            @Override
            protected void loadIssues() {
                isLoading = true;
                page++;
                Toast.makeText(
                        getContext(),
                        "Display page " + page,
                        Toast.LENGTH_SHORT).show();
                IssuesRepo loadIssues = new IssuesRepo(getActivity(), args.getLong(ARG_ID));
                loadIssues.execute(BASE_URL +
                        USER_REPOS +
                        AppManager.getOurInstance().getAccount() +
                        "/" +args.getString(ARG_REPO_NAME) +
                        USER_ISSUES + STATE_ALL + "&page=" + page);
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
    public void update(ArrayList<DataOfIssues> issues) {
        if (page > PAGE_ONE) {
            adapter.deleteLoading();
            isLoading = false;
        }
        if (!adapter.isLoading()) {
            adapter.addIssues(issues);
            if (issues.size() < PAGE_SIZE) isLastPage = true;
            else adapter.addLoading();
        }
    }
}
