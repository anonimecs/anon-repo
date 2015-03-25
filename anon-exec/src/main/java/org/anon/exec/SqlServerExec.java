package org.anon.exec;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.vendor.constraint.ConstraintBundle;
import org.anon.vendor.constraint.referential.SqlServerForeignKeyConstraintManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class SqlServerExec extends BaseExec {

	@Override
	public ConstraintBundle createConstraintBundle(AnonymisedColumnInfo col, AnonymisationMethod anonymisationMethod) {
		ConstraintBundle constraintBundle = new ConstraintBundle(dataSource, col, anonymisationMethod);
		if (true)throw new RuntimeException("Unimpelmented");
		//constraintBundle.setUniqueConstraintManager( new SqlServerUniqueConstraintManager(dataSource));
		constraintBundle.setForeignKeyConstraintManager(new SqlServerForeignKeyConstraintManager(dataSource));
		
		
		return constraintBundle;
	}

}

