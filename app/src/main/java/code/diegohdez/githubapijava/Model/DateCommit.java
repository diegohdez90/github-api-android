package code.diegohdez.githubapijava.Model;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DateCommit extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String name;
    private Date date;

    public DateCommit() { }

    public DateCommit(String id, String name, Date date) {
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
