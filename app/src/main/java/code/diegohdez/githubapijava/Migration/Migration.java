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
    }
}
