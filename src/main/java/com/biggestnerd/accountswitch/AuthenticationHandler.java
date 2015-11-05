package com.biggestnerd.accountswitch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import com.google.gson.Gson;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import scala.actors.threadpool.Arrays;

public class AuthenticationHandler {
	
	Minecraft mc;
	
	public AuthenticationHandler(Minecraft mc) {
		this.mc = mc;
	}
	
	public void validateAccount(String username, String password) {
		Response response = authenticate(username, password);
		if(response != null) {
			String accountName = response.getSelectedProfile().getName();
			String uuid = response.getSelectedProfile().getId();
			Account acct = new Account(accountName, username, password);
			AccountSwitch.getInstance().getAccountList().addAccount(acct);
			AccountSwitch.getInstance().saveAccounts();
			setSession(makeSession(acct));
		}
	}
	
	public void setSession(Session newSession) {
		try {
			for(Field f : mc.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if(f.get(mc) instanceof Session) {
					System.out.println("Session field is: " + f.getName());
					f.set(mc, newSession);
				} else {
					f.setAccessible(false);
				}
			}
			AccountSwitch.getInstance().setCurrent(newSession.getUsername());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public Session makeSession(Account acct) {
		Response response = authenticate(acct.getUsername(), acct.getPassword());
		if(response == null) {
			return new Session("username", UUID.randomUUID().toString(), "token", "legacy");
		} 
		String username = response.getSelectedProfile().getName();
		String id = response.getSelectedProfile().getId();
		String token = response.getToken();
		boolean legacy = response.getSelectedProfile().isLegacy();
		//username, uuid, token, sessiontype
		return new Session(username, id, token, legacy ? "legacy" : "mojang");
	}
	
	public boolean validSession(Session session) {
		Gson gson = new Gson();
		try {
			int responseCode = getHttpRequestCode(new URL("https://authserver.mojang.com/validate"), gson.toJson(new Validate(session.getToken())));
			return responseCode == 204;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	private Response authenticate(String username, String password) {
		Gson gson = new Gson();
		Response response = null;
		try {
			String rString = httpRequest(new URL("https://authserver.mojang.com/authenticate"), makeJsonRequest(username, password));
			System.out.println(rString);
			response = (Response) gson.fromJson(rString, Response.class);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}
	
	private String makeJsonRequest(String username, String password){
		Gson gson = new Gson();
		Payload payload = new Payload(username, password);
		System.out.println(gson.toJson(payload));
		return gson.toJson(payload);
    }
	
	private int getHttpRequestCode(URL url, String content) throws Exception {
		byte[] contentBytes = content.getBytes();
		
		URLConnection connection = url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("Accept-Charset", "UTF-8");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Content-Length", Integer.toString(contentBytes.length));
		
		OutputStream requestStream = connection.getOutputStream();
		requestStream.write(contentBytes, 0, contentBytes.length);
		requestStream.close();
		
		int code = ((HttpURLConnection)connection).getResponseCode();
		return code;
	}
	
	private String httpRequest(URL url, String content) throws Exception {
		byte[] contentBytes = content.getBytes();
		
		URLConnection connection = url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("Accept-Charset", "UTF-8");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setRequestProperty("Content-Length", Integer.toString(contentBytes.length));
		
		OutputStream requestStream = connection.getOutputStream();
		requestStream.write(contentBytes, 0, contentBytes.length);
		requestStream.close();
		
		int code = ((HttpURLConnection)connection).getResponseCode();
		String response = "";
		BufferedReader responseStream;
		if(code == 200) {
			responseStream = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
		} else {
			responseStream = new BufferedReader(new InputStreamReader(((HttpURLConnection) connection).getErrorStream(), "UTF-8"));
		}
		response = responseStream.readLine();
		responseStream.close();
		return response;
	}
	
	class Response {
		private String accessToken;
		private String clientToken;
		private Profile[] availableProfiles;
		private Profile selectedProfile;
		
		public Profile getSelectedProfile() {
			return selectedProfile;
		}
		
		public String getToken() {
			return accessToken;
		}
	}
	
	class Profile {
		private String id;
		private String name;
		private boolean legacy;
		
		public String getName() {
			return name;
		}
		
		public String getId() {
			return id;
		}
		
		public boolean isLegacy() {
			return legacy;
		}
	}
	
	class Payload {
		Agent agent = new Agent();
		private String username;
		private String password;
		public Payload(String username, String password) {
			this.username = username;
			this.password = password;
		}
	}
	
	class Agent {
		private String name = "Minecraft";
		private int version = 1;
	}
	
	class Validate {
		private String accessToken;
		
		public Validate(String accessToken) {
			this.accessToken = accessToken;
		}
	}
}
