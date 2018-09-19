package code.diegohdez.githubapijava.Adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Data.DataOfBranches;
import code.diegohdez.githubapijava.R;

public class BranchesFragmentAdapter extends android.support.v4.app.Fragment implements UpdateableBranchesFragment{

    public static final String ARG_ID = "ID";
    public static final String ARG_REPO_NAME = "REPO_NAME";

    BranchesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.branches_fragment, container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = root.findViewById(R.id.branches_list);
        Bundle args = getArguments();
        adapter = new BranchesAdapter(args.getLong(ARG_ID), args.getString(ARG_REPO_NAME), getContext());
        recyclerView.setScrollbarFadingEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void update(ArrayList<DataOfBranches> branches) {
        adapter.addBranches(branches);
    }
}
