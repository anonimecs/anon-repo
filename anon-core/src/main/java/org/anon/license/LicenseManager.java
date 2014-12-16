package org.anon.license;



public interface LicenseManager {
	
	void checkLicenseExpired() ;
	
	int getMaxDbConnections();
	
	int getMaxTablesAnonimised();
}
