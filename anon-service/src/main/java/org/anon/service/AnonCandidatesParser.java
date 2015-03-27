package org.anon.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.anon.data.AnonCandidate;
import org.anon.data.AnonConfig;
import org.anon.data.DatabaseColumnInfo;
import org.anon.data.DatabaseTableInfo;
import org.anon.vendor.DatabaseSpecifics;
import org.springframework.stereotype.Service;

@Service
public class AnonCandidatesParser {

	DatabaseSpecifics databaseSpecifics;
	Map<String, AnonCandidate> anonCandidates;
	
	public void run(AnonConfig anonConfig ) {
		anonCandidates = new HashMap<>();
		for(DatabaseTableInfo databaseTableInfo:anonConfig.getTables()){
			testTableName(databaseTableInfo);		
			
			
		}
		
	}

	private void testTableName(DatabaseTableInfo databaseTableInfo) {
		for(DatabaseColumnInfo databaseColumnInfo: databaseTableInfo.getColumns()){
			if(databaseSpecifics.isJavaTypeString(databaseColumnInfo)){
				String colName = databaseColumnInfo.getName().toLowerCase();
				if(colName.contains("mail") 
						|| colName.contains("user") 
						|| colName.contains("phone") 
						|| colName.contains("address") 
						|| colName.contains("personal") 
						|| colName.matches(".*last.*name.*") 
						|| colName.matches(".*first.*name.*") 
						|| colName.matches(".*family.*name.*") 
					){
					AnonCandidate anonCandidate = getCandidate("Column " + databaseColumnInfo.getName() + " may contain data to anonymise");
					databaseColumnInfo.setAnonCandidate(anonCandidate);
					anonCandidate.addColumn(databaseColumnInfo);
					databaseTableInfo.addAnonCandidate(anonCandidate);
				}
			}
			
		}
		
		if(databaseTableInfo.getAnonCandidateList().isEmpty()){
			String tableName = databaseTableInfo.getName().toLowerCase();
			if(tableName.contains("user")){
				databaseTableInfo.addAnonCandidate(getCandidate("Table may contain personal data to anonymise"));
			}
			if(tableName.contains("mail")){
				databaseTableInfo.addAnonCandidate(getCandidate("Table may contain user emails to anonymise"));
			}

		}
	}

	public AnonCandidate getCandidate(String message) {
		AnonCandidate anonCandidate = anonCandidates.get(message);
		if(anonCandidate == null){
			anonCandidate = new AnonCandidate(message);
			anonCandidates.put(message, anonCandidate);
		}
		return anonCandidate;
	}

	public void setDatabaseSpefifics(DatabaseSpecifics databaseSpecifics) {
		this.databaseSpecifics = databaseSpecifics;
		
	}
	
	public List<AnonCandidate> getAnonCandidatesList() {
		return new ArrayList<>(anonCandidates.values());
	}
}
