package org.anon.persistence.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="SECURITYUSERROLES")
public class SecurityRole implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long userId;
	private SecurityRoleEnum role;
	
	private SecurityUser user;
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO)
	@Column( name = "ID", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column( name = "USER_ID", nullable = false)
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ROLE", nullable = false)
	public SecurityRoleEnum getRole() {
		return role;
	}
	
	public void setRole(SecurityRoleEnum role) {
		this.role = role;
	}

	@ManyToOne
	@JoinColumn(name="USER_ID", referencedColumnName="ID", insertable = false, updatable = false)
	public SecurityUser getUser() {
		return user;
	}

	public void setUser(SecurityUser user) {
		this.user = user;
	}
}
