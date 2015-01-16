package org.anon.exec;

import java.util.Collections;
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
		return new ConstraintManager(dataSource) {

			@Override
			protected List<? extends Constraint> loadConstraints(AnonymisedColumnInfo anonymisedColumnInfo) {
				// TODO Auto-generated method stub
				return Collections.EMPTY_LIST;
			}

			@Override
			protected String createDeactivateSql(Constraint constraint) {
				// TODO Auto-generated method stub
				return "TODO";
			}

			@Override
			protected String createActivateSql(Constraint constraint) {
				// TODO Auto-generated method stub
				return "TODO";
			}};
	}

}
