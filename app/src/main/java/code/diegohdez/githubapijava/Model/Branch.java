package code.diegohdez.githubapijava.Model;

import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Branch extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private RealmList<Commit> commits;

    public Branch () { }

    public Branch (String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setCommits(RealmList<Commit> commits) {
        this.commits = commits;
    }

    public RealmList<Commit> getCommits() {
        return commits;
    }
}