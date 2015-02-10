package org.anon.exec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.exec.constraint.ConstraintManager;
import org.anon.exec.constraint.SybaseConstraint;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class SybaseExec extends BaseExec {

	@Override
	protected ConstraintManager getConstraintManager(final DataSource dataSource) {

		return new ConstraintManager(dataSource){

			@Override
			protected List<SybaseConstraint> loadConstraints(AnonymisedColumnInfo anonymisedColumnInfo) {
				String sp_helpconstraint = "sp_helpconstraint '" + anonymisedColumnInfo.getTable().getName() + "', 'detail'";
				List<SybaseConstraint> allConstraints = jdbcTemplate.query(sp_helpconstraint, new RowMapper<SybaseConstraint>(){
					@Override
					public SybaseConstraint mapRow(ResultSet rs, int rowNum) throws SQLException {
						try {
							return new SybaseConstraint(rs);
						} catch (Exception e) {
							logger.error("deactivateConstraints failed", e);
							return null;
						}
					}
					
				});
				
				List<SybaseConstraint> referentialConstraints = new ArrayList<>();
				for(SybaseConstraint sybaseConstraint:allConstraints){
					if(sybaseConstraint != null && sybaseConstraint.isReferentialConstraint()){
						referentialConstraints.add(sybaseConstraint);
					}
				}
				return referentialConstraints;
			}


		};
	}


}

