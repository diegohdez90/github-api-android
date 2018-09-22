package code.diegohdez.githubapijava.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import code.diegohdez.githubapijava.Builder.CommitsBuilder;
import code.diegohdez.githubapijava.Model.Commit;

public class DataOfCommits {

    private String sha;
    private String author;
    private String message;
    private Date date;

    public DataOfCommits() {}

    public DataOfCommits(CommitsBuilder builder) {
        this.sha = builder.getSha();
        this.author = builder.getAuthor();
        this.message = builder.getMessage();
        this.date = builder.getDate();
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

    public static ArrayList<DataOfCommits> createList(List<Commit> commits) {
        ArrayList<DataOfCommits> list = new ArrayList<>();
        for (Commit commit : commits) {
            DataOfCommits item = new CommitsBuilder(commit.getSha())
                    .setAuthor(commit.getAuthor(), commit.getCommitInfo())
                    .setMessage(commit.getCommitInfo())
                    .build();
            list.add(item);
        }
        return list;
    }
}
