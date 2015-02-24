package org.anon.logic.map;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.ExecutionMessage;

public class AnonymisationMethodMappingMySql extends AnonymisationMethodMapping {

	
	@Override
	public ExecutionMessage runOnColumn(AnonymisedColumnInfo col) {
		if(col.isJavaTypeString()){
			SqlCreator sqlCreator = new SqlCreator(col);
			int updatedRows = update(sqlCreator.compileSql());
			if (updatedRows != col.getTable().getRowCount()){
				logger.warn("Row count discrepancy. Updated: " + updatedRows + " table rowcount:" + col.getTable().getRowCount() );
			}
			return new ExecutionMessage("Updated String records", updatedRows );
		}
		
		throw new RuntimeException();
		
		
	}
	
	public class SqlCreator{
		private AnonymisedColumnInfo col;
		public SqlCreator(AnonymisedColumnInfo col) {
			super();
			this.col = col;
		}
		
		public String compileSql() {
			StringBuffer SQL = new StringBuffer();
			SQL.append("update " + col.getTable().getName() );
			SQL.append(" set " + col.getName() + "=");
			SQL.append(" case");
			for(int i = 0; i < mappingRulesList.size(); i++){
				MappingRule mappingRule = mappingRulesList.get(i);
				SQL.append(" " + mappingRule.generateWhenSql(col));
				
			}
			SQL.append(" else '" + mappingDefault.getDefaultValue() + "'");
			SQL.append(" end case");		
			
			return SQL.toString();
		}
		
		
		
	}

}
