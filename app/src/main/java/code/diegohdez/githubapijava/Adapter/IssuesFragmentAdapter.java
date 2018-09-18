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

import java.util.ArrayList;

import code.diegohdez.githubapijava.Data.DataOfIssues;
import code.diegohdez.githubapijava.R;

@SuppressLint("ValidFragment")
public class IssuesFragmentAdapter extends Fragment implements UpdateableFragment {

    public static final String ISSUE_TITLE = "ISSUE_TITLE";

    private ArrayList<DataOfIssues> issues;
    RecyclerView recyclerView;
    IssuesAdapter adapter;

    public IssuesFragmentAdapter(ArrayList<DataOfIssues> issues) {
        this.issues = issues;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.issues_fragment, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.issues_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new IssuesAdapter();
        recyclerView.setScrollbarFadingEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return  root;
    }

    @Override
    public void update(ArrayList<DataOfIssues> issues) {
        adapter.addIssues(issues);
    }
}
