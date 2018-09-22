package code.diegohdez.githubapijava.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Author extends RealmObject {

    @PrimaryKey
    private long id;
    private String login;

    public Author() {}

    public Author(long id, String login) {
        this.id = id;
        this.login = login;
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
}
