package code.diegohdez.githubapijava.Builder;

import code.diegohdez.githubapijava.Data.DataOfRepos;

public class ReposBuilder {

    private String name;
    private String description;
    private int watchers;
    private int stars;
    private int forks;

    public ReposBuilder (String name) {
        this.name = name;
    }

    public ReposBuilder setDescription (String description) {
        this.description = description;
        return this;
    }

    public ReposBuilder setWatchers (int watchers) {
        this.watchers = watchers;
        return this;
    }

    public ReposBuilder setStars (int stars) {
        this.stars = stars;
        return this;
    }

    public ReposBuilder setForks (int forks) {
        this.forks = forks;
        return this;
    }

    public DataOfRepos build(){
        return new DataOfRepos(this);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getWatchers() {
        return watchers;
    }

    public int getStars() {
        return stars;
    }

    public int getForks() {
        return forks;
    }
}
