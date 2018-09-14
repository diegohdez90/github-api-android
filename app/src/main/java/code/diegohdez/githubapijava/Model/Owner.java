package code.diegohdez.githubapijava.Model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Owner extends RealmObject{

    @PrimaryKey
    private long id;
    private String login;
    @SerializedName("public_repos")
    private int repos;

    public Owner() { }

    public Owner(long id, String login, int repos) {
        this.id = id;
        this.login = login;
        this.repos = repos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setRepos(int repos) {
        this.repos = repos;
    }

    public int getRepos() {
        return repos;
    }
}
