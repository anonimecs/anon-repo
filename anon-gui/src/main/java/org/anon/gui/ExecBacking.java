package org.anon.gui;

import java.util.concurrent.Executor;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.anon.AbstractDbConnection;
import org.anon.exec.BaseExec;
import org.anon.logic.AnonymisationMethod;
import org.anon.service.DbConnectionFactory;

@ManagedBean
@ViewScoped
public class ExecBacking extends BackingBase{

	// TODO turn this into a factory
	@ManagedProperty(value="#{sybaseExec}")
	protected BaseExec baseExec;

	
	@ManagedProperty(value="#{dbConnectionFactory}")
	protected DbConnectionFactory dbConnectionFactory; 
	

	@ManagedProperty(value="#{execBackingExecutor}")
	protected Executor execBackingExecutor;
	

	public void onRunSingle(final AnonymisationMethod anonymisationMethod){
		try{
			logDebug("Runnig single " + anonymisationMethod);
			AbstractDbConnection connection = dbConnectionFactory.getConnection();
			logDebug("Database " + connection.getProperties());
	
			baseExec.setDataSource(connection.getDataSource());
			
			execBackingExecutor.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						baseExec.run(anonymisationMethod);
					} catch (Exception e) {
						logError(e.getMessage(), e);
					}
					
				}
			});
			
			Thread.sleep(250);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			showErrorInGui("Run failed: " + e.getMessage());
		}
	}
	
	public void onRun(){
		try{
			logDebug("Anonymising all methods" );
			AbstractDbConnection connection = dbConnectionFactory.getConnection();
			logDebug("Database " + connection.getProperties());
			baseExec.setDataSource(connection.getDataSource());
	
			execBackingExecutor.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						baseExec.runAll();
					} catch (Exception e) {
						logError(e.getMessage(), e);
					}
					
				}
			});
			
			Thread.sleep(250);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			showErrorInGui("Run failed: " + e.getMessage());
		}
		

	}


	public DbConnectionFactory getDbConnectionFactory() {
		return dbConnectionFactory;
	}


	public void setDbConnectionFactory(DbConnectionFactory dbConnectionFactory) {
		this.dbConnectionFactory = dbConnectionFactory;
	}


	public void setBaseExec(BaseExec baseExec) {
		this.baseExec = baseExec;
	}
	
	public BaseExec getBaseExec() {
		return baseExec;
	}

	public Executor getExecBackingExecutor() {
		return execBackingExecutor;
	}

	public void setExecBackingExecutor(Executor execBackingExecutor) {
		this.execBackingExecutor = execBackingExecutor;
	}

}
