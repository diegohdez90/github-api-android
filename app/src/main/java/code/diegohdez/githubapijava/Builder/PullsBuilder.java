package code.diegohdez.githubapijava.Builder;

import java.util.Date;

import code.diegohdez.githubapijava.Data.DataOfPulls;
import code.diegohdez.githubapijava.Model.Owner;

public class PullsBuilder {

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

    public PullsBuilder (long id) {
        this.id = id;
    }

    public PullsBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public PullsBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public PullsBuilder setNumber(long number) {
        this.number = number;
        return this;
    }

    public PullsBuilder setState(String state) {
        this.state = state;
        return this;
    }

    public PullsBuilder setUser(Owner user) {
        this.user = user.getLogin();
        return this;
    }

    public PullsBuilder setAssignee(Owner assignee) {
        if (assignee != null) this.assignee = assignee.getLogin();
        return this;
    }

    public PullsBuilder setCreatedAt(Date createdAt) {
        this.createdAt =createdAt;
        return this;
    }

    public PullsBuilder setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public PullsBuilder setClosedAt(Date closedAt) {
        if (closedAt != null) this.closedAt = closedAt;
        return this;
    }

    public PullsBuilder setMergedAt(Date mergedAt) {
        if (mergedAt != null) this.mergedAt = mergedAt;
            return this;
    }

    public DataOfPulls build(){
        return new DataOfPulls(this);
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
}
