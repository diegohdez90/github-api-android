package code.diegohdez.githubapijava.Model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CommitInfo extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String message;
    private DateCommit author;

    public CommitInfo () {}

    public CommitInfo (String message, DateCommit date) {
        this.message = message;
        this.author = date;
    }

    public String getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setAuthor(DateCommit author) {
        this.author = author;
    }

    public DateCommit getAuthor() {
        return author;
    }
}
