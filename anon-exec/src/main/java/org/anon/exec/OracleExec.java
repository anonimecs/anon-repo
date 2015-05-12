package org.anon.exec;

import org.anon.vendor.DatabaseSpecifics;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class OracleExec extends AnonExec{

	@Override
	protected DatabaseSpecifics getDatabaseSpecifics() {
		return DatabaseSpecifics.OracleSpecific;
	}

}
