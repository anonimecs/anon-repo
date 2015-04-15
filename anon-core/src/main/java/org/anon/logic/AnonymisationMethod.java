package org.anon.logic;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.data.AnonymizationType;
import org.anon.data.ExecutionMessage;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.FileCopyUtils;

public abstract class AnonymisationMethod implements Serializable{

	protected transient Logger logger = Logger.getLogger(getClass());
	
	protected Long id;
	//protected static AtomicLong idCounter = new AtomicLong(); // turn this into a DB ID
	
	protected AnonymizationType type;
	protected List<AnonymisedColumnInfo> applyedToColumns = new LinkedList<>();
	
	protected transient DataSource dataSource;
	protected transient String schema;	
	
	protected List<String> setupSqls = new LinkedList<>();
	protected List<String> cleanupSqls = new LinkedList<>();

	protected String password = "default";
	protected int hashmodint;
	
	
	protected AnonymisationMethod(AnonymizationType type) {
		super();
		this.type = type;
		calcHashMod();
		//id=idCounter.incrementAndGet();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}
		
        if (obj == null || !(obj instanceof AnonymisationMethod)) {
        	return false;
        }
        
        AnonymisationMethod other = (AnonymisationMethod) obj;

        return new EqualsBuilder().append(this.id, other.id).isEquals();
	}
	
	@Override
	public int hashCode() {
		if(id == null){
			return super.hashCode();
		}
		else {
			return id.hashCode();
		}
	}

	protected void calcHashMod() {
		hashmodint = Math.abs(password.hashCode()) % 30;
		
	}
	
	public void setPassword(String password) {
		if(password == null){
			return;
		}
		this.password = password;
		calcHashMod();
	}
	
	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return type + " Columns: " + applyedToColumns;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public AnonymizationType getType() {
		return type;
	}

	protected String getFileContent(String fileNameInResourcesFolder)  {
		try {
			return FileCopyUtils.copyToString(new InputStreamReader(getClass().getResourceAsStream("/"+fileNameInResourcesFolder)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	
	public void addColumn(AnonymisedColumnInfo column){
		applyedToColumns.add(column);
		column.setAnonymisationMethod(this);
	}


	public Object anonymise(Object exampleValue, AnonymisedColumnInfo anonymizedColumn) {
		this.schema = anonymizedColumn.getTable().getSchema();			
				
		if(anonymizedColumn.isJavaTypeString()){
			return anonymiseString((String)exampleValue);
		
		}
		else if(anonymizedColumn.isJavaTypeDouble()){
			return anonymiseDouble((Double)exampleValue);
		
		}
		else if(anonymizedColumn.isJavaTypeLong()){
			return anonymiseLong((Long) exampleValue);
		
		}
		else if(anonymizedColumn.isJavaTypeDate()){
			return anonymiseDate((Date) exampleValue);
		
		}
		else {
			throw new RuntimeException("unsupported value: " + exampleValue);
		}
	}
	
	protected Object anonymiseDate(Date exampleValue) {
		throw new RuntimeException("Unsupported");
	}



	protected Long anonymiseLong(Long exampleValue) {
		throw new RuntimeException("Unsupported");
	}



	protected Double anonymiseDouble(Double exampleValue) {
		throw new RuntimeException("Unsupported");
	}



	protected String anonymiseString(String exampleValue) {
		throw new RuntimeException("Unsupported");
	}



	public boolean supports(AnonymisedColumnInfo anonymizedColumn){
		return false;
	}
	
	
	public List<AnonymisedColumnInfo> getApplyedToColumns() {
		return applyedToColumns;
	}




	public void removeColumn(AnonymisedColumnInfo selectedAnonymizedColumn) {
		boolean removed= applyedToColumns.remove(selectedAnonymizedColumn);
		if (!removed){
			throw new RuntimeException("not removed, as not found " + selectedAnonymizedColumn);
		}
	}


	public ExecutionMessage runOnColumn(AnonymisedColumnInfo col){
		throw new RuntimeException("UNIMPLEMENTED");
	}
	
	public void setupInDb(){
		for(String setupSql:setupSqls){
			execute(MessageFormat.format(setupSql, schema));
		}
	}
	public void cleanupInDb(){
		for(String cleanupSql:cleanupSqls){
			execute(MessageFormat.format(cleanupSql, schema));
		}
	}

	protected 	void execute(String sql) {
		Logger.getLogger(getClass()).log(Level.DEBUG, "Executing " + sql);
		new JdbcTemplate(dataSource).execute(sql);
	}
	
	protected int update(String sql, Object... args){
		Logger.getLogger(getClass()).log(Level.DEBUG, "Updating " + sql + "\n Params: " + Arrays.toString(args));
		return new JdbcTemplate(dataSource).update(sql, args); 
	}

	protected void addSetupSqlStatements(String ... statements) {
		for(String st : statements) {
			setupSqls.add(st);
		}
	}

	protected void addSetupSqlFiles(String ... filesInResoucesFolder){
		for(String fileNameInResourcesFolder:filesInResoucesFolder){
			String setupSql = getFileContent(fileNameInResourcesFolder);
			setupSqls.add(setupSql);
		}
	}
	
	protected void addCleanupSqlStatements(String ... statements) {
		for(String st : statements) {
			cleanupSqls.add(st);
		}
	}

	protected void addCleanupSqlFiles(String ... filesInResoucesFolder){
		for(String fileNameInResourcesFolder:filesInResoucesFolder){
			String cleanupSql = getFileContent(fileNameInResourcesFolder);
			cleanupSqls.add(cleanupSql);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
