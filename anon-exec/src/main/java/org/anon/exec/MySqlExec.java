package org.anon.exec;

import javax.sql.DataSource;

import org.anon.vendor.constraint.referential.ForeignKeyConstraintManager;
import org.anon.vendor.constraint.referential.MySqlForeignKeyConstraintManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class MySqlExec extends BaseExec{
	@Override
	protected ForeignKeyConstraintManager getConstraintManager(DataSource dataSource) {
		return new MySqlForeignKeyConstraintManager(dataSource);
	}

}
