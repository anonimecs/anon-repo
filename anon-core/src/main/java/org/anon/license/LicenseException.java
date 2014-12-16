package org.anon.license;

public class LicenseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public LicenseException(String message) {
	        super(message);
	    }
	
    public LicenseException(Throwable cause) {
        super(cause);
    }
}
