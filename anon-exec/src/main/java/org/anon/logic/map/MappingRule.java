package org.anon.logic.map;

import org.anon.data.AnonymisedColumnInfo;

public abstract class MappingRule {

	public abstract String generateWhenSql(AnonymisedColumnInfo col);

}
