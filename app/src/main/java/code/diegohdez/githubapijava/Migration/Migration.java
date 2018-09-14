package code.diegohdez.githubapijava.Migration;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 1) {
            realm.getSchema().get("Repo")
                    .addPrimaryKey("id");
            oldVersion++;
        }

        if (oldVersion == 2) {
            schema.create("Owner")
                    .addField("login", String.class)
                    .addField("id", long.class, FieldAttribute.PRIMARY_KEY);

            realm.getSchema()
                    .get("Repo")
                    .addField("_new_id", long.class)
                    .removeField("id")
                    .renameField("_new_id", "id")
                    .addPrimaryKey("id")
                    .addRealmObjectField("owner", schema.get("Owner"));
            oldVersion++;
        }

        if (oldVersion == 3) {
            schema.get("Repo")
                    .renameField("full_name", "fullName")
                    .renameField("pushed_at", "pushedAt")
                    .renameField("created_at", "createdAt")
                    .renameField("updated_at", "updatedAt")
                    .removeField("forks_count")
                    .removeField("stargazers_count")
                    .removeField("watchers_count")
                    .removeField("subscribers_count")
                    .addField("forks", long.class)
                    .addField("stars", long.class)
                    .addField("watchers", long.class)
                    .addField("subscribers", long.class);
            oldVersion++;
        }

        if (oldVersion == 4) {
            schema.get("Owner")
                    .addField("repos", long.class);
        }
    }
}
