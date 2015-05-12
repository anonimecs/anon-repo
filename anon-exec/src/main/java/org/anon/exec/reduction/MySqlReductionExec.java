package org.anon.exec.reduction;

import org.anon.AbstractDbConnection;
import org.anon.vendor.DatabaseSpecifics;
import org.anon.vendor.MySqlDbConnection;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class MySqlReductionExec extends ReductionExec{

	@Override
	protected DatabaseSpecifics getDatabaseSpecifics() {
		return DatabaseSpecifics.MySqlSpecific;
	}


	@Override
	protected AbstractDbConnection createDbConnection(String tableSchema) {
		return new MySqlDbConnection(tableSchema);
	}

}
