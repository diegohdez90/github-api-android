package code.diegohdez.githubapijava.Model;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Commit extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String sha;
    @SerializedName("commit")
    private CommitInfo commitInfo;
    private Author author;

    public Commit () {}

    public Commit (String sha, CommitInfo commitInfo, Author author) {
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

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Author getAuthor() {
        return author;
    }
}
