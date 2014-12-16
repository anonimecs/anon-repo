package org.anon.persistence.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.anon.data.Database;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class DatabaseConfig {
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	protected Long id;	
	
	private String url;	
	
	private String login;
	
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Database vendor;	
	
	private String version;
	
	private String guiName;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy = "databaseConfig")
	@Fetch(FetchMode.SELECT)
	private Set<AnonymisationMethodData> anonymisationMethodData = new HashSet<AnonymisationMethodData>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Database getVendor() {
		return vendor;
	}
	public void setVendor(Database vendor) {
		this.vendor = vendor;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getGuiName() {
		return guiName;
	}
	public void setGuiName(String guiName) {
		this.guiName = guiName;
	}
	public Set<AnonymisationMethodData> getAnonymisationMethodData() {
		return anonymisationMethodData;
	}
	public void setAnonymisationMethodData(
			Set<AnonymisationMethodData> anonymisationMethodData) {
		this.anonymisationMethodData = anonymisationMethodData;
	}
	
	public String toString() {
		return id + " / " + url + " / " + login + " / " + guiName;
	}
}
