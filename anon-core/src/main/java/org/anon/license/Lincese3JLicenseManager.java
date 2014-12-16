package org.anon.license;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.verhas.licensor.License;

@Service
public class Lincese3JLicenseManager implements LicenseManager {

	Logger logger = Logger.getLogger(getClass());

	private License lic = null;

	@Value("${licenseSignature}")
	String licenseSignature;

	@Override
	public void checkLicenseExpired() {
		try {
			String validDateString = lic.getFeature("valid-date");
			Date validDate = new SimpleDateFormat("yyyy-MM-dd").parse(validDateString);
			boolean expired = new Date().after(validDate);
			logger.debug("checkLicenseExpired: " + expired);
			if (expired) {
				throw new LicenseException("You license has expired on "
						+ validDateString);
			}
		}
		catch(LicenseException e){
			throw e;
		}
		catch (Exception e) {
			throw new LicenseException(e);
		}

	}

	@PostConstruct
	private void initLicense() {
		try {
			if (licenseSignature == null || licenseSignature.length() == 0) {
				throw new RuntimeException("Empty license signature");
			}

			lic = new License();
			lic.loadKeyRingFromResource("anon_pub.gpg", null);
			lic.setLicenseEncoded(licenseSignature);
			String edition = lic.getFeature("edition");

			logger.warn("Loaded license for the " + edition + " edition");

		} catch (Exception e) {
			logger.error("failed to initialise the license", e);
			
		}
	}

}
