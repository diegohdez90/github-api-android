package code.diegohdez.githubapijava.Migration;

import java.util.Date;

import code.diegohdez.githubapijava.Utils.Constants.Fields;
import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

import static code.diegohdez.githubapijava.Utils.Constants.Schema.BRANCH_SCHEMA;
import static code.diegohdez.githubapijava.Utils.Constants.Schema.ISSUE_SCHEMA;
import static code.diegohdez.githubapijava.Utils.Constants.Schema.OWNER_SCHEMA;
import static code.diegohdez.githubapijava.Utils.Constants.Schema.PULL_INFO_SCHEMA;
import static code.diegohdez.githubapijava.Utils.Constants.Schema.PULL_SCHEMA;
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

        if (oldVersion == 5) {
            schema.create(ISSUE_SCHEMA)
                    .addField(Fields.ID, long.class, FieldAttribute.PRIMARY_KEY)
                    .addField(Fields.TITLE, String.class)
                    .addField(Fields.DESCRIPTION, String.class)
                    .addField(Fields.NUMBER, long.class)
                    .addField(Fields.STATE, String.class)
                    .addRealmObjectField(Fields.USER, schema.get(OWNER_SCHEMA))
                    .addRealmObjectField(Fields.ASSIGNEE, schema.get(OWNER_SCHEMA))
                    .addField(Fields.CLOSED_AT, Date.class)
                    .addField(Fields.CREATED_AT, Date.class)
                    .addField(Fields.UPDATED_AT, Date.class);

            schema.get(REPO_SCHEMA)
                    .addRealmListField(Fields.ISSUES, schema.get(ISSUE_SCHEMA));

            oldVersion++;
        }

        if (oldVersion == 6) {
            schema.create(PULL_SCHEMA)
                    .addField(Fields.ID, long.class, FieldAttribute.PRIMARY_KEY)
                    .addField(Fields.TITLE, String.class)
                    .addField(Fields.DESCRIPTION, String.class)
                    .addField(Fields.NUMBER, long.class)
                    .addField(Fields.STATE, String.class)
                    .addRealmObjectField(Fields.USER, schema.get(OWNER_SCHEMA))
                    .addRealmObjectField(Fields.ASSIGNEE, schema.get(OWNER_SCHEMA))
                    .addField(Fields.CLOSED_AT, Date.class)
                    .addField(Fields.CREATED_AT, Date.class)
                    .addField(Fields.UPDATED_AT, Date.class)
                    .addField(Fields.MERGED_AT, Date.class);

            schema.create(PULL_INFO_SCHEMA)
                    .addField(Fields.ID, String.class, FieldAttribute.PRIMARY_KEY)
                    .addField(Fields.PULL_STATE, String.class);

            schema.get(REPO_SCHEMA)
                    .addRealmListField(Fields.PULLS, schema.get(PULL_SCHEMA));

            schema.get(ISSUE_SCHEMA)
                    .addRealmObjectField(Fields.PULL_INFO, schema.get(PULL_INFO_SCHEMA));

            oldVersion++;
        }

        if (oldVersion == 7) {
            schema.create(BRANCH_SCHEMA)
                    .addField(Fields.ID, String.class, FieldAttribute.PRIMARY_KEY)
                    .addField(Fields.BRANCH_NAME, String.class);

            schema.get(REPO_SCHEMA)
                    .addRealmListField(Fields.BRANCHES, schema.get(BRANCH_SCHEMA));

            oldVersion++;
        }
    }
}
