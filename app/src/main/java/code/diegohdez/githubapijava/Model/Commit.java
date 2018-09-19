package code.diegohdez.githubapijava.Model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Commit extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String sha;
    private CommitInfo commitInfo;
    private Owner author;

    public Commit () {}

    public Commit (String sha, CommitInfo commitInfo, Owner author) {
        this.sha = sha;
        this.commitInfo = commitInfo;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getSha() {
        return sha;
    }

    public void setCommitInfo(CommitInfo commitInfo) {
        this.commitInfo = commitInfo;
    }

    public CommitInfo getCommitInfo() {
        return commitInfo;
    }

    public void setAuthor(Owner author) {
        this.author = author;
    }

    public Owner getAuthor() {
        return author;
    }
}
