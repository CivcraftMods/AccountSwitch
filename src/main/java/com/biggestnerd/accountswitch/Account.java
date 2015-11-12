package com.biggestnerd.accountswitch;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;

public class Account {

	private String name;
	private String username;
	private byte[] password;
	private byte[] iv;
	
	public Account(String name, String username, String password) {
		this.name = name;
		this.username = username;
		iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		this.password = AccountSwitch.getInstance().getEncrypt().encrypt(password, iv);
	}
	
	public Account(String name, String username, byte[] password) {
		this.name = name;
		this.username = username;
		this.password = password;
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
			return new String(AccountSwitch.getInstance().getEncrypt().decrypt(password, iv), "UTF-8").trim();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public void setPassword(String password) {
		this.password = AccountSwitch.getInstance().getEncrypt().encrypt(password, iv);
	}
	
	public byte[] getIv() {
		return iv;
	}
	
	public void setIv(byte[] iv) {
		this.iv = iv;
	}
	
	public void changeEncryption(Encrypt newCrypt) {
		try {
			String pass = new String(AccountSwitch.getInstance().getEncrypt().decrypt(password, iv), "UTF-8").trim();
			byte[] newPass = newCrypt.encrypt(pass, iv);
			this.password = newPass;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
