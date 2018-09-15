package code.diegohdez.githubapijava.Builder;

import code.diegohdez.githubapijava.Data.DataOfRepos;

public class ReposBuilder {

    private long id;
    private String name;
    private String description;
    private long watchers;
    private long stars;
    private long forks;
    private long subscribers;

    public ReposBuilder (long id) {
        this.id = id;
    }

    public ReposBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ReposBuilder setDescription (String description) {
        this.description = description;
        return this;
    }

    public ReposBuilder setWatchers (long watchers) {
        this.watchers = watchers;
        return this;
    }

    public ReposBuilder setStars (long stars) {
        this.stars = stars;
        return this;
    }

    public ReposBuilder setForks (long forks) {
        this.forks = forks;
        return this;
    }

    public ReposBuilder setSubscribers (long subscribers) {
        this.subscribers = subscribers;
        return this;
    }

    public DataOfRepos build(){
        return new DataOfRepos(this);
    }

    public void setId(long id) { this.id = id; }

    public long getId() { return id; }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getWatchers() {
        return watchers;
    }

    public long getStars() {
        return stars;
    }

    public long getForks() {
        return forks;
    }

    public long getSubscribers() { return subscribers; }
}
