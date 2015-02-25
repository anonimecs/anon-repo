package org.anon;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.anon.data.AnonymizationType;
import org.anon.logic.AnonymisationMethod;
import org.anon.logic.map.MappingRule.MappingRuleType;
import org.anon.persistence.data.AnonymisationMethodData;
import org.anon.persistence.data.AnonymisationMethodMappingData;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.MappingDefaultData;
import org.anon.persistence.data.MappingRuleData;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AnonMethodMappingDataTest extends DbEntitiesTest {

		static MappingDefaultData mappingDefaultData;
		static MappingRuleData mappingRuleData;
		static AnonymisationMethodMappingData anonymisationMethodMappingData;
	
		@Test
		public void test0_0_dbSetup() throws IOException {
			recreateInMemDb();
		}

		
		@Test
		public void test1InsertAnonymisationMethodData() {
			do_test1InsertAnonymisationMethodData();
			
		}

		@Test
		public void test2InsertAnonymisationMethodMappedData() {
//			databaseConfig =  createDatabaseConfig();
//			databaseConfigDao.addDatabaseConfig(databaseConfig);

			AnonymisationMethodMappingData anonymisationMethodMappingData = new AnonymisationMethodMappingData();
			anonymisationMethodMappingData.setDatabaseConfigId(databaseConfig.getId());
			anonymisationMethodMappingData.setAnonymizationType(AnonymizationType.MAP);
			anonymisationMethodMappingData.setAnonMethodClass(AnonymisationMethod.class.getName());
			
			mappingDefaultData = new MappingDefaultData();
			mappingDefaultData.setDefaultValue("testvalue");
			anonymisationMethodMappingData.setMappingDefaultData(mappingDefaultData);
			
			mappingRuleData = new MappingRuleData();
			mappingRuleData.setBoundary("AAA");
			mappingRuleData.setMappedValue("BBB");
			mappingRuleData.setMappingRuleType(MappingRuleType.LessThan);
			anonymisationMethodMappingData.addMappingRuleData(mappingRuleData);
			
			anonymisedColumnData = new AnonymisedColumnData();
			anonymisedColumnData.setColumnName("TESTCOLNAME2");
			anonymisedColumnData.setTableName("TestTableName2");
			anonymisedColumnData.setSchemaName("TestSchema2");
			anonymisedColumnData.setColumnType("VARCHAR");
			
			anonymisationMethodMappingData.addColumn(anonymisedColumnData);
			entitiesDao.save(anonymisationMethodMappingData);
			methodId = anonymisationMethodMappingData.getId();
			
			Assert.assertNotNull("id was assigned", methodId);
			Assert.assertNotNull("id was assigned", anonymisedColumnData.getId());
			
			
		}
		
		@Test
		public void test3LoadAnonymisationMethodMappedData() {
			List<AnonymisationMethodData> list = entitiesDao.loadAllAnonMethods(databaseConfig);
			Assert.assertEquals("two items loaded",2, list.size());
			for (AnonymisationMethodData anonymisationMethodData : list) {
				if(anonymisationMethodData instanceof AnonymisationMethodMappingData){
					anonymisationMethodMappingData = (AnonymisationMethodMappingData)anonymisationMethodData;
					Assert.assertEquals(1, anonymisationMethodMappingData.getMappingRules().size());
					Assert.assertEquals(mappingRuleData.getMappingRuleType(), anonymisationMethodMappingData.getMappingRules().iterator().next().getMappingRuleType());
					Assert.assertEquals(mappingRuleData.getBoundary(), anonymisationMethodMappingData.getMappingRules().iterator().next().getBoundary());
					Assert.assertEquals(mappingDefaultData.getDefaultValue(), anonymisationMethodMappingData.getMappingDefaultData().getDefaultValue());
					return;
				}
			}
			Assert.fail("One of the loaded data must have been the AnonymisationMethodMappedData");
		}

		@Test
		public void test4RemoveAnonymisationMethodMappedData() {
			assertEquals(1, entitiesDao.removeAnonymisationMethodData(anonymisationMethodMappingData.getId()));
			List<AnonymisationMethodData> list = entitiesDao.loadAllAnonMethods(databaseConfig);
			Assert.assertEquals("one item loaded",1, list.size());
		}

		

}

