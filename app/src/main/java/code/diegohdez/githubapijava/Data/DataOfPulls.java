package code.diegohdez.githubapijava.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import code.diegohdez.githubapijava.Builder.PullsBuilder;
import code.diegohdez.githubapijava.Model.Pull;

public class DataOfPulls {

    private long id;
    private String title;
    private String description;
    private long number;
    private String state;
    private String user;
    private String assignee;
    private Date closedAt;
    private Date createdAt;
    private Date updatedAt;
    private Date mergedAt;

    public DataOfPulls() {}

    public DataOfPulls(PullsBuilder builder) {
        this.id = builder.getId();
        this.title = builder.getTitle();
        this.description = builder.getDescription();
        this.number = builder.getNumber();
        this.state = builder.getState();
        this.user = builder.getUser();
        this.assignee = builder.getAssignee();
        this.createdAt = builder.getCreatedAt();
        this.updatedAt = builder.getUpdatedAt();
        this.closedAt = builder.getClosedAt();
        this.mergedAt = builder.getMergedAt();
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

    public long getNumber() {
        return number;
    }

    public String getState() {
        return state;
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

    public Date getMergedAt() {
        return mergedAt;
    }

    public static ArrayList<DataOfPulls> createList(List<Pull> pulls) {
        ArrayList<DataOfPulls> list = new ArrayList<>();
        for (Pull pull : pulls) {
            DataOfPulls item = new PullsBuilder(pull.getId())
                    .setTitle(pull.getTitle())
                    .setDescription(pull.getDescription())
                    .setNumber(pull.getNumber())
                    .setState(pull.getState())
                    .setUser(pull.getUser())
                    .setAssignee(pull.getAssignee())
                    .setCreatedAt(pull.getCreatedAt())
                    .setUpdatedAt(pull.getUpdatedAt())
                    .setClosedAt(pull.getClosedAt())
                    .setMergedAt(pull.getMergedAt())
                    .build();
            list.add(item);
        }
        return list;
    }
}
