package org.anon.license;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.anon.test.AnonUnitTest;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration("classpath:spring-test-base.xml")
public class Lincese3JLicenseManagerTest extends AbstractJUnit4SpringContextTests implements AnonUnitTest{

	
	@Test
	public void checkFreeLicense() throws Exception {
		check("../anon-build/License/free/license.txt");
	}

	protected void check(String file) throws IOException, FileNotFoundException {
		Lincese3JLicenseManager licenseManager = new Lincese3JLicenseManager();
		Properties properties = new Properties();
		properties.load(new FileReader(file));
		licenseManager.licenseSignature = properties.getProperty("licenseSignature");
		licenseManager.initLicense();
		licenseManager.checkLicenseExpired();
	}

	@Test
	public void checkEnterpriseLicense() throws Exception {
		check("../anon-build/License/enterprise/license.txt");
	}

}
