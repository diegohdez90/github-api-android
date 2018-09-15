package code.diegohdez.githubapijava.Migration;

import code.diegohdez.githubapijava.Utils.Constants.Fields;
import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

import static code.diegohdez.githubapijava.Utils.Constants.Schema.OWNER_SCHEMA;
import static code.diegohdez.githubapijava.Utils.Constants.Schema.REPO_SCHEMA;

public class Migration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 1) {
            realm.getSchema().get(REPO_SCHEMA)
                    .addPrimaryKey(Fields.ID);
            oldVersion++;
        }

        if (oldVersion == 2) {
            schema.create(OWNER_SCHEMA)
                    .addField(Fields.LOGIN, String.class)
                    .addField(Fields.ID, long.class, FieldAttribute.PRIMARY_KEY);

            realm.getSchema()
                    .get(REPO_SCHEMA)
                    .addField("_new_id", long.class)
                    .removeField(Fields.ID)
                    .renameField("_new_id", Fields.ID)
                    .addPrimaryKey(Fields.ID)
                    .addRealmObjectField(Fields.OWNER, schema.get(OWNER_SCHEMA));
            oldVersion++;
        }

        if (oldVersion == 3) {
            schema.get(REPO_SCHEMA)
                    .renameField(Fields.OLD_FULL_NAME, Fields.FULL_NAME)
                    .renameField(Fields.OLD_PUSHED_AT, Fields.PUSHED_AT)
                    .renameField(Fields.OLD_CREATED_AT, Fields.CREATED_AT)
                    .renameField(Fields.OLD_UPDATED_AT, Fields.UPDATED_AT)
                    .removeField("forks_count")
                    .removeField("stargazers_count")
                    .removeField("watchers_count")
                    .removeField("subscribers_count")
                    .addField(Fields.FORKS, long.class)
                    .addField(Fields.STARS, long.class)
                    .addField(Fields.WATCHERS, long.class)
                    .addField(Fields.SUBSCRIBERS, long.class);
            oldVersion++;
        }

        if (oldVersion == 4) {
            schema.get(OWNER_SCHEMA)
                    .addField(Fields.REPOS, long.class);
            oldVersion++;
        }
    }
}
