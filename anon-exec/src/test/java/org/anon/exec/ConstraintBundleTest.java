package org.anon.exec;

import java.util.List;

import org.anon.data.AnonConfig;
import org.anon.data.AnonymisedColumnInfo;
import org.anon.logic.AnonymisationMethodDestoryMySql;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.constraint.ColumnConstraintBundle;
import org.anon.vendor.constraint.ConstraintBundleFactory;
import org.anon.vendor.constraint.notnull.NotNullConstraint;
import org.anon.vendor.constraint.referential.ForeignKeyConstraint;
import org.anon.vendor.constraint.unique.UniqueConstraint;
import org.junit.Assert;
import org.junit.Test;

public class ConstraintBundleTest extends MySqlExecTestBase{

	
	@Test
	public void testCreateMysqlExec() {
		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		
		AnonymisationMethodDestoryMySql anonymisationMethod = new AnonymisationMethodDestoryMySql();
		AnonymisedColumnInfo comsiIdCol =  getTestAnonimisedColumnInfo("COMSIID", "VARCHAR2", "TMP_TABLE_B", anonymisationMethod, DatabaseSpecifics.MySqlSpecific, anonConfig);
		AnonymisedColumnInfo roleIdCol =  getTestAnonimisedColumnInfo("ROLE_ID", "NUMERIC", "TMP_TABLE_B", anonymisationMethod, DatabaseSpecifics.MySqlSpecific, anonConfig);

		ConstraintBundleFactory constraintBundleFactory = new ConstraintBundleFactory();
		ColumnConstraintBundle comsiIdColConstraintBundle = constraintBundleFactory.createConstraintBundle(DatabaseSpecifics.MySqlSpecific, comsiIdCol, getDataSource());

		ColumnConstraintBundle roleIdColConstraintBundle = constraintBundleFactory.createConstraintBundle(DatabaseSpecifics.MySqlSpecific, roleIdCol, getDataSource());
		
		
		// show in the GUI
		NotNullConstraint notNullConstraint = comsiIdColConstraintBundle.getNotNullConstraint();
		Assert.assertNotNull(notNullConstraint);
		
		UniqueConstraint primaryKey = comsiIdColConstraintBundle.getPrimaryKey();
		Assert.assertNull(primaryKey);
		
		List<UniqueConstraint> uniqueConstraints = comsiIdColConstraintBundle.getUniqueConstraints();
		Assert.assertTrue(uniqueConstraints.isEmpty());

		List<ForeignKeyConstraint> outgoingFKs = roleIdColConstraintBundle.getForeignKeyConstraintsFrom();
		Assert.assertEquals(1, outgoingFKs.size());
		
		List<ForeignKeyConstraint> incomingFKs = roleIdColConstraintBundle.getForeignKeyConstraintsTo();
		Assert.assertTrue(incomingFKs.isEmpty());
		
	}

}
