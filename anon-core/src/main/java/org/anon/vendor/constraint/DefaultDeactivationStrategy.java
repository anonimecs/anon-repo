package org.anon.vendor.constraint;

public class DefaultDeactivationStrategy implements DeactivationStrategy {

	@Override
	public boolean deactivateNulls() {
		return true;
	}

	@Override
	public boolean deactivateUniques() {
		return true;
	}

	@Override
	public boolean deactivateReferential() {
		return true;
	}

}
