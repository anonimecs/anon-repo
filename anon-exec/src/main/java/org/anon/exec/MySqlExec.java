package org.anon.exec;

import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.exec.constraint.Constraint;
import org.anon.exec.constraint.ConstraintManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class MySqlExec extends BaseExec{
	@Override
	protected ConstraintManager getConstraintManager(DataSource dataSource) {
		return new ConstraintManager() {
			
			@Override
			public List<? extends Constraint> deactivateConstraints(
					AnonymisedColumnInfo anonymisedColumnInfo) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void activateConstraints(AnonymisedColumnInfo anonymisedColumnInfo,
					List<? extends Constraint> deactivatedContstraints) {
				// TODO Auto-generated method stub
				
			}
		};
	}

}
