package org.anon.exec;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.vendor.constraint.ConstraintBundle;
import org.anon.vendor.constraint.referential.SybaseForeignKeyConstraintManager;
import org.anon.vendor.constraint.unique.SybaseUniqueConstraintManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class SybaseExec extends BaseExec {

	@Override
	public ConstraintBundle createConstraintBundle(AnonymisedColumnInfo col, AnonymisationMethod anonymisationMethod) {
		ConstraintBundle constraintBundle = new ConstraintBundle(dataSource, col, anonymisationMethod);
		constraintBundle.setUniqueConstraintManager( new SybaseUniqueConstraintManager(dataSource));
		constraintBundle.setForeignKeyConstraintManager(new SybaseForeignKeyConstraintManager(dataSource));
		
		
		return constraintBundle;
	}


}

