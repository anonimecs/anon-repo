package org.anon.vendor.constraint;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.logic.AnonymisationMethod;
import org.anon.vendor.constraint.notnull.NotNullConstraint;
import org.anon.vendor.constraint.notnull.NotNullConstraintManager;
import org.anon.vendor.constraint.referential.ForeignKeyConstraintManager;
import org.anon.vendor.constraint.unique.UniqueConstraintManager;

@SuppressWarnings({"unchecked","rawtypes"})
public class ConstraintBundle {

	private DataSource dataSource;
	private AnonymisedColumnInfo col;
	private AnonymisationMethod anonymisationMethod;
	
	private NotNullConstraintManager notNullConstraintManager;
	private UniqueConstraintManager uniqueConstraintManager;
	private ForeignKeyConstraintManager foreignKeyConstraintManager;
	
	List<NamedConstraint> deactivatedUniqueConstraints;
	List<NamedConstraint> deactivatedReferentialConstraints;
	List<NotNullConstraint> notNullConstraints;
	
	DeactivationStrategy deactivationStrategy = new DefaultDeactivationStrategy();
	
	public ConstraintBundle(DataSource dataSource, AnonymisedColumnInfo col,AnonymisationMethod anonymisationMethod) {
		super();
		this.dataSource = dataSource;
		this.col = col;
		this.anonymisationMethod = anonymisationMethod;
		
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

	public NotNullConstraintManager getNotNullConstraintManager() {
		return notNullConstraintManager;
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
