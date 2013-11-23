package com.maestro.api.schoology.support;

import java.util.*;

/**
 * Holds the information from the SAMLResponse from Schoology.
 * 
 * @author aris
 */
public class SchoologyUser {
	
	protected String schoolId;
	protected String schoolName;
	
	protected String uid;
	protected String fullName;
	protected String firstName;
	protected String lastName;
	
	protected boolean isAdmin;
	
	protected String roleId;
	protected String roleName;
	
	protected String timezone;
	protected String domain;
	
	public SchoologyUser(Map<String, String> attributeMap) {
		
		setSchoolId(attributeMap.get("school_nid"));
		setSchoolName(attributeMap.get("school_title"));
		
		setUid(attributeMap.get("uid"));
		setFullName(attributeMap.get("name_display"));
		setFirstName(attributeMap.get("name_first"));
		setLastName(attributeMap.get("name_last"));
		
		String isAdminStr = attributeMap.get("is_admin");
		isAdmin = isAdminStr.equals("1")?true:false;
		
		setRoleId(attributeMap.get("role_id"));
		setRoleName(attributeMap.get("role_name"));
		
		setTimezone(attributeMap.get("timezone_name"));
		setDomain(attributeMap.get("domain"));
		
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
}
