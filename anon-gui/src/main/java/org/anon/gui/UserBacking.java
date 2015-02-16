package org.anon.gui;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.anon.gui.navigation.NavigationCaseEnum;
import org.anon.persistence.data.SecurityRole;
import org.anon.persistence.data.SecurityRoleEnum;
import org.anon.persistence.data.SecurityUser;
import org.anon.service.ServiceResult;
import org.anon.service.admin.UserService;

@ManagedBean
@SessionScoped
public class UserBacking extends BackingBase {

	@ManagedProperty(value="#{userServiceImpl}")
	private UserService userService;
	
	private List<SecurityUser> users;
	
	private SecurityUser selectedUser;
	private SecurityRoleEnum selectedRole;
	
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
	
	public void saveUser() {
		
		SecurityRole role = new SecurityRole();
		role.setRole(getSelectedRole());
		selectedUser.getRoles().add(role);
		ServiceResult result = userService.addNewUser(selectedUser);
		
		if(!result.isFailed()) {
			showExtInfoInGui("User added", selectedUser.getUsername());
			init();
		}
		handleServiceResultAsInfoMessage(result);	
		redirectPageTo(NavigationCaseEnum.USERS);
	}
	
	public void deleteUser(SecurityUser user) {
		ServiceResult result = userService.deleteUser(user);
		
		if(!result.isFailed()) {
			showExtInfoInGui("User deleted", user.getUsername());
		}
		init();
		handleServiceResultAsInfoMessage(result);	
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
