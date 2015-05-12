package org.anon.exec.reduction;

import org.anon.AbstractDbConnection;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.SqlServerDbConnection;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class SqlServerReductionExec extends ReductionExec {

	@Override
	protected DatabaseSpecifics getDatabaseSpecifics() {
		return DatabaseSpecifics.SqlServerSpecific;
	}
	@Override
	protected AbstractDbConnection createDbConnection(String tableSchema) {
		return new SqlServerDbConnection(tableSchema);
	}

}

