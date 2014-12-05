package org.anon.persistence.data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.anon.data.Database;

@Entity
public class DatabaseConfig extends PersistentObject{

	protected Long id;	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	private String url;	
	private String login;	
	private String password;	
	private Database vendor;	
	private String version;
	
	private String guiName;

	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Enumerated(EnumType.STRING)
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

}
