package code.diegohdez.githubapijava.Adapter;

import java.util.ArrayList;

import code.diegohdez.githubapijava.Data.DataOfPulls;

public interface UpdateablePullsFragment {
    public void update(ArrayList<DataOfPulls> pulls);
}
