package org.anon.exec.reduction;

import java.util.ArrayList;
import java.util.List;

import org.anon.AbstractDbConnection;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.data.ReductionMethod;
import org.anon.data.ReductionMethodDefinition;
import org.anon.data.ReductionMethodReferencingTable;
import org.anon.data.ReductionType;
import org.anon.exec.AbstractExec;
import org.anon.vendor.constraint.Constraint;
import org.anon.vendor.constraint.referential.ForeignKeyConstraintManager;

public abstract class ReductionExec extends AbstractExec{

	protected List<? extends ReductionMethod> reductionMethods;
	
	@Override
	public void runAll() {
		
		tablesProcessed = 0; 
		
		try {
			execAuditor.insertExecution("Run All Reductions", userName, dbConnectionFactory.getDatabaseConfig());
			for (ReductionMethod reductionMethod : reductionMethods) {
				do_run(reductionMethod);
			}
			execAuditor.executionFinished();
		} catch (RuntimeException e){
			execAuditor.executionFailed(e.getMessage());
			throw e;
		}	
	}
	
	public void run(ReductionMethod reductionMethod) {
		try {
			execAuditor.insertExecution("Run One Reduction", userName, dbConnectionFactory.getDatabaseConfig());
			do_run(reductionMethod);
			execAuditor.executionFinished();
		} catch (RuntimeException e){
			execAuditor.executionFailed(e.getMessage());
			throw e;
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void do_run(ReductionMethod reductionMethod) {
		licenseManager.checkLicenseExpired();
		assertDataSourceSet();
		
		try{
			List<Constraint> allDeactivatedConstraints = new ArrayList<>();
			
			ForeignKeyConstraintManager foreignKeyConstraintManager = constraintBundleFactory.createForeignKeyConstraintManager(getDatabaseSpecifics() , dataSource);
			List<Constraint> deactivatedConstraints = foreignKeyConstraintManager.deactivateConstraints(reductionMethod.getTableName(), getColumns(reductionMethod.getTableName(), reductionMethod.getSchemaName()), reductionMethod.getSchemaName());
			allDeactivatedConstraints.addAll(deactivatedConstraints);
			
			executeReduction(reductionMethod);
			
			// run the reduction on all referencing tables
			for (ReductionMethodReferencingTable referencingTable: reductionMethod.getReferencingTableDatas()) {
				assertFreeEditionRunCount();
				
				deactivatedConstraints = foreignKeyConstraintManager.deactivateConstraints(referencingTable.getTableName(), getColumns(referencingTable.getTableName(), referencingTable.getSchemaName()), referencingTable.getSchemaName());
				
				
				if(reductionMethod.getReductionType() != ReductionType.DEREFERENCE){
					allDeactivatedConstraints.addAll(deactivatedConstraints);
					executeReduction(referencingTable);
				}

					
			}
			
			foreignKeyConstraintManager.activateConstraints(allDeactivatedConstraints);
		}
		catch(RuntimeException e){		
			logger.debug("ReductionMethod failed : " + reductionMethod, e);
			throw e;				
		}
	}

	protected void executeReduction(ReductionMethodDefinition reductionMethod) {
		
		switch (reductionMethod.getReductionType()) {
		case TRUNCATE:
			String sql = createTruncateSql(reductionMethod);
			execute(sql);
			break;

		case DELETE_ALL:
			sql = createDeleteAllSql(reductionMethod);
			execute(sql);
			break;

		case DELETE_WHERE:
			sql = createDeleteSql(reductionMethod);
			execute(sql);
			break;

		case DEREFERENCE:
			// it is sufficient to remove the Referencial Integrity when dereferencing was chosen
			// this was already been done before
			break;
			
		default:
			throw new RuntimeException("Usupported or unimplemented " + reductionMethod);
		}
		
	}

	protected String createDeleteSql(ReductionMethodDefinition reductionMethod) {
		return createDeleteAllSql(reductionMethod) + " where " + reductionMethod.getWhereCondition();
	}

	protected String createDeleteAllSql(ReductionMethodDefinition reductionMethod) {
		return "delete from "+reductionMethod.getSchemaName()+"."+reductionMethod.getTableName();
	}

	protected String createTruncateSql(ReductionMethodDefinition reductionMethod) {
		return "truncate table "+reductionMethod.getSchemaName()+"."+reductionMethod.getTableName();
	}

	protected List<DatabaseColumnInfo> getColumns(String tableName, String tableSchema) {
		AbstractDbConnection abstractDbConnection = createDbConnection(tableSchema);
		abstractDbConnection.setDataSource(dataSource);
		return abstractDbConnection.getColumns(new DatabaseTableInfo(tableName, tableSchema));
	}


	public void setReductionMethods(List<? extends ReductionMethod> reductionMethods) {
		this.reductionMethods = reductionMethods;
	}

	protected abstract AbstractDbConnection createDbConnection(String tableSchema);

}
