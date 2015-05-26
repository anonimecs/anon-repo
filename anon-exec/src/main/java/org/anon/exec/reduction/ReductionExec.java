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
import org.anon.persistence.data.audit.ReductionExecutionData;
import org.anon.vendor.constraint.Constraint;
import org.anon.vendor.constraint.referential.ForeignKeyConstraintManager;

public abstract class ReductionExec extends AbstractExec{

	protected List<? extends ReductionMethod> reductionMethods;
	
	@Override
	public void runAll() {
		
		tablesProcessed = 0; 
		
		try {
			if(executionData == null){
				createExecution("Run All Reductions");
			}
			for (ReductionMethod reductionMethod : reductionMethods) {
				do_run(reductionMethod);
			}
			execAuditor.executionFinished(executionData);
		} catch (RuntimeException e){
			execAuditor.executionFailed(executionData, e.getMessage());
			throw e;
		}	
	}

	
	public void run(ReductionMethod reductionMethod) {
		try {
			if(executionData == null){
				createExecution("Run One Reduction " + reductionMethod);
			}

			do_run(reductionMethod);
			execAuditor.executionFinished(executionData);
		} catch (RuntimeException e){
			execAuditor.executionFailed(executionData, e.getMessage());
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
			
			int rowCount = executeReduction(reductionMethod);
			ReductionExecutionData reductionExecutionData = execAuditor.auditReduction(executionData, reductionMethod, rowCount);
			guiNotifier.refreshExecGui(null);
			
			// run the reduction on all referencing tables
			for (ReductionMethodReferencingTable referencingTable: reductionMethod.getReferencingTableDatas()) {
				assertFreeEditionReduceTables();
				
				deactivatedConstraints = foreignKeyConstraintManager.deactivateConstraints(referencingTable.getTableName(), getColumns(referencingTable.getTableName(), referencingTable.getSchemaName()), referencingTable.getSchemaName());
				
				
				if(reductionMethod.getReductionType() != ReductionType.DEREFERENCE){
					allDeactivatedConstraints.addAll(deactivatedConstraints);
					rowCount = executeReduction(referencingTable);
					execAuditor.auditRefTableReduction(reductionExecutionData, referencingTable, rowCount);
					guiNotifier.refreshExecGui(null);

				}

					
			}
			
			foreignKeyConstraintManager.activateConstraints(allDeactivatedConstraints);
		}
		catch(RuntimeException e){		
			logger.debug("ReductionMethod failed : " + reductionMethod, e);
			throw e;				
		}
	}

	protected int executeReduction(ReductionMethodDefinition reductionMethod) {
		
		switch (reductionMethod.getReductionType()) {
		case TRUNCATE:
			String sql = createTruncateSql(reductionMethod);
			return update(sql);

		case DELETE_ALL:
			sql = createDeleteAllSql(reductionMethod);
			return update(sql);

		case DELETE_WHERE:
			sql = createDeleteSql(reductionMethod);
			return update(sql);

		case DEREFERENCE:
			// it is sufficient to remove the Referencial Integrity when dereferencing was chosen
			// this was already been done before
			return 0;
			
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
