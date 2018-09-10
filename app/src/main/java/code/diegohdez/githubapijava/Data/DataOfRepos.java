package code.diegohdez.githubapijava.Data;

import java.util.ArrayList;
import java.util.List;

import code.diegohdez.githubapijava.Builder.ReposBuilder;
import code.diegohdez.githubapijava.Model.Repo;

public class DataOfRepos {

    private String name;
    private String description;
    private int stars;
    private int watchers;
    private int forks;

    public DataOfRepos (ReposBuilder builder) {
        this.name = builder.getName();
        this.description = builder.getDescription();
        this.stars = builder.getStars();
        this.watchers = builder.getWatchers();
        this.forks = builder.getForks();
    }

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

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getWatchers() {
        return watchers;
    }

    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    public int getForks() {
        return forks;
    }

    public void setFork(int forks) {
        this.forks = forks;
    }

    public static ArrayList<DataOfRepos> createRepoList(List<Repo> repos) {
        ArrayList<DataOfRepos> list = new ArrayList<>();
        for (Repo repo: repos) {
            DataOfRepos item = new ReposBuilder(repo.getName())
                    .setDescription(repo.getDescription())
                    .setWatchers(repo.getWatchers_count())
                    .setStars(repo.getStargazers_count())
                    .setForks(repo.getForks_count())
                    .build();
            list.add(item);
        }
        return list;
    }
}
