package org.anon.vendor.constraint;

public interface DeactivationStrategy {

	boolean deactivateNulls();

	boolean deactivateUniques();
	
	boolean deactivateReferential();

}
