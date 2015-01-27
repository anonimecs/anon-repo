package org.anon.gui;

import java.util.concurrent.Executor;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.anon.AbstractDbConnection;
import org.anon.exec.BaseExec;
import org.anon.exec.ExecFactory;
import org.anon.logic.AnonymisationMethod;
import org.anon.service.DbConnectionFactory;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

@ManagedBean
@ViewScoped
public class ExecBacking extends BackingBase{

	@ManagedProperty(value="#{execFactory}")
	protected ExecFactory execFactory;

	
	@ManagedProperty(value="#{dbConnectionFactory}")
	protected DbConnectionFactory dbConnectionFactory; 
	

	@ManagedProperty(value="#{execBackingExecutor}")
	protected Executor execBackingExecutor;
	

	public void onRunSingle(final AnonymisationMethod anonymisationMethod){
		try{
			
			logDebug("Running single " + anonymisationMethod);
			
			final BaseExec baseExec = execFactory.createExec(dbConnectionFactory.getDatabaseSpecifics());
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
					} finally {
						fireEvent();
					}
					
				}
			});
			
			showExtInfoInGui("Disconnect from the DB", "Please close all sessions to the target database." );
			
			Thread.sleep(250);
			
		} catch (Exception e) {
			logError(e.getMessage(), e);
			showErrorInGui("Run failed: " + e.getMessage());
		} finally {
			fireEvent();
		}
		
	}
	

	public void onRun(){
		try{
			logDebug("Anonymising all methods" );
			final BaseExec baseExec = execFactory.createExec(dbConnectionFactory.getDatabaseSpecifics());
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
					} finally {
						fireEvent();
					}
				}
			});

			showExtInfoInGui("Disconnect from the DB", "Please close all sessions to the target database." );
			
			Thread.sleep(250);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			showErrorInGui("Run failed: " + e.getMessage());
		} finally {
			fireEvent();
		}
	}
	
	
	public void fireEvent() {
		EventBus eventBus = EventBusFactory.getDefault().eventBus();
		eventBus.publish("/execEvent", "event");
	}


	public DbConnectionFactory getDbConnectionFactory() {
		return dbConnectionFactory;
	}


	public void setDbConnectionFactory(DbConnectionFactory dbConnectionFactory) {
		this.dbConnectionFactory = dbConnectionFactory;
	}

	public Executor getExecBackingExecutor() {
		return execBackingExecutor;
	}

	public void setExecBackingExecutor(Executor execBackingExecutor) {
		this.execBackingExecutor = execBackingExecutor;
	}

	public ExecFactory getExecFactory() {
		return execFactory;
	}

	public void setExecFactory(ExecFactory execFactory) {
		this.execFactory = execFactory;
	}

}