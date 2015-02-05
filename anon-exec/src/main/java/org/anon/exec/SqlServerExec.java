package org.anon.exec;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.exec.constraint.Constraint;
import org.anon.exec.constraint.ConstraintManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class SqlServerExec extends BaseExec {

	@Override
	protected ConstraintManager getConstraintManager(final DataSource dataSource) {

		return new ConstraintManager(dataSource){

			@Override
			protected List<Constraint> loadConstraints(AnonymisedColumnInfo anonymisedColumnInfo) {
				// TODO

				return new ArrayList<>();
			}


		};
	}


}

