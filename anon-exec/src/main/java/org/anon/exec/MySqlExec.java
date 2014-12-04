package org.anon.exec;

import java.util.List;

import org.anon.data.AnonConfig;
import org.anon.logic.AnonymisationMethod;

public class MySqlExec extends BaseExec{

	
	public void runExecution(AnonConfig anonConfig){
		throw new RuntimeException("Unimplemented");
	}

	@Override
	protected List<Constraint> deactivateConstraints(AnonymisationMethod anonymisationMethod) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void activateConstraints(AnonymisationMethod anonymisationMethod, List<Constraint> deactivatedContstraints) {
		// TODO Auto-generated method stub
		
	}
}
