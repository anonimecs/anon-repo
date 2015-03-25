package org.anon.exec;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.vendor.constraint.ConstraintBundle;
import org.anon.vendor.constraint.referential.OracleForeignKeyConstraintManager;
import org.anon.vendor.constraint.unique.OracleUniqueConstraintManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class OracleExec extends BaseExec{

	@Override
	public ConstraintBundle createConstraintBundle(AnonymisedColumnInfo col, AnonymisationMethod anonymisationMethod) {
		ConstraintBundle constraintBundle = new ConstraintBundle(dataSource, col, anonymisationMethod);
		constraintBundle.setUniqueConstraintManager( new OracleUniqueConstraintManager(dataSource));
		constraintBundle.setForeignKeyConstraintManager(new OracleForeignKeyConstraintManager(dataSource));
		
		
		return constraintBundle;
	}

}
