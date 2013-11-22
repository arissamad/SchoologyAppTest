package com.maestro.api.accounts.entity;

import javax.persistence.*;

import com.sirra.appcore.accounts.*;

@Entity
@Table(name = "accounts")
public class Account extends BaseAccount {
	
	public Account() {
		
	}
}