package code.diegohdez.githubapijava.Adapter;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Data.DataOfBranches;

public interface UpdateableBranchesFragment {
    void update(ArrayList<DataOfBranches> branches);
}
