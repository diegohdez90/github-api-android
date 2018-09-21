package code.diegohdez.navbottom.githubapijava.Adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Adapter.IssuesAdapter;
import code.diegohdez.githubapijava.AsyncTask.IssuesRepo;
import code.diegohdez.githubapijava.Data.DataOfIssues;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Model.Issue;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.ScrollListener.IssuesPaginationScrollListener;
import code.diegohdez.githubapijava.Utils.Constants.Fields;
import io.realm.Realm;
import io.realm.RealmList;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.PAGE_SIZE;
import static code.diegohdez.githubapijava.Utils.Constants.API.STATE_ALL;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_ISSUES;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_REPOS;
import static code.diegohdez.githubapijava.Utils.Constants.Numbers.PAGE_ONE;

public class IssuesFragment extends Fragment {

    public static final String ARG_ID = "ID";
    public static final String ARG_REPO_NAME = "REPO_NAME";

    IssuesFragment fragment;
    IssuesAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<DataOfIssues> list = new ArrayList<>();
    Bundle args;
    int page = PAGE_ONE;
    boolean isLoading = false;
    boolean isLastPage = false;

    public static IssuesFragment newInstance() {
        return new IssuesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.issues_fragment, container, false);
        fragment = this;
        args = getArguments();
        adapter = new IssuesAdapter(this.list);
        recyclerView = root.findViewById(R.id.issues_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new IssuesPaginationScrollListener(linearLayoutManager) {

            @Override
            protected void loadIssues() {
                isLoading = true;
                page++;
                IssuesRepo issuesRepo = new IssuesRepo(fragment, args.getLong(ARG_ID));
                issuesRepo.execute(BASE_URL + USER_REPOS + AppManager.getOurInstance().getAccount() + "/" + args.getString(ARG_REPO_NAME) + USER_ISSUES + STATE_ALL + "&page=" + page);
                AppManager.getOurInstance().setIssuesPage(page);
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
        setIssuesList();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        page = AppManager.getOurInstance().getCurrentIssuesPage();
        Log.i(IssuesFragment.class.getSimpleName(), "Return to issues : " + page);
    }

    public void setIssuesList() {
        Realm realm = Realm.getDefaultInstance();
        Repo repo = realm.where(Repo.class).equalTo(Fields.ID, args.getLong(ARG_ID)).findFirst();
        RealmList<Issue> issues = repo != null ? repo.getIssues() : new RealmList<Issue>();
        realm.close();
        ArrayList<DataOfIssues> list = DataOfIssues.createList(issues);
        this.adapter.addIssues(list);
        adapter.addLoading();
    }

    public void addIssues(RealmList<Issue> issues) {
        adapter.deleteLoading();
        isLoading = false;
        ArrayList<DataOfIssues> list = DataOfIssues.createList(issues);
        this.adapter.addIssues(list);
        if (list.size() < PAGE_SIZE) isLastPage = true;
        else adapter.addLoading();
    }
}
