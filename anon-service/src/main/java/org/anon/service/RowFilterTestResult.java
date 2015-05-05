package org.anon.service;

import java.util.HashMap;
import java.util.Map;

import org.anon.data.RelatedTableColumnInfo;

public class RowFilterTestResult{
	private ColResult headColumnresult;
	private Map<RelatedTableColumnInfo, ColResult> relatedTableResults = new HashMap<>();
	
	public void setHeadColumnresult(ColResult headColumnresult) {
		this.headColumnresult = headColumnresult;
	}
	
	public ColResult getHeadColumnresult() {
		return headColumnresult;
	}
	
	public void addRelatedColumnresult(ColResult columnresult, RelatedTableColumnInfo relatedTableColumnInfo) {
		relatedTableResults.put(relatedTableColumnInfo, columnresult);
	}
	
	public ColResult getRelatedColumnresult(RelatedTableColumnInfo relatedTableColumnInfo) {
		return relatedTableResults.get(relatedTableColumnInfo);
	}

}
