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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import static code.diegohdez.githubapijava.Utils.Constants.API.AND_PAGE;
import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.PAGE_SIZE;
import static code.diegohdez.githubapijava.Utils.Constants.API.STATE_ALL;
import static code.diegohdez.githubapijava.Utils.Constants.API.STATE_CLOSED;
import static code.diegohdez.githubapijava.Utils.Constants.API.STATE_OPEN;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_ISSUES;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_REPOS;
import static code.diegohdez.githubapijava.Utils.Constants.Numbers.PAGE_ONE;

public class IssuesFragment extends Fragment {

    public static final String TAG = IssuesFragment.class.getSimpleName();

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
    String state;
    CheckBox open;
    CheckBox closed;
    ProgressBar progressBar;

    public static IssuesFragment newInstance() {
        return new IssuesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.issues_fragment, container, false);
        fragment = this;
        args = getArguments();
        state = STATE_ALL;
        adapter = new IssuesAdapter(this.list);
        recyclerView = root.findViewById(R.id.issues_list);
        open = root.findViewById(R.id.open);
        closed = root.findViewById(R.id.closed);
        progressBar = root.findViewById(R.id.loading_selected_checkbox_issues);
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
                Toast.makeText(
                        getActivity(),
                        "Display " + page + " page",
                        Toast.LENGTH_SHORT).show();
                issuesRepo.execute(BASE_URL + USER_REPOS + AppManager.getOurInstance().getAccount() + "/" + args.getString(ARG_REPO_NAME) + USER_ISSUES + state + AND_PAGE + page);
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
        open.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.clear();
                AppManager.getOurInstance().setIssuesPage(PAGE_ONE);
                page = 1;
                Realm realm = Realm.getDefaultInstance();
                final Repo repo = realm.where(Repo.class).equalTo(Fields.ID, args.getLong(ARG_ID)).findFirst();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        repo.getIssues().deleteAllFromRealm();
                    }
                });
                if (isChecked) {
                    state = STATE_OPEN;
                    if (closed.isChecked()) state = STATE_ALL;
                } else {
                    if (closed.isChecked()) state = STATE_CLOSED;
                    else state = STATE_ALL;
                }
                IssuesRepo issuesRepo = new IssuesRepo(fragment, args.getLong(ARG_ID));
                issuesRepo.execute(BASE_URL + USER_REPOS + AppManager.getOurInstance().getAccount() + "/" + args.getString(ARG_REPO_NAME) + USER_ISSUES + state + AND_PAGE + page);
                progressBar.setVisibility(View.VISIBLE);
                realm.close();
            }
        });
        closed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.clear();
                AppManager.getOurInstance().setIssuesPage(PAGE_ONE);
                page = 1;
                Realm realm = Realm.getDefaultInstance();
                final Repo repo = realm.where(Repo.class).equalTo(Fields.ID, args.getLong(ARG_ID)).findFirst();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        repo.getIssues().deleteAllFromRealm();
                    }
                });
                if (isChecked) {
                    state = STATE_CLOSED;
                    if (open.isChecked()) state = STATE_ALL;
                } else {
                    if (open.isChecked()) state = STATE_OPEN;
                    else state = STATE_ALL;
                }
                IssuesRepo issuesRepo = new IssuesRepo(fragment, args.getLong(ARG_ID));
                issuesRepo.execute(BASE_URL + USER_REPOS + AppManager.getOurInstance().getAccount() + "/" + args.getString(ARG_REPO_NAME) + USER_ISSUES + state + AND_PAGE + page);
                progressBar.setVisibility(View.VISIBLE);
                realm.close();
            }
        });
        setIssuesList();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        page = AppManager.getOurInstance().getCurrentIssuesPage();
    }

    public void setIssuesList() {
        Realm realm = Realm.getDefaultInstance();
        Repo repo = realm.where(Repo.class).equalTo(Fields.ID, args.getLong(ARG_ID)).findFirst();
        RealmList<Issue> issues = repo != null ? repo.getIssues() : new RealmList<Issue>();
        ArrayList<DataOfIssues> list = DataOfIssues.createList(issues);
        this.adapter.addIssues(list);
        if (list.size() == PAGE_SIZE) adapter.addLoading();
        realm.close();
    }

    public void addIssues(RealmList<Issue> issues) {
        progressBar.setVisibility(View.GONE);
        if (page > PAGE_ONE) {
            adapter.deleteLoading();
            isLoading = false;
        }
        ArrayList<DataOfIssues> list = DataOfIssues.createList(issues);
        this.adapter.addIssues(list);
        if (list.size() < PAGE_SIZE) isLastPage = true;
        else adapter.addLoading();
    }
}
