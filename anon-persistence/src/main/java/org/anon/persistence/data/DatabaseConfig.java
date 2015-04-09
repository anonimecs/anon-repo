package org.anon.persistence.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class DatabaseConfig extends PersistentObject{


	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	protected Long id;	
	
	private String defaultSchema;

	private String configurationName;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy = "databaseConfig")
	@Fetch(FetchMode.SELECT)
	private Set<AnonymisationMethodData> anonymisationMethodData = new HashSet<AnonymisationMethodData>();
	
	@ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL) @JoinColumn(name="DatabaseConnection_ID")
	private DatabaseConnection databaseConnection;
	
	@ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="SecurityUser_ID")
	private SecurityUser securityUser;
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
	        return true;
	    }
		if (!(o instanceof DatabaseConfig)) {
	        return false;
	    }
		DatabaseConfig c = (DatabaseConfig) o;
		
		return this.id.equals(c.getId());
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDefaultSchema() {
		return defaultSchema;
	}
	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
	}


	public Set<AnonymisationMethodData> getAnonymisationMethodData() {
		return anonymisationMethodData;
	}
	public void setAnonymisationMethodData(
			Set<AnonymisationMethodData> anonymisationMethodData) {
		this.anonymisationMethodData = anonymisationMethodData;
	}


	public String getConfigurationName() {
		return configurationName;
	}
	public void setConfigurationName(String configurationName) {
		this.configurationName = configurationName;
	}

	public DatabaseConnection getDatabaseConnection() {
		return databaseConnection;
	}

	public void setDatabaseConnection(DatabaseConnection databaseConnection) {
		this.databaseConnection = databaseConnection;
	}

	public SecurityUser getSecurityUser() {
		return securityUser;
	}

	public void setSecurityUser(SecurityUser securityUser) {
		this.securityUser = securityUser;
	}
	
	public String toString() {
		return id + " / " + databaseConnection.getUrl() + " / " + databaseConnection.getLogin() + " / " + databaseConnection.getGuiName();
	}

}
