package org.anon.vendor.constraint;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.constraint.referential.ForeignKeyConstraintManager;
import org.anon.vendor.constraint.referential.MySqlForeignKeyConstraintManager;
import org.anon.vendor.constraint.referential.OracleForeignKeyConstraintManager;
import org.anon.vendor.constraint.referential.SqlServerForeignKeyConstraintManager;
import org.anon.vendor.constraint.referential.SybaseForeignKeyConstraintManager;
import org.anon.vendor.constraint.unique.MySqlUniqueConstraintManager;
import org.anon.vendor.constraint.unique.OracleUniqueConstraintManager;
import org.anon.vendor.constraint.unique.SqlServerUniqueConstraintManager;
import org.anon.vendor.constraint.unique.SybaseUniqueConstraintManager;
import org.springframework.stereotype.Service;

@Service
public class ConstraintBundleFactory {
	
	
	public ColumnConstraintBundle createConstraintBundle(DatabaseSpecifics databaseSpecifics, DatabaseColumnInfo col, DataSource dataSource){
		
		if(databaseSpecifics == DatabaseSpecifics.SybaseSpecific){
			ColumnConstraintBundle constraintBundle = new ColumnConstraintBundle(dataSource, col);
			constraintBundle.setUniqueConstraintManager( new SybaseUniqueConstraintManager(dataSource));
			constraintBundle.setForeignKeyConstraintManager(new SybaseForeignKeyConstraintManager(dataSource));
			
			
			return constraintBundle;
		}
		else if(databaseSpecifics == DatabaseSpecifics.OracleSpecific){
			ColumnConstraintBundle constraintBundle = new ColumnConstraintBundle(dataSource, col);
			constraintBundle.setUniqueConstraintManager( new OracleUniqueConstraintManager(dataSource));
			constraintBundle.setForeignKeyConstraintManager(new OracleForeignKeyConstraintManager(dataSource));
			
			
			return constraintBundle;
		} 
		else if(databaseSpecifics == DatabaseSpecifics.MySqlSpecific) {
			ColumnConstraintBundle constraintBundle = new ColumnConstraintBundle(dataSource, col);
			constraintBundle.setUniqueConstraintManager( new MySqlUniqueConstraintManager(dataSource));
			constraintBundle.setForeignKeyConstraintManager(new MySqlForeignKeyConstraintManager(dataSource));
			
			
			return constraintBundle;
		}
		else if(databaseSpecifics == DatabaseSpecifics.SqlServerSpecific) {
			ColumnConstraintBundle constraintBundle = new ColumnConstraintBundle(dataSource, col);
			constraintBundle.setUniqueConstraintManager( new SqlServerUniqueConstraintManager(dataSource));
			constraintBundle.setForeignKeyConstraintManager(new SqlServerForeignKeyConstraintManager(dataSource));
			
			
			return constraintBundle;
		}

		throw new RuntimeException("Unspecified db " + databaseSpecifics);
	}
	
	public ForeignKeyConstraintManager createForeignKeyConstraintManager(DatabaseSpecifics databaseSpecifics, DataSource dataSource){
		
		if(databaseSpecifics == DatabaseSpecifics.SybaseSpecific){
			return new SybaseForeignKeyConstraintManager(dataSource);
		}
		else if(databaseSpecifics == DatabaseSpecifics.OracleSpecific){
			return new OracleForeignKeyConstraintManager(dataSource);
		} 
		else if(databaseSpecifics == DatabaseSpecifics.MySqlSpecific) {
			return new MySqlForeignKeyConstraintManager(dataSource);
		}
		else if(databaseSpecifics == DatabaseSpecifics.SqlServerSpecific) {
			return new SqlServerForeignKeyConstraintManager(dataSource);
		}		

		throw new RuntimeException("Unspecified db " + databaseSpecifics);
	}

}
