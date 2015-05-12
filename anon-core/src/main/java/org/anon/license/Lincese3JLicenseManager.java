package org.anon.license;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.verhas.licensor.License;

@Service("licenseManager")
public class Lincese3JLicenseManager implements LicenseManager {

	private static final String ANON_PUBLIC_KEY = "anon_pub.gpg";

	Logger logger = Logger.getLogger(getClass());

	private License lic = null;

	@Value("${licenseSignature}")
	String licenseSignature;

	@Override
	public void checkLicenseExpired() {
		Date validDate = getDate("valid-date");
		boolean expired = new Date().after(validDate);
		logger.debug("checkLicenseExpired: " + expired);
		if (expired) {
			throw new LicenseException("You license has expired on "
					+ validDate);
		}

	}

	@Override
	public boolean reachedMaxDbConnections(int connections) {
		return connections >= getInt("maxDbConnections");
	}
	
	@Override
	public boolean reachedMaxTablesAnonimised(int tables) {
		return tables >= getMaxTablesAnonimised();
	}
	
	@Override
	public boolean reachedMaxTablesReduced(int tablesReduced) {
		return tablesReduced >= getMaxTablesReduced();
	}

	@Override
	public int getMaxTablesAnonimised() {
		return getInt("maxTablesAnonimised");
	}

	@Override
	public int getMaxTablesReduced() {
		return getInt("maxTablesReduced");
	}

	@Override
	public boolean isFreeEdition() {
		return "free".equalsIgnoreCase(lic.getFeature("edition"));
	}

	@Override
	public boolean isEnterpriseEdition() {
		return "enterprise".equalsIgnoreCase(lic.getFeature("edition"));
	}

	private Date getDate(String key)  {
		try{
			String string = lic.getFeature(key);
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse(string);
			return date;
		}
		catch (Exception e) {
			throw new LicenseException(e);
		}
	}
	
	@Override
	public String getCompanyName() {
		return lic.getFeature("company");
	}

	private int getInt(String key)  {
		try{
			String string = lic.getFeature(key);
			int intValue= Integer.parseInt(string);
			return intValue;
		}
		catch (Exception e) {
			throw new LicenseException(e);
		}
	}

	@PostConstruct
	public void initLicense() {
		try {
			if (licenseSignature == null || licenseSignature.length() == 0) {
				throw new RuntimeException("Empty license signature");
			}

			lic = new License();
			lic.loadKeyRingFromResource(ANON_PUBLIC_KEY, null);
			lic.setLicenseEncoded(licenseSignature);
			String edition = lic.getFeature("edition");

			logger.warn("Loaded license for the " + edition + " edition");
			logger.info("License details: \n " + lic.getLicenseString());

		} catch (Exception e) {
			logger.error("failed to initialise the license", e);
			
		}
	}

}
