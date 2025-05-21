package me.sathish.runswithshedlock.config;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;


public class RunswithshedlockNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {

    private static final String TABLE_PREFIX = "garminrun_";

    private Identifier adjustName(final Identifier name) {
        if (name == null) {
            return null;
        }
        final String adjustedName = TABLE_PREFIX + name.getText();
        return new Identifier(adjustedName, true);
    }

    @Override
    public Identifier toPhysicalTableName(final Identifier name, final JdbcEnvironment context) {
        return adjustName(super.toPhysicalTableName(name, context));
    }

}
