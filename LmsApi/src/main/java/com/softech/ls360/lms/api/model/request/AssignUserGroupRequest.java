package com.softech.ls360.lms.api.model.request;

import java.util.ArrayList;
import java.util.List;

public class AssignUserGroupRequest {
	List<Long> usergroups = new ArrayList<Long>();
	List<String> users= new ArrayList<String>();
	String organizationGroup;
	
	
	public List<Long> getUsergroups() {
		return usergroups;
	}
	public void setUsergroups(List<Long> usergroups) {
		this.usergroups = usergroups;
	}
	public List<String> getUsers() {
		return users;
	}
	public void setUsers(List<String> users) {
		this.users = users;
	}
	public String getOrganizationGroup() {
		return organizationGroup;
	}
	public void setOrganizationGroup(String organizationGroup) {
		this.organizationGroup = organizationGroup;
	}
}
