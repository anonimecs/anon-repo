package org.anon.exec;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.anon.data.AnonymisedColumnInfo;
import org.anon.exec.constraint.Constraint;
import org.anon.exec.constraint.ConstraintManager;
import org.anon.exec.constraint.SybaseConstraint;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class SybaseExec extends BaseExec {

	@Override
	protected ConstraintManager getConstraintManager(final DataSource dataSource) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		return new ConstraintManager(){

			@Override
			public List<? extends Constraint> deactivateConstraints(AnonymisedColumnInfo anonymisedColumnInfo) {
				String sp_helpconstraint = "sp_helpconstraint '" + anonymisedColumnInfo.getTable().getName() + "', 'detail'";
				List<SybaseConstraint> constraints = jdbcTemplate.query(sp_helpconstraint, new RowMapper<SybaseConstraint>(){
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
				
				for(SybaseConstraint sybaseConstraint:constraints){
					if(sybaseConstraint != null && sybaseConstraint.isReferentialConstraint()){
						String dropConstraint = "alter table " + sybaseConstraint.getTableName() + " drop constraint " + sybaseConstraint.getName();
						logger.debug(dropConstraint);
						jdbcTemplate.update(dropConstraint);
						sybaseConstraint.setActive(false);
					}
				}
				
				return constraints;
			}

			@SuppressWarnings("unchecked")
			@Override
			public void activateConstraints(AnonymisedColumnInfo anonymisedColumnInfo, List<? extends Constraint> deactivatedContstraints) {
				for(SybaseConstraint sybaseConstraint:(List<SybaseConstraint>)deactivatedContstraints){
					if(sybaseConstraint != null &&  sybaseConstraint.isReferentialConstraint() && ! sybaseConstraint.isActive()){
						String createConstraint = "alter table " + sybaseConstraint.getTableName() + " add constraint " + sybaseConstraint.getName() + " " + sybaseConstraint.getStrippedDefinition();
						logger.debug(createConstraint);
						jdbcTemplate.update(createConstraint);
						sybaseConstraint.setActive(true);
					}
				}
				
			}};
	}


}
