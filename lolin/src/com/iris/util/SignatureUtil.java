package com.iris.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * 암호화 유틸
 */
public class SignatureUtil {

	public static String getHash(String rawSignature){

		MessageDigest crypt;
		String hash = null;
		try {
			crypt = MessageDigest.getInstance("SHA-1");

			crypt.reset();
			crypt.update(rawSignature.getBytes("UTF-8"));
			hash = byteToHex(crypt.digest());

		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return hash;
	}
	
	public static boolean compareHash(String rawSignature , String compareHash){
		
		MessageDigest crypt;
		String hash = null;
		boolean isOk = false;
		try {
			crypt = MessageDigest.getInstance("SHA-1");

			crypt.reset();
			crypt.update(rawSignature.getBytes("UTF-8"));
			hash = byteToHex(crypt.digest());

			if(compareHash.equals(hash)){
				isOk = true;
			}else{
				isOk = false;
			}
			
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return isOk;
	}

	public static String byteToHex ( byte[] hash ){

		Formatter formatter = new Formatter();
		for (byte b : hash) { formatter.format("%02x", b); }
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
