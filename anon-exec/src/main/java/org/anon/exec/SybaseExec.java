package org.anon.exec;

import javax.sql.DataSource;

import org.anon.vendor.constraint.referential.ForeignKeyConstraintManager;
import org.anon.vendor.constraint.referential.SybaseForeignKeyConstraintManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class SybaseExec extends BaseExec {

	@Override
	protected ForeignKeyConstraintManager getConstraintManager(final DataSource dataSource) {

		return new SybaseForeignKeyConstraintManager(dataSource);
	}


}

