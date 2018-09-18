package code.diegohdez.githubapijava.Model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PullInfo extends RealmObject {

    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String state;

    public PullInfo ( ) {}

    public PullInfo(String state) {
        this.state = state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
