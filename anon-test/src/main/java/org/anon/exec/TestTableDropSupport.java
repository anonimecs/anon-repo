package org.anon.exec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestTableDropSupport extends TestTableCreatorSupport {

	@Override
	public void runScript(JdbcTemplate jdbcTemplate, String filePath, String schema) throws IOException {
		jdbcTemplate.execute("use " + schema);
		for (String sql : getStatements(filePath)) {
			try {
				jdbcTemplate.execute(sql);
			} catch (DataAccessException ignore) {}
		}
	}

	@Override
	public List<String> filerStatements(String[] statements) {
		List<String> res = new ArrayList<>();
		for (String sql : statements) {
			if (sql.isEmpty()) {
				continue;
			}
			if (sql.length() < 10) {
				continue;
			}
			if (sql.trim().startsWith("--")) {
				continue;
			}
			if (sql.trim().startsWith("/*")) {
				continue;
			}
			res.add(sql.trim().replace("\n", " ").replace("\r", " "));
		}
		return res;
	}
}
