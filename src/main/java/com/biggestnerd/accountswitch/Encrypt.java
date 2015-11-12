package com.biggestnerd.accountswitch;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class Encrypt {

	private Cipher cipher;
	private SecretKey secret;
	private String encryptionKey;
	private byte[] salt;
	public static byte[] oldSalt = new byte[]{58,123,-49,20,33,-120,7,100};
	
	public Encrypt(String encryptionKey, byte[] salt) {
		try {
			this.encryptionKey = encryptionKey;
			this.salt = salt;
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			PBEKeySpec spec = new PBEKeySpec(encryptionKey.toCharArray(), salt, 65536, 128);
			SecretKey tmp = factory.generateSecret(spec);
			secret = new SecretKeySpec(tmp.getEncoded(), "AES");
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public byte[] encrypt(String str, byte[] iv) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(iv));
			byte[] cipherText = cipher.doFinal(str.getBytes("UTF-8"));
			System.out.println(cipherText.length);
			return cipherText;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public byte[] decrypt(byte[] password, byte[] iv) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
			byte[] cipherText = cipher.doFinal(password);
			return cipherText;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public String getCurrentKey() {
		return encryptionKey;
	}
}
