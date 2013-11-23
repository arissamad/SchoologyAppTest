package com.maestro.api.schoology.entity;

import javax.persistence.*;

/**
 * This is the OAuth access token for a specific schoology user.
 * 
 * @author aris
 */
@Entity
@Table(name = "SchoologyTokens")
public class SchoologyToken {

	protected String uid;
	
	protected String key;
	protected String secret;
	
	protected boolean isFinalToken;
	
	public SchoologyToken() {
		isFinalToken = false;
	}
	
	@Id
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public boolean getIsFinalToken() {
		return isFinalToken;
	}

	public void setIsFinalToken(boolean isFinalToken) {
		this.isFinalToken = isFinalToken;
	}
}
