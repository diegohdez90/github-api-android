package code.diegohdez.githubapijava.Builder;

import java.util.Date;

import code.diegohdez.githubapijava.Data.DataOfIssues;
import code.diegohdez.githubapijava.Model.Owner;
import code.diegohdez.githubapijava.Model.PullInfo;

public class IssuesBuilder {
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
    private boolean isPull;
    private String pullState;

    public IssuesBuilder (long id) {
        this.id = id;
    }

    public IssuesBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public IssuesBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public IssuesBuilder setNumber(long number) {
        this.number = number;
        return this;
    }

    public IssuesBuilder setState(String state) {
        this.state = state;
        return this;
    }

    public IssuesBuilder setUser(String user) {
        this.user = user;
        return this;
    }

    public IssuesBuilder setAssignee(Owner assignee) {
        if (assignee != null) this.assignee = assignee.getLogin();
        return this;
    }

    public IssuesBuilder setClosedAt(Date closedAt) {
        if (closedAt != null) this.closedAt = closedAt;
        return this;
    }

    public IssuesBuilder setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public IssuesBuilder setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public IssuesBuilder setIsPull(PullInfo pullInfo) {
        if (pullInfo != null) {
            this.isPull = true;
            this.pullState = pullInfo.getState();
        }
        else {
            this.isPull = false;
            this.pullState = "none";
        }
        return this;
    }

    public DataOfIssues build() {
        return new DataOfIssues(this);
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

    public boolean isPull() { return isPull; }

    public String getPullState() { return pullState; }
}
