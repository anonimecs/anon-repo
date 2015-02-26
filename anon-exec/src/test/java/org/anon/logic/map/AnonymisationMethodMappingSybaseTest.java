package org.anon.logic.map;

import org.anon.data.AnonConfig;
import org.anon.exec.SybaseExec;
import org.anon.exec.SybaseExecTestBase;
import org.anon.logic.AnonymisationMethodMapping;
import org.anon.vendor.DatabaseSpecifics;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class AnonymisationMethodMappingSybaseTest extends SybaseExecTestBase{

	@Test
	public void testMapIntegersSybase() {

		AnonymisationMethodMapping anonymisationMethod = new AnonymisationMethodMapping();
		anonymisationMethod.setMappingDefault(new MappingDefault("xxxx"));
		
		anonymisationMethod.addMappingRule(new LessThan("K", "aaa"));
		anonymisationMethod.addMappingRule(new LessThan("O", "lll"));
		
		
		SybaseExec sybaseExec = createSybaseExec();

		AnonConfig anonConfig = new AnonConfig();
		anonConfig.init();
		
		getTestAnonimisedColumnInfo("SSY_SourceSystem", "VARCHAR2", "TMP_TABLE_A", anonymisationMethod, DatabaseSpecifics.SybaseSpecific, anonConfig);
		
		anonConfig.addAnonMethod(anonymisationMethod);
		
		
		sybaseExec.setExecConfig(anonConfig);
		sybaseExec.runAll();
		System.out.println(
				new JdbcTemplate(dataSourceSybase).queryForList("select distinct(SSY_SourceSystem) from TMP_TABLE_A")
				);		
	}

}
