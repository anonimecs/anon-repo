package org.anon.exec;

import javax.sql.DataSource;

import org.anon.vendor.constraint.ForeignKeyConstraintManager;
import org.anon.vendor.constraint.SybaseConstraintManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class SybaseExec extends BaseExec {

	@Override
	protected ForeignKeyConstraintManager getConstraintManager(final DataSource dataSource) {

		return new SybaseConstraintManager(dataSource);
	}


}

