package code.diegohdez.githubapijava.Adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Data.DataOfIssues;

public class PageRepoAdapter extends FragmentStatePagerAdapter {

    private ArrayList<DataOfIssues> issues;

    @SuppressLint("SimpleDateFormat")
    public PageRepoAdapter(FragmentManager fm) {
        super(fm);
        issues = new ArrayList<>();
    }

    public void setIssues (ArrayList<DataOfIssues> issues) {
        this.issues = issues;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new IssuesFragmentAdapter(issues);
        }
        return new Fragment();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (object instanceof IssuesFragmentAdapter) {
            ((IssuesFragmentAdapter) object).update(this.issues);
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
}
