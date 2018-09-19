package code.diegohdez.githubapijava.Data;

import java.util.ArrayList;
import java.util.List;

import code.diegohdez.githubapijava.Model.Branch;

public class DataOfBranches {

    private String name;

    public DataOfBranches() { }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ArrayList<DataOfBranches> createList(List<Branch> branches) {
        ArrayList<DataOfBranches> list = new ArrayList<>();
        for (Branch branch : branches) {
            DataOfBranches item = new DataOfBranches();
            item.setName(branch.getName());
            list.add(item);
        }
        return list;
    }
}
