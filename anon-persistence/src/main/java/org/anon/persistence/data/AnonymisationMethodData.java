package org.anon.persistence.data;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.anon.data.AnonymizationType;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.jasypt.hibernate4.type.EncryptedStringType;

@TypeDef(
		name="encryptedString",
		typeClass=EncryptedStringType.class,
		parameters= {
			@Parameter(name="encryptorRegisteredName", value="strongHibernateStringEncryptor")
		}
) 

@Entity
public class AnonymisationMethodData extends PersistentObject{
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	protected Long id;	
	
	@Column
	protected AnonymizationType anonymizationType;
	
	@Type(type="encryptedString")
	@Column
	private String password;
	
	@Column 
	protected String anonMethodClass; 
	
	@OneToMany(mappedBy="anonymisationMethodData", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true) 
	protected List<AnonymisedColumnData> applyedToColumns = new LinkedList<>();

	@Column(name="DATABASECONFIG_ID")
	private Long databaseConfigId;
	
	@ManyToOne
	@JoinColumn(name="DATABASECONFIG_ID", referencedColumnName="ID", insertable = false, updatable = false)
	private DatabaseConfig databaseConfig;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AnonymizationType getAnonymizationType() {
		return anonymizationType;
	}

	public void setAnonymizationType(AnonymizationType anonymizationType) {
		this.anonymizationType = anonymizationType;
	}

	public List<AnonymisedColumnData> getApplyedToColumns() {
		return applyedToColumns;
	}

	public void setApplyedToColumns(List<AnonymisedColumnData> applyedToColumns) {
		this.applyedToColumns = applyedToColumns;
	}

	public void addColumn(AnonymisedColumnData column) {
		applyedToColumns.add(column);
		column.setAnonymisationMethodData(this);
	}

	public String getAnonMethodClass() {
		return anonMethodClass;
	}

	public void setAnonMethodClass(String anonMethodClass) {
		this.anonMethodClass = anonMethodClass;
	}

	public Long getDatabaseConfigId() {
		return databaseConfigId;
	}

	public void setDatabaseConfigId(Long databaseConfigId) {
		this.databaseConfigId = databaseConfigId;
	}

	public DatabaseConfig getDatabaseConfig() {
		return databaseConfig;
	}

	public void setDatabaseConfig(DatabaseConfig databaseConfig) {
		this.databaseConfig = databaseConfig;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
