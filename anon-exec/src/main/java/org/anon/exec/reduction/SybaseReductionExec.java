package org.anon.exec.reduction;

import org.anon.AbstractDbConnection;
import org.anon.data.ReductionMethodDefinition;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.SybaseDbConnection;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class SybaseReductionExec extends ReductionExec {

	@Override
	protected DatabaseSpecifics getDatabaseSpecifics() {
		return DatabaseSpecifics.SybaseSpecific;
	}

	@Override
	protected AbstractDbConnection createDbConnection(String tableSchema) {
		return new SybaseDbConnection(tableSchema);
	}


	@Override
	protected String createDeleteSql(ReductionMethodDefinition reductionMethod) {
		return createDeleteAllSql(reductionMethod) + " where " + reductionMethod.getWhereCondition();
	}

	@Override
	protected String createDeleteAllSql(ReductionMethodDefinition reductionMethod) {
		return "delete from "+reductionMethod.getSchemaName()+".."+reductionMethod.getTableName();
	}

	@Override
	protected String createTruncateSql(ReductionMethodDefinition reductionMethod) {
		return "truncate table "+reductionMethod.getSchemaName()+".."+reductionMethod.getTableName();
	}

	
}

