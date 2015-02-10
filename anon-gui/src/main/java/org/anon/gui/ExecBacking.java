package org.anon.gui;

import java.util.concurrent.Executor;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.exec.BaseExec;
import org.anon.exec.ExecFactory;
import org.anon.exec.GuiNotifier;
import org.anon.gui.navigation.NavigationCaseEnum;
import org.anon.logic.AnonymisationMethod;
import org.anon.persistence.dao.AuditDao;
import org.anon.persistence.dao.EntitiesDao;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.audit.ExecutionColumnData;
import org.anon.service.DatabaseLoaderService;
import org.anon.service.DbConnectionFactory;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

@ManagedBean
@ViewScoped
public class ExecBacking extends BackingBase{
	
	@ManagedProperty(value="#{infoBacking}")
	protected InfoBacking infoBacking;

	@ManagedProperty(value="#{guiNotifierImpl}")
	protected GuiNotifier guiNotifier;
	
	@ManagedProperty(value="#{auditDaoImpl}")
	protected AuditDao auditDao;
	
	@ManagedProperty(value="#{entitiesDaoImpl}")
	protected EntitiesDao entitiesDao;
	
	@ManagedProperty(value="#{execFactory}")
	protected ExecFactory execFactory;

	@ManagedProperty(value="#{dbConnectionFactory}")
	protected DbConnectionFactory dbConnectionFactory; 
	
	@ManagedProperty(value="#{execBackingExecutor}")
	protected Executor execBackingExecutor;
	
	@ManagedProperty(value="#{databaseLoaderService}")
	protected DatabaseLoaderService databaseLoaderService;

	
	public void onRunSingle(final AnonymisationMethod anonymisationMethod){
		try{
			logDebug("Running single " + anonymisationMethod);
			final BaseExec baseExec = execFactory.createExec(dbConnectionFactory.getDatabaseSpecifics(), infoBacking.getUserName());

			execBackingExecutor.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						baseExec.run(anonymisationMethod);
					} catch (Exception e) {
						logError(e.getMessage(), e);
					} finally {
						guiNotifier.refreshExecGui();
					}
					
				}
			});
						
			Thread.sleep(250);
			
		} catch (Exception e) {
			logError(e.getMessage(), e);
			showErrorInGui("Run failed: " + e.getMessage());
		} finally {
			guiNotifier.refreshExecGui();
		}
		
	}

	public void onRun(){
		try{
			logDebug("Anonymising all methods" );
			final BaseExec baseExec = execFactory.createExec(dbConnectionFactory.getDatabaseSpecifics(), infoBacking.getUserName());
	
			execBackingExecutor.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						baseExec.runAll();
					} catch (Exception e) {
						logError(e.getMessage(), e);
					} finally {
						guiNotifier.refreshExecGui();
					}
				}
			});
			
			Thread.sleep(250);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			showErrorInGui("Run failed: " + e.getMessage());
		} finally {
			guiNotifier.refreshExecGui();
		}
	}
	
	public Long getEstimatedRuntimeSec(AnonymisedColumnInfo anonymisedColumnInfo){
		AnonymisedColumnData anonymisedColumnData  = entitiesDao.loadAnonymisedColumnData(anonymisedColumnInfo);
		ExecutionColumnData executionColumnData = auditDao.getLastExecutionColumnData(anonymisedColumnData);
		if(executionColumnData != null){
			return executionColumnData.getRuntimeSec();
		}
		
		return null;
	}
	
	public void onClickExecute() {
		databaseLoaderService.loadExecConfig();
		redirectPageTo(NavigationCaseEnum.EXECUTE);
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

	public void setInfoBacking(InfoBacking infoBacking) {
		this.infoBacking = infoBacking;
	}

	public ExecFactory getExecFactory() {
		return execFactory;
	}

	public void setExecFactory(ExecFactory execFactory) {
		this.execFactory = execFactory;
	}

	public void setDatabaseLoaderService(DatabaseLoaderService databaseLoaderService) {
		this.databaseLoaderService = databaseLoaderService;
	}
	
	public EntitiesDao getEntitiesDao() {
		return entitiesDao;
	}

	public void setEntitiesDao(EntitiesDao entitiesDao) {
		this.entitiesDao = entitiesDao;
	}

	public void setAuditDao(AuditDao auditDao) {
		this.auditDao = auditDao;
	}

	public void setGuiNotifier(GuiNotifier guiNotifier) {
		this.guiNotifier = guiNotifier;
	}
}