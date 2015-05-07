package org.anon.service;

import java.util.HashMap;
import java.util.Map;

import org.anon.data.RelatedTableColumnInfo;

public class RowFilterTestResult{
	private TestResult headColumnresult;
	private Map<RelatedTableColumnInfo, TestResult> relatedTableResults = new HashMap<>();
	
	public void setHeadColumnresult(TestResult headColumnresult) {
		this.headColumnresult = headColumnresult;
	}
	
	public TestResult getHeadColumnresult() {
		return headColumnresult;
	}
	
	public void addRelatedColumnresult(TestResult columnresult, RelatedTableColumnInfo relatedTableColumnInfo) {
		relatedTableResults.put(relatedTableColumnInfo, columnresult);
	}
	
	public TestResult getRelatedColumnresult(RelatedTableColumnInfo relatedTableColumnInfo) {
		return relatedTableResults.get(relatedTableColumnInfo);
	}

}
