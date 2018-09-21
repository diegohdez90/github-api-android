package code.diegohdez.githubapijava.Adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Data.DataOfBranches;
import code.diegohdez.githubapijava.Data.DataOfIssues;
import code.diegohdez.githubapijava.Data.DataOfPulls;

public class PageRepoAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = PageRepoAdapter.class.getSimpleName();
    private static final String ID = "ID";
    private long id;
    private static final String REPO_NAME = "REPO_NAME";
    private String repoName;

    private ArrayList<DataOfIssues> issues;
    private ArrayList<DataOfPulls> pulls;
    private ArrayList<DataOfBranches> branches;

    @SuppressLint("SimpleDateFormat")
    public PageRepoAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setIssues (ArrayList<DataOfIssues> issues) {
        this.issues = issues;
        notifyDataSetChanged();
    }

    public void setPulls(ArrayList<DataOfPulls> pulls) {
        this.pulls = pulls;
        notifyDataSetChanged();
    }

    public void setData(ArrayList<DataOfIssues> issues,
                        ArrayList<DataOfPulls> pulls,
                        ArrayList<DataOfBranches> branches) {
        this.issues = issues;
        this.pulls = pulls;
        this.branches = branches;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle args = new Bundle();
        args.putLong(ID, this.id);
        args.putString(REPO_NAME, this.repoName);
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new IssuesFragment();
                fragment.setArguments(args);
                break;
            case 1:
                fragment = new PullsFragment();
                fragment.setArguments(args);
                break;
        }
        return fragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object instanceof IssuesFragment) {
            ((IssuesFragment) object).update(this.issues);
        } else if (object instanceof PullsFragment) {
            ((PullsFragment) object).update(this.pulls);
        }
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Issues";
            case 1:
                return "Pull Request";
        }
        return null;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRepoName (String repoName) {
        this.repoName = repoName;
    }
}
