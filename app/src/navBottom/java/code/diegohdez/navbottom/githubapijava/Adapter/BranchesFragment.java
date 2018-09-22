package code.diegohdez.navbottom.githubapijava.Adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Adapter.BranchesAdapter;
import code.diegohdez.githubapijava.AsyncTask.BranchesRepo;
import code.diegohdez.githubapijava.Data.DataOfBranches;
import code.diegohdez.githubapijava.Manager.AppManager;
import code.diegohdez.githubapijava.Model.Branch;
import code.diegohdez.githubapijava.Model.Repo;
import code.diegohdez.githubapijava.R;
import code.diegohdez.githubapijava.ScrollListener.BranchPaginationScrollListener;
import code.diegohdez.githubapijava.Utils.Constants.Fields;
import io.realm.Realm;
import io.realm.RealmList;

import static code.diegohdez.githubapijava.Utils.Constants.API.BASE_URL;
import static code.diegohdez.githubapijava.Utils.Constants.API.BRANCHES;
import static code.diegohdez.githubapijava.Utils.Constants.API.PAGE_SIZE;
import static code.diegohdez.githubapijava.Utils.Constants.API.USER_REPOS;
import static code.diegohdez.githubapijava.Utils.Constants.Numbers.PAGE_ONE;

public class BranchesFragment extends Fragment {

    public static final String ARG_ID = "ID";
    public static final String ARG_REPO_NAME = "REPO_NAME";

    BranchesFragment fragment;
    BranchesAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<DataOfBranches> list = new ArrayList<>();
    Bundle args;
    int page = PAGE_ONE;
    boolean isLoading = false;
    boolean isLastPage = false;

    public static BranchesFragment newInstance() {
        return new BranchesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.branches_fragment, container, false);
        fragment = this;
        args = getArguments();
        adapter = new BranchesAdapter(args.getLong(ARG_ID), args.getString(ARG_REPO_NAME), this);
        recyclerView = root.findViewById(R.id.branches_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new BranchPaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadBranches() {
                isLoading = true;
                page++;
                AppManager.getOurInstance().setBranchesPage(page);
                BranchesRepo branchesRepo = new BranchesRepo(fragment, args.getLong(ARG_ID));
                branchesRepo.execute(BASE_URL + USER_REPOS + AppManager.getOurInstance().getAccount() + "/" + args.getString(ARG_REPO_NAME) + BRANCHES + "&page=" + page);
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
        setBranchesList();
        return root;
    }

    public void setBranchesList(){
        Realm realm = Realm.getDefaultInstance();
        Repo repo = realm.where(Repo.class).equalTo(Fields.ID, args.getLong(ARG_ID)).findFirst();
        RealmList<Branch> branches = repo != null ? repo.getBranches() : new RealmList<Branch>();
        ArrayList<DataOfBranches> list = DataOfBranches.createList(branches);
        this.adapter.addBranches(list);
        realm.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        page = AppManager.getOurInstance().getCurrentBranchPage();
    }

    public void addBranches(RealmList<Branch> branches) {
        adapter.deleteLoading();
        isLoading = false;
        ArrayList<DataOfBranches> list = DataOfBranches.createList(branches);
        this.adapter.addBranches(list);
        if (list.size() < PAGE_SIZE) isLastPage = true;
        else adapter.addLoading();
    }
}
