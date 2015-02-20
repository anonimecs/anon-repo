package org.anon.gui;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.anon.gui.navigation.NavigationCaseEnum;
import org.anon.persistence.data.SecurityRole;
import org.anon.persistence.data.SecurityRoleEnum;
import org.anon.persistence.data.SecurityUser;
import org.anon.service.ServiceException;
import org.anon.service.admin.UserService;

@ManagedBean
@SessionScoped
public class UserBacking extends BackingBase {

	@ManagedProperty(value="#{userServiceImpl}")
	private UserService userService;
	
	private List<SecurityUser> users;
	
	private SecurityUser selectedUser;
	private SecurityRoleEnum selectedRole;
	private String newPassword;
	
	@PostConstruct
	public void init() {
		this.users = userService.getAllUser();
		this.selectedUser = new SecurityUser();
		this.selectedRole = SecurityRoleEnum.ROLE_GUEST;
	}
	
	public void prepareNew() {
		selectedUser = new SecurityUser();
		redirectPageTo(NavigationCaseEnum.ADD_USER);
	}
	
	public void prepareEdit(SecurityUser user) {
		selectedUser = user;
		selectedRole = getFirstRole(user).getRole();
		redirectPageTo(NavigationCaseEnum.EDIT_USER);
	}
	
	public void saveUser() {
		
		SecurityRole role = new SecurityRole();
		role.setRole(getSelectedRole());
		selectedUser.getRoles().add(role);
		
		try {
			userService.addNewUser(selectedUser);
			showExtInfoInGui("User added", selectedUser.getUsername());
		} catch (ServiceException e) {
			handleServiceResultAsInfoMessage(e);
		}
		init();
		redirectPageTo(NavigationCaseEnum.USERS);
	}
	
	public void saveEditUser() {
		
		try {
			userService.updateUser(selectedUser, newPassword);
			showExtInfoInGui("User changed", selectedUser.getUsername());
		} catch (ServiceException e) {
			handleServiceResultAsInfoMessage(e);
		}
		init();
		redirectPageTo(NavigationCaseEnum.USERS);
	}
	
	public void deleteUser(SecurityUser user) {
		
		try {
			userService.deleteUser(user);
			showExtInfoInGui("User deleted", user.getUsername());
		} catch (ServiceException e) {
			handleServiceResultAsInfoMessage(e);
		}
		init();
	}
	
	public SecurityRole getFirstRole(SecurityUser user) {
		Iterator<SecurityRole> iter = user.getRoles().iterator();
		if(iter.hasNext()) {
			return (SecurityRole) iter.next();
		}
		return null;
	}
	
	public String getUsername() {
		return userService.getUsername();
	}

	public List<SecurityUser> getUsers() {
		return users;
	}

	public SecurityUser getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(SecurityUser selectedUser) {
		this.selectedUser = selectedUser;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public SecurityRoleEnum getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(SecurityRoleEnum selectedRole) {
		this.selectedRole = selectedRole;
	}

	public SecurityRoleEnum[] getRoleSelection() {
		return SecurityRoleEnum.values();
	}

}
