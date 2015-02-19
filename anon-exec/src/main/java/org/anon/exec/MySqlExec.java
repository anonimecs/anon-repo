package org.anon.exec;

import javax.sql.DataSource;

import org.anon.vendor.constraint.ConstraintManager;
import org.anon.vendor.constraint.MySqlConstraintManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class MySqlExec extends BaseExec{
	@Override
	protected ConstraintManager getConstraintManager(DataSource dataSource) {
		return new MySqlConstraintManager(dataSource);
	}

}
