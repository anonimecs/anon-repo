package org.anon.exec;

import java.util.List;

import org.anon.logic.AnonymisationMethod;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class OracleExec extends BaseExec{

	

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
