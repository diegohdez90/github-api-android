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

import java.util.ArrayList;

import code.diegohdez.githubapijava.Data.DataOfPulls;
import code.diegohdez.githubapijava.R;

public class PullsFragmentAdapter extends Fragment implements UpdateablePullsFragment{

    PullsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pulls_fragment, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.pulls_list);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        adapter = new PullsAdapter();
        recyclerView.setScrollbarFadingEnabled(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(adapter);
        return  root;
    }

    @Override
    public void update(ArrayList<DataOfPulls> pulls) {
        adapter.addPulls(pulls);
    }
}
