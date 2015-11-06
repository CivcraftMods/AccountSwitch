package com.biggestnerd.accountswitch;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LegacyAccountList {
	
	private HashMap<String, LegacyAccount> accounts;
	
	public LegacyAccountList() {
		accounts = new HashMap<String, LegacyAccount>();
	}
	
	public void migrate() {
		for(LegacyAccount lAcct : accounts.values()) {
			AccountSwitch.getInstance().getAccountList().addAccount(new Account(lAcct.getName(), lAcct.getUsername(), lAcct.getPassword()));
		}
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
	
	public static LegacyAccountList load(File f) {
		Gson gson = new Gson();
		try {
			return (LegacyAccountList) gson.fromJson(new FileReader(f), LegacyAccountList.class);
		} catch (Exception ex) {
			return null;
		}
	}

	class LegacyAccount {
		private String name;
		private String username;
		private String password;
		
		public LegacyAccount(String name, String username, String password) {
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
			return password;
		}
		
		public void setPassword(String password) {
			this.password = password;
		}
	}
}
