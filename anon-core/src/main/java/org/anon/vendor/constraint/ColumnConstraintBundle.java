package org.anon.vendor.constraint;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.vendor.constraint.notnull.NotNullConstraint;
import org.anon.vendor.constraint.notnull.NotNullConstraintManager;
import org.anon.vendor.constraint.referential.ForeignKeyConstraint;
import org.anon.vendor.constraint.referential.ForeignKeyConstraintManager;
import org.anon.vendor.constraint.unique.UniqueConstraint;
import org.anon.vendor.constraint.unique.UniqueConstraintManager;

@SuppressWarnings({"unchecked","rawtypes"})
public class ColumnConstraintBundle {

	private DatabaseColumnInfo col;
	
	private NotNullConstraintManager notNullConstraintManager;
	private UniqueConstraintManager uniqueConstraintManager;
	private ForeignKeyConstraintManager foreignKeyConstraintManager;
	
	List<NamedConstraint> deactivatedUniqueConstraints;
	List<NamedConstraint> deactivatedReferentialConstraints;
	List<NotNullConstraint> notNullConstraints;
	
	DeactivationStrategy deactivationStrategy = new DefaultDeactivationStrategy();
	
	public ColumnConstraintBundle(DataSource dataSource, DatabaseColumnInfo col) {
		super();
		this.col = col;
		
		notNullConstraintManager = new NotNullConstraintManager(dataSource);
	}

	public List<Constraint> deactivate() {
		
		List<Constraint> res = new ArrayList<>();
		
		if(deactivationStrategy.deactivateReferential()){
			deactivatedReferentialConstraints = foreignKeyConstraintManager.deactivateConstraints(col);
			res.addAll(deactivatedReferentialConstraints);
		}
		if(deactivationStrategy.deactivateUniques()){
			deactivatedUniqueConstraints = uniqueConstraintManager.deactivateConstraints(col);
			res.addAll(deactivatedUniqueConstraints);
		}
		if(deactivationStrategy.deactivateNulls()){
			notNullConstraints= notNullConstraintManager.deactivateConstraints(col);
			res.addAll(notNullConstraints);
		}
		return res;
	}

	public void activate() {
		if(deactivationStrategy.deactivateNulls() && !notNullConstraints.isEmpty()){
			notNullConstraintManager.activateConstraints(col,notNullConstraints);
		}
		if(deactivationStrategy.deactivateUniques()){
			uniqueConstraintManager.activateConstraints(col, deactivatedUniqueConstraints);
		}
		if(deactivationStrategy.deactivateReferential()){
			foreignKeyConstraintManager.activateConstraints(col,deactivatedReferentialConstraints);
		}
	}

	public NotNullConstraint getNotNullConstraint(){
		return notNullConstraintManager.getNotNullConstraint(col);
	}
	
	public UniqueConstraint getPrimaryKey(){
		List<UniqueConstraint> primaryKeys = getUniqueConstraintManager().loadPrimaryKeys(col.getTable().getName(), col.getName(), col.getTable().getSchema());
		if(primaryKeys.isEmpty()){
			return null;
		}
		else {
			return primaryKeys.get(0);
		}
	}
	
	public List<UniqueConstraint> getUniqueConstraints(){
		return getUniqueConstraintManager().loadUniques(col.getTable().getName(), col.getName(), col.getTable().getSchema());
	}
	
	/**
	 * Outgoing FKs
	 */
	public List<ForeignKeyConstraint> getForeignKeyConstraintsFrom(){
		return getForeignKeyConstraintManager().loadForeignKeysFrom(col.getTable().getName(), col.getName(), col.getTable().getSchema());
	}
	
	/**
	 * Incoming FKs
	 */
	public List<ForeignKeyConstraint> getForeignKeyConstraintsTo(){
		return getForeignKeyConstraintManager().loadForeignKeysTo(col.getTable().getName(), col.getName(), col.getTable().getSchema());
	}
	

	

	public void setNotNullConstraintManager(NotNullConstraintManager notNullConstraintManager) {
		this.notNullConstraintManager = notNullConstraintManager;
	}

	public UniqueConstraintManager getUniqueConstraintManager() {
		return uniqueConstraintManager;
	}

	public void setUniqueConstraintManager(UniqueConstraintManager uniqueConstraintManager) {
		this.uniqueConstraintManager = uniqueConstraintManager;
	}

	public ForeignKeyConstraintManager getForeignKeyConstraintManager() {
		return foreignKeyConstraintManager;
	}

	public void setForeignKeyConstraintManager(ForeignKeyConstraintManager foreignKeyConstraintManager) {
		this.foreignKeyConstraintManager = foreignKeyConstraintManager;
	}
	

}
