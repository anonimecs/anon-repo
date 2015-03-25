package org.anon.exec;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.vendor.constraint.ConstraintBundle;
import org.anon.vendor.constraint.referential.MySqlForeignKeyConstraintManager;
import org.anon.vendor.constraint.unique.MySqlUniqueConstraintManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class MySqlExec extends BaseExec{

	@Override
	public ConstraintBundle createConstraintBundle(AnonymisedColumnInfo col, AnonymisationMethod anonymisationMethod) {
		ConstraintBundle constraintBundle = new ConstraintBundle(dataSource, col, anonymisationMethod);
		constraintBundle.setUniqueConstraintManager( new MySqlUniqueConstraintManager(dataSource));
		constraintBundle.setForeignKeyConstraintManager(new MySqlForeignKeyConstraintManager(dataSource));
		
		
		return constraintBundle;
	}

}
