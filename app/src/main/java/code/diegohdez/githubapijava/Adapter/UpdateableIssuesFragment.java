package code.diegohdez.githubapijava.Adapter;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Data.DataOfIssues;

interface UpdateableIssuesFragment {
    public void update(ArrayList<DataOfIssues> issues);
}
