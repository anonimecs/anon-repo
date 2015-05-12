package org.anon.license;



public interface LicenseManager {
	
	void checkLicenseExpired() ;
	
	boolean reachedMaxDbConnections(int connections);
	
	boolean reachedMaxTablesAnonimised(int tablesAnonimised);

	boolean reachedMaxTablesReduced(int tablesReduced);

	int getMaxTablesAnonimised();
	
	boolean isFreeEdition();
	
	boolean isEnterpriseEdition();
	
	String getCompanyName();

	int getMaxTablesReduced();

}
