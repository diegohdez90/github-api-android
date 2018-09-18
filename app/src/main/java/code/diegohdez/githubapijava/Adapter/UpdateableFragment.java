package code.diegohdez.githubapijava.Adapter;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Data.DataOfIssues;

interface UpdateableFragment {
    public void update(ArrayList<DataOfIssues> issues);
}
