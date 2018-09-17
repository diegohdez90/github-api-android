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

public class IssueItemAdapter extends Fragment {

    public static final String ISSUE_TITLE = "ISSUE_TITLE";
    public static final String ISSUE_STATE = "ISSUE_STATE";
    public static final String ISSUE_CLOSED = "ISSUE_CLOSED";
    public static final String ISSUE_CREATED = "ISSUE_CREATED";

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
