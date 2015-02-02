package org.anon.license;



public interface LicenseManager {
	
	void checkLicenseExpired() ;
	
	boolean reachedMaxDbConnections(int connections);
	
	boolean reachedMaxTablesAnonimised(int tablesAnonimised);

	int getMaxTablesAnonimised();
	
	boolean isFreeEdition();
	
	boolean isEnterpriseEdition();

}
