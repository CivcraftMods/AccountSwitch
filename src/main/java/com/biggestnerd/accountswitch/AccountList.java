package com.biggestnerd.accountswitch;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AccountList {

	private HashMap<String, Account> accounts;
	private byte[] salt;
	
	public AccountList() {
		this.accounts = new HashMap<String, Account>();
		salt = new byte[8];
		SecureRandom secRand = new SecureRandom();
		secRand.nextBytes(salt);
	}
	
	public void addAccount(Account acct) {
		accounts.put(acct.getName(), acct);
	}
	
	public Account getAccount(String name) {
		return accounts.get(name);
	}
	
	public ArrayList<Account> getAccounts() {
		ArrayList<Account> out = new ArrayList<Account>();
		out.addAll(accounts.values());
		return out;
	}
	
	public void remove(String name) {
		accounts.remove(name);
	}
	
	public byte[] getSalt() {
		return salt;
	}
	
	public void setSalt(byte[] salt) {
		this.salt = salt;
	}
	
	public void save(File f) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			String json = gson.toJson(this);
			
			FileWriter fw = new FileWriter(f);
			fw.write(json);
			fw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static AccountList load(File f) {
		Gson gson = new Gson();
		try {
			return (AccountList) gson.fromJson(new FileReader(f), AccountList.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new AccountList();
	}
}
