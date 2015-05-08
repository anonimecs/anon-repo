package org.anon.gui.reduce;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.anon.data.DatabaseTableInfo;
import org.anon.data.ReductionMethod;
import org.anon.gui.BackingBase;
import org.anon.service.reduce.ReduceService;


@ManagedBean
@ViewScoped
public class ReduceTableListBacking extends BackingBase {
	
	@ManagedProperty(value="#{reduceService}")
	private ReduceService reduceService;
	
	private List<? extends ReductionMethod> allReductionMethods;
	private Map<String, ReductionMethod> tableMap = new Hashtable<>();
	

	public ReductionMethod getReductionMethod(DatabaseTableInfo databaseTableInfo){
		return tableMap.get(key(databaseTableInfo.getSchema(), databaseTableInfo.getName()));
	}

	
	@PostConstruct
	public void init(){
		
		
		allReductionMethods = reduceService.loadPersistedReductions();
		
		for (ReductionMethod reductionMethodData : allReductionMethods) {
			tableMap.put(key(reductionMethodData.getSchemaName(), reductionMethodData.getTableName()), reductionMethodData);
		}
	}
	
	private String key(String schema, String table){
		return schema + "_" + table;
	}
	
	public void setReduceService(ReduceService reduceService) {
		this.reduceService = reduceService;
	}
		
}
