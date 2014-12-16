package org.anon.license;

import org.anon.test.AnonUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration("classpath:spring-test-base.xml")
public class Lincese3JLicenseManagerTest extends AbstractJUnit4SpringContextTests implements AnonUnitTest{

	@Autowired
	LicenseManager licenseManager;
	
	@Test
	public void testCheckLicenseExpired() throws Exception {
		licenseManager.checkLicenseExpired();
	}

}
