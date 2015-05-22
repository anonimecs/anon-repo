package org.anon.gui.exec;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.anon.data.ReductionMethod;
import org.anon.exec.reduction.ReductionExec;
import org.anon.service.reduce.ReduceService;

@ManagedBean
@ViewScoped
public class ReductionExecBacking extends AbstractExecBacking{
	
	@ManagedProperty(value="#{reduceService}")
	private ReduceService reduceService;

	
	private List<? extends ReductionMethod> allReductionMethods;
	private int tableCount;
	
	@PostConstruct
	public void postConstruct(){
		allReductionMethods = reduceService.loadPersistedReductions();
		tableCount = calcTableCount();
	}
	
	private int calcTableCount() {
		int res = 0;
		for(ReductionMethod reductionMethod: allReductionMethods){
			res ++;
			if(reductionMethod.getReferencingTableDatas() != null){
				res += reductionMethod.getReferencingTableDatas().size();
			}
		}
		
		return res;

	}
	
	public void onRunAll() {
		try{
			logDebug("running all reductions" );
			final ReductionExec reductionExec = execFactory.createReductionExec(dbConnectionFactory.getDatabaseSpecifics(), infoBacking.getUserName());
			reductionExec.setReductionMethods(allReductionMethods);
			reductionExec.setDataSource(dbConnectionFactory.getConnection().getDataSource());
	
			runAllBackground(reductionExec);
		} catch (Exception e) {
			logError(e.getMessage(), e);
			showErrorInGui("Run failed: " + e.getMessage());
		} finally {
			guiNotifier.refreshExecGui(null);
		}

	}

	
	public void setReduceService(ReduceService reduceService) {
		this.reduceService = reduceService;
	}
	
	public List<? extends ReductionMethod> getAllReductionMethods() {
		return allReductionMethods;
	}
	
	public int getTableCount() {
		return tableCount;
	}
	
}