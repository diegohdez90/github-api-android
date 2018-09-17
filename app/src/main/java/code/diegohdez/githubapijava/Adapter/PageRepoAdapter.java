package code.diegohdez.githubapijava.Adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import code.diegohdez.githubapijava.Data.DataOfIssues;

import static code.diegohdez.githubapijava.Utils.Constants.API.DATE_REPO_FORMAT;

public class PageRepoAdapter extends FragmentStatePagerAdapter {

    private SimpleDateFormat dateFormat;
    private ArrayList<DataOfIssues> list;


    public PageRepoAdapter(FragmentManager fm) {
        super(fm);
        list = new ArrayList<>();
        dateFormat = new SimpleDateFormat(DATE_REPO_FORMAT);
    }

    public void setList (ArrayList<DataOfIssues> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new IssuesAdapter();
        Bundle args = new Bundle();
        args.putString(IssuesAdapter.ISSUE_TITLE, list.get(position).getTitle());
        args.putString(IssuesAdapter.ISSUE_STATE, list.get(position).getState());
        args.putString(IssuesAdapter.ISSUE_CLOSED, dateFormat.format(list.get(position).getClosedAt()));
        args.putString(IssuesAdapter.ISSUE_CLOSED, dateFormat.format(list.get(position).getClosedAt()));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return (this.list.size() > 0) ? this.list.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return this.list.get(position).getTitle();
    }
}
