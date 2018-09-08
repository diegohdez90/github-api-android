package code.diegohdez.githubapijava.Model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Repo extends RealmObject{
    @PrimaryKey
    private int id;
    private String name;
    private String full_name;
    private String description;
    private int forks_count;
    private int stargazers_count;
    private int watchers_count;
    private Date pushed_at;
    private Date created_at;
    private Date updated_at;
    private int subscribers_count;

    public Repo() {
    }

    public Repo(int id,
                String name,
                String full_name,
                String description,
                int forks_count,
                int stargazers_count,
                int watchers_count,
                Date pushed_at,
                Date created_at,
                Date updated_at,
                int subscribers_count) {
        this.id = id;
        this.name = name;
        this.full_name = full_name;
        this.description = description;
        this.forks_count = forks_count;
        this.stargazers_count = stargazers_count;
        this.watchers_count = watchers_count;
        this.pushed_at = pushed_at;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.subscribers_count = subscribers_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getForks_count() {
        return forks_count;
    }

    public void setForks_count(int forks_count) {
        this.forks_count = forks_count;
    }

    public int getStargazers_count() {
        return stargazers_count;
    }

    public void setStargazers_count(int stargazers_count) {
        this.stargazers_count = stargazers_count;
    }

    public int getWatchers_count() {
        return watchers_count;
    }

    public void setWatchers_count(int watchers_count) {
        this.watchers_count = watchers_count;
    }

    public Date getPushed_at() {
        return pushed_at;
    }

    public void setPushed_at(Date pushed_at) {
        this.pushed_at = pushed_at;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public int getSubscribers_count() {
        return subscribers_count;
    }

    public void setSubscribers_count(int subscribers_count) {
        this.subscribers_count = subscribers_count;
    }
}
