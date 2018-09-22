package code.diegohdez.githubapijava.Builder;

import android.util.Log;

import java.util.Date;

import code.diegohdez.githubapijava.Data.DataOfCommits;
import code.diegohdez.githubapijava.Model.Author;
import code.diegohdez.githubapijava.Model.CommitInfo;

public class CommitsBuilder {

    private String sha;
    private String author;
    private String message;
    private Date date;

    public CommitsBuilder(String sha) {
        this.sha = sha;
    }

    public CommitsBuilder setAuthor(Author author, CommitInfo commitInfo) {
        this.author = (author != null) ? author.getLogin() : commitInfo.getAuthor().getName();
        return this;
    }

    public CommitsBuilder setMessage(CommitInfo commitInfo) {
        this.message = commitInfo.getMessage();
        this.date = commitInfo.getAuthor().getDate();
        return this;
    }

    public DataOfCommits build(){
        return new DataOfCommits(this);
    }

    public String getSha() {
        return sha;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }
}
