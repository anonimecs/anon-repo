package org.anon;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationInfo {

	@Value("${applicationinfo.name}")
	private String name;
	
	public String getName() {
		return name;
	}

}
