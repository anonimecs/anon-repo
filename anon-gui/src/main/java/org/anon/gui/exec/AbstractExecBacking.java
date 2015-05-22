package org.anon.gui.exec;

import java.util.concurrent.Executor;

import javax.faces.bean.ManagedProperty;

import org.anon.exec.AbstractExec;
import org.anon.exec.ExecFactory;
import org.anon.exec.GuiNotifier;
import org.anon.gui.BackingBase;
import org.anon.gui.admin.InfoBacking;
import org.anon.service.DbConnectionFactory;

public class AbstractExecBacking extends BackingBase{

	@ManagedProperty(value="#{execFactory}")
	protected ExecFactory execFactory;

	@ManagedProperty(value="#{execBackingExecutor}")
	protected Executor execBackingExecutor;

	@ManagedProperty(value="#{dbConnectionFactory}")
	protected DbConnectionFactory dbConnectionFactory; 
	
	@ManagedProperty(value="#{infoBacking}")
	protected InfoBacking infoBacking;

	@ManagedProperty(value="#{guiNotifierImpl}")
	protected GuiNotifier guiNotifier;

	
	protected void runAllBackground(final AbstractExec abstractExec) throws InterruptedException {
		execBackingExecutor.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					abstractExec.runAll();
				} catch (Exception e) {
					logError(e.getMessage(), e);
				} finally {
					guiNotifier.refreshExecGui(null);
				}
			}
		});
		
		Thread.sleep(250);
	}

	
	public void setInfoBacking(InfoBacking infoBacking) {
		this.infoBacking = infoBacking;
	}

	public void setGuiNotifier(GuiNotifier guiNotifier) {
		this.guiNotifier = guiNotifier;
	}	
	
	public void setDbConnectionFactory(DbConnectionFactory dbConnectionFactory) {
		this.dbConnectionFactory = dbConnectionFactory;
	}
	
	public void setExecBackingExecutor(Executor execBackingExecutor) {
		this.execBackingExecutor = execBackingExecutor;
	}

	public void setExecFactory(ExecFactory execFactory) {
		this.execFactory = execFactory;
	}

}
