package org.anon.exec.mock;

import org.anon.license.LicenseManager;

public class LicenseManagerMock implements LicenseManager {

	@Override
	public void checkLicenseExpired() {
	}

	@Override
	public boolean reachedMaxDbConnections(int connections) {
		return false;
	}

	@Override
	public boolean reachedMaxTablesAnonimised(int tablesAnonimised) {
		return false;
	}
	
	@Override
	public boolean reachedMaxTablesReduced(int tablesReduced) {
		return false;
	}

	@Override
	public int getMaxTablesAnonimised() {
		return 100;
	}

	@Override
	public int getMaxTablesReduced() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public boolean isFreeEdition() {
		return true;
	}

	@Override
	public boolean isEnterpriseEdition() {
		return false;
	}

	@Override
	public String getCompanyName() {
		return "test";
	}

}
