package org.anon.exec.where;

import org.anon.data.AnonymisedColumnInfo;
import org.springframework.stereotype.Service;

@Service
public class WhereConditionBuilder {
	public enum Applicability {APPLY, EXCLUDE}

	public String build(Applicability applicability, String whereCondition) {
		if(applicability == Applicability.APPLY){
			return whereCondition;
		}
		else if(applicability == Applicability.EXCLUDE){
			return "not(" + whereCondition+ ")";
		}
		throw new RuntimeException("Invalid params " + applicability + whereCondition);
	}

	
	public String buildForRelatedTable(Applicability applicability, String originalWhereCondition, AnonymisedColumnInfo relatedCol, AnonymisedColumnInfo mainCol) {
		if(applicability == Applicability.APPLY){
			return relatedCol.getName() +"     in (select " + mainCol.getName() + " from " + mainCol.getTable().getName() + " where " +originalWhereCondition + ")";
		}
		else if(applicability == Applicability.EXCLUDE){
			return relatedCol.getName() +" not in (select " + mainCol.getName() + " from " + mainCol.getTable().getName() + " where " +originalWhereCondition + ")";
		}
		throw new RuntimeException("Invalid params " + applicability + originalWhereCondition);
		
	}
}
