package org.anon.gui.exec;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.exec.AnonExec;
import org.anon.gui.navigation.NavigationCaseEnum;
import org.anon.logic.AnonymisationMethod;
import org.anon.persistence.dao.AuditDao;
import org.anon.persistence.dao.EntitiesDao;
import org.anon.persistence.data.AnonymisedColumnData;
import org.anon.persistence.data.audit.ExecutionColumnData;
import org.anon.service.DatabaseLoaderService;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

@ManagedBean
@ViewScoped
public class ExecBacking extends AbstractExecBacking {
	
	
	@ManagedProperty(value="#{auditDaoImpl}")
	protected AuditDao auditDao;
	
	@ManagedProperty(value="#{entitiesDaoImpl}")
	protected EntitiesDao entitiesDao;
	
	@ManagedProperty(value="#{databaseLoaderService}")
	protected DatabaseLoaderService databaseLoaderService;

	
	public void onRunSingle(final AnonymisationMethod anonymisationMethod){
		try{
			logDebug("Running single " + anonymisationMethod);
			final AnonExec anonExec = execFactory.createExec(dbConnectionFactory.getDatabaseSpecifics(), infoBacking.getUserName());

			execBackingExecutor.execute(new Runnable() {
				
				@Override
				public void run() {
					try {
						anonExec.run(anonymisationMethod);
					} catch (Exception e) {
						logError(e.getMessage(), e);
					} finally {
						guiNotifier.refreshExecGui(null);
					}
					
				}
			});
						
			Thread.sleep(250);
			
		} catch (Exception e) {
			logError(e.getMessage(), e);
			showErrorInGui("Run failed: " + e.getMessage());
		} finally {
			guiNotifier.refreshExecGui(null);
		}
		
	}

	public void onRun(){
		try{
			logDebug("Anonymising all methods" );
			final AnonExec anonExec = execFactory.createExec(dbConnectionFactory.getDatabaseSpecifics(), infoBacking.getUserName());
	
			runAllBackground(anonExec);
			
		} catch (Exception e) {
			logError(e.getMessage(), e);
			showErrorInGui("Run failed: " + e.getMessage());
		} finally {
			guiNotifier.refreshExecGui(null);
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


}