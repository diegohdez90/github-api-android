package code.diegohdez.githubapijava.Model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CommitInfo extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String message;
    @SerializedName("author.date")
    private Date date;

    public CommitInfo () {}

    public CommitInfo (String message, Date date) {
        this.message = message;
        this.date = date;
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

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }
}
