package org.anon.persistence.data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="SECURITYUSER")
public class SecurityUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String username;
	private String password;
	private String name;
	private String surname;
	private String enabled;
	private String encrypted;
	private Set<SecurityRole> roles = new HashSet<SecurityRole>(0);
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	@Column( name = "ID", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column( name = "USERNAME", unique = true, nullable = false)
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column( name = "PASSWORD", nullable = false)
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column( name = "NAME", nullable = true)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column( name = "SURNAME", nullable = true)
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	@Column( name = "ENABLED" )
	public String getEnabled() {
		return enabled;
	}
	
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	
	@Column( name = "ENCRYPTED" )
	public String getEncrypted() {
		return encrypted;
	}
	
	public void setEncrypted(String encrypted) {
		this.encrypted = encrypted;
	}
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true, mappedBy = "user")
	public Set<SecurityRole> getRoles() {
		return roles;
	}
	
	public void setRoles(Set<SecurityRole> roles) {
		this.roles = roles;
	}
	
}
