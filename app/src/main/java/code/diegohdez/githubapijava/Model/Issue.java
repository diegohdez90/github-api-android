package code.diegohdez.githubapijava.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Issue extends RealmObject {

    @PrimaryKey
    private long id;
    private String title;
    @SerializedName("body")
    private String description;
    private String state;
    private long number;
    private Owner user;
    private Owner assignee;
    @SerializedName("closed_at")
    private Date closedAt;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;
    private PullInfo pullInfo;

    public Issue() { }

    public Issue(long id,
                 String title,
                 String description,
                 String state,
                 long number,
                 Owner user,
                 Owner assignee,
                 Date closedAt,
                 Date createdAt,
                 Date updatedAt,
                 PullInfo pullInfo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.state = state;
        this.number = number;
        this.user = user;
        this.assignee = assignee;
        this.closedAt = closedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.pullInfo = pullInfo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Owner getUser() {
        return user;
    }

    public void setUser(Owner user) {
        this.user = user;
    }

    public Owner getAssignee() {
        return assignee;
    }

    public void setAssignee(Owner assignee) {
        this.assignee = assignee;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setPullInfo(PullInfo pullInfo) { this.pullInfo = pullInfo; }

    public PullInfo getPullInfo() { return pullInfo; }
}
