package org.anon;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.data.RelatedTableColumnInfo;
import org.anon.vendor.DatabaseSpecifics;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractDbConnection {
	protected Logger logger = Logger.getLogger(getClass());

	protected JdbcTemplate jdbcTemplate;
	protected DataSource dataSource;
	protected Properties properties;
	protected String defaultSchema;
	
	
    public AbstractDbConnection(String defaultSchema) {
		super();
		this.defaultSchema = defaultSchema;
	}

	public void setDataSource(DataSource dataSource) {
    	this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setProperties(Properties properties) {
		this.properties = properties;
	}
    
    public Properties getProperties() {
		return properties;
	}
    

    public DataSource getDataSource() {
    	return dataSource;
    }
	

	public int getTableCount(String selectedSchema) {
		return getTableNamesAndRowcounts(selectedSchema).size();
	}
	

	public List<DatabaseTableInfo> getTableList(String selectedSchema) {
		List<DatabaseTableInfo> tableList = new LinkedList<DatabaseTableInfo>(); 
		for(DatabaseTableInfo  databaseTableInfo : getTableNamesAndRowcounts(selectedSchema)){
			databaseTableInfo.setSchema(selectedSchema);
			tableList.add(databaseTableInfo);
			databaseTableInfo.addColumns(getColumns(databaseTableInfo));			
		}
		return tableList;
	}

	public String getDefaultSchema() {
		return defaultSchema;
	}

	
	abstract public List<DatabaseTableInfo> getTableNamesAndRowcounts(String selectedSchema);

	abstract public List<DatabaseColumnInfo> getColumns(DatabaseTableInfo  databaseTableInfo);

	abstract public void fillExampleValues(DatabaseTableInfo editedTable);

	abstract public List<RelatedTableColumnInfo> findRelatedTables(DatabaseTableInfo editedTable, DatabaseColumnInfo editedColumn);

	abstract public DatabaseSpecifics getDatabaseSpecifics();

	abstract public List<String> getSchemas();


}
