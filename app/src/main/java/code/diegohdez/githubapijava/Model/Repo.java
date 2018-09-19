package code.diegohdez.githubapijava.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Repo extends RealmObject{
    @PrimaryKey
    private long id;
    private String name;
    @SerializedName("full_name")
    private String fullName;
    private String description;
    @SerializedName("forks_count")
    private long forks;
    @SerializedName("stargazers_count")
    private long stars;
    @SerializedName("watchers_count")
    private long watchers;
    @SerializedName("pushed_at")
    private Date pushedAt;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;
    @SerializedName("subscribers_count")
    private long subscribers;
    private Owner owner;
    private RealmList<Issue> issues;
    private RealmList<Pull> pulls;
    private RealmList<Branch> branches;

    public Repo() { }

    public Repo(long id,
                String name,
                String fullName,
                String description,
                long forks,
                long stars,
                long watchers,
                Date pushedAt,
                Date createdAt,
                Date updatedAt,
                long subscribers,
                Owner owner,
                RealmList<Issue> issues,
                RealmList<Pull> pulls,
                RealmList<Branch> branches) {
        this.id = id;
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.forks = forks;
        this.stars = stars;
        this.watchers = watchers;
        this.pushedAt = pushedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.subscribers = subscribers;
        this.owner = owner;
        this.issues = issues;
        this.pulls = pulls;
        this.branches = branches;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getForks() {
        return forks;
    }

    public void setForks(long forks) {
        this.forks = forks;
    }

    public long getStars() {
        return stars;
    }

    public void setStars(long stars) {
        this.stars = stars;
    }

    public long getWatchers() {
        return watchers;
    }

    public void setWatchers(long watchers) {
        this.watchers = watchers;
    }

    public Date getPushedAt() {
        return pushedAt;
    }

    public void setPushedAt(Date pushedAt) {
        this.pushedAt = pushedAt;
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

    public long getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(long subscribers) {
        this.subscribers = subscribers;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public RealmList<Issue> getIssues() { return issues; }

    public void setIssues(RealmList<Issue> issues) { this.issues = issues; }

    public void setPulls (RealmList<Pull> pulls) { this.pulls = pulls; }

    public RealmList<Pull> getPulls() { return pulls; }

    public void setBranches(RealmList<Branch> branches) { this.branches = branches; }

    public RealmList<Branch> getBranches() {  return branches;
    }
}
