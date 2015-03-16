package org.anon.persistence.data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.anon.data.Database;
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
public class DatabaseConnection extends PersistentObject{

	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	protected Long id;	
	
	private String url;	
	
	private String login;
	
	@Type(type="encryptedString")
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Database vendor;	
	
	private String version;
	
	private String guiName;
	
	@ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="SecurityUser_ID")
	private SecurityUser securityUser;
	
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


	@Override
	public boolean equals(Object o) {
		if (o == this) {
            return true;
        }
		if (!(o instanceof DatabaseConnection)) {
            return false;
        }
		DatabaseConnection c = (DatabaseConnection) o;
		
		return this.id.equals(c.getId());
	}
	public SecurityUser getSecurityUser() {
		return securityUser;
	}
	public void setSecurityUser(SecurityUser securityUser) {
		this.securityUser = securityUser;
	}
}
