package org.anon.service.reduce;

import java.util.HashMap;
import java.util.Map;

import org.anon.service.TestResult;

public class ReduceTestResult{
	private TestResult headResult;
	private Map<RelatedTable, TestResult> relatedTableResults = new HashMap<>();
	
	public void setHeadResult(TestResult headColumnresult) {
		this.headResult = headColumnresult;
	}
	
	public TestResult getHeadResult() {
		return headResult;
	}
	
	public void addRelatedResult(TestResult columnresult, RelatedTable relatedTable) {
		relatedTableResults.put(relatedTable, columnresult);
	}
	
	public TestResult getRelatedResult(RelatedTable relatedTable) {
		return relatedTableResults.get(relatedTable);
	}

}
