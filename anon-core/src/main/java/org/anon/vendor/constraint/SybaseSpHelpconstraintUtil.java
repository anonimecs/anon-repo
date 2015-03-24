package org.anon.vendor.constraint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.anon.vendor.constraint.referential.SybaseForeignKeyConstraint;
import org.anon.vendor.constraint.unique.SybasePrimaryKeyConstraint;
import org.anon.vendor.constraint.unique.SybaseUniqueConstraint;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class SybaseSpHelpconstraintUtil {
	protected static Logger logger = Logger.getLogger(SybaseSpHelpconstraintUtil.class);

	public static boolean isUnique(ResultSet rs) throws SQLException {
		
		return "unique constraint".equalsIgnoreCase(rs.getString("type"))
				&&
				rs.getString("definition").toLowerCase().startsWith("unique");
	}

	public static boolean isPrimaryKey(ResultSet rs) throws SQLException {
		return "unique constraint".equalsIgnoreCase(rs.getString("type"))
				&&
				rs.getString("definition").toLowerCase().startsWith("primary key");
	}

	public static boolean isForeignKey(ResultSet rs) throws SQLException {
		return "referential constraint".equalsIgnoreCase(rs.getString("type"));
	}

	/**
	 * Examples
	 * UNIQUE INDEX ( COL1, COL2) : NONCLUSTERED
	 * UNIQUE INDEX ( COL1) : NONCLUSTERED
	 */
	public static String [] parseColumnsForUnique(String definition) {
		return definition.split("\\(")[1].split("\\)")[0].trim().split("\\s*,\\s*");
	}

	/**
	 * Examples
	 * PRIMARY KEY INDEX ( COL1, COL2) : CLUSTERED
	 * PRIMARY KEY INDEX ( COL1) : CLUSTERED
	 */
	public static String [] parseColumnsForPk(String definition) {
		return definition.split("\\(")[1].split("\\)")[0].trim().split("\\s*,\\s*");
	}
	
	public static <C extends Constraint> Constraint mapRow(final String tableName, ResultSet rs, Class<C> requestedConstraintClass) throws SQLException {
		if(requestedConstraintClass.equals(SybaseUniqueConstraint.class) && isUnique(rs) ){
			return new SybaseUniqueConstraint(rs, tableName);
		}
		else if(requestedConstraintClass.equals(SybasePrimaryKeyConstraint.class) && isPrimaryKey(rs)){
			return new SybasePrimaryKeyConstraint(rs, tableName);
		}
		else if(requestedConstraintClass.equals(SybaseForeignKeyConstraint.class) && isForeignKey(rs)){
			return new SybaseForeignKeyConstraint(rs);
		}
		else {
			//logger.debug("unprocessed constraint " + rs.getString("type") + " : "+ rs.getString("definition"));
			return null;
		}
	}
	
	public static void removeNulls(List<Constraint> allConstraints) {
		Iterator<Constraint> iterator = allConstraints.iterator();
		while(iterator.hasNext()){
			Constraint constraint = iterator.next();
			if(constraint == null){
				iterator.remove();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <C extends Constraint> List spHelpconstraint(final String tableName, String schema, final Class<C> requestedConstraintClass, JdbcTemplate jdbcTemplate) {
		jdbcTemplate.execute("use " + schema);
		String sp_helpconstraint = "sp_helpconstraint '" + tableName + "', 'detail'";
		try {
			List<Constraint> allConstraints = jdbcTemplate.query(sp_helpconstraint, new RowMapper<Constraint>(){
				@Override
				public Constraint mapRow(ResultSet rs, int rowNum) throws SQLException {
					return SybaseSpHelpconstraintUtil.mapRow(tableName, rs, requestedConstraintClass);
				}

				
			});
			// remove nulls
			SybaseSpHelpconstraintUtil.removeNulls(allConstraints);
			return allConstraints;
		} catch (Exception e) {
			// sybase sp_helpconstraint fails for tables with no constraints
			logger.error("Probably unconstrained table. sp_helpconstraints failes with " + sp_helpconstraint, e);
			return (List<Constraint>)Collections.EMPTY_LIST;
		}
	}



}
