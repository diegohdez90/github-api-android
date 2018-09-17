package code.diegohdez.githubapijava.Data;

import java.util.ArrayList;
import java.util.List;

import code.diegohdez.githubapijava.Builder.ReposBuilder;
import code.diegohdez.githubapijava.Model.Repo;

public class DataOfRepos {

    private long id;
    private String name;
    private String description;
    private long stars;
    private long watchers;
    private long forks;
    private long subscribers;

    public DataOfRepos (ReposBuilder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.description = builder.getDescription();
        this.stars = builder.getStars();
        this.watchers = builder.getWatchers();
        this.forks = builder.getForks();
        this.subscribers = builder.getSubscribers();
    }

    public DataOfRepos() { }

    public void setId(long id) { this.id = id; }

    public long getId() { return id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public long getForks() {
        return forks;
    }

    public void setFork(int forks) {
        this.forks = forks;
    }

    public void setSubscribers(long subscribers) {
        this.subscribers = subscribers;
    }

    public long getSubscribers() {
        return subscribers;
    }

    public static ArrayList<DataOfRepos> createRepoList(List<Repo> repos) {
        ArrayList<DataOfRepos> list = new ArrayList<>();
        for (Repo repo: repos) {
            DataOfRepos item = new ReposBuilder(repo.getId())
                    .setName(repo.getName())
                    .setDescription(repo.getDescription())
                    .setWatchers(repo.getWatchers())
                    .setStars(repo.getStars())
                    .setForks(repo.getForks())
                    .setSubscribers(repo.getSubscribers())
                    .build();
            list.add(item);
        }
        return list;
    }
}
