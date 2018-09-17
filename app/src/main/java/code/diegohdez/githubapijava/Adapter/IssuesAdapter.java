package code.diegohdez.githubapijava.Adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import code.diegohdez.githubapijava.R;

public class IssuesAdapter extends Fragment {

    public static final String ISSUE_TITLE = "ISSUE_TITLE";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.item_issue, container, false);
        Bundle args = getArguments();
        ((TextView) root.findViewById(R.id.issue_title))
                .setText(args.getString(ISSUE_TITLE));
        return  root;
    }
}
