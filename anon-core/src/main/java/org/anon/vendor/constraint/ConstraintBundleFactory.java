package org.anon.vendor.constraint;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.constraint.referential.MySqlForeignKeyConstraintManager;
import org.anon.vendor.constraint.referential.OracleForeignKeyConstraintManager;
import org.anon.vendor.constraint.referential.SqlServerForeignKeyConstraintManager;
import org.anon.vendor.constraint.referential.SybaseForeignKeyConstraintManager;
import org.anon.vendor.constraint.unique.MySqlUniqueConstraintManager;
import org.anon.vendor.constraint.unique.OracleUniqueConstraintManager;
import org.anon.vendor.constraint.unique.SybaseUniqueConstraintManager;
import org.springframework.stereotype.Service;

@Service
public class ConstraintBundleFactory {
	
	
	public ColumnConstraintBundle createConstraintBundle(DatabaseSpecifics databaseSpecifics, DatabaseColumnInfo col, AnonymisationMethod anonymisationMethod, DataSource dataSource){
		
		if(databaseSpecifics == DatabaseSpecifics.SybaseSpecific){
			ColumnConstraintBundle constraintBundle = new ColumnConstraintBundle(dataSource, col, anonymisationMethod);
			constraintBundle.setUniqueConstraintManager( new SybaseUniqueConstraintManager(dataSource));
			constraintBundle.setForeignKeyConstraintManager(new SybaseForeignKeyConstraintManager(dataSource));
			
			
			return constraintBundle;
		}
		else if(databaseSpecifics == DatabaseSpecifics.OracleSpecific){
			ColumnConstraintBundle constraintBundle = new ColumnConstraintBundle(dataSource, col, anonymisationMethod);
			constraintBundle.setUniqueConstraintManager( new OracleUniqueConstraintManager(dataSource));
			constraintBundle.setForeignKeyConstraintManager(new OracleForeignKeyConstraintManager(dataSource));
			
			
			return constraintBundle;
		} 
		else if(databaseSpecifics == DatabaseSpecifics.MySqlSpecific) {
			ColumnConstraintBundle constraintBundle = new ColumnConstraintBundle(dataSource, col, anonymisationMethod);
			constraintBundle.setUniqueConstraintManager( new MySqlUniqueConstraintManager(dataSource));
			constraintBundle.setForeignKeyConstraintManager(new MySqlForeignKeyConstraintManager(dataSource));
			
			
			return constraintBundle;
		}
		else if(databaseSpecifics == DatabaseSpecifics.SqlServerSpecific) {
			ColumnConstraintBundle constraintBundle = new ColumnConstraintBundle(dataSource, col, anonymisationMethod);
			if (true)throw new RuntimeException("Unimpelmented");
			//constraintBundle.setUniqueConstraintManager( new SqlServerUniqueConstraintManager(dataSource));
			constraintBundle.setForeignKeyConstraintManager(new SqlServerForeignKeyConstraintManager(dataSource));
			
			
			return constraintBundle;
		}

		throw new RuntimeException("Unspecified db " + databaseSpecifics);
	}

}
