package com.biggestnerd.accountswitch;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class Account {

	private String name;
	private String username;
	private byte[] password;
	
	public Account(String name, String username, String password) {
		this.name = name;
		this.username = username;
		this.password = AccountSwitch.getInstance().getEncrypt().encrypt(password);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		try {
			return new String(AccountSwitch.getInstance().getEncrypt().decrypt(password), "UTF-8").trim();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void setPassword(String password) {
		this.password = AccountSwitch.getInstance().getEncrypt().encrypt(password);
	}
}
