package code.diegohdez.githubapijava.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import code.diegohdez.githubapijava.Builder.IssuesBuilder;
import code.diegohdez.githubapijava.Model.Issue;

public class DataOfIssues {

    private long id;
    private String title;
    private String description;
    private String state;
    private long number;
    private String user;
    private String assignee;
    private Date closedAt;
    private Date createdAt;
    private Date updatedAt;


    public DataOfIssues(IssuesBuilder builder) {
        this.id = builder.getId();
        this.title = builder.getTitle();
        this.description = builder.getDescription();
        this.state = builder.getState();
        this.number = builder.getNumber();
        this.user = builder.getUser();
        this.assignee = builder.getAssignee();
        this.closedAt = builder.getClosedAt();
        this.createdAt = builder.getCreatedAt();
        this.updatedAt = builder.getUpdatedAt();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    public long getNumber() {
        return number;
    }

    public String getUser() {
        return user;
    }

    public String getAssignee() {
        return assignee;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public static ArrayList<DataOfIssues> createList (List<Issue> issues) {
        ArrayList<DataOfIssues> list = new ArrayList<>();
        for (Issue issue :  issues) {
            DataOfIssues item = new IssuesBuilder(issue.getId())
                    .setTitle(issue.getTitle())
                    .setDescription(issue.getDescription())
                    .setNumber(issue.getNumber())
                    .setState(issue.getState())
                    .setUser(issue.getUser().getLogin())
                    .setAssignee(issue.getAssignee().getLogin())
                    .setCreatedAt(issue.getCreatedAt())
                    .setUpdatedAt(issue.getUpdatedAt())
                    .setClosedAt(issue.getClosedAt())
                    .build();
            list.add(item);
        }
        return list;
    }
}
