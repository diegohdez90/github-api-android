package code.diegohdez.githubapijava.Migration;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

public class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        if (oldVersion == 1) {
            realm.getSchema().get("Repo")
                    .addPrimaryKey("id");
            oldVersion++;
        }
    }
}
