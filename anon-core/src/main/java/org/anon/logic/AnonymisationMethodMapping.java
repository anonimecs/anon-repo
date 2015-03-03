package org.anon.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.anon.data.ExecutionMessage;
import org.anon.logic.map.MappingDefault;
import org.anon.logic.map.MappingRule;
import org.springframework.jdbc.core.JdbcTemplate;

public class AnonymisationMethodMapping extends AnonymisationMethod {

	/**
	 * If no rules apply, this default will be used
	 */
	protected MappingDefault mappingDefault;
	protected List<MappingRule> mappingRulesList = new ArrayList<>();

	public AnonymisationMethodMapping() {
		super(AnonymizationType.MAP);
	}

	@Override
	public boolean supports(AnonymisedColumnInfo anonymizedColumn) {
		if(anonymizedColumn.isJavaTypeDate()){
			return false;
		}
		
		return true;

	}

	public void setMappingDefault(MappingDefault mappingDefault) {
		this.mappingDefault = mappingDefault;
	}

	public MappingDefault getMappingDefault() {
		return mappingDefault;
	}

	public void addMappingRule(MappingRule mappingRule) {
		mappingRulesList.add(mappingRule);
	}

	public void deleteMappingRule(MappingRule mappingRule) {
		mappingRulesList.remove(mappingRule);
	}

	/**
	 * Moves this rule one up in the list
	 */
	public void moveUp(MappingRule mappingRule) {
		int index = mappingRulesList.indexOf(mappingRule);
		if (index > 0) {
			Collections.swap(mappingRulesList, index, index - 1);
		} else {
			logger.warn("Can not move up rule on the top");
		}
	}

	/**
	 * Moves this rule one down in the list
	 */
	public void moveDown(MappingRule mappingRule) {
		int index = mappingRulesList.indexOf(mappingRule);
		if (index < mappingRulesList.size() - 1) {
			Collections.swap(mappingRulesList, index, index + 1);
		} else {
			logger.warn("Can not move down rule on the bottom");
		}
	}

	public List<MappingRule> getMappingRulesList() {
		return mappingRulesList;
	}

	@Override
	public ExecutionMessage runOnColumn(AnonymisedColumnInfo col) {
		BasicSqlCreator sqlCreator = createSqlCreator(col);
		int updatedRows = update(sqlCreator.compileSql());
		if (updatedRows != col.getTable().getRowCount()) {
			logger.warn("Row count discrepancy. Updated: " + updatedRows + " table rowcount:"
					+ col.getTable().getRowCount());
		}
		return new ExecutionMessage("Updated  records", updatedRows);

	}

	protected BasicSqlCreator createSqlCreator(AnonymisedColumnInfo col) {
		return new BasicSqlCreator(col);
	}

	public class BasicSqlCreator {
		private AnonymisedColumnInfo col;

		public BasicSqlCreator(AnonymisedColumnInfo col) {
			super();
			this.col = col;
		}

		public String compileSql() {
			StringBuffer SQL = new StringBuffer();
			SQL.append("update " + col.getTable().getName());
			SQL.append(" set " + col.getName() + "=");
			SQL.append(" case");
			for (int i = 0; i < mappingRulesList.size(); i++) {
				MappingRule mappingRule = mappingRulesList.get(i);
				SQL.append(" " + mappingRule.generateWhenSql(col));

			}
			SQL.append(" else " + mappingDefault.getDefaultValueSql(col));
			SQL.append(" end ");

			return SQL.toString();
		}

		public String compilePreviewSql(Object value) {
			StringBuffer SQL = new StringBuffer();
			SQL.append("select  ");
			if(mappingRulesList.size() > 0){
				SQL.append(" case");
				for (int i = 0; i < mappingRulesList.size(); i++) {
					MappingRule mappingRule = mappingRulesList.get(i);
					SQL.append(" " + mappingRule.generatePreviewWhenSql(value, col));
	
				}
				SQL.append(" else " + mappingDefault.getDefaultValueSql(col));
				SQL.append(" end ");
			}
			else {
				SQL.append(mappingDefault.getDefaultValueSql(col));
			}
			SQL.append(col.getDatabaseSpecifics().getFromDual());

			return SQL.toString();
		}

	}

	@Override
	public Object anonymise(Object exampleValue, AnonymisedColumnInfo anonymizedColumn) {
//		if(!mappingRulesList.isEmpty() && mappingDefault != null){
			BasicSqlCreator sqlCreator = createSqlCreator(anonymizedColumn);
			String sql = sqlCreator.compilePreviewSql(exampleValue);
			return new JdbcTemplate(dataSource).queryForObject(sql, exampleValue.getClass());
//		}
//		else {
//			return exampleValue;
//		}
	}

}
