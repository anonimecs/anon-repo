package org.anon.exec.constraint;

import java.util.List;

import org.anon.data.AnonymisedColumnInfo;

public interface ConstraintManager {

	List<? extends Constraint> deactivateConstraints(AnonymisedColumnInfo anonymisedColumnInfo);

	void activateConstraints(AnonymisedColumnInfo anonymisedColumnInfo, List<? extends Constraint> deactivatedContstraints);

}
