package org.anon.exec;

import org.anon.license.LicenseManager;

public class DummyLicenseManager implements LicenseManager {
	boolean freeEdition;
	
	public DummyLicenseManager(boolean freeEdition) {
		this.freeEdition = freeEdition;
	}
	
	@Override
	public void checkLicenseExpired() {}

	@Override
	public boolean reachedMaxDbConnections(int connections) {
		return false;
	}

	@Override
	public boolean reachedMaxTablesAnonimised(int tablesAnonimised) {
		return false;
	}

	@Override
	public int getMaxTablesAnonimised() {
		return 10000;
	}

	@Override
	public boolean isFreeEdition() {
		return freeEdition;
	}

	@Override
	public boolean isEnterpriseEdition() {
		return !freeEdition;
	}

}
